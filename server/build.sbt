name := "server"

version := "0.1"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.3"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
    "org.specs2" %% "specs2-core" % "4.5.1" % Test,
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "ch.qos.logback"%"logback-classic"%"1.2.3",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0"
)
