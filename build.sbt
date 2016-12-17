name := "twitter-images-collector"

version := "0.1.0"

organization := "org.littlewings"

scalaVersion := "2.12.1"

updateOptions := updateOptions.value.withCachedResolution(true)

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature", "-Xexperimental")

assemblyJarName in assembly := "twitter-images-collector.jar"
mainClass in assembly := Some("org.littlewings.twitterimages.Bootstrap")

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "4.0.5" % Compile,
  "com.github.scopt" %% "scopt" % "3.5.0" % Compile,
  "com.squareup.okhttp3" % "okhttp" % "3.4.1" % Compile,
  "org.slf4j" % "slf4j-api" % "1.7.21" % Compile,
  "org.slf4j" % "slf4j-simple" % "1.7.21" % Compile,
  "org.scalatest" %% "scalatest" % "3.0.0" % Test
)
