package jp.riken.mnn.types

import java.net.URI
import java.time.ZonedDateTime

import jp.riken.mnn.types.TaggedTypes._

/** Lists information about a node in the network. */
case class Node(id: Option[NodeID], modified: Option[ZonedDateTime], namespace: Identifier, href: URI, nodeType: NodeType.Value)

sealed trait NodeType
object NodeType extends Enumeration {
  val Registry, Data = Value
}

