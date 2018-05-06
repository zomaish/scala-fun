package com.names

import spray.json._

case class Name(name: String)

object NameProtocol extends DefaultJsonProtocol {
  implicit val nameFormat: JsonFormat[Name] = jsonFormat1(Name.apply)
}
