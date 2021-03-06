/*
 * Copyright 2013-2015 Websudos, Limited.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Explicit consent must be obtained from the copyright owner, Websudos Limited before any redistribution is made.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import com.twitter.sbt.{GitProject, VersionManagement}

lazy val Versions = new {
  val util = "0.18.2"
  val finagle = "6.35.0"
  val spark = "1.2.1"
  val FinaglePostgres = "0.1.0-SNAPSHOT"
  val FinagleZkVersion = "6.28.0"
  val shapeless = "2.2.5"
  val diesel = "0.2.4"
  val lift = "3.0-RC3"
  val slf4j = "1.7.21"
  val joda = "2.9.4"
  val jodaConvert = "1.8.1"
  val twitterUtil = "6.33.0"
}

val liftVersion: String => String = {
  s => CrossVersion.partialVersion(s) match {
    case Some((major, minor)) if minor >= 11 => Versions.lift
    case _ => "3.0-M1"
  }
}

val bintrayPublishing: Seq[Def.Setting[_]] = Seq(
  publishMavenStyle := true,
  bintrayOrganization := Some("websudos"),
  bintrayRepository := "oss-releases",
  bintrayReleaseOnPublish := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => true },
  licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
)

val sharedSettings: Seq[Def.Setting[_]] = Seq(
  organization := "com.websudos",
  version := "0.3.0",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", "2.11.8"),
  resolvers ++= Seq(
    Resolver.typesafeRepo("releases"),
    Resolver.sonatypeRepo("releases"),
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("websudos", "oss-releases"),
    "Twitter Repository" at "http://maven.twttr.com"
  ),
  scalacOptions ++= Seq(
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-language:existentials",
    "-Yinline-warnings",
    "-Xlint",
    "-deprecation",
    "-feature",
    "-unchecked"
  ),
  fork in Test := true,
  javaOptions in Test ++= Seq("-Xmx2G")
) ++ bintrayPublishing ++
  VersionManagement.newSettings ++
  GitProject.gitSettings

lazy val morpheus = (project in file("."))
  .settings(sharedSettings: _*)
  .settings(
    name := "morpheus",
    moduleName := "morpheus"
  ).aggregate(
    morpheusDsl,
    morpheusMySQL,
    // morpheusPostgres,
    // morpheusSpark,
    morpheusTestkit
    // morpheusZookeeper
  )

  lazy val morpheusDsl = (project in file("morpheus-dsl"))
    .settings(sharedSettings: _*)
    .settings(
      name := "morpheus-dsl",
      moduleName := "morpheus-dsl",
      libraryDependencies ++= Seq(
        "com.twitter" %% "util-core" % Versions.twitterUtil,
        "org.slf4j" % "slf4j-api" % Versions.slf4j,
        "com.websudos" %% "diesel-engine" % Versions.diesel,
        "com.chuusai" %% "shapeless" % Versions.shapeless,
        "org.scala-lang" % "scala-reflect" % scalaVersion.value,
        "joda-time" % "joda-time" % Versions.joda,
        "org.joda" % "joda-convert" % Versions.jodaConvert,
        "net.liftweb" %% "lift-json" % liftVersion(scalaVersion.value) % Test
      )
    ).dependsOn(
      morpheusTestkit % Test
    )

  lazy val morpheusMySQL = (project in file("morpheus-mysql"))
    .settings(sharedSettings: _*)
    .settings(
      moduleName := "morpheus-mysql",
      name := "morpheus-mysql",
      libraryDependencies ++= Seq(
        "com.twitter" %% "finagle-mysql" % Versions.finagle
      )
    ).dependsOn(
      morpheusDsl,
      morpheusTestkit % Test
    )

  lazy val morpheusPostgres = (project in file("morpheus-postgres"))
    .settings(sharedSettings: _*)
    .settings(
      name := "morpheus-postgres",
      moduleName := "morpheus-postgres",
      libraryDependencies ++= Seq(
        // "com.twitter" %% "finagle-postgres" % "0.1.0-SNAPSHOT"
      )
    ).dependsOn(
      morpheusDsl,
      morpheusTestkit % Test
    )

/*
  lazy val morpheusZookeeper = Project(
    id = "morpheus-zookeeper",
    base = file("morpheus-zookeeper"),
    settings = Defaults.coreDefaultSettings ++ sharedSettings
  ).settings(
    name := "morpheus-zookeeper",
    libraryDependencies ++= Seq(
      "com.twitter"                  %% "finagle-zookeeper"                 % FinagleZkVersion,
      "com.websudos"                 %% "util-zookeeper"                    % UtilVersion
    )
  )

  lazy val morpheusSpark = Project(
    id = "morpheus-spark",
    base = file("morpheus-spark"),
    settings = Defaults.coreDefaultSettings ++ sharedSettings
  ).settings(
    name := "morpheus-spark",
    libraryDependencies ++= Seq(
      "org.apache.spark"             %% "spark-sql"                    % SparkVersion
    )
  ).dependsOn(
    morpheusTestkit
  )*/


  lazy val morpheusTestkit = (project in file("morpheus-testkit"))
    .settings(sharedSettings: _*)
    .settings(
    name := "morpheus-testkit",
    libraryDependencies ++= Seq(
      "com.h2database"                   % "h2"                        % "1.4.181",
      "com.outworkers"                   %% "util-testing"             % Versions.util excludeAll {
        ExclusionRule("org.scala-lang", "scala-reflect")
      }
    )
  ).dependsOn(
    // morpheusZookeeper
  )
