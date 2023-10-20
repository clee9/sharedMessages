package models

import collection.mutable

object SharedMessagesMemoryModel {
    private val users = mutable.Map[String, String]("web" -> "apps", "mlewis" -> "prof")
    private var gmsgs = Seq[(String, String)](("mlewis", "Noonball anyone?"))
    private val pmsgs = mutable.Map[String, List[(String, String)]] ("web" -> List(("mlewis", "test")))

    def validateUser(username: String, password: String): Boolean = {
        users.get(username).map(_ == password).getOrElse(false)
    }

    def createUser(username: String, password: String): Boolean = {
        if (users.contains(username)) false else {
            users(username) = password
            true
        }
    }

    def getPrivateMessages(username: String): Seq[(String,String)] = {
        pmsgs.get(username).getOrElse(Nil)
    }

     def getGeneralMessages(): Seq[(String,String)] = {
        gmsgs
    }

    def addPrivateMessage(to: String, message: (String, String)): Unit = {
        pmsgs(to) = message :: pmsgs.get(to).getOrElse(Nil)
    }
    
    def addGeneralMessage(from: String, message: String): Unit = {
        gmsgs = gmsgs :+ (from, message)
    }

}