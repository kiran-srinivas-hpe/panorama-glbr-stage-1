package com.etl.msSql
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  MsSqlDatabase 
}


object MsSqlDatabaseResource extends App with SparkObject {
  val MsSqlDatabaseJob = new MsSqlDatabaseResource(spark)
  MsSqlDatabaseJob.run
}
  
class MsSqlDatabaseResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(MSSQL_DATABASE_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      col("applicationHostInfo.id").alias("application_host_id"),
                                      col("instanceInfo.id").alias("instance_id"),
                                      col("mssqlDatabaseProtectionGroupInfo.id").alias("mssql_database_protection_group_id"),
                                      col("protectionStatus").alias("protection_status"),
                                      col("sizeInBytes").alias("size_in_bytes").cast(LongType),
                                      col("virtualizationInfo.virtualMachineInfo.resourceUri").alias("resourceUri"),
                                      col("virtualizationInfo.hypervisorManagerInfo.id").alias("hypervisor_manager_id")
                                      
)

val msSqlDatabase  = flattenedDf.withColumn("time_stamp",current_timestamp())
.withColumn("vm_id", regexp_extract(col("resourceUri") ,"/([^/]+)$",1))
.drop("resourceUri").as[MsSqlDatabase]

msSqlDatabase.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/MsSqlDatabase")

/*reading from delta table
val msSqlDatabaseTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/MsSqlDatabase")
//msSqlDatabaseTable.select("vm_id").show(truncate = false)*/
   
 }
  
  }
