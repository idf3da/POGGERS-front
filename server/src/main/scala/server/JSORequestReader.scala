package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.Done
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes

import scala.concurrent.Future
import scala.io.StdIn
// for JSON serialization/deserialization following dependency is required:
// "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.7"
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object JSORequestReader extends App{

    implicit val system = ActorSystem(Behaviors.empty, "SprayExample")
    implicit val executionContext = system.executionContext

    var orders: List[Item] = Nil

    final case class Item(name: String, id: Long)
    final case class Order(items: List[Item])

    implicit val itemFormat = jsonFormat2(Item)
    implicit val orderFormat = jsonFormat1(Order)

    def fetchItem(itemId: Long): Future[Option[Item]] = Future {
        orders.find(o => o.id == itemId)
    }

    def saveOrder(order: Order): Future[Done] = {
        orders = order match {
            case Order(items) => items ::: orders
            case _            => orders
        }
        Future { Done }
    }

    val route: Route =
        concat(
            get {
                pathPrefix("item" / LongNumber) { id =>
                    val maybeItem: Future[Option[Item]] = fetchItem(id)
                    onSuccess(maybeItem) {
                        case Some(item) => complete(item)
                        case None       => complete(StatusCodes.NotFound)
                    }

                }
            },
            post {
                path("create-order") {
                    entity(as[Order]) { order =>
                        val saved: Future[Done] = saveOrder(order)
                        onSuccess(saved) { _ =>
                            complete("Order created.")
                        }
                    }
                }
            }
        )

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
            .flatMap(_.unbind()) // trigger unbinding from the port
            .onComplete(_ => system.terminate()) // and shutdown when done
}