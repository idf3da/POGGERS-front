package server

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object HealthRoute {
    val healthRoute: Route =
        path("health") {
            get {
                complete(StatusCodes.OK)
            }
        }
        path("test") {
            get {
                complete(HttpResponse(entity = "OWO"))
            }
        }
}