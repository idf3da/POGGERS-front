package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName, provide, _}
import akka.http.scaladsl.server.{Directive1, Route}
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.json4s.native.JsonMethods.parse
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtOptions}
import play.api.libs.json.{Json, Reads}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import scala.util.parsing.json.JSONObject
import scala.util.{Failure, Success}





case class CreateUserRequest(
                                    email: String,
                                    password: String,
                                    confirmPassword: String
                            )

object CreateUserRequest {
    implicit val createUserRequestReads: Reads[CreateUserRequest] = Json.reads[CreateUserRequest]
}

case class LoginRequest(
                               email: String,
                               password: String
                       )

object LoginRequest {
    implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
}

class UserManagementService {

    def userLogin(loginRequest: LoginRequest): String ={
        "Login Successful"
    }

    def createUser(createUserRequest: CreateUserRequest):  String = "User Created"

    def protectedContent: String  = "This method is secured"

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
                case _ =>  complete(StatusCodes.Unauthorized ->"Invalid Token")
            }
        }
    }

    private def getClaims(jwtToken: String): Map[String, String] =
        Jwt.decodeRaw(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
            case Success(value) => parse(value).values.asInstanceOf[Map[String, String]]
            case Failure(value) => Map.empty[String, String]
        }
}

class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport {
    val routes: Route =
        pathPrefix("user") {
            path("login") {
                (post & entity(as[LoginRequest])) { loginRequest =>
                    if (service.userLogin(loginRequest) == "Login Successful") {
                        val token = TokenAuthorization.generateToken(loginRequest.email)
                        complete((StatusCodes.OK, token))
                    } else {
                        complete(StatusCodes.Unauthorized)
                    }
                }
            } ~
                    path("register") {
                        (post & entity(as[CreateUserRequest])) { createUserRequest =>
                            if (service.createUser(createUserRequest) == "User Created") {
                                val token: String = TokenAuthorization.generateToken(createUserRequest.email)
                                complete((StatusCodes.OK, token))
                            } else {
                                println("Пипец")
                                complete(StatusCodes.BadRequest -> "Cannot Register")
                            }
                        }
                    }
        } ~
                path("protectedcontent") {
                    get {
                        TokenAuthorization.authenticated { _ =>
                            val response = service.protectedContent
                            println(response)
                            complete(response)
                        }
                    }
                }
}

object Login extends App {
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