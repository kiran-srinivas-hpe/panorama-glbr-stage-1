package com.etl
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.model.{
  LookupDataStoreToVolume,
  Datastore,
  LookupDatastoreToDataCenter,
  LookupDatastoreToHost
}
object DsResource extends App with SparkObject {
  val dsResourceJob = new DsResource(spark)
  dsResourceJob.run
}
  

  class DsResource(spark: SparkSession){
     private def run: Unit = {
  val fileData = spark.read.format("parquet").load(DS_FILEPATH)
  import spark.implicits._
  val flattenedDSFrame = fileData.select(
                                      col("id").alias("datastore_id"),
                                      col("displayName").alias("display_name"),
                                      col("type").alias("resource_type"),
                                      col("protected").alias("protection_status") ,
                                      col("capacityInBytes").alias("capacity_in_bytes"),
                                      col("clusterInfo.displayName").alias("cluster_name"),
                                      col("clusterInfo.id").alias("cluster_id"),
                                      col("hypervisorManagerInfo.id").alias("hypervisor_manager_id"),
                                      col("hypervisorManagerInfo.name").alias("hypervisor_manager_name"),
                                      col("protectionJobInfo.id").alias("protection_job_id"),
                                      col("protectionJobInfo.protectionPolicyInfo.id").alias("protection_policy_id"),
                                      col("capacityFree").alias("capacity_free"),
                                      col("customerId")alias("customer_id"),
                                      col("services"),
                                      col("vmCount").alias("vm_count"),
                                      col("volumesInfo.id").alias("Volume_id"),
                                      col("datacentersInfo"),
                                      col("hostsInfo")
                                      )


flattenedDSFrame.show(false)
val lookupDataStoreToVolume = flattenedDSFrame.select($"datastore_id", $"customer_id",explode($"volume_id").as("volume_id")).filter($"volume_id" =!= "").as[LookupDataStoreToVolume]
val lookupDatastoreToDataCenter = flattenedDSFrame.select($"datastore_id",$"customer_id",explode($"datacentersInfo")).select($"datastore_id",$"customer_id", $"col.id".as("datacenter_id"), $"col.name".as("datacenter_name")).filter($"datacenter_id" =!= "").as[LookupDatastoreToDataCenter]
val lookupDatastoreToHost = flattenedDSFrame.select($"datastore_id", $"customer_id",explode($"hostsInfo")).select($"datastore_id",$"customer_id", $"col.id".as("host_id"), $"col.name".as("host_name")).filter($"host_id" =!= "").as[LookupDatastoreToHost]
val datastore = flattenedDSFrame.withColumn("time_stamp",current_timestamp()).drop("hostsInfo","Volume_id","datacentersInfo").as[Datastore]
//writing to delta lake
datastore.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/Datastore")
lookupDatastoreToDataCenter.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/lookupDatastoreToDataCenter")
lookupDataStoreToVolume.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/lookupDataStoreToVolume")
lookupDatastoreToHost.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/lookupDatastoreToHost")

/*
println("before reading") //reading from delta table
val datastoreTable = spark.read.format("delta").load(S3_PATH + "delta-tables/Datastore")
val lookupDatastoreToDataCenterTable = spark.read.format("delta").load(S3_PATH + "delta-tables/lookupDatastoreToDataCenter")
val lookupDataStoreToVolumeTable = spark.read.format("delta").load(S3_PATH + "delta-tables/lookupDataStoreToVolume")
val lookupDatastoreToHostTable = spark.read.format("delta").load(S3_PATH + "delta-tables/lookupDatastoreToHost")
   datastoreTable.show(false)
   lookupDatastoreToDataCenterTable.show(false) 
   lookupDataStoreToVolumeTable.show(false)
   datastoreTable.show(false) 
   lookupDatastoreToHostTable.show(false)*/
  
  }
}
