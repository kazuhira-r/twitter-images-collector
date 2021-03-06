name := "twitter-images-collector"

version := "0.1.0"

organization := "org.littlewings"

scalaVersion := "2.12.4"

updateOptions := updateOptions.value.withCachedResolution(true)

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")

assemblyJarName in assembly := "twitter-images-collector.jar"
mainClass in assembly := Some("org.littlewings.twitterimages.Bootstrap")

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.6" % Compile,
  "com.github.scopt" %% "scopt" % "3.7.0" % Compile,
  "com.squareup.okhttp3" % "okhttp" % "3.9.1" % Compile,
  "org.slf4j" % "slf4j-api" % "1.7.25" % Compile,
  "org.slf4j" % "slf4j-simple" % "1.7.25" % Compile,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)
