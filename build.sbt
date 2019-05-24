name := "kafka-streams-warsaw-meetup"

scalaVersion := "2.12.8"

libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.2.0"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.26"

// sbt-native-packager
enablePlugins(JavaAppPackaging)
