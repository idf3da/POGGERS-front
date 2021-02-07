package server

import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim, JwtOptions}

import java.security.spec.PKCS8EncodedKeySpec
import java.util.concurrent.TimeUnit
import scala.util.parsing.json.JSONObject
import scala.util.{Failure, Success}
import java.security.{KeyFactory, SecureRandom}

object JwtTest extends App {
    val email: String = "email@mail.ru"
    val secretKey: String = "supersecretkey"
    val algo = JwtAlgorithm.HS256


    val claims = JwtClaim(
        JSONObject(Map(
            "email" -> email,
            "expiredAt" -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100))))
                .toString())

    var token = Jwt.encode(claims, "secret", algo)
    println("New token", token)

//    token += "2"

    println("Legit?", Jwt.isValid(token, "secret", Seq(algo)))

    println("Expired?", ! Jwt.isValid(token, "secret", Seq(algo), JwtOptions(expiration = false)))

//    Jwt.decodeRaw(token, "secret", Seq(JwtAlgorithm.HS256)) match {
//        case Success(value) => println(Json.parse(value).as[Map[String, String]])
//        case Failure(value) => println(Map.empty[String, String])
//    }

    val jsonMap = {
        Jwt.decodeRaw(token, "secret", Seq(algo)) match {
            case Success(value) => println()
            case Failure(value) => println(Map.empty[String, String])
        }
    }

    val claim2 = JSONObject(Map(
        "email" -> email,
        "expiredAt" -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(100))
    )).toString()

    val token2 = Jwt.encode(claim2, "secretKey", JwtAlgorithm.HS256)
    println(token2)
}