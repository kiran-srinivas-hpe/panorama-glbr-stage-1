package com.etl.volume
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  VolumeProtectionGroupSnapshot 
}


object VolumeProtectionGroupSnapsResource extends App with SparkObject {
  val volumePGJob = new VolumeProtectionGroupSnapsResource(spark)
  volumePGJob.run
}
  
class VolumeProtectionGroupSnapsResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(VOLUME_PG_SNAPS_FILEPATH)
 
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      col("SourceID").alias("source_id"),
                                      col("individualSnapshotsInfo"),
                                      col("storageSystemSnapshotId").alias("storage_system_snapshot_id"),
                                      col("createdByInfo.id").alias("create_by_id"),
                                      col("pointInTime").alias("point_in_time"),
                                      col("expiresAt").alias("expires_at")
                                      
)
import spark.implicits._
// attempts to cast the "point_in_time" column to a timestamp. If the casting fails (e.g., due to invalid date formats), it returns null
val volumePgSnapsDF  = flattenedDf.withColumn("time_stamp",current_timestamp())
/*.withColumn(
  "point_in_time",
  expr("TRY(CAST(point_in_time AS TIMESTAMP))").otherwise(lit(0).cast("timestamp"))
)
.withColumn(
  "expires_at",
  expr("TRY(CAST(expires_at AS TIMESTAMP))").otherwise(lit(0).cast("timestamp"))
)*/
.withColumn("point_in_time", to_timestamp(col("point_in_time")))
.withColumn("expires_at", to_timestamp(col("expires_at")))
.withColumn("size_in_bytes", aggregate($"individualSnapshotsInfo.sizeInMiB", lit(0), (x, y) => (x.cast("Int") + y.cast("Int"))))
.drop("individualSnapshotsInfo").as[VolumeProtectionGroupSnapshot] //sum of sizeInBytes
volumePgSnapsDF.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/VolumeProtectionGroupSnapshot")


   
 }
  
  }
