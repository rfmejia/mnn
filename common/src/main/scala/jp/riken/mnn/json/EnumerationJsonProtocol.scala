package jp.riken.mnn.json

import jp.riken.mnn.types.{ ManagementPolicy, NodeType }
import spray.json._

trait EnumerationJsonProtocol {
  def jsonEnum[T <: Enumeration](enu: T) = new JsonFormat[T#Value] {
    def write(obj: T#Value) = JsString(obj.toString)
    def read(json: JsValue) = json match {
      case JsString(txt) => enu.withName(txt)
      case something     => deserializationError(s"Expected a value from enum ${enu} instead of ${something}")
    }
  }

  implicit val nodeTypeFormat = jsonEnum(NodeType)
  implicit val managementPolicyFormat = jsonEnum(ManagementPolicy)
}
