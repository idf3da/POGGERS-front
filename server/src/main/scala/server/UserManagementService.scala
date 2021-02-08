package server

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class UserManagementService {

    private val db = Database.forConfig("db")
    private val users = TableQuery[UsersTable]

    def userLogin(loginRequest: LoginRequest): String = {
        val usernameFilter = users.filter(_.username === loginRequest.username).result
        val resultFuture = db.run(usernameFilter)
        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 1 => if (res.head.password == loginRequest.password) "Login successful." else "Wrong password."
                    case 0 => "User not found."
                    case _ => "Several users found."
                }
                case Failure(e) => s"Login DB ERROR: ${e.getMessage}"
            }
            case Failure(e) => s"DB timeout: ${e.getMessage}"
        }
    }

    def createUser(newUser: RegisterUserRequest):  String = {
        val insertUserQuery = users += newUser
        val resultFuture: Future[Int] = db.run(insertUserQuery)

        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(_) => "User registered."
                case Failure(e) => s"ERROR: ${e.getMessage}"
            }
            case Failure(e) => s"DB timeout: ${e.getMessage}"
        }
    }

    def protectedContent: String  = "Super mega secret content that nobody needs to see if you know what I mean amirite?"
}