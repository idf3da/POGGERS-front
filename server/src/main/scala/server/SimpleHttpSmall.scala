package server

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import server.HealthRoute

object SimpleHttpSmall extends App {

    implicit val system: ActorSystem = ActorSystem("simple-http")
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    implicit val log = Logging(system, "main")

    val port = 8080

    val bindingFuture =
        Http().bindAndHandle(HealthRoute.healthRoute, "localhost", port)

    log.info(s"Server started at the port $port")
}