import sbt._
import sbt.Keys._

object MnnBuild extends Build {
  val akkaVersion = "2.4.2"

  override def settings = super.settings ++ Seq(
    organization := "jp.riken.mnn",
    version := "0.5.0-SNAPSHOT",
    scalaVersion in ThisBuild := "2.11.7",
    // Show (sub)project name in prompt
    shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " },
    scalacOptions ++= Seq(
      "-Xlint",
      "-deprecation",
      "-Xfatal-warnings",
      "-feature",
      "-unchecked",
      "-encoding", "utf8")
  )

  import MainDependencies._
  import TestDependencies._
  import ScalariformSettings._

  lazy val root = Project(id = "mnn", base = file("."))
    .aggregate(common, nodeCommon, registryNode)
    .settings(
      scalariformSettings,
      // don't publish an empty jar for the root project
      publish := {},
      publishLocal := {}
    )

  lazy val common = Project(id = "common", base = file("common"))
    .settings(
      name := "mnn-common",
      scalariformSettings,
      libraryDependencies ++= Seq(
        parserCombinators,
        sprayJson,
        scalaz,
        scalatest,
        scalacheck
      )
    )

  lazy val nodeCommon = Project(id = "node-common", base = file("node-common"))
    .dependsOn(common)
    .settings(
      name := "mnn-node-common",
      scalariformSettings,
      libraryDependencies ++= Seq(
        config,
        scalaz
      ) ++ akka ++ slick ++ akkaTestkit
    )

  lazy val registryNode = Project(id = "registry-node", base = file("registry-node"))
    .dependsOn(common, nodeCommon)
    .settings(
      name := "mnn-registry-node",
      scalariformSettings,
      mainClass in Compile := Some("jp.riken.mnn.node.registry.HttpServer"),
      libraryDependencies ++= Seq(
        config,
        scalaz
      ) ++ akka ++ slick ++ akkaTestkit
    )

}

