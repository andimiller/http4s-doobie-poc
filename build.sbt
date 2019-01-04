val Http4sVersion = "0.20.0-M4"
val Specs2Version = "4.2.0"
val LogbackVersion = "1.2.3"
val DoobieVersion = "0.6.0"

lazy val root = (project in file("."))
  .settings(
    organization := "net.andimiller",
    name := "http4s-doobie-poc",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.7",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.specs2"     %% "specs2-core"          % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.tpolecat"   %% "doobie-core"          % DoobieVersion,
      "io.circe"       %% "circe-generic"        % "0.10.1",
      "org.xerial"      % "sqlite-jdbc"          % "3.25.2",
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
