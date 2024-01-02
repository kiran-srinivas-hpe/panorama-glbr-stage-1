package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  LookupVmToDataStore,
  LookupDatastoreToDataCenter,
  LookUpVMWareProtectionGroupAndResources,
  LookupVmToVolume,
  VM
}


object VmResource extends App with SparkObject {
  val processVmJob = new VmResource(spark)
  processVmJob.run
}
  
class VmResource(spark: SparkSession){

  //implicit val snapshot_TSDB = Encoders[TSDBSnapshots]
  private def run: Unit = {

  val fileData = spark.read.format("parquet").load(VM_FILEPATH)
  import spark.implicits._
  val flattenedDf = fileData.select(
                                      col("id").alias("vm_id"),
                                      col("displayName").alias("display_name"),
                                      col("type").alias("resource_type"),
                                      col("capacityInBytes").alias("capacity_in_bytes"),
                                      col("appInfo.vmware.datacenterInfo.id").alias("datacenter_id"),
                                      col("appInfo.vmware.datacenterInfo.name").alias("datacenter_name"),
                                      col("hostInfo.id").alias("host_id"),
                                      col("hostInfo.name").alias("host_name"),
                                      col("clusterInfo.displayName").alias("cluster_name"),
                                      col("clusterInfo.id").alias("cluster_id"),
                                      col("hypervisorManagerInfo.id").alias("hypervisor_manager_id"),
                                      col("hypervisorManagerInfo.name").alias("hypervisor_manager_name"),
                                      col("protectionJobInfo.id").alias("protection_job_id"),
                                      col("protectionJobInfo.protectionPolicyInfo.id").alias("protection_policy_id"),
                                      col("protectionJobInfo.protectionPolicyInfo.name").alias("protection_policy_name"),
                                      col("vmPerfMetricInfo.totalReadIops").alias("total_read_iops"),
                                      col("vmPerfMetricInfo.totalWriteIops").alias("total_write_ops"),
                                      col("vmPerfMetricInfo.averageReadLatency").alias("average_read_latency"),
                                      col("vmPerfMetricInfo.averageWriteLatency").alias("average_Write_latency"),
                                      col("vmClassification").alias("vm_classification"),
                                      col("customerId").alias("customer_id"),
                                      col("protectionStatus").alias("protection_status"),
                                      col("vmPerfMetricInfo.storageUsedInBytes").alias("storage_used_in_bytes"),
                                      col("services"),
                                      col("volumesInfo"),
                                      col("appInfo.vmware.datastoresInfo.id").alias("datastore_id"),
                                      col("vmProtectionGroupsInfo.id").alias("protection_group_id"))


//creating dataframe

val lookupVmToDataStore  = flattenedDf.filter(size(col("datastore_id")) > 0).select($"vm_id",explode($"datastore_id").as("datastore_id")).as[LookupVmToDataStore]
val lookupVmToVolume = flattenedDf.select($"vm_id", $"customer_id",explode($"volumesInfo")).select($"vm_id",$"customer_id", $"col.id".as("volume_id"), $"col.storageSystemInfo.id".as("system_id")).as[LookupVmToVolume]
val lookUpVMWareProtectionGroupAndResources = flattenedDf.filter(size(col("protection_group_id")) > 0).select($"vm_id",$"resource_type", $"customer_id",explode($"protection_group_id").as("protection_group_id")).as[LookUpVMWareProtectionGroupAndResources]
val vm = flattenedDf.withColumn("time_stamp",current_timestamp()).drop("datastore_id","volumesInfo","protection_group_id").withColumnRenamed("vm_id", "id").as[VM]

//writing to delta tables
vm.write.format("delta").mode("Overwrite").option("overwriteSchema", "true").save(S3_PATH + "delta-tables/vm")
lookupVmToDataStore.write.format("delta").mode("Overwrite").option("overwriteSchema", "true").save(S3_PATH + "delta-tables/lookupVmToDataStore")
lookupVmToVolume.write.format("delta").mode("Overwrite").option("overwriteSchema", "true").save(S3_PATH + "delta-tables/lookupVmToVolume")
lookUpVMWareProtectionGroupAndResources.write.format("delta").mode("Overwrite").option("overwriteSchema", "true").save(S3_PATH + "delta-tables/lookUpVMWareProtectionGroupAndResources")


 /*reading from delta tables
val datavm = spark.read.format("delta").load(S3_PATH + "delta-tables/vm")
val datavmds = spark.read.format("delta").load(S3_PATH + "delta-tables/lookupVmToDataStore")
val lookupVmToVolumeTable = spark.read.format("delta").load(S3_PATH + "delta-tables/lookupVmToVolume")
val lookUpVMWareProtectionGroupAndResourcesTable =spark.read.format("delta").load(S3_PATH + "delta-tables/lookUpVMWareProtectionGroupAndResources")
   
datavm.show(false)
datavmds.show(false) 
lookupVmToVolumeTable.show(false) 
lookupVmToVolume.show(false)
lookUpVMWareProtectionGroupAndResourcesTable.show(false)*/

 }
  
  }