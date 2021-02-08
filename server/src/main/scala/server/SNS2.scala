package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

/**
 * TODO createPost(); new route; get identity from token)
 * TODO Comments on posts(case class, CommentsTable; createComment(); get identity from token)
 */


object SNS2 extends App {
    val conf = ConfigFactory.load()

    implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "system")
    implicit val executionContext: ExecutionContextExecutor = actorSystem.executionContext


    val bindingFuture = Http().newServerAt("localhost", 8080).bind(Routes.routes)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
            .flatMap(_.unbind())
            .onComplete(_ => actorSystem.terminate())
}
