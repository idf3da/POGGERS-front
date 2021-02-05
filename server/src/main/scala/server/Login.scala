//package server
//
//import akka.actor.ActorSystem
//import akka.http.scaladsl.Http
//import akka.stream.ActorMaterializer
//import com.typesafe.config.ConfigFactory
//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.server.Directives._
//import play.api.libs.json.{Json, Reads}
//
//import akka.http.scaladsl.server.Route
//import play.api.libs.json.{Json, Reads}
//import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
//import java.util.concurrent.TimeUnit
//
//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.server.Directive1
//import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName, provide}
//
//
//import scala.concurrent.ExecutionContextExecutor
//import scala.io.StdIn
//
//case class CreateUserRequest(
//                                    email: String,
//                                    password: String,
//                                    confirmPassword: String
//                            )
//
//object CreateUserRequest {
//    implicit val createUserRequestReads: Reads[CreateUserRequest] = Json.reads[CreateUserRequest]
//}
//
//case class LoginRequest(
//                               email: String,
//                               password: String
//                       )
//
//object LoginRequest {
//    implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
//}
//
//class UserManagementService {
//
//    def userLogin(loginRequest: LoginRequest): String ={
//        "Login Successful"
//    }
//
//    def createUser(createUserRequest: CreateUserRequest):  String = "User Created"
//
//    def protectedContent: String  = "This method is secured"
//
//}
//
//class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport {
//    val routes: Route =
//        pathPrefix("user") {
//            path("login") {
//                (post & entity(as[LoginRequest])) { loginRequest =>
//                    if (service.userLogin(loginRequest) == "Login Successful") {
//                        val token = TokenAuthorization.generateToken(loginRequest.email)
//                        complete((StatusCodes.OK, token))
//                    } else {
//                        complete(StatusCodes.Unauthorized)
//                    }
//                }
//            } ~
//                    path("register") {
//                        (post & entity(as[CreateUserRequest])) { createUserRequest =>
//                            if (service.createUser(createUserRequest) == "User Created") {
//                                val token: String = TokenAuthorization.generateToken(createUserRequest.email)
//                                complete((StatusCodes.OK, token))
//                            } else {
//                                complete(StatusCodes.BadRequest -> "Cannot Register")
//                            }
//                        }
//                    }
//        } ~
//                path("protectedcontent") {
//                    get {
//                        TokenAuthorization.authenticated { _ =>
//                            val response = service.protectedContent
//                            println(response)
//                            complete(response)
//                        }
//                    }
//                }
//}
//
//object TokenAuthorization {
//    private val secretKey = "super_secret_key"
//    private val header = JwtHeader("HS256")
//    private val tokenExpiryPeriodInDays = 999
//
//    def generateToken(email: String): String = {
//        println("Generating new token for: ", email)
//
//        val claims = JwtClaimsSet(
//            Map(
//                "email" -> email,
//                "expiredAt" -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenExpiryPeriodInDays))
//            )
//        )
//        JsonWebToken(header, claims, secretKey)
//    }
//
//    def authenticated: Directive1[Map[String, Any]] = {
//
//        optionalHeaderValueByName("Authorization").flatMap { tokenFromUser =>
//
//            val jwtToken = tokenFromUser.get.split(" ")
//            jwtToken(1) match {
//                case token if isTokenExpired(token) =>
//                    complete(StatusCodes.Unauthorized -> "Session expired.")
//
//                case token if JsonWebToken.validate(token, secretKey) =>
//                    provide(getClaims(token))
//
//                case _ =>  complete(StatusCodes.Unauthorized ->"Invalid Token")
//            }
//        }
//    }
//
//    private def isTokenExpired(jwt: String): Boolean =
//        getClaims(jwt).get("expiredAt").exists(_.toLong < System.currentTimeMillis())
//
//    private def getClaims(jwt: String): Map[String, String] =
//        JsonWebToken.unapply(jwt) match {
//            case Some(value) => value._2.asSimpleMap.getOrElse(Map.empty[String, String])
//            case None => Map.empty[String, String]
//
//        }
//}
//
//object Main extends App {
//
//    val conf = ConfigFactory.load()
//
//    implicit val actorSystem: ActorSystem = ActorSystem("system")
//    implicit val materializer: ActorMaterializer = ActorMaterializer()(actorSystem)
//    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
//    val userManagementService = new UserManagementService
//    val userMananagementRoutes = new UserManagementRoutes(userManagementService)
//    val routes = userMananagementRoutes.routes
//
//    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8081)
//    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
//    StdIn.readLine()
//    bindingFuture
//            .flatMap(_.unbind())
//            .onComplete(_ => actorSystem.terminate())
//
//}