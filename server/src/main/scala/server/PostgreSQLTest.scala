package server

import akka.Done
import akka.http.scaladsl.model.Uri.Empty
import akka.http.scaladsl.server.Directives.{complete, onSuccess}
import server.JSORequestReader.{Item, orders, saveOrder}
import server.SNS.{db, users}

import scala.concurrent.ExecutionContext.Implicits.global
import sun.rmi.runtime.Log

import scala.concurrent.duration.{SECONDS, _}
import java.lang.Error
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future
import scala.concurrent.Await
import java.sql.SQLException
import scala.util.{Failure, Success, Try}

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

    def Register(newUser: User): Future[Int] = {
        val insertUserQuery = users += newUser
        val resultFuture: Future[Int] = db.run(insertUserQuery)
        resultFuture
    }

    def GetUser(userid: Int): Future[Seq[User]] = Future {
        val userFilter = users.filter(_.userid === userid).result
        val userFuture: Future[Seq[User]] = db.run(userFilter)
        Await.result(userFuture, 2.seconds)
    }

    val db = Database.forConfig("db")
    val users = TableQuery[UsersTable]
    val newUser = User(1337, "name2", "password", "email2@mail.ru")

//    val resultFuture = Register(newUser)
//
//    val result = Await.ready(resultFuture, 2.seconds)
//    result.onComplete {
//        case Success(v) => println("EEEEEEEEEEEEEE", v)
//        case Failure(e) => println(e)
//        case _ => println("ALL")
//    }

    val userFilter = users.filter(_.userid === 1).result
    val resultFuture: Future[Seq[User]] = db.run(userFilter)

    Try(Await.ready(resultFuture, 5.second)) match {
        case Success(f) => f.value.get match {
            case Success(res) => res.length match {
                case 0 => println("Man, dis guy was not found.....")
                case 1 => println("Jesus Christ it's Jason Born", res)
                case _ => println("There were several users with that ID.\nSomething certainly went wrong with DB.")
            }
            case Failure(e) => println("NOT SUCCESS", e.getMessage)
        }
        case Failure(_) => println("OOPSIE THE TIMEOUT HAS HAPPENED...")
    }




}
