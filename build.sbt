name := "topwords"

version := "0.4"

libraryDependencies ++= Seq(
  "com.lihaoyi"   %% "mainargs"   % "0.7.8",
  "org.scalatest" %% "scalatest"  % "3.2.19" % Test,
  "org.scalacheck" %% "scalacheck" % "1.19.0" % Test
)

enablePlugins(JavaAppPackaging)
Compile / run / mainClass := Some("main.TopWordsMain")
Compile / run / fork := true