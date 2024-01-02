package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  DataOrchestrator
}

object DataOrchestratorResource extends App with SparkObject {
  val dataOrchestratorResourceJob = new DataOrchestratorResource(spark)
  dataOrchestratorResourceJob.run
}
  
class DataOrchestratorResource(spark: SparkSession) {
private def run: Unit = { 
val fileData = spark.read.format("parquet").load(DO_FILEPATH)
import spark.implicits._
val flattenedDf = fileData.select(
                                      col("id"),
                                      col("name"),
                                      col("type").alias("resource_type"),
                                      col("serialNumber").alias("serial_number"),
                                      col("connectionState").alias("connection_state"),
                                      col("upTimeInSeconds").alias("up_time_in_seconds"),
                                      col("customerId").alias("customer_id")
)


val dataOrchestrator  = flattenedDf.withColumn("time_stamp",current_timestamp()).as[DataOrchestrator]

//writing to delta table9
dataOrchestrator.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/do")
/*reading from delta table
val doTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/do")
doTable.show()*/

   
 }
  
  }

  
