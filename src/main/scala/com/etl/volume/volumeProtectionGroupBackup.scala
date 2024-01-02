package com.etl.volume
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  VolumeProtectionGroupBackup 
}


object VolumeProtectionGroupBackupResource extends App with SparkObject {
  val volumePGJob = new VolumeProtectionGroupBackupResource(spark)
  volumePGJob.run
}
  
class VolumeProtectionGroupBackupResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(VOLUME_PG_BACKUP_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      //col("backupSetsInfo.backups")alias("backups"),
                                      explode($"backupSetsInfo.backups").as("backups"),
                                      col("SourceID").alias("source_id"),
                                      col("backupType").alias("backup_type"),
                                      col("protectionStoreInfo.id").alias("protection_store_id"),
                                      col("protectionStoreInfo.type").alias("protection_store_type"),
                                      col("createdByInfo.id").alias("create_by_id"),
                                      col("storageSystemInfo.id").alias("target_system_id"),
                                      col("storageSystemInfo.type").alias("target_system_type"),
                                      col("pointInTime").alias("point_in_time"),
                                      col("expiresAt").alias("expires_at")  
                                      
)

val MB: Int = 1048576
val volumePgBackupDF  = flattenedDf.withColumn("time_stamp",current_timestamp())
.withColumn("point_in_time", to_timestamp(col("point_in_time")))
.withColumn("expires_at", to_timestamp(col("expires_at")))
.withColumn("size_in_mb", aggregate($"backups.sizeInBytes", lit(0), (x, y) => (x.cast("int")/lit(MB) + y.cast("int")/lit(MB)).cast("int"))) 
.drop("backups").as[VolumeProtectionGroupBackup] //sum of sizeInBytes
volumePgBackupDF.show(truncate=false)
//volumePgBackupDF.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/VolumeProtectionGroupBackup")


 }
  
  }
