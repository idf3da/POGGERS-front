package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.lang.module.Configuration
import scala.util.Random
import scala.concurrent.duration.{SECONDS, _}



class UserAPI extends Simulation {

    val randInt = Iterator.continually(
        Map("Rand" -> Random.nextInt(Integer.MAX_VALUE))
    )

    val httpConf = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("*/*")
            .contentTypeHeader("application/json")
            .acceptEncodingHeader("gzip, deflate, br")

    val scn = scenario("JSONRequest")
            .feed(randInt)
            .exec(
                http("Send json")
                        .post("/register")
                        .body(StringBody("""{"userid": ${Rand}, "username": "User${Rand}", "password": "password${Rand}", "email": "email${Rand}@mail.ru"}"""))
                        .check(status is 200)

            )
            .pause(3)
            .exec(
                http("Get json")
                        .get("/user/${Rand}")
                        .check(status is 200)
            )

    setUp(scn.inject(
        nothingFor(2.seconds),
        atOnceUsers(1),
        rampUsers(400).during(5.seconds),
        constantUsersPerSec(400).during(2.minutes)
    ))
            .assertions(global.responseTime.max.lt(50))
            .protocols(httpConf)

}
