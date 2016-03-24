package jp.riken.mnn.json

import jp.riken.mnn.types.TaggedTypes._
import spray.json._

/** JSON converters for [[jp.riken.mnn.types.TaggedTypes]] members. */
trait TaggedTypesJsonProtocol extends ExtendedJsonProtocol {
  trait StringTagFormat[T <: StringTag] extends JsonFormat[T] {
    def write(tag: T) = JsString(tag)
  }

  implicit object MnnIdentifierFormat extends StringTagFormat[Identifier] {
    def read(v: JsValue) = deserializeJsString(v)(_.asIdentifier)
  }

  implicit object UsernameFormat extends StringTagFormat[Username] {
    def read(v: JsValue) = deserializeJsString(v)(_.asUsername)
  }

  implicit object EmailFormat extends StringTagFormat[Email] with TaggedTypesJsonProtocol {
    def read(v: JsValue) = deserializeJsString(v)(_.asEmail)
  }
}
