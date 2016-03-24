package jp.riken.mnn.json

import jp.riken.mnn.types._
import jp.riken.mnn.util.IDParsers
import spray.json._
import scalaz._

trait IDsJsonProtocol extends ExtendedJsonProtocol with IDParsers {
  // TODO Try to abstract ID instance parsing

  trait IDFormat[I <: ID] extends JsonFormat[I] {
    def write(id: I) = JsString(id.urn)
  }

  implicit object NodeIDFormat extends IDFormat[NodeID] {
    def read(v: JsValue) = v match {
      case JsString(s) => NodeIDParser.parse(s) match {
        case Success(id) => id
        case Failure(_)  => deserializationError(s"'${s}' is an invalid node ID")
      }
      case _ => deserializationError(s"'${v.toString} is an invalid node ID")
    }
  }

  implicit object UserIDFormat extends IDFormat[UserID] {
    def read(v: JsValue) = v match {
      case JsString(s) => UserIDParser.parse(s) match {
        case Success(id) => id
        case Failure(_)  => deserializationError(s"'${s}' is an invalid user ID")
      }
      case _ => deserializationError(s"'${v.toString} is an invalid user ID")
    }
  }

  implicit object CollectionIDFormat extends IDFormat[CollectionID] {
    def read(v: JsValue) = v match {
      case JsString(s) => CollectionIDParser.parse(s) match {
        case Success(id) => id
        case Failure(_)  => deserializationError(s"'${s}' is an invalid user ID")
      }
      case _ => deserializationError(s"'${v.toString} is an invalid user ID")
    }
  }
}

