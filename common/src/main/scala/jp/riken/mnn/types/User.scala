package jp.riken.mnn.types

import jp.riken.mnn.types.TaggedTypes._

case class User(userId: Option[UserID], username: Username, password: Option[Array[Byte]],
  salt: Option[Array[Byte]], name: String, email: Email)

object User {
  def apply(username: Username, name: String, email: Email): User = User(None, username, None, None, name, email)
}
