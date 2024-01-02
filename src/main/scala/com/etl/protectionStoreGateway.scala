package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  ProtectionStoreGateway
}


object ProtectionStoreGatewayResource extends App with SparkObject {
  val psgJob = new ProtectionStoreGatewayResource(spark)
  psgJob.run
}
  
class ProtectionStoreGatewayResource(spark: SparkSession) {
  private def run: Unit = {
val fileData = spark.read.format("parquet").load(PSG_FILEPATH)
import spark.implicits._  
val flattenedDf = fileData.select(
                                      col("id"),
                                      col("displayName").alias("name"),
                                      col("type").alias("resource_type"),
                                      col("serialNumber").alias("serial_number"),
                                      col("vmId").alias("vm_id"),
                                      col("size.maxOnPremDailyProtectedDataTiB"), 
                                      col("size.maxInCloudDailyProtectedDataTiB"),
                                      col("customerId").alias("customer_id")
)
//writing to delta table
val psg = flattenedDf.withColumn("time_stamp",current_timestamp()).as[ProtectionStoreGateway]
psg.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/ProtectionStoreGateway")

/*reading from delta table
val psgTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/ProtectionStoreGateway")
psgTable.show()*/
   
 }
  
  }  
