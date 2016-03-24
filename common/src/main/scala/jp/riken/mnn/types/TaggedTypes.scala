package jp.riken.mnn.types

import scala.language.implicitConversions
import scala.util.matching.Regex
import scalaz._
import scalaz.Scalaz._

/** Contains unboxed tagged types for common datatypes such as valid string formats (identifiers, usernames) or numeric
  * formats (positive values). Used for type checking and runtime validation. This strategy avoids creating an in-memory
  * object as opposed to implicit classes.
  */
object TaggedTypes {
  /** Implicitly convert tag back to its base type */
  implicit def unwrapTag[T](t: @@[T, _]): T = Tag.unwrap(t)

  protected object definitions {
    trait Validator {
      val pattern: Regex
      val errorMessage: String

      def validate(s: String): Unit = require(pattern.findFirstIn(s).nonEmpty, errorMessage)
    }

    object IdentifierTag extends Validator {
      val pattern = """[a-zA-Z][a-zA-Z\d_-]{2,}[a-zA-Z\d]""".r
      val errorMessage = "An identifier must be at least 4 characters long, start with a letter, " +
        "and contain only letters, numbers, hyphens and underscores"
    }

    object UsernameTag extends Validator {
      val pattern = """[a-zA-Z][a-zA-Z\d_]*""".r
      val errorMessage = "A username must start with a letter and contain only letters, numbers, and underscores"
    }

    object EmailTag extends Validator {
      val pattern = """^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*
                      |@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$""".stripMargin.r
      val errorMessage = "An email must start with a letter and contain only letters, numbers, and underscores"
    }
  }

  import definitions._

  type StringTag = @@[String, _]

  type Identifier = String @@ IdentifierTag.type
  type Username = String @@ UsernameTag.type
  type Email = String @@ EmailTag.type

  /** Value class for string to tagged typed converters */
  implicit class TaggedString(val s: String) extends AnyVal {
    private def applyTag[T](validator: Validator)(s: String): T = {
      validator.validate(s)
      s.asInstanceOf[T]
    }

    def asIdentifier = applyTag[Identifier](definitions.IdentifierTag)(s)

    def asUsername = applyTag[Username](definitions.UsernameTag)(s)

    def asEmail = applyTag[Email](definitions.EmailTag)(s)
  }

  /** Case-insensitive comparison of string tags */
  def caseInsensitiveStringTagOrder[T <: StringTag]: Order[T] = Order.order({ (a, b) =>
    val _a: String = Tag.unwrap(a)
    val _b: String = Tag.unwrap(b)
    (_a.toLowerCase ?|? _b.toLowerCase)
  })

  implicit val identifierOrder = caseInsensitiveStringTagOrder[Identifier]
  implicit val usernameOrder = caseInsensitiveStringTagOrder[Username]
  implicit val emailOrder = caseInsensitiveStringTagOrder[Email]
}
