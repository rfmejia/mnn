package jp.riken.mnn.types

import java.time.ZonedDateTime

import jp.riken.mnn.types.TaggedTypes._

/** A container for user-uploaded resources */
case class Collection(
  id: Option[CollectionID],
  modified: Option[ZonedDateTime],
  owner: UserID,
  name: Identifier,
  description: Option[String],
  readme: Option[String],
  policy: ManagementPolicy.Value = ManagementPolicy.Public
)

object Collection extends ((Option[CollectionID], Option[ZonedDateTime], UserID, Identifier, Option[String], Option[String], ManagementPolicy.Value) => Collection) {
}

/** Scope of  permissions for a collection */
sealed trait ManagementPolicy
object ManagementPolicy extends Enumeration {
  val Private, Protected, Public = Value
}

