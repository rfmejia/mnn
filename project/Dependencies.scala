import sbt._

object MainDependencies {
  val akkaVersion = MnnBuild.akkaVersion

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor"                        % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core"                    % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental"            % akkaVersion,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
  )

  val slick = Seq(
    "com.typesafe.slick" %% "slick"     % "3.1.1",
    "org.slf4j"          %  "slf4j-api" % "1.7.18")

  val authentikat       = "com.jason-goodwin"      %% "authentikat-jwt"          % "0.3.5"
  val config            = "com.typesafe"           %  "config"                   % "1.3.0"
  val parserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
  val scalaz            = "org.scalaz"             %% "scalaz-core"              % "7.2.1"
  val sprayJson         = "io.spray"               %% "spray-json"               % "1.3.2"

  // Local dependencies
  val mnnCommon = "jp.riken.mnn"           %% "mnn-common"      % "0.1-SNAPSHOT"
}

object TestDependencies {
  val akkaVersion = MnnBuild.akkaVersion

  val akkaTestkit = Seq(
    "com.typesafe.akka" %% "akka-testkit"      % akkaVersion % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion % "test")

  val scalatest  = "org.scalatest"  %% "scalatest"  % "2.2.6"  % "test"
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
}

