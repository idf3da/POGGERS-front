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
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object SNS extends App{

    implicit val system = ActorSystem(Behaviors.empty, "SprayExample")
    implicit val executionContext = system.executionContext

    case class User(
                       userid: Int,
                       username: String,
                       password: String,
                       email: String
                   )

    var users: List[User] = Nil

    implicit val userFormat = jsonFormat4(User)

    def Register(user: User): Future[Done] = {
        users = user :: users
        Future { Done }
    }

    def GetUser(userid: Int): Future[Option[User]] = Future {
        users.find(u => u.userid == userid)
    }

    val route: Route =
        concat(
            get {
                pathPrefix("user" / IntNumber) { userid =>
                    val maybeUser: Future[Option[User]] = GetUser(userid)
                    onSuccess(maybeUser) {
                        case Some(user) => complete(user)
                        case None       => complete(StatusCodes.NotFound)
                    }
                }
            },
            post {
                path("register") {
                    entity(as[User]) { user =>
                        val saved: Future[Done] = Register(user)
                        onSuccess(saved) { _ =>
                            complete("User registered.")
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