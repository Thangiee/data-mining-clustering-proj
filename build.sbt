name := "data-mining-proj-clustering"

version := "1.0"

scalaVersion := "2.11.7"

mainClass in assembly := Some("project2.Main")
assemblyOutputPath in assembly := new File("./project2.jar")

libraryDependencies  ++= Seq(
  "org.scalanlp" %% "breeze" % "0.11.2",
  "org.scalanlp" %% "breeze-viz" % "0.11.2"
)