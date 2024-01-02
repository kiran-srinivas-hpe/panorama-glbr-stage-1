package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  LookUpVMWareProtectionGroupToResources,
  VMWareProtectionGroup
}

object VMWareProtectionGroupResource extends App with SparkObject {
  val vMWareProtectionGroupJob = new VMWareProtectionGroupResource(spark)
  vMWareProtectionGroupJob.run
}
  
class VMWareProtectionGroupResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(VMPG_FILEPATH)
import spark.implicits._  
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      //col("customerId"),
                                      col("type").alias("resource_type"),
                                      col("assetsCategory").alias("asset_category"),
                                      col("dataOrchestratorId").alias("dataOrchestrator_id"),
                                      col("protectedResourcesInfo.virtualMachinesCount").alias("protected_resources_count"),
                                      col("hypervisorManagerInfo.id").alias("hypervisor_manager_id"),
                                      col("hypervisorManagerInfo.name").alias("hypervisor_manager_name"),
                                      col("protectionJobInfo.id").alias("protection_job_id"),
                                      col("assets.id").alias("asset_id")

)
//customerid not present in restapi json

val lookUpVMWareProtectionGroupToResources = flattenedDf.select($"id".as("protection_group_id"), $"asset_category",explode($"asset_id").as("asset_id")).filter($"asset_id" =!= "").as[LookUpVMWareProtectionGroupToResources]
val vMWareProtectionGroup  = flattenedDf.withColumn("time_stamp",current_timestamp()).drop("asset_id").as[VMWareProtectionGroup]

vMWareProtectionGroup.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/VMWareProtectionGroup")
lookUpVMWareProtectionGroupToResources.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/LookUpVMWareProtectionGroupToResources")
/*reading from delta table
val vMWareProtectionGroupTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/VMWareProtectionGroup")
val lookUpVMWareProtectionGroupToResourcesTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/LookUpVMWareProtectionGroupToResources")
vMWareProtectionGroupTable.show()
lookUpVMWareProtectionGroupToResourcesTable.show()*/
   
 }
  
  }

/*case class VMWareProtectionGroup (
id : String,
name : String,
displayName : String,
customerId : String,
resourceType : String,
dataOrchestratorID : String,
dataOrchestratorType : String,  // not there
hypervisorManagerID : String,
hypervisorManagerName : String,
assetsCategory : String,
// assetsIds : Array[String], --- LookUpVMWareProtectionGroupToResources
protectedResourcesCount: Int,
ProtectionJobId: String,
Time: java.sql.Timestamp
)

case class LookUpVMWareProtectionGroupToResources (
VMWareProtectionGroupId: String,
assetsCategory: String,
AssetId: String,
customerId : String
)
*/
