package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  Protection,
  ProtectionJob,
  ProtectionPolicy

}

object ProtectionPolicyResource extends App with SparkObject {
  val protectionPolicyJob = new ProtectionPolicyResource(spark)
  protectionPolicyJob.run
}
  
class ProtectionPolicyResource(spark: SparkSession) {
   private def run: Unit = {
   
   val psData = spark.read.format("parquet").load(PP_FILEPATH)

val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("protections"),
                                      col("protectionJobsInfo")
                                      //col("customerId")
)
//writing to delta table
import spark.implicits._
val protectionPolicyTemp  = flattenedDf.withColumn("time_stamp",current_timestamp())
val protection = protectionPolicyTemp.select($"id",$"time_stamp",explode($"protections")).select($"id".as("protection_policy_id"), $"col.id", $"col.type".as("resource_type"),$"col.applicationType".as("application_type"),$"col.protectionStoreInfo.id".as("protection_store_id"),$"col.protectionStoreInfo.type".as("protection_store_type"),$"time_stamp").as[Protection]
val protectionJob = protectionPolicyTemp.select($"id",$"time_stamp",explode($"protectionJobsInfo")).select($"id".as("protection_policy_id"), $"col.id", $"col.assetInfo.type".as("asset_Type"),$"col.assetInfo.id".as("asset_id"),$"time_stamp").as[ProtectionJob]
val protectionPolicy = protectionPolicyTemp.drop("protections","protectionJobsInfo").as[ProtectionPolicy]


protectionPolicy.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/ProtectionPolicy")
protection.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/Protection")
protection.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/ProtectionJob")
/*reading from delta table
val protectionPolicyTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/ProtectionPolicy")

protectionPolicyTable.show()*/
   
 }
  
  }
/*  
case class ProtectionPolicy (
id : String,
name : String,
customerId : String,
Time: java.sql.Timestamp
// protectionJobIds : Array[String], -- parent relation in ProtectionJob model
// protectionIds : Array[String] -- parent relation in Protection model
)*/
