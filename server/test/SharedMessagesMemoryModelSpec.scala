import org.scalatestplus.play._
import models.SharedMessagesMemoryModel

class SharedMessagesMemoryModelSpec extends PlaySpec {
    "SharedMessagesMemoryModel" must {
        "do valid login for default user" in {
            SharedMessagesMemoryModel.validateUser("web", "apps") mustBe (true)
            SharedMessagesMemoryModel.validateUser("mlewis", "prof") mustBe (true)
        }
    
        "reject login with wrong password" in {
            SharedMessagesMemoryModel.validateUser("web", "application") mustBe (false)
        }

        "reject login with wrong username" in {
            SharedMessagesMemoryModel.validateUser("webb", "apps") mustBe (false)
        }

        "get correct default private messages" in {
            SharedMessagesMemoryModel.getPrivateMessages("web") mustBe ( List(("mlewis", "test")))
        }

        "get correct default general messages" in {
            SharedMessagesMemoryModel.getGeneralMessages() mustBe (List(("mlewis", "Noonball anyone?")))
        }

        "create new user with no tasks" in {
            SharedMessagesMemoryModel.createUser("Cole", "monke") mustBe (true)
            SharedMessagesMemoryModel.getPrivateMessages("Cole") mustBe (Nil)
        }

        "create new user with existing name" in {
            SharedMessagesMemoryModel.createUser("mlewis", "password") mustBe (false)
        }

        "add new private message for default user" in {
            SharedMessagesMemoryModel.addPrivateMessage("mlewis", ("web", "testing"))
            SharedMessagesMemoryModel.getPrivateMessages("mlewis") must contain (("web","testing"))
        }

        "add new general messae for default user" in {
            SharedMessagesMemoryModel.addGeneralMessage("web", "test")
            SharedMessagesMemoryModel.getGeneralMessages() must contain (("web", "test"))
        }

        "add new private message for new user" in {
            SharedMessagesMemoryModel.addPrivateMessage("Cole", ("mlewis", "testing"))
            SharedMessagesMemoryModel.getPrivateMessages("Cole") must contain (("mlewis", "testing"))
        }

        "add new general message for new user" in {
            SharedMessagesMemoryModel.addGeneralMessage("Cole", "test2")
            SharedMessagesMemoryModel.getGeneralMessages() must contain (("Cole", "test2"))
        }
 
    }

}