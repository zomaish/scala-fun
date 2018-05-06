package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import org.slf4j.LoggerFactory
import com.typesafe.scalalogging._
import spray.httpx.SprayJsonSupport._
import com.names.NameProtocol._
import com.names.{ Name, NameService }
import spray.json._

import scala.util.{ Try, Success, Failure }

import scala.concurrent.Future

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {
  //  def addTwo(n:Int):Future[Int] = Future { n + 2 }
  //  def addTowThenDouble(n: Int): Future[Int] =  addTwo(n).map { x:Int => println(x); x*2 }

  def toJson(route: Route): Route = {
    respondWithMediaType(`application/json`) {
      route
    }
  }

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`application/json`) {
          complete {
            val logger = Logger(LoggerFactory.getLogger("MyService"))

            logger.debug("Here goes my debug message.")
            val names = NameService.getAllNames()

            names
          }
        }
      }
    }
}

