package jp.riken.mnn.types

import jp.riken.mnn.types.TaggedTypes._
import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.util.{ Random, Success, Try }

class IdentiferSpec extends WordSpec with GeneratorDrivenPropertyChecks with Matchers {
  def otherCharsGen: Gen[Char] = Gen.oneOf[Char](Seq('-', '_'))

  def validGen(length: Int = 12): Gen[String] = {
    require(length >= 2)
    for {
      start <- Gen.alphaChar
      mid <- Gen.listOfN(length - 2, Gen.frequency((9, Gen.alphaNumChar), (1, otherCharsGen)))
      end <- Gen.alphaNumChar
    } yield (start +: mid :+ end).mkString
  }

  "An identifier string" when {

    "parsed from some string" must {

      "accept letters, numbers, a dash and an underline" in {
        forAll(validGen()) { (s: String) =>
          Try(s.asIdentifier) shouldBe a[Success[_]]
        }
      }

      "allow for case-insensitive matching" in {
        def shuffleCase(s: String) = s.map { c =>
          if (Random.nextBoolean()) {
            if (c.isUpper) c.toLower
            else c.toUpper
          }
          else c
        }

        forAll(validGen(200)) { (s: String) =>
          val shuffled = shuffleCase(s)
          s.asIdentifier === shuffled.asIdentifier
        }
      }

      "not accept invalid characters" in {
        def invalidChar = Gen.choose(Char.MinValue, Char.MaxValue).suchThat(c => !(c.isLetterOrDigit && "-_".contains(c)))
        def invalidGen = Gen.listOfN(4, invalidChar).map(_.mkString)

        forAll(invalidGen) { (s: String) =>
          intercept[IllegalArgumentException](s.asIdentifier)
        }
      }

      "not accept empty strings" in {
        intercept[IllegalArgumentException]("".asIdentifier)
      }

      "not accept strings that do not begin with a letter" in {
        def invalidGen: Gen[String] = for {
          start <- Gen.oneOf(Gen.numChar, otherCharsGen)
        } yield start + "xx"

        forAll(invalidGen) { (s: String) =>
          intercept[IllegalArgumentException](s.asIdentifier)
        }
      }

      "not accept strings that end with a dash or underscore" in {
        def invalidGen: Gen[String] = for {
          start <- Gen.alphaChar
          end <- otherCharsGen
        } yield start + "xx" + end

        import org.scalacheck.Shrink.shrinkString
        forAll(invalidGen) { (s: String) =>
          intercept[IllegalArgumentException](s.asIdentifier)
        }
      }

      "not accept strings shorter than 4 characters long" in {
        forAll(validGen(2)) { (s: String) =>
          intercept[IllegalArgumentException](s.asIdentifier)
        }
        forAll(validGen(3)) { (s: String) =>
          intercept[IllegalArgumentException](s.asIdentifier)
        }
      }

    }
  }
}
