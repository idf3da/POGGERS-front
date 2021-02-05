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
import slick.jdbc.H2Profile.api._
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import java.sql.SQLException
import scala.util.{Failure, Success, Try}
import server.User
import server.UsersTable



object SNS extends App{

    implicit val system = ActorSystem(Behaviors.empty, "SprayExample")
    implicit val executionContext = system.executionContext

    implicit val userFormat = jsonFormat4(User)

    val db = Database.forConfig("db")
    val users = TableQuery[UsersTable]


    def GetUser(userid: Int): Future[Seq[User]] = {
        val userFilter = users.filter(_.userid === userid).result
        val filterUserFuture: Future[Seq[User]] = db.run(userFilter)
        filterUserFuture
    }


    def Register(newUser: User): Future[Int] = {
        val insertUserQuery = users += newUser
        val resultFuture: Future[Int] = db.run(insertUserQuery)
        resultFuture
    }



    val route: Route =
        concat(
            get {
                pathPrefix("user" / IntNumber) { userid =>
                    val resultFuture = GetUser(userid)
                    Try(Await.ready(resultFuture, 5.second)) match {
                        case Success(f) => f.value.get match {
                            case Success(res) => res.length match {
                                case 0 => complete("Man, dis guy was not found.....")
                                case 1 => complete(s"Jesus Christ it's Jason Born\n${res}")
                                case _ => complete("There were several users with that ID.\nSomething certainly went wrong with DB.")
                            }
                            case Failure(e) => complete(s"NOT SUCCESS\n${e.getMessage}")
                        }
                        case Failure(_) => complete("OOPSIE THE TIMEOUT HAS HAPPENED...")
                    }
                }
            },
            post {
                path("register") {
                    entity(as[User]) { user =>
                        val resultFuture = Register(user)
                        Try(Await.ready(resultFuture, 5.second)) match {
                            case Success(f) => f.value.get match {
                                case Success(res) => complete("User registered!")
                                case Failure(e) => complete(s"UwU something went wrong...\n${e.getMessage}")
                            }
                            case Failure(_) => complete("Oopsie OwU db has timed out....")
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