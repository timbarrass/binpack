name := "binpack"

version := "1.0"

scalaVersion := "2.11.8"

enablePlugins(ScalaJSPlugin)

//libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"

//libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.9.0"

//libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.4.6"

// core = essentials only. No bells or whistles.
libraryDependencies += "com.github.japgolly.scalajs-react" % "core_sjs0.6_2.11" % "0.11.2"

// React JS itself (Note the filenames, adjust as needed, eg. to remove addons.)
jsDependencies ++= Seq(

  "org.webjars.bower" % "react" % "15.3.2"
    /        "react-with-addons.js"
    minified "react-with-addons.min.js"
    commonJSName "React",

  "org.webjars.bower" % "react" % "15.3.2"
    /         "react-dom.js"
    minified  "react-dom.min.js"
    dependsOn "react-with-addons.js"
    commonJSName "ReactDOM",

  "org.webjars.bower" % "react" % "15.3.2"
    /         "react-dom-server.js"
    minified  "react-dom-server.min.js"
    dependsOn "react-dom.js"
    commonJSName "ReactDOMServer")