package server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

object Routes {
    val userManagementServiceInstance = new UserManagementService
    val userManagementRoutesInstance = new UserManagementRoutes(userManagementServiceInstance)

    val postManagementServiceInstance = new PostManagementService
    val postManagementRoutesInstance = new PostManagmentRoutes(postManagementServiceInstance)

    val routes: Route = userManagementRoutesInstance.routes ~ postManagementRoutesInstance.routes
}

class PostManagmentRoutes(service: PostManagementService) extends PlayJsonSupport {
    implicit val CreatePostRequestFormat: RootJsonFormat[CreatePostRequest] = jsonFormat4(CreatePostRequest)

    val routes: Route = pathPrefix("post") {
        path("create") {
            (post & entity(as[CreatePostRequest])) { createPostRequest => {
                TokenAuthorization.authenticated { credentials => {
                    println(credentials)
                    val createPostResult: String = service.createPost(createPostRequest)
                    createPostResult match {
                        case "Post created." => complete((StatusCodes.OK, "Post created."))
                        case _ => complete(StatusCodes.InternalServerError -> createPostResult)
                    }
                }
                }
            }
            }
        }
    }
}

class UserManagementRoutes(service: UserManagementService) extends PlayJsonSupport {
    implicit val RegisterUserRequestFormat: RootJsonFormat[RegisterUserRequest] = jsonFormat4(RegisterUserRequest)
    implicit val LoginRequestFormat: RootJsonFormat[LoginRequest] = jsonFormat2(LoginRequest)

    val routes: Route =
        pathPrefix("user") {
            path("register") {
                (post & entity(as[RegisterUserRequest])) { createUserRequest => {
                    val (createUserResult: String, userid: Int) = service.createUser(createUserRequest)
                    createUserResult match {
                        case "User registered." => complete((StatusCodes.OK, TokenAuthorization.generateToken(userid)))
                        case "Register error" => complete(StatusCodes.InternalServerError -> createUserResult)
                        case _ => complete(StatusCodes.InternalServerError -> createUserResult)
                    }
                }
                }
            } ~ path("login") {
                (post & entity(as[LoginRequest])) { loginRequest => {
                    val (userLoginResult: String, userid: Int) = service.userLogin(loginRequest)
                    userLoginResult match {
                        case "Login successful." => complete((StatusCodes.OK, TokenAuthorization.generateToken(userid)))
                        case "Wrong password." => complete((StatusCodes.BadRequest, "Wrong password."))
                        case "Several users found." => complete((StatusCodes.InternalServerError, "Found several users with that username? BRUH something went super wrong"))
                        case _ => complete(StatusCodes.InternalServerError -> userLoginResult)
                    }
                }
                }
            }

        } ~ path("protectedcontent") {
            get {
                TokenAuthorization.authenticated { creds =>
                    val response = service.protectedContent + "\n" + creds.toString()
                    complete(response)
                }
            }
        }
}