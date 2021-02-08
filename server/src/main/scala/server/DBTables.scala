package server

import slick.jdbc.H2Profile.api._

import java.sql.Timestamp

case class RegisterUserRequest (
                                       userid: Int,
                                       username: String,
                                       password: String,
                                       email: String
                               )

case class LoginRequest(
                               username: String,
                               password: String
                       )

case class CreatePostRequest (
                                    creatorid: Int,
                                     title: String,
                                     descriptorid: Int,
                                     description: String
                             )

class UsersTable(tag: Tag) extends Table[RegisterUserRequest](tag,"users") {
    def userid = column[Int]("userid", O.PrimaryKey, O.AutoInc)
    def username  = column[String]("username")
    def password = column[String]("password")
    def email = column[String]("email")
    def * = (userid, username, password, email).mapTo[RegisterUserRequest]
}

class PostsTable(tag: Tag) extends Table[CreatePostRequest](tag,"posts") {
    def postid = column[Int]("postid", O.PrimaryKey, O.AutoInc)
    def creatorid = column[Int]("creatorid")
    def title  = column[String]("title")
    def descriptorid = column[Int]("descriptorid")
    def created_at = column[Timestamp]("created_at")
    def updated_at = column[Timestamp]("updated_at")
    def description = column[String]("description")
    def * = (creatorid, title, descriptorid, description).mapTo[CreatePostRequest]
}

