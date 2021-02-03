//package server
//
//import akka.actor.typed.ActorSystem
//import akka.actor.typed.scaladsl.Behaviors
//import akka.http.scaladsl.Http
//import akka.Done
//import akka.http.scaladsl.server.Route
//import akka.http.scaladsl.server.Directives._
//import akka.http.scaladsl.model.StatusCodes
//
//import scala.concurrent.Future
//import scala.io.StdIn
//import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
//import server.PostgreSQLTest.{db, newUser, users}
//import spray.json.DefaultJsonProtocol._
//import slick.jdbc.H2Profile.api._
//import slick.jdbc.H2Profile.api._
//
//import scala.concurrent.Future
//import scala.concurrent.Await
//import scala.concurrent.duration._
//import java.sql.SQLException
//import scala.util.{Failure, Success}
//import server.User
//import server.UsersTable
//
//
//
//object SNS extends App{
//
//    implicit val system = ActorSystem(Behaviors.empty, "SprayExample")
//    implicit val executionContext = system.executionContext
//
//    val db = Database.forConfig("db")
//    def exec [T](action: DBIO[T]): T =
//        Await.result(db.run(action), 2.seconds)
//    val users = TableQuery[UsersTable]
//
//    implicit val userFormat = jsonFormat4(User)
//
//    def Register(user: User): Future[Int] = db.run { users += user }
//
//    def Register2(newUser: User): Future[Int] = db.run { users += newUser }
//
//
//    def GetUser(userid: Int): Future[Seq[User]] = Future {
//
//        val userFilter = users.filter(_.userid === userid).result
//        val userFuture: Future[Seq[User]] = db.run(userFilter)
//
//        Await.result(userFuture, 2.seconds)
//    }
//
//
//
//    val route: Route =
//        concat(
//            get {
//                pathPrefix("user" / IntNumber) { userid =>
//                    val maybeUser: Future[Seq[User]] = GetUser(userid)
//                    complete(maybeUser)
//                }
//            },
//            post {
//                path("register") {
//                    entity(as[User]) { user =>
//                        Register(newUser) onComplete {
//                            case Success(user) => complete(user)
//                            case Failure(t) => complete("Error: ", t)
//                        }
//                    }
//                }
//            }
//        )
//
//    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
//    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
//    StdIn.readLine() // let it run until user presses return
//    bindingFuture
//            .flatMap(_.unbind()) // trigger unbinding from the port
//            .onComplete(_ => system.terminate()) // and shutdown when done
//}