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
import scala.collection.mutable.{Map,
  SynchronizedMap, HashMap}
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
  def generator(choice:String, lenght:Int): String ={
    var temp = ""
    for (a <- 1 to lenght){
      val r = scala.util.Random.nextInt(choice.length)
      temp+= choice.charAt(r)
    }
    temp
  }

  def checkGuess(ans:String, guess:String): JObject ={
    var totWhite = 0
    var totRed = 0
    var copguess = guess.to[ListBuffer]
    for(i <- 0 until ans.length){
      if(ans.charAt(i).equals(guess.charAt(i))) totRed+=1
    }
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
    dbMap.get(name)
  }
  get("/ans/:id"){
    val id = params("id")
    val guess = params("guess")
    contentType = "application/json"
    val js = (checkGuess(map.getOrElseUpdate(dbMap(id), generator(colorChoice, 4)),guess))
    println(map.get(dbMap(id)))
    compact(render(js))
  }

}
