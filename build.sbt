name := "scala-samples"
version := "0.1"
scalaVersion := "2.13.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "com.h2database" % "h2" % "1.4.199",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

//scalacOptions += "-deprecation"
