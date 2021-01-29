package server

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.testkit.Specs2RouteTest
import org.specs2.Specification
import org.specs2.specification.core.SpecStructure
import server.HealthRoute.healthRoute

class HealthRouteSpec extends Specification with Specs2RouteTest {
    override def is: SpecStructure =
        s2"""
        Health Check route should
          respond HTTP 200 $e1
          have OK text message as response $e2
      """

    private def e1 =
        Get("/health") ~> healthRoute ~> check {
            status must beEqualTo(StatusCodes.OK)
        }
    private def e2 =
        Get("/health") ~> healthRoute ~> check({
            responseAs[String] must beEqualTo("OK")
        })
}