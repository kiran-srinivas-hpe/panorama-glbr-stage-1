package com.etl.msSql
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  MsSqlBackup 
}


object MsSqlBackupResource extends App with SparkObject {
  val MsSqlBackupJob = new MsSqlBackupResource(spark)
  MsSqlBackupJob.run
}
  
class MsSqlBackupResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(MSSQL_BACKUP_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      col("SourceID").alias("source_id"),
                                      flatten($"backupSetsInfo.backups").as("backups"),
                                      col("associatedDatabases.id").alias("associated_databases"),
                                      col("backupType").alias("backup_type"),
                                      col("pointInTime").alias("point_in_time"),
                                      col("expiresAt").alias("expires_at"),
                                      col("backupGranularity").alias("backup_granularity"),
                                      col("protectionStoreInfo.protectionStoreType").alias("protection_store_type"),
                                      col("protectionStoreInfo.id").alias("protection_store_id"), 
                                      col("storageSystemInfo.type").alias("target_system_type"),
                                      col("storageSystemInfo.id").alias("target_system_id"),
                                      col("createdByInfo.id").alias("created_by_id")                
)

val MsSqlBackup  = flattenedDf.withColumn("time_stamp",current_timestamp())
.withColumn("point_in_time", to_timestamp(col("point_in_time")))
.withColumn("expires_at", to_timestamp(col("expires_at")))
.withColumn("size_in_bytes", aggregate($"backups.sizeInBytes", lit(0), (x, y) => (x.cast("Int") + y.cast("Int"))))
.drop("backups").as[MsSqlBackup]

MsSqlBackup.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/MsSqlBackup")

/*reading from delta table
val MsSqlBackupTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/MsSqlBackup")
//MsSqlBackupTable.select("vm_id").show(truncate = false)*/
   
 }
  
  }
