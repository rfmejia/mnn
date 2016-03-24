package jp.riken.mnn.types

import java.util.UUID
import jp.riken.mnn.types.TaggedTypes._

/** Defines the ID hierarchy for all resources in the system.
  *
  * NodeID:                        mnn://<ns>
  * - UserID:                            <ns>/<username>
  * - CollectionID:                      <ns>/<username>/<collection>
  * - ResourceID:
  * - PlainID                          <ns>.<serial>
  * - VersionedID                    <ns>.<serial>.<version>
  * - TemporaryID                      <uuid>
  * - RevisionID                         <ns>/<username>/<collection>/<uuid>
  */

sealed trait ID extends Ordered[ID] {
  val urn: String
  val urlSegment: String

  override def toString = urn

  override def hashCode = this.urn.hashCode

  def compare(that: ID) = this.urn.compareTo(that.urn)
}

case class NodeID(namespace: Identifier) extends ID {
  val urn = s"mnn::${namespace}"
  val urlSegment = s"${namespace}"
}

case class UserID(namespace: Identifier, username: Username) extends ID {
  val urn = s"mnn::${namespace}/${username}"
  val urlSegment = s"${username}"
}

case class CollectionID(namespace: Identifier, username: Username, collectionName: Identifier) extends ID {
  val urn = s"mnn::${namespace}/${username}/${collectionName}"
  val urlSegment = s"${username}/${collectionName}"
  val userId = UserID(namespace, username)
}

case class RevisionID(namespace: Identifier, username: Username, collectionName: Identifier, uuid: UUID) extends ID {
  val urn = uuid.toString
  val urlSegment = urn
  val collectionId = CollectionID(namespace, username, collectionName)
}

sealed trait ResourceID extends ID

/** ID assigned to **committed** resources, hence assigned a serial (abstract or concrete). */
sealed trait SerialID extends ResourceID {
  val serial: Int
  val hasVersion: Boolean
  val asPlainID: PlainID
}

/** ID assigned to **concrete** resource instances (draft or committed). */
sealed trait InstanceID extends ResourceID
case class PlainID(namespace: Identifier, serial: Int) extends SerialID {
  val urn = f"mnn::${namespace}.${serial}%06d"
  val urlSegment = f"${namespace}.${serial}%06d"
  val hasVersion = false
  val asPlainID = this
}
case class VersionedID(namespace: Identifier, serial: Int, version: Int)
    extends SerialID with InstanceID {
  val urn = f"mnn::${namespace}.${serial}%06d.${version}"
  val urlSegment = f"${namespace}.${serial}%06d.${version}"
  val hasVersion = true
  val asPlainID = PlainID(namespace, serial)

  lazy val nextVersion: VersionedID = VersionedID(namespace, serial, version + 1)
}

case class TemporaryID(uuid: UUID) extends InstanceID {
  val urn = uuid.toString
  val urlSegment = urn
}
object TemporaryID {
  def random = TemporaryID(UUID.randomUUID)
}
