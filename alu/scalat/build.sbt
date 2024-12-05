ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

lazy val root = (project in file("."))
  .settings(
    name := "scalat"
  )

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

unmanagedBase := baseDirectory.value / "lib"

Test / javaOptions ++= Seq(
  "-XX:+UseZGC", // 使用 ZGC
  "-XX:SoftRefLRUPolicyMSPerMB=50" // 减少软引用保留时间
)

