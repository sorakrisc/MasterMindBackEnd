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

  post("/join/:lobid/:name") {
    val name = params("name")
    val lobid = params("lobid")


    if (GameLogic.lobbies.get(lobid).isEmpty && GameLogic.nameUIDMap.get(name).isDefined){
      val js = ("lobStatus"-> "invalid")~("nameStatus"->"invalid")
      compact(render(js))
    }
    else if (GameLogic.nameUIDMap.get(name).isDefined){
      val js = ("lobStatus"-> "valid")~("nameStatus"->"valid")
      compact(render(js))
    }
    else {
      val generatedID = GameLogic.createNewUser(name)
      GameLogic.updateLobbies(generatedID,lobid)
      println(GameLogic.lobbies(lobid))
      val js = ("lobStatus"-> "valid")~("nameStatus"->"valid")
      compact(render(js))
    }
  }
  post("/create/:inilobid/:name"){
    val inilobid = params("inilobid")
    val name = params("name")
    if(GameLogic.nameUIDMap.get(name).isDefined){
      compact(render("nameStatus"->"invalid"))
    }
    else {
      val generatedID = GameLogic.createNewUser(name)
      GameLogic.lobbies(inilobid) = List(generatedID) //update lobbies map [lobbyid, list(userid)]
      GameLogic.lobbiesStatus(inilobid) = "Waiting"
      GameLogic.lobbyIDWinnerMap(inilobid) = Nil
      compact(render("nameStatus" -> "valid"))
    }
  }

  post("/gamestatusStart/:lobid"){
    val lobid = params("lobid")
    GameLogic.lobbiesStatus(lobid) = "Started"

  }
  post("/myTimeElapsed/:name/:timeElapsed"){
    val name = params("name")
    val timeElapsed = params("timeElapsed")
    GameLogic.updateTimeUsed(name, timeElapsed.toInt)

  }

  post("/updateWinner/:lobid/:name/:timeElapsed"){
    val lobid = params("lobid")
    val name = params("name")
    val timeElapsed = params("timeElapsed")
    val id = GameLogic.nameUIDMap(name)
    val numGuess = GameLogic.userNumTriesMap(id)
    GameLogic.lobbyIDWinnerMap(lobid)=(name+", time used: "+timeElapsed+", number of guess: "+numGuess)::GameLogic.lobbyIDWinnerMap(lobid)
  }
  get("/winnerLst/:lobid"){
    val lobid = params("lobid")
    compact(render("winnerLst"->GameLogic.lobbyIDWinnerMap(lobid)))

  }

  get("/isLobIDEmpty/:lobid"){
    val lobid = params("lobid")
    val js ="isLobIDEmpty"-> GameLogic.lobbies.get(lobid).isEmpty.toString
    compact(render(js))
  }
  get("/gamestatus/:lobid"){
    val lobid = params("lobid")
    compact(render("gameStatus"->GameLogic.lobbiesStatus(lobid)))
  }

  get("/players/:lobid"){
    val lobid = params("lobid")
    val js = ("playerLst"->GameLogic.lobbies(lobid).map(userID =>GameLogic.uidNameMap(userID)))
    compact(render(js))
  }
  get("/isUserInTimeMap/:name"){
    val name = params("name")
    val js = ("timeElapsed"->GameLogic.uidTimeElapsed.getOrElseUpdate(GameLogic.nameUIDMap(name), 0))
    compact(render(js))
  }
  get("/ans/:lobbyID"){
    val lobbyID = params("lobbyID")
    val guess = params("guess")
    val name = params("name")
    val timeElapsed = params("timeElapsed")
    GameLogic.updateTimeUsed(name, timeElapsed.toInt)
    contentType = "application/json"
    val js = GameLogic.checkGuess(lobbyID,guess,name)
    compact(render(js))
  }

}
