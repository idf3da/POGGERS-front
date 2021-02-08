//package server
//
//import play.api.libs.json.{Json, Reads}
//import slick.jdbc.H2Profile.api._
//
//import java.sql.Timestamp
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.{Await, Future}
//import scala.concurrent.duration._
//import scala.util.{Failure, Success, Try}
//
//case class User(
//
//                       username: String,
//                       password: String,
//                       email: String
//               )
//
//
//
//class UsersTable(tag: Tag) extends Table[User](tag,"users") {
//    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
//    def username  = column[String]("username")
//    def password = column[String]("password")
//    def email = column[String]("email")
//    def * = (username, password, email).mapTo[User]
//}
//
//case class LoginRequest(
//                           username: String,
//                           password: String
//                       )
//
//object LoginRequestObj {
//    implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
//}
//
//case class Post (
//                        creatorid: Int,
//                        title: String,
//                        descriptorid: Int,
//                        description: String
//                )
//
//class PostsTable(tag: Tag) extends Table[Post](tag,"posts") {
//    def postid = column[Int]("postid", O.PrimaryKey, O.AutoInc)
//    def creatorid = column[Int]("creatorid")
//    def title  = column[String]("title")
//    def descriptorid = column[Int]("descriptorid")
//    def created_at = column[Timestamp]("created_at")
//    def updated_at = column[Timestamp]("updated_at")
//    def description = column[String]("description")
//    def * = (creatorid, title, descriptorid, description).mapTo[Post]
//}
//
//object PostgreSQLTest extends App {
//    val db = Database.forConfig("db")
//    val posts = TableQuery[PostsTable]
//    val newPost = Post(1, "Title1", 404, "Some description")
//    val resultFuture = db.run(posts += newPost)
//
//
//
//    val result = Await.ready(resultFuture, 2.seconds)
//    result.onComplete {
//        case Success(v) => println("EEEEEEEE", v)
//        case Failure(e) => println(e)
//        case _ => println("ALL")
//    }
//
//}
//
////object PostgreSQLTest extends App {
////
////    def Register(newUser: User): Future[Int] = {
////        val insertUserQuery = users += newUser
////        val resultFuture: Future[Int] = db.run(insertUserQuery)
////        resultFuture
////    }
////
////    def GetUser(userid: Int): Future[Seq[User]] = Future {
////        val userFilter = users.filter(_.userid === userid).result
////        val userFuture: Future[Seq[User]] = db.run(userFilter)
////        Await.result(userFuture, 2.seconds)
////    }
////
////    val db = Database.forConfig("db")
////    val users = TableQuery[UsersTable]
////    val newUser = User("noidtest", "password", "noidtest@mail.ru")
////
////    var resultFuture = Register(newUser)
////
////    val result = Await.ready(resultFuture, 2.seconds)
////    result.onComplete {
////        case Success(v) => println("EEEEEEEE", v)
////        case Failure(e) => println(e)
////        case _ => println("ALL")
////    }
////
////    def userLogin(loginRequest: LoginRequest): String = {
////        val usernameFilter = users.filter(_.username === loginRequest.username).result
////        val resultFuture: Future[Seq[User]] = db.run(usernameFilter)
////        Try(Await.ready(resultFuture, 5.second)) match {
////            case Success(f) => f.value.get match {
////                case Success(res) => res.length match {
////                    case 1 => if (res.head.password == loginRequest.password) "Login successful." else "Wrong password."
////                    case 0 => "User not found."
////                    case _ => "Several users found."
////                }
////                case Failure(e) => s"Login DB ERROR: ${e.getMessage}"
////            }
////            case Failure(e) => s"DB timeout: ${e.getMessage}"
////        }
////    }
////
////    println(userLogin(LoginRequest("noidtest", "password")))
////}