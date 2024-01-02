package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  StoreOnce
}


object StoreonceResource extends App with SparkObject {
  val storeonceJob = new StoreonceResource(spark)
  storeonceJob.run
}
  
class StoreonceResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(STOREONCE_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      //col("type").alias("resourceType"),
                                      col("serialNumber").alias("serial_number"),
                                      col("storage.configuredStorageBytes").alias("configured_storage_bytes"),
                                      col("storage.usedBytes").alias("used_bytes"),
                                      col("storage.freeBytes").alias("free_bytes"),
                                      lit("0").cast(IntegerType).as("cost")
)

val storeonce  = flattenedDf.withColumn("time_stamp",current_timestamp()).as[StoreOnce]
storeonce.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/storeonce")

/*reading from delta table
val storeonceTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/storeonce")
storeonceTable.show()*/
   
 }
  
  }



/*
case class StoreOnce (
id : String,
name : String,
serialNumber : String,
configuredStorageBytes : Long,
usedBytes : Long,
freeBytes : Long,
// dataOrchestratorIds : Array[String],
customerId : String,
Time: java.sql.Timestamp
)
*/