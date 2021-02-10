package server

import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class UserManagementService {

    private val db = Database.forConfig("db")
    private val users = TableQuery[UsersTableDB]

    def createUser(newUser: RegisterUserRequest):  (String, Int) = {
        val newUserDB: RegisterUserRequestDB = RegisterUserRequestDB(-1, newUser.username, newUser.password, newUser.email)
        val insertUserQuery = users returning users.map(_.userid) += newUserDB
        val resultFuture: Future[Int] = db.run(insertUserQuery)
        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(userid) => ("User registered.", userid)
                case Failure(e) => (s"ERROR: ${e.getMessage}", 0)
            }
            case Failure(e) => (s"DB timeout: ${e.getMessage}", 0)
        }

    }

    def userLogin(loginRequest: LoginRequest): (String, Int) = {
        val usernameFilter = users.filter(_.username === loginRequest.username).result
        val resultFuture = db.run(usernameFilter)
        Try(Await.ready(resultFuture, 5.second)) match {
            case Success(f) => f.value.get match {
                case Success(res) => res.length match {
                    case 1 => if (res.head.password == loginRequest.password) ("Login successful.", res.head.userid) else ("Wrong password.", -1)
                    case 0 => ("User not found.", -1)
                    case _ => ("Several users found.", -1)
                }
                case Failure(e) => (s"Login DB ERROR: ${e.getMessage}", -1)
            }
            case Failure(e) => (s"DB timeout: ${e.getMessage}", -1)
        }
    }
    def protectedContent: String  = "Super mega secret content that nobody needs to see if you know what I mean amirite?"
}