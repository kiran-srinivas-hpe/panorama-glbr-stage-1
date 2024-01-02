package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  Backup
}

object DsBackupResource extends App with SparkObject {
  val dsBackupResourceJob = new DsBackupResource(spark)
  dsBackupResourceJob.run
}
  
class DsBackupResource(spark: SparkSession) {
  private def run: Unit = {
   val fileData = spark.read.format("parquet").load(DSBK_FILEPATH)
import spark.implicits._
val flattenedDf = fileData.select(
                                      col("id"),
                                      col("name"),
                                      flatten($"backupSetsInfo.backups").as("backups"),
                                      col("sourceID").alias("source_id"),
                                      col("backupType").alias("backup_type"),
                                      col("appType").alias("app_type"),
                                      col("backupMode").alias("backup_mode"),
                                      //col("backupGranularity").alias("backup_granularity"),  // not there in ds
                                      col("pointInTime").alias("point_in_time"),
                                      col("expiresAt").alias("expires_at"),
                                      col("protectionStoreInfo.id").alias("protection_store_id"),
                                      col("protectionStoreInfo.type").alias("protection_store_type"),
                                      col("dataOrchestratorId").alias("dataorchestrator_id"),
                                      col("storageSystemInfo.id").alias("target_system_id"),
                                      col("storageSystemInfo.displayName").alias("target_system_name"),
                                      col("storageSystemInfo.type").alias("target_system_type") //verify once
                                     // col("customerId").alias("customer_id")
)

flattenedDf.printSchema()
val dsbackup  = flattenedDf
.withColumn("time_stamp",current_timestamp())
.withColumn("backup_granularity", lit(""))
.withColumn("size_in_bytes", aggregate($"backups.sizeInBytes", lit(0), (x, y) => (x.cast("Int") + y.cast("Int"))))
.drop("backups").as[Backup] //sum of sizeInBytes in backup array
//writing to delta table
dsbackup.write.format("delta").mode("append").save(S3_PATH + "delta-tables/dsBackup")
/*reading from delta table
val backupTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/backup")
backup.show()*/

   
 }
  
  }
