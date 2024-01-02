package com.etl.volume
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  VolumeProtectionGroup 
}


object VolumeProtectionGroupResource extends App with SparkObject {
  val volumePGJob = new VolumeProtectionGroupResource(spark)
  volumePGJob.run
}
  
class VolumeProtectionGroupResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(VOLUME_PG_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      //col("customerId").alias("customer_id"),
                                      col("assets.id").alias("assets"),
                                      col("volumeProtectionGroupType").alias("protection_group_type"),
                                      col("storageSystemInfo.id").alias("system_id")
                                      )

val volumePgDF  = flattenedDf.withColumn("time_stamp",current_timestamp()).as[VolumeProtectionGroup]
volumePgDF.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/VolumeProtectionGroup")


 }
  
  }
