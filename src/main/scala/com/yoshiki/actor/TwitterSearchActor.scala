package com.yoshiki

import akka.actor._
import com.twitter.hbc.ClientBuilder
import com.twitter.hbc.core._
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint
import com.twitter.hbc.core.processor.StringDelimitedProcessor
import java.util.concurrent.LinkedBlockingQueue
import org.apache.commons.lang3.StringEscapeUtils
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.JavaConversions._

object TwitterSearchActor {
  val props = Props[TwitterSearchActor]
  case class Search(word: String)
}

class TwitterSearchActor extends Actor with ActorLogging {
  import TwitterSearchActor._
  implicit val tweetReads = (
    (__ \ "text").read[String] ~
    (__ \ "created_at").read[String] ~
    (__ \ "timestamp_ms").read[String] ~
    (__ \ "user" \ "screen_name").read[String] ~
    (__ \ "user" \ "followers_count").read[Int]
  )(Tweet)

  val recordActor = context.actorOf(RecordActor.props, "recordActor")

  def receive = {

    case Search(word) =>
      log.info(s"Search: $word")

      val texts = new LinkedBlockingQueue[String](100000)

      val endpoint = new StatusesFilterEndpoint()
      endpoint.trackTerms(List(word))

      val client = new ClientBuilder()
        .hosts(Constants.STREAM_HOST)
        .endpoint(endpoint)
        .authentication(ApplicationMain.oauth)
        .processor(new StringDelimitedProcessor(texts))
        .build()
      client.connect

      Stream.continually(texts.take).foreach(json => {
        val out = StringEscapeUtils.unescapeJava(json)
        val tweet: JsResult[Tweet] = Json.fromJson(Json.parse(json))
        log.info(s"Tweet: $tweet")
        tweet match {
          case JsSuccess(tweet, _) => recordActor ! RecordActor.FileWrite(tweet)
          case JsError(e)          => log.error(s"JsonError: $e")
        }
      })
  }
}

