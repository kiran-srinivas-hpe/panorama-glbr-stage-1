// Copyright 2024 Hewlett Packard Enterprise Development LP

name := "br-etl-stage-1"

version := sys.env.get("ETL_BUILD_VERSION").getOrElse("0.1")

scalaVersion := "2.12.18"

val sparkVersion = "3.4.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)

libraryDependencies += "io.delta" %% "delta-core" % "2.4.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.2.2" //for localstack
//libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.3.3" //for actual s3

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "services", xg @ _*) => MergeStrategy.concat
  case PathList("META-INF", xs @ _*)             => MergeStrategy.discard
  case x                                         => MergeStrategy.first
}

assembly / artifact := {
  val art = (assembly / artifact).value
  art.withClassifier(Some("assembly"))
}

Compile / run :=
  Defaults
    .runTask(
      Compile / fullClasspath,
      Compile / run / mainClass,
      Compile / run / runner
    )
    .evaluated
Compile / runMain := Defaults
  .runMainTask(Compile / fullClasspath, Compile / run / runner)
  .evaluated

assembly / assemblyJarName := s"${name.value}-${version.value}.jar"

Test / fork := {
  val jenkins = sys.env.get("CI").getOrElse(false)
  if (jenkins == "true") true else false
}
Test / javaOptions ++= Seq("base/sun.nio.ch").map(
  "--add-opens=java." + _ + "=ALL-UNNAMED"
)
Test / javaOptions ++= Seq("base/util").map(
  "--add-opens=java." + _ + "=ALL-UNNAMED"
)
Test / parallelExecution := false
addArtifact(assembly / artifact, assembly)

publishArtifact in (Compile, packageBin) := false
publishArtifact in (Compile, packageDoc) := false
publishArtifact in (Compile, packageSrc) := false

/*publishTo := Some(
  "Artifactory Realm" at "https://hpecds.jfrog.io/artifactory/atlas-sbt-local"
)*/
resolvers += "Artifactory" at "https://hpecds.jfrog.io/artifactory/atlas-sbt-local"
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")