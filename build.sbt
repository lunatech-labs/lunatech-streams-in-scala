import Dependencies._

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"
val AkkaVersion = "2.8.3"
lazy val root = (project in file("."))
  .settings(
    name := "stream-in-scala",
    libraryDependencies  ++= Seq(
      munit % Test,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
       "co.fs2" %% "fs2-core" % "3.7.0",
       "co.fs2" %% "fs2-reactive-streams" % "3.7.0",
       "co.fs2" %% "fs2-io" % "3.7.0",
      "dev.zio" %% "zio-streams" % "2.0.13",
      "dev.zio" %% "zio-nio" % "2.0.1"

    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
