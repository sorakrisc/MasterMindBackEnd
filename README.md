# MasterMind Backend #

A scalatra backend for webapp, mastermind game. The project was build for OPL class

And here is the frontend
https://github.com/sorakrisc/MasterMindFrontEnd

### Prerequisites

* sbt
* scala

## Build & Run ##

```sh
$ cd <folder>
$ sbt
> jetty:start

```

## Project Structure ##

```
|-project                                   # All the plugins and builds
|  |-build.properties
|  |-plugins.sbt
|-src                                       # Code here
|  |-main
|  |  |-scala
|  |  |  |-com.example.app
|  |  |  |  |-GameLogic.scala               # All the logical function
|  |  |  |  |-MyScalatraServlet.scala       # Gets and Posts
|  |  |  |-JettyLauncher.scala
|  |  |  |-ScalatraBootstrap.scala
|-build.sbt                                 # All the dependencies and builds

```