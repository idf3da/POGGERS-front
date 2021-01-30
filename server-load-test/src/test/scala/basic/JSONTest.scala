package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

class JSONTest extends Simulation {

    def randID() = Random.nextInt(Integer.MAX_VALUE)

    val httpConf = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json, text/plain, */*")
            .acceptEncodingHeader("gzip, deflate")
            .acceptLanguageHeader("en-US,en;q=0.5")

    val scn = scenario("JSONRequest")
            .exec(
                http("OrderCreation")
                .post("/create-order")
                        .body(StringBody(_ => s"""{ "items" : [{"name": "name${randID()}", "id":${randID()}}]}""")).asJson
                        .check(status is 200)
    )

    setUp(scn.inject(atOnceUsers(2)))
            .assertions(global.responseTime.max.lt(50))
            .protocols(httpConf)

}
