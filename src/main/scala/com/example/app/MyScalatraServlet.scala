package com.example.app

import java.util.Date

import org.scalatra._
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.json4s.jackson.JsonMethods._
import org.json4s._
import org.json4s.JsonDSL._

import scala.collection.mutable.ListBuffer

import scala.util.control.Breaks._

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import scala.collection.mutable.HashMap
import org.scalatra.CorsSupport

class MyScalatraServlet extends ScalatraServlet with CorsSupport {

  options("/*") {
    response.setHeader(
      "Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers")
    )

  }
  protected implicit lazy val jsonFormats: Formats = DefaultFormats
  var map=HashMap[String, String]() //id,answer
  val colorChoice = "roygbp"
  val numbersChoice = "0123456789"
  val dbMap = HashMap[String, String]()

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
  get("/login/:id"){

    val name = params("id")
    println(name)
    dbMap(name) = generator(numbersChoice, 4)
    val js = ("userID" ->dbMap.get(name))
    compact(render(js))
  }
  get("/ans/:name"){
    val name = params("name")
    val guess = params("guess")
    contentType = "application/json"
    val id = dbMap(name)
    val js = checkGuess(map.getOrElseUpdate(id, generator(colorChoice, 4)),guess)
    compact(render(js))
  }

}
