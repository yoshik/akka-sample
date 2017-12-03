package com.yoshiki
import akka.actor.ActorSystem
import com.twitter.hbc.httpclient.auth.OAuth1

object ApplicationMain extends App {
  val oauth = new OAuth1(
    /* consumerKey       */ "xxx",
    /* consumerSecret    */ "xxx",
    /* accessToken       */ "xxx",
    /* accessTokenSecret */ "xxx"
  )

  val system = ActorSystem("actor")
  val tweetSearchActor = system.actorOf(TwitterSearchActor.props, "tweetSearchActor")
  tweetSearchActor ! TwitterSearchActor.Search("hoge")
  system.awaitTermination()
}