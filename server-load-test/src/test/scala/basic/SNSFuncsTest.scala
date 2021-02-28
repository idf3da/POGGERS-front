package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import java.lang.module.Configuration
import scala.util.Random
import scala.concurrent.duration.{SECONDS, _}



class SNSFuncsTest extends Simulation {

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
                        .post("/create-order")
                        .body(StringBody("""{ "items" : [{"name": "name${Rand}", "id":${Rand}}]}"""))
                        .check(status is 200)

            )
            .pause(3)
            .exec(
                http("Get json")
                        .get("/item/${Rand}")
                        .check(status is 200)
            )

    setUp(scn.inject(
        nothingFor(2.seconds),
        atOnceUsers(10),
        constantUsersPerSec(400).during(4.minutes)
        //        rampUsers(30).during(5.seconds),
        //        constantUsersPerSec(20).during(15.seconds),
        //        constantUsersPerSec(20).during(15.seconds).randomized,
        //        rampUsersPerSec(10).to(20).during(1.minutes),
        //        rampUsersPerSec(40).to(80).during(1.minutes).randomized,
        //        heavisideUsers(1000).during(20.seconds)
    ))
            .assertions(global.responseTime.max.lt(50))
            .protocols(httpConf)

}
