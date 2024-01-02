package com.model
import java.sql.Timestamp

case class VM (
    id:  String,
    total_read_iops :   Long,
    total_write_ops  : Long,
    display_name: String,
    resource_type:    String,
    capacity_in_bytes: Long,
    datacenter_id:    String,
    datacenter_name:  String,
    cluster_id:   String,
    cluster_name: String,
    host_id:  String,
    host_name:    String,
    hypervisor_manager_id: String,
    hypervisor_manager_name:   String,
    protection_job_id: String,
    protection_policy_id:  String,
    protection_policy_name:    String,
    average_read_latency  : Long,
    average_Write_latency : Long,
    vm_classification    : String,
    customer_id  : String,
    protection_status    : String,
    services   : Array[String],
    time_stamp : Timestamp
)

case class LookupVmToVolume (
 vm_id : String,
 volume_id : String,
 system_id : String, 
 customer_id : String
)


case class LookupVmToDataStore (
 vm_id: String,
 dataStore_id: String,
 //customer_id : String  add this
)

case class LookUpVMWareProtectionGroupAndResources (
 vm_id: String,
 resource_type: String,
 protection_group_id: String,
 customer_id : String
)


case class Datastore(
 datastore_id : String,  // change to id
 display_name : String,          
 protection_status : String,
 protection_job_id : String,
 protection_policy_id : String,
 resource_type : String,
 cluster_id : String,
 cluster_name : String,
 capacity_free : Long,
 capacity_in_bytes : Long,
 hypervisor_manager_id : String,
 hypervisor_manager_name : String,
 customer_id : String,
 services : Array[String],
 vm_count : Long,
 time_stamp: Timestamp
)

case class LookupDataStoreToVolume (
 datastore_id: String,
 volume_id: String,
 customer_id : String
)

case class LookupDatastoreToDataCenter (
 datastore_id: String,
 datacenter_id: String,
 datacenter_name: String,
 customer_id : String
)

case class LookupDatastoreToHost (
 datastore_id: String,
 host_id: String,
 host_name: String,
 customer_id : String
)


case class VMWareProtectionGroup (
id : String,
name : String,
//displayName : String,
//customer_id : String,
resource_type : String,
dataOrchestrator_id : String,
//dataOrchestratorType : String,
hypervisor_manager_id : String,
hypervisor_manager_name : String,
asset_category : String,
//asset_id : String,
protected_resources_count: Long,
protection_job_id: String,
time_stamp: Timestamp
)

case class LookUpVMWareProtectionGroupToResources (
protection_group_id: String,
asset_category: String,
asset_id: String,
//customer_id : String
)

case class ProtectionPolicy (
id : String,
name : String,
//customer_id : String,
time_stamp: Timestamp
)

case class ProtectionJob (
id : String,
asset_id : String,
asset_Type :String, // rename to asset_type
protection_policy_id: String,
//customer_id: String,
time_stamp: Timestamp
)

case class Protection (
id : String,
resource_type : String,
application_type : String,
protection_store_id : String,
protection_store_type : String,
protection_policy_id: String,
//customer_id : String,
time_stamp: Timestamp
)

case class ProtectionStore (
id : String,
name : String,
resource_type : String,
region : String,
sizeOnDiskInBytes : Long,
userDataStoredInBytes : Long,
maxCapacityInBytes : Long,
protection_store_type : String,
storage_system_id : String,
storage_system_type : String,
storage_system_name : String,
//customer_id : String,
time_stamp: Timestamp
)

case class ProtectionStoreGateway (
id : String,
name : String,
//displayName : String,
serial_number : String,
vm_id : String,
maxOnPremDailyProtectedDataTiB : Long,
maxInCloudDailyProtectedDataTiB : Long,
//cost: Long,
customer_id : String,
time_stamp: Timestamp
)



case class StoreOnce (
id : String,
name : String,
serial_number : String,
configured_storage_bytes : Long,
used_bytes : Long,
free_bytes : Long,
cost: Long,
// dataOrchestratorIds : Array[String],
customer_id : String,
time_stamp: Timestamp
)




 case class DataOrchestrator (
 id : String,
 name : String,
 serial_number : String,
 resource_type : String,
 connection_state: String,
 up_time_in_seconds: String,  //convert to long
 customer_id : String,
 time_stamp: Timestamp
 )





case class Backup (
id: String,
name: String,
size_in_bytes: Long, 
source_id: String, 
backup_type: String,
app_type: String,
backup_mode: String,
backup_granularity: String,  //not there in ds
point_in_time: String,  //Timestamp,
expires_at: String,    //Timestamp,
protection_store_id: String,
protection_store_type: String,
dataorchestrator_id: String,
target_system_id: String, 
target_system_name :String,
target_system_type :String,
//customer_id : String,
time_stamp: Timestamp
)



// case class Volume (
// id: String,
// name: String,
// storageSystemId: String,
// storageSystemName: String,
// storageSerialNumber: String,
// customer_id : String,
// time: Timestamp
// )



case class System (
id: String,
name: String,
Type: String,
capacity_in_bytes: Long,
cost: Long,
customer_id : String,
time: Timestamp
)

//MYSQL RELATED MODELS

case class MsSqlDatabase (
id: String,
name: String,
application_host_id: String,
instance_id: String,
mssql_database_protection_group_id: String,
protection_status: String,
size_in_bytes: Long,
vm_id: String, // from virtualizationInfo -> virtualMachineInfo -> resourceUri
hypervisor_manager_id: String,
customer_id: String,
time_stamp: Timestamp,
)



case class ApplicationHost (
id: String,
name: String,
platform: String,
data_orchestrator_id: String,
vm_id: String, // from virtualizationInfo -> virtualMachineInfo -> resourceUri
hypervisor_manager_id: String,
customer_id: String,
time_stamp: Timestamp,
)

case class MsSqlInstance (
id: String,
name: String,
application_host_id: String,
customer_id: String,
time_stamp: Timestamp,
)

case class MySqlDatabasePG (
id: String,
name: String,
members: Array[String], // instance ids
protection_group_type: String,
protection_job_id: String,
protection_policy_id: String,
hypervisor_manager_id: String,
customer_id : String,
time_stamp: Timestamp,
)

case class MsSqlSnapshot (
id: String,
name: String,
size_in_mib: Int, // sum of volumesSnapshotInfo.sizeInMib
associated_databases: Array[String],
snapshot_type: String,
point_in_time: Timestamp,
created_by_id: String,
customer_id : String,
time_stamp: Timestamp,
)


case class MsSqlBackup (
id: String,
name: String,
size_in_bytes: Long, 
associated_databases: Array[String],
backup_type: String, 
protection_store_type: String, 
target_system_type: String,
backup_granularity: String,
point_in_time: Timestamp,
expires_at: java.sql.Timestamp,
protection_store_id: String,
//on_prem_engine_id: String, 
target_system_id: String, 
created_by_id: String, 
customer_id : String,
time_stamp: Timestamp,
source_id: String,
)

//-- Volume Models — 



case class VolumeProtectionGroup (
id: String,
name: String,
assets: Array[String],
protection_group_type: String,
system_id: String,
//customer_id: String,
time_stamp: java.sql.Timestamp,
)



case class VolumeProtectionGroupBackup (
id: String,
name: String,
size_in_mb: Long, // add sizes in backupSetsInfo
source_id: String,
backup_type: String,
create_by_id: String,
protection_store_id: String,
protection_store_type: String,
target_system_id: String, // storageSystemInfo.id of store once or PSG
target_system_type: String, // possible values (STOREONCE, PROTECTION_STORE_GATEWAY) -- from storageSystemInfo.type
point_in_time: java.sql.Timestamp,
expires_at: java.sql.Timestamp,
customer_id: String,
time_stamp: java.sql.Timestamp,

)

case class VolumeProtectionGroupSnapshot (
id: String,
name: String,
size_in_bytes: Long, // sum of sizeInMiB under individualSnapshotsInfo
source_id: String,
storage_system_snapshot_id: String,
create_by_id: String,
point_in_time: java.sql.Timestamp,
expires_at: java.sql.Timestamp,
customer_id: String,
time_stamp: Timestamp,
)





