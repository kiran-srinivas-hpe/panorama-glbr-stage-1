package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  ProtectionStore
}

object ProtectionStoreResource extends App with SparkObject {
  val protectionStoreJob = new ProtectionStoreResource(spark)
  protectionStoreJob.run
}
  
class ProtectionStoreResource(spark: SparkSession) {
 private def run: Unit = {
  val psData = spark.read.format("parquet").load(PS_FILEPATH)
import spark.implicits._  
val flattenedPsDf = psData.select(
                                      col("id"),
                                      col("displayName").alias("name"),
                                      col("type").alias("resource_type"),
                                      col("region"),
                                      col("sizeOnDiskInBytes"),
                                      col("userDataStoredInBytes"), 
                                      col("maxCapacityInBytes"),
                                      col("protectionStoreType").alias("protection_store_type"),
                                      col("storageSystemInfo.id").alias("storage_system_id"),
                                      col("storageSystemInfo.displayName").alias("storage_system_name"),
                                      col("storageSystemInfo.type").alias("storage_system_type")
)
//writing to delta table
val vm = flattenedPsDf.withColumn("time_stamp",current_timestamp()).as[ProtectionStore]
vm.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/protectionStore")

/*reading from delta table
val protectionStoreTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/protectionStore")
protectionStoreTable.show()*/
   
 }
  
  }
