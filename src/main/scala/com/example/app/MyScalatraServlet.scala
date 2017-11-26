package com.example.app

import org.scalatra._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.scalatra.CorsSupport

class MyScalatraServlet extends ScalatraServlet with CorsSupport {

  options("/*") {
    response.setHeader(
      "Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers")
    )
  }

  post("/join/:lobid/:id") {

    val name = params("id")
    val lobid = params("lobid")

    if (GameLogic.lobbies.get(lobid) == None){

      val js = ("lobStatus"-> "invalid")
      compact(render(js))
    }
    else {
      GameLogic.dbMap(name) = GameLogic.generateUserID()
      println(name, GameLogic.dbMap(name))
      GameLogic.updateLobbies(name,lobid)
      println(GameLogic.lobbies(lobid))
      val js = ("lobStatus"-> "valid")
      compact(render(js))
    }
  }
  post("/create/:inilobid/:name"){
    val inilobid = params("inilobid")
    val name = params("name")
    GameLogic.lobbies(inilobid) = List(GameLogic.dbMap(name)) //update lobbies map [lobbyid, list(userid)]
    println(inilobid, GameLogic.lobbies)
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
