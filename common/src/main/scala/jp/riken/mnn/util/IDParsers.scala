package jp.riken.mnn.util

import java.util.UUID
import jp.riken.mnn.types._
import jp.riken.mnn.types.TaggedTypes._
import scala.util.parsing.combinator.JavaTokenParsers
import scalaz._
import scalaz.Scalaz._

trait IDParser[T <: ID] extends JavaTokenParsers {
  private val prefix = "mnn::"

  def structure: Parser[T]

  def parse(s: String): Validation[String, T] =
    if (s.startsWith(prefix)) parseUrlSegment(s.drop(prefix.length))
    else parseUrlSegment(s)

  def parseUrlSegment(s: String): Validation[String, T] =
    parseAll(structure, s) match {
      case Success(id, _)    => id.success[String]
      case NoSuccess(err, _) => s"'${s}' is an invalid ID".failure[T]
    }

  def javaUUID: Parser[UUID] = ".+".r ^^ { case uuid => UUID.fromString(uuid) }
}

trait IDParsers {
  def parseId(s: String): Validation[String, ID] = {
    List(UserIDParser, CollectionIDParser, ResourceIDParser, RevisionIDParser)
      .map(_.parse(s))
      .collectFirst({ case Success(id) => id })
      .toSuccess("Invalid ID")
  }

  implicit object NodeIDParser extends IDParser[NodeID] {
    def structure = ident ^^ {
      case namespace => NodeID(namespace.asIdentifier)
    }
  }

  implicit object UserIDParser extends IDParser[UserID] {
    def structure = ident ~ "/" ~ ident ^^ {
      case namespace ~ _ ~ username => UserID(namespace.asIdentifier, username.asUsername)
    }
  }

  implicit object CollectionIDParser extends IDParser[CollectionID] {
    def structure = ident ~ "/" ~ ident ~ "/" ~ ident ^^ {
      case namespace ~ _ ~ username ~ _ ~ collectionName =>
        CollectionID(namespace.asIdentifier, username.asUsername, collectionName.asIdentifier)
    }
  }

  implicit object ResourceIDParser extends IDParser[ResourceID] {
    def versionedId = ident ~ "." ~ wholeNumber ~ "." ~ wholeNumber
    def plainId = ident ~ "." ~ wholeNumber
    def temporaryId = javaUUID

    def structure =
      (versionedId ^^ { case namespace ~ _ ~ serial ~ _ ~ version => VersionedID(namespace.asIdentifier, serial.toInt, version.toInt) }
        | plainId ^^ { case namespace ~ _ ~ serial => PlainID(namespace.asIdentifier, serial.toInt) }
        | temporaryId ^^ { case uuid => TemporaryID(uuid) })
  }

  implicit object SerialIDParser extends IDParser[SerialID] {
    def versionedId = ident ~ "." ~ wholeNumber ~ "." ~ wholeNumber
    def plainId = ident ~ "." ~ wholeNumber
    def structure =
      (versionedId ^^ { case namespace ~ _ ~ serial ~ _ ~ version => VersionedID(namespace.asIdentifier, serial.toInt, version.toInt) }
        | plainId ^^ { case namespace ~ _ ~ serial => PlainID(namespace.asIdentifier, serial.toInt) })
  }

  implicit object InstanceIDParser extends IDParser[InstanceID] {
    def versionedId = ident ~ "." ~ wholeNumber ~ "." ~ wholeNumber
    def temporaryId = javaUUID
    def structure =
      (versionedId ^^ { case namespace ~ _ ~ serial ~ _ ~ version => VersionedID(namespace.asIdentifier, serial.toInt, version.toInt) }
        | temporaryId ^^ { case uuid => TemporaryID(uuid) })
  }

  implicit object VersionedIDParser extends IDParser[VersionedID] {
    def structure = ident ~ "." ~ wholeNumber ~ "." ~ wholeNumber ^^ {
      case namespace ~ _ ~ serial ~ _ ~ version => VersionedID(namespace.asIdentifier, serial.toInt, version.toInt)
    }
  }

  implicit object PlainIDParser extends IDParser[PlainID] {
    def structure = ident ~ "." ~ wholeNumber ^^ {
      case namespace ~ _ ~ serial => PlainID(namespace.asIdentifier, serial.toInt)
    }
  }

  implicit object RevisionIDParser extends IDParser[RevisionID] {
    def structure = ident ~ "/" ~ ident ~ "/" ~ ident ~ "/" ~ javaUUID ^^ {
      case namespace ~ _ ~ username ~ _ ~ collectionName ~ _ ~ uuid =>
        RevisionID(namespace.asIdentifier, username.asUsername, collectionName.asIdentifier, uuid)
    }
  }

  implicit object TemporaryIDParser extends IDParser[TemporaryID] {
    def structure = javaUUID ^^ { case uuid => TemporaryID(uuid) }
  }
}

object IDParsers extends IDParsers
