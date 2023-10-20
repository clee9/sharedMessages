package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.SharedMessagesMemoryModel

@Singleton
class SharedMessages1 @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
    
    
    def login = Action { implicit request =>
        Ok(views.html.login1())
    }

    def validateLoginPost = Action { request => 
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args => 
            val username = args("username").head
            val password = args("password").head
            if (SharedMessagesMemoryModel.validateUser(username,password)) {
                Redirect(routes.SharedMessages1.messages).withSession("username" -> username)
            } else {
                Redirect(routes.SharedMessages1.login).flashing("error" -> "Invalid username/password combination.")
            }

        }.getOrElse(Redirect(routes.SharedMessages1.login))
    } 

    def createUser = Action { request => 
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args => 
            val username = args("username").head
            val password = args("password").head
            if (SharedMessagesMemoryModel.createUser(username,password)) {
                Redirect(routes.SharedMessages1.login).withSession("username" -> username).flashing("success" -> "User created")
            } else {
                Redirect(routes.SharedMessages1.login).flashing("error" -> "User creation failed.")
            }
        }.getOrElse(Redirect(routes.SharedMessages1.login))
    }

    def messages = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val pmsgs = SharedMessagesMemoryModel.getPrivateMessages(username)
            val gmsgs = SharedMessagesMemoryModel.getGeneralMessages()
            Ok(views.html.messages1(pmsgs, gmsgs))
        }.getOrElse(Redirect(routes.SharedMessages1.login))
    }

    def logout = Action {
        Redirect(routes.SharedMessages1.login).withNewSession
    }

    def addPrivateMessage = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map { args => 
                val to = args("whoTo").head
                val msg = args("newMessage").head
                SharedMessagesMemoryModel.addPrivateMessage(to, (username, msg))
                Redirect(routes.SharedMessages1.messages).flashing("success" -> s"Message sent to $to.")
            }.getOrElse(Redirect(routes.SharedMessages1.messages))
        }.getOrElse(Redirect(routes.SharedMessages1.login))
    }

    def addGeneralMessage = Action { implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map { username =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map { args => 
                val msg = args("newGeneralMessage").head
                SharedMessagesMemoryModel.addGeneralMessage(username, (username + ": " +msg))
                Redirect(routes.SharedMessages1.messages)
            }.getOrElse(Redirect(routes.SharedMessages1.messages))
        }.getOrElse(Redirect(routes.SharedMessages1.login))
    }

    // def deleteMessage = Action { implicit request =>
    //     val usernameOption = request.session.get("username")
    //     usernameOption.map { username =>
    //         val postVals = request.body.asFormUrlEncoded
    //         postVals.map { args => 
    //             val index = args("index").head.toInt
    //             SharedMessagesMemoryModel.removeMessage(username, index)
    //             Redirect(routes.SharedMessages1.messages)
    //         }.getOrElse(Redirect(routes.SharedMessages1.messages))
    //     }.getOrElse(Redirect(routes.SharedMessages1.login))
    // }
}