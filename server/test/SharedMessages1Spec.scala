import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import org.scalatestplus.play.OneBrowserPerSuite
import org.scalatestplus.play.HtmlUnitFactory

class SharedMessages1Spec extends PlaySpec with GuiceOneServerPerSuite with OneBrowserPerSuite with HtmlUnitFactory {
    "Shared Messages 1" must {
        "login and access functions" in {
            go to s"http://localhost:$port/login1"
            pageTitle mustBe "Login"
            find(cssSelector("h2")).isEmpty mustBe false
            find(cssSelector("h2")).foreach(e => e.text mustBe "Login")
            click on "username-login"
            textField("username-login").value = "web"
            click on "password-login"
            pwdField(id("password-login")).value = "apps"
            submit()
            eventually {
                pageTitle mustBe "Messages"
                find(cssSelector("h2")).isEmpty mustBe false
                find(cssSelector("h2")).foreach(e => e.text must contain ("Message"))
                findAll(id("pers")).toList.map(_.text) mustBe List("mlewis says:")
                findAll(cssSelector("pers")).toList.map(_.text) mustBe List("test")
                findAll(id("gen")).toList.map(_.text) mustBe List("mlewis says:")
                findAll(cssSelector("gen")).toList.map(_.text) mustBe List("Noonball anyone?")
            } 
        }
    }
}