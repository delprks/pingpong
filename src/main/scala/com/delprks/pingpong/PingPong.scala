package com.delprks.pingpong

import akka.actor.{Actor, ActorSystem, Props}

case object StartGame

class Ping extends Actor {
  def receive: Receive = {
    case StartGame => println("Starting the game...")
    case _ => throw new Exception("invalid message received")
  }
}

object PingPong extends App {
  val actorSystem = ActorSystem("PingPong")
  val ping = actorSystem.actorOf(Props[Ping], "ping")

  ping ! StartGame

  actorSystem.terminate()
}
