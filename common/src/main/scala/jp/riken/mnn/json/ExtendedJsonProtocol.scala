package jp.riken.mnn.json

import java.net.URI
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
import java.util.UUID
import scala.util.{ Try, Success, Failure }
import spray.json._

/** JSON formats for built-in types. */
trait ExtendedJsonProtocol extends DefaultJsonProtocol {
  def deserializeJsString[T](value: JsValue)(f: String => T): T = value match {
    case JsString(s) => Try(f(s)) match {
      case Success(x) => x
      case Failure(e) => deserializationError(e.getMessage)
    }
    case x => deserializationError(s"Invalid URI '${x.toString}'")
  }

  implicit object uuidFormat extends JsonFormat[UUID] {
    def read(value: JsValue) = deserializeJsString(value)(UUID.fromString(_))
    def write(id: UUID) = JsString(id.toString)
  }

  implicit object uriFormat extends JsonFormat[URI] {
    def read(value: JsValue) = deserializeJsString(value)(new URI(_))
    def write(uri: URI) = JsString(uri.toString)
  }

  implicit object dateTimeFormat extends JsonFormat[ZonedDateTime] {
    def read(value: JsValue) = deserializeJsString(value)(ZonedDateTime.parse(_, ISO_OFFSET_DATE_TIME))
    def write(date: ZonedDateTime) = JsString(date.format(ISO_OFFSET_DATE_TIME))
  }
}

object ExtendedJsonProtocol extends ExtendedJsonProtocol
