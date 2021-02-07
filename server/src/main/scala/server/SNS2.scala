package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName, provide, _}
import akka.http.scaladsl.server.{Directive1, Route}
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.json4s.native.JsonMethods.parse
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtOptions}
import play.api.libs.json.{Json, Reads}
import slick.jdbc.H2Profile.api._
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.io.StdIn
import scala.util.parsing.json.JSONObject
import scala.util.{Failure, Success, Try}

case class RegisterUserRequest (
                                   userid: Int,
                                   username: String,
                                   password: String,
                                   email: String
                               )

object RegisterUserRequestObj {
    implicit val createUserRequestReads: Reads[RegisterUserRequest] = Json.reads[RegisterUserRequest]
}

class UsersTable(tag: Tag) extends Table[RegisterUserRequest](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (userid, username, password, email).mapTo[RegisterUserRequest]
}

case class LoginRequest(
                           username: String,
                           password: String
                       )

object LoginRequestObj {
    implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
}

class UserManagementService {

    private val db = Database.forConfig("db")
    private val users = TableQuery[UsersTable]

    def userLogin(loginRequest: LoginRequest): String = {
        val usernameFilter = users.filter(_.username === loginRequest.username).result
        val resultFuture: Future[Seq[RegisterUserRequest]] = db.run(usernameFilter)
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
            case Failure(_) => "DB timeout"
        }
    }

    def protectedContent: String  = "Super mega secret content that nobody needs to see if you know what I mean amirite?"

}
object TokenAuthorization {
    private val secretKey = "super_secret_key"
    private val tokenExpiryPeriodInDays = 365

    def generateToken(email: String): String = {
        val claims = JwtClaim(
            JSONObject(Map(
                "email" -> email,
                "expiredAt" -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenExpiryPeriodInDays))))
                    .toString())
        val token = Jwt.encode(claims, secretKey, JwtAlgorithm.HS256)
        println("Generating new token for: ", email, token)
        token
    }

    def authenticated: Directive1[Map[String, Any]] = {
        optionalHeaderValueByName("Authorization").flatMap { tokenFromUser =>
            val jwtToken = tokenFromUser.get.split(" ")
            jwtToken(1) match {
                case jwtToken if Jwt.isValid(jwtToken, secretKey, Seq(JwtAlgorithm.HS256), JwtOptions()) => provide(getClaims(jwtToken))
                case jwtToken if Jwt.isValid(jwtToken, secretKey, Seq(JwtAlgorithm.HS256), JwtOptions(expiration = false)) => complete(StatusCodes.Unauthorized -> "Session expired.")
                case _ =>  complete(StatusCodes.Unauthorized -> "Invalid Token")
            }
        }
    }

    private def getClaims(jwtToken: String): Map[String, String] =
        Jwt.decodeRaw(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
            case Success(value) => parse(value).values.asInstanceOf[Map[String, String]]
            case Failure(_) => Map.empty[String, String]
        }
}

class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport {

    implicit val RegisterUserRequestFormat: RootJsonFormat[RegisterUserRequest] = jsonFormat4(RegisterUserRequest)
    implicit val LoginRequestFormat: RootJsonFormat[LoginRequest] = jsonFormat2(LoginRequest)

    val routes: Route =
        pathPrefix("user") {
            path("login") {
                (post & entity(as[LoginRequest])) { loginRequest =>
                    val userLoginResult: String = service.userLogin(loginRequest)
                    if (userLoginResult == "Login successful.") {
                        val token = TokenAuthorization.generateToken(loginRequest.username)
                        complete((StatusCodes.OK, token))
                    } else if (userLoginResult == "Wrong password.") {
                        complete((StatusCodes.BadRequest, "Wrong password."))
                    } else if (userLoginResult == "Several users found.") {
                        complete((StatusCodes.InternalServerError, "Found several users with that username? BRUH something went super wrong"))
                    } else if (userLoginResult == "DB timeout") {
                        println("DB timeout")
                        complete(StatusCodes.RequestTimeout -> "DB timeout")
                    } else {
                        println("Login error", userLoginResult)
                        complete(StatusCodes.InternalServerError -> userLoginResult)
                    }
                }
            } ~
            path("register") {
                (post & entity(as[RegisterUserRequest])) { createUserRequest =>
                    val createUserResult: String =  service.createUser(createUserRequest)
                    if (createUserResult == "User registered.") {
                        val token: String = TokenAuthorization.generateToken(createUserRequest.username)
                        complete((StatusCodes.OK, token))
                    } else if (createUserResult == "DB timeout") {
                        println("DB timeout")
                        complete(StatusCodes.RequestTimeout -> "DB timeout")
                    } else {
                        println("Register error:", createUserResult)
                        complete(StatusCodes.InternalServerError -> createUserResult)
                    }
                }
            }
            } ~
            path("protectedcontent") {
                get {
                    TokenAuthorization.authenticated { _ =>
                        val response = service.protectedContent
                        complete(response)
                    }
                }
            }
}

object SNS2 extends App {
    val conf = ConfigFactory.load()

    implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "system")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.executionContext



    val userManagementService = new UserManagementService
    val userManagementRoutes = new UserManagementRoutes(userManagementService)
    val routes = userManagementRoutes.routes


    val bindingFuture = Http().newServerAt("localhost", 8080).bind(routes)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
            .flatMap(_.unbind())
            .onComplete(_ => actorSystem.terminate())
}
