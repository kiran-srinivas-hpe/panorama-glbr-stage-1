name := "spark-scala-k8-app"

version := "0.1"

scalaVersion := "2.12.15"
val sparkVersion = "3.4.1"
assemblyMergeStrategy in assembly := {
  case "META-INF/services/org.apache.spark.sql.sources.DataSourceRegister" => MergeStrategy.concat
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case "application.conf" => MergeStrategy.concat
  case x => MergeStrategy.first
}

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
)

libraryDependencies += "io.delta" %% "delta-core" % "2.4.0"
libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.2.2" //for localstack
//libraryDependencies += "org.apache.hadoop" % "hadoop-aws" % "3.3.3" //for actual s3



