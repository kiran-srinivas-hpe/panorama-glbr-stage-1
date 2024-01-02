package com.etl.msSql
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  MsSqlSnapshot 
}


object MsSqlSnapshotResource extends App with SparkObject {
  val MsSqlSnapshotJob = new MsSqlSnapshotResource(spark)
  MsSqlSnapshotJob.run
}
  
class MsSqlSnapshotResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(MSSQL_SNAPS_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("volumesSnapshotInfo"),
                                      col("associatedDatabases.id").alias("associated_databases"),
                                      col("snapshotType").alias("snapshot_type"),
                                      col("pointInTime").alias("point_in_time"),
                                      col("createdByInfo.id").alias("created_by_id"),
                                      col("customerId").alias("customer_id")
                                      
)

val MsSqlSnapshot  = flattenedDf.withColumn("time_stamp",current_timestamp())
.withColumn("point_in_time", to_timestamp(col("point_in_time")))
.withColumn("size_in_mib", aggregate($"volumesSnapshotInfo.sizeInMib", lit(0), (x, y) => (x.cast("Int") + y.cast("Int"))))
.drop("volumesSnapshotInfo").as[MsSqlSnapshot]

MsSqlSnapshot.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/MsSqlSnapshot")

/*reading from delta table
val MsSqlSnapshotTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/MsSqlSnapshot")
//MsSqlSnapshotTable.select("vm_id").show(truncate = false)*/
   
 }
  
  }
