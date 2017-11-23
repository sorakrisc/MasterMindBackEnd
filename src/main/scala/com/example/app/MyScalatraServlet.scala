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
import com.example.app.GameLogic
class MyScalatraServlet extends ScalatraServlet with CorsSupport {

  options("/*") {
    response.setHeader(
      "Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers")
    )
  }
  
  get("/login/:id"){

    val name = params("id")
    println(name)
    GameLogic.dbMap(name) = GameLogic.generateUserID()
    val js = ("userID" ->GameLogic.dbMap.get(name))
    compact(render(js))
  }
  get("/ans/:name"){
    val name = params("name")
    val guess = params("guess")
    contentType = "application/json"
    val id = GameLogic.dbMap(name)
    val js = GameLogic.checkGuess(GameLogic.map.getOrElseUpdate(id, GameLogic.generateAns()),guess)
    compact(render(js))
  }

}
