package com.example.app

import org.json4s.{DefaultFormats, Formats, JObject}
import org.json4s._
import org.json4s.JsonDSL._

import scala.collection.mutable
import scala.collection.mutable.{HashMap, ListBuffer}
import scala.util.control.Breaks.{break, breakable}

object GameLogic {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  val colorChoice = "roygbp"
  val numbersChoice = "0123456789"

  val uidNameMap = HashMap[String, String]()
  val lobidAnsMap = HashMap[String, String]()
  val lobbies = HashMap[String, List[String]]()
  val lobbiesStatus= HashMap[String, String]()
  val nameUIDMap = HashMap[String, String]()
  val userNumTriesMap = HashMap[String, Int]()
  val uidTimeElapsed = HashMap[String, Int]()
  val lobbyIDWinnerMap = HashMap[String, List[String]]()
  lobbies("catnap") = List("1234")
  uidNameMap("1234") = "Jamess"
  lobidAnsMap("catnap") = "royg"
  lobbiesStatus("catnap")= "Waiting"
  nameUIDMap("Jamess")="1234"


  def updateTimeUsed(name: String, timeUsed: Int)={
    val uid = nameUIDMap(name)
    if(uidTimeElapsed.get(uid).isEmpty){
      uidTimeElapsed(uid)=timeUsed
    }
    else if (uidTimeElapsed(uid) < timeUsed){
      uidTimeElapsed(uid) = timeUsed
    }
    else{
      uidTimeElapsed(uid) = uidTimeElapsed(uid)+timeUsed
    }

  }
  def updateLobbies(userID:String, lobbyID:String)={
    lobbies(lobbyID) = userID::lobbies(lobbyID)
  }
  def createNewUser(name: String): String={
    var generatedID = generateUserID()
    while(uidNameMap.get(generatedID).isDefined){
      generatedID = generateUserID()
    }
    uidNameMap(generatedID) = name
    nameUIDMap(name)=generatedID
    userNumTriesMap(generatedID)= 0
    generatedID
  }
  def generateUserID()={
    generator(numbersChoice ,4)
  }
  def generateAns()={
    generator(colorChoice,4)
  }
  def generator(choice: String, length: Int): String = {
    import scala.util.Random
    val numChoices = choice.length
    val random: Seq[Char] = (1 to length).map { _ => choice.charAt(Random.nextInt(numChoices))}
    random.mkString
  }


  def checkGuess(lobbyID:String, guess:String, name:String): JObject ={
    val uid =nameUIDMap(name)
    val checkCount =userNumTriesMap(uid)+1
    userNumTriesMap(uid)=checkCount
    val ans = lobidAnsMap.getOrElseUpdate(lobbyID, generateAns())
    var totWhite = 0
    var totRed = 0
    var copguess = guess.to[ListBuffer]
    totRed = ans.zip(guess).count { case (a, g) => a == g }
    for(i<- 0 until ans.length){
      val ansc = ans.charAt(i)
      breakable {
        for (j <- 0 until copguess.length) {
          if (ansc.equals(copguess(j))) {
            copguess.remove(j)
            totWhite += 1
            break
          }
        }
      }
    }
    ("white" -> math.abs(totWhite-totRed).toString) ~ ("red" -> totRed.toString) ~ ("checkCount"->checkCount.toString)
  }
}