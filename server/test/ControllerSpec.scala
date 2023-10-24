import org.scalatestplus.play.PlaySpec
import controllers.Application
import play.api.test.Helpers
import play.api.test.FakeRequest
import play.api.test.Helpers._


class ControllerSpec extends PlaySpec {
    "Application#index" must {
        "give back expected page" in {
            val controller = new Application(Helpers.stubControllerComponents())
            val result = controller.index.apply(FakeRequest())
            val bodyText = contentAsString(result)
            bodyText must include ("lay and Scala.js")
            bodyText must include ("Play shouts out:")
            bodyText must include ("Scala.js shouts out:")
           
        }
    }
}