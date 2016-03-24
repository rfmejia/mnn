package jp.riken.mnn.types

import org.scalatest._
import org.scalatest.prop._

/*class IDPropertiesSpec extends PropSpec with PropertyChecks with Matchers {
  forAll { (n: Int) =>
    whenever(n > 0) {
      n should be >= 1
    }
  }*/

/*"All IDs" should {

    "not be created using empty or null strings" in {
      ResourceID.parse("") must beLike { case Failure(f) => ok }
      ResourceID.parse(null) must beLike { case Failure(f) => ok }
    }

    "not be accept invalid formats" in {
      ResourceID.parse("ABC12#") must beLike { case Failure(f) => ok }
      ResourceID.parse("ABC12#") must beLike { case Failure(f) => ok }
      ResourceID.extractNamespace("ABC-123.") must beLike { case Success(f) => ok }
      ResourceID.extractNamespace("ABC_123.") must beLike { case Success(f) => ok }
    }

    "check for parsed plain ID equality" in {
      ResourceID.parse("abc.123") must beLike {
        case Success(id) => id must beLike {
          case plain: PlainID => plain === PlainID("abc", 123)
        }
      }
    }

    "check for parsed versioned ID equality" in {
      ResourceID.parse("abc.123.1") must beLike {
        case Success(id) => id === VersionedID("abc", 123, 1)
      }
    }
  }
  */
//}
