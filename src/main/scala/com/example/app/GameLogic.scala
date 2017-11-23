package com.example.app

import org.json4s.{DefaultFormats, Formats, JObject}
import org.json4s._
import org.json4s.JsonDSL._
import scala.collection.mutable.{HashMap, ListBuffer}
import scala.util.control.Breaks.{break, breakable}

object GameLogic {

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  var map=HashMap[String, String]() //id,answer
  val colorChoice = "roygbp"
  val numbersChoice = "0123456789"
  val dbMap = HashMap[String, String]()

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

  def checkGuess(ans:String, guess:String): JObject ={
    var totWhite = 0
    var totRed = 0
    var copguess = guess.to[ListBuffer]
    //    for(i <- 0 until ans.length){
    //      if(ans.charAt(i).equals(guess.charAt(i))) totRed+=1
    //    }
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
    ("white" -> math.abs(totWhite-totRed).toString) ~ ("red" -> totRed.toString)
  }
}