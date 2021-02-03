package server

import akka.Done
import akka.http.scaladsl.server.Directives.{complete, onSuccess}
import server.JSORequestReader.saveOrder

import scala.concurrent.ExecutionContext.Implicits.global
import sun.rmi.runtime.Log
import scala.concurrent.duration.{SECONDS, _}

import java.lang.Error
import slick.jdbc.H2Profile.api._
import scala.concurrent.Future
import scala.concurrent.Await


import java.sql.SQLException
import scala.util.{Failure, Success}

case class User(
                       userid: Int,
                       username: String,
                       password: String,
                       email: String
               )

class UsersTable(tag: Tag) extends Table[User](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (userid, username, password, email).mapTo[User]
}



object PostgreSQLTest extends App {
    val db = Database.forConfig("db")
    val users = TableQuery[UsersTable]
    val insertUserQuery = users += User(1337, "name", "password", "email1338@mail.ru")
    val resultFuture: Future[Int] = db.run(insertUserQuery)
    val result = Await.result(resultFuture, 2.seconds)


    // OK I am not crazy but this Await stuff is kinda suss
    // .result waits for Future to complete but this fcker wont work with onComplete
    // But .complete will...........
    // OMG I am so dead........



    //    result.onComplete {
    //        case Success(v) => println("EEEEEEEEEEEEEE", v)
    //        case Failure(e) => println(e)
    //        case _ => println("ALL")
    //    }

    println(result)

    //    result onComplete {
    //        case Success(posts) => println(posts)
    //        case Failure(t) => println("An error has occurred: " + t.getMessage)
    //    }


}
