package server

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{complete, optionalHeaderValueByName, provide}

import scala.util.parsing.json.JSONObject

import java.util.concurrent.TimeUnit
import scala.util.{Try,Success,Failure}

import java.time.Clock

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtHeader, JwtOptions}


object TokenAuthorization {
    private val secretKey = "super_secret_key"
    private val header = "HS512"
    private val tokenExpiryPeriodInDays = 999

    def generateToken(email: String): String = {
        println("Generating new token for: ", email)

        val claims = JwtClaim(
            JSONObject(Map(
                "email" -> email,
                "expiredAt" -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenExpiryPeriodInDays))
            )
        ).toString())
        Jwt.encode(claims)
    }

    def authenticated(jwtToken: String)  = {
        jwtToken match {
            case jwtToken if Jwt.isValid(jwtToken) => println("Token is OK")
            case jwtToken if Jwt.isValid(jwtToken, JwtOptions(expiration = false)) => println("Token is expired")
            case _ =>  println("Invalid token")
        }

    }

    def getClaims(jwtToken: String): String =
        Jwt.decodeRaw(jwtToken) match {
            case Success(value) => value
            case Failure(value) => ""
        }
}

object JwtTest extends App {

    val token: String = TokenAuthorization.generateToken("user1@mail.ru")

    println("New token", token)
    TokenAuthorization.authenticated(token)
    println(TokenAuthorization.getClaims(token))
}
