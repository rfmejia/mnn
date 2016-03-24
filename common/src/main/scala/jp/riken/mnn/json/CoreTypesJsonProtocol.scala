package jp.riken.mnn.json

import jp.riken.mnn.types._
import jp.riken.mnn.types.TaggedTypes._
import spray.json._

/** Additional JSON formats for commonly used types.
  */
trait CoreTypesJsonProtocol extends ExtendedJsonProtocol
    with TaggedTypesJsonProtocol
    with EnumerationJsonProtocol
    with IDsJsonProtocol {

  implicit val nodeFormat = jsonFormat5(Node.apply)
  implicit val collectionFormat = jsonFormat7(Collection.apply)

  /** JSON-serialized user objects should always contain `username`, `name` and `email` fields. It will contain the `id`
    * field on serialization only. It should never contain `password` and `salt`.
    */
  implicit object UserFormat extends JsonFormat[User] {
    def write(user: User): JsValue =
      JsObject(
        "id" -> user.userId.toJson,
        "username" -> user.username.toJson,
        "name" -> user.name.toJson,
        "email" -> user.email.toJson
      )

    def read(value: JsValue): User = value match {
      case obj: JsObject => obj.getFields("username", "name", "email") match {
        case Seq(JsString(username), JsString(name), JsString(email)) => User(username.asUsername, name, email.asEmail)
        case _ => deserializationError("Missing fields: `username`, `name` and `email` required")
      }
      case _ => deserializationError("Expected JSON object")
    }
  }
}

object CoreTypesJsonProtocol extends CoreTypesJsonProtocol
