package com.yoshiki

import akka.actor.{ Actor, ActorLogging, Props }
import akka.actor.ActorSystem
import java.io.FileWriter
import play.api.libs.json.Json
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object RecordActor {
  val props = Props[RecordActor]
  case class FileWrite(tweet: Tweet)
}

class RecordActor extends Actor with ActorLogging {
  import RecordActor._
  def receive = {
    case FileWrite(tweet) => {
      val day = new DateTime(tweet.timestamp.toLong)
      val fmt = DateTimeFormat.forPattern("yyyyMMdd")
      val fw = new FileWriter("log/" + fmt.print(day) + ".txt", true)

      log.info(s"Write: ${tweet.screen_name}'s tweet")
      fw.write(tweet.screen_name + " / " + tweet.text.replaceAll("\n", " ") + "\n")
      fw.close
    }
  }
}

