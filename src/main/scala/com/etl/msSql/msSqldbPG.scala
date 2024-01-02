package com.etl.msSql
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  MySqlDatabasePG 
}


object MsSqlDatabasePgResource extends App with SparkObject {
  val msSqlPGJob = new MsSqlDatabasePgResource(spark)
  msSqlPGJob.run
}
  
class MsSqlDatabasePgResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(MSSQL_PG_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      col("members.instanceInfo.id").alias("members"),
                                      col("protectionGroupType").alias("protection_group_type"),
                                      col("protectionJobInfo.id").alias("protection_job_id"),
                                      col("protectionJobInfo.ProtectionPolicyInfo.id").alias("protection_policy_id"),
                                      col("virtualizationInfo.hypervisorManagerInfo.id").alias("hypervisor_manager_id")
                                      
)

val msSqlInstance  = flattenedDf.withColumn("time_stamp",current_timestamp()).as[MySqlDatabasePG]
msSqlInstance.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/MsSqlDatabasePG")

/*reading from delta table
val msSqlInstanceTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/MsSqlDatabasePG")
msSqlInstanceTable.show()*/
   
 }
  
  }
