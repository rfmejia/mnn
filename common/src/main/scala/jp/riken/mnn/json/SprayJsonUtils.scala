package jp.riken.mnn.json

import spray.json._

trait SprayJsonUtils {
  def valueToString(v: JsValue): String = v match {
    case JsString(value) => value
    case value: JsValue  => value.toString
  }

  def valueToJsString(v: JsValue): JsString = v match {
    case s: JsString    => s
    case value: JsValue => JsString(valueToString(value))
  }

  def valueToList(v: JsValue): Vector[JsValue] = v match {
    case JsArray(elements) => elements
    case value: JsValue    => Vector(value)
  }

  def anyToJsValue(any: Any): JsValue = any match {
    case s: String  => JsString(s)
    case n: Int     => JsNumber(n)
    case n: Long    => JsNumber(n)
    case n: Double  => JsNumber(n)
    case b: Boolean => JsBoolean(b)
    case ls: Iterable[_] =>
      val arr = ls.map(l => anyToJsValue(l))
      JsArray(arr.toVector)
    case js: JsValue => js
    case _           => JsString(any.toString)
  }

  def alphabeticalJsField(e1: JsField, e2: JsField): Boolean =
    e1._1.toLowerCase < e2._1.toLowerCase

  def find(js: JsValue, keys: String): Option[JsValue] = find(js, List(keys))
  def find(js: JsValue, keys: String*): Option[JsValue] = find(js, keys.toList)

  def find(js: JsValue, keys: Iterable[String]): Option[JsValue] = js match {
    case JsObject(fields) => find(fields, keys)
    case JsArray(elements) => {
      elements.map({
        case o: JsObject => find(o, keys)
        case _           => None
      }).reduceOption((a, b) => a orElse b).get
    }
    case _ => None
  }

  def find(m: Iterable[JsField], keys: Iterable[String]): Option[JsValue] = keys match {
    case Nil      => None
    case h :: Nil => m.find(_._1 == h) map (_._2)
    case h :: t => m.find(_._1 == h) map (_._2) flatMap {
      case value => find(value, t)
    }
  }
}

object SprayJsonUtils extends SprayJsonUtils
