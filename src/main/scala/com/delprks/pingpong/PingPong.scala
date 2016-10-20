package com.delprks.pingpong

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}

case object StartGame
case object PokePing
case object PokePong

class Ping extends Actor {
  def receive: Receive = {
    case StartGame => println("Starting the game...")
      Thread.sleep(500)
      val pong = context.actorOf(Props[Pong], "pong")
      println("Ping!")
      pong ! PokePong
    case PokePing => println("Ping!")
      Thread.sleep(500)
      sender ! PokePong
    case PoisonPill => println("received the poison pill...")
    case _ => throw new Exception("invalid message received")
  }
}

class Pong extends Actor {
  var pongCounter = 0
  def receive: Receive = {
    case PokePong => println("Pong!")
      Thread.sleep(500)
      if (pongCounter < 10) {
        sender ! PokePing
        pongCounter += 1
      }
      else {
        println("shutting down...")
        sender ! PoisonPill
        context.stop(self)
      }
    case _ => throw new Exception("invalid message received")
  }
}

object PingPong extends App {
  val actorSystem = ActorSystem("PingPong")
  val ping = actorSystem.actorOf(Props[Ping], "ping")

  ping ! StartGame

  Thread.sleep(15000)
  actorSystem.terminate
}
