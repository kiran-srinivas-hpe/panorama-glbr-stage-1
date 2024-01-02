package com.etl
object Constants {
  
   val AWS_ACCESS_KEY ="test"
    val AWS_SECRET_KEY ="test"
    val AWS_ENDPOINT ="http://localstack:4566"

  val  S3_PATH =   "s3a://fleet/"

  val DS_FILEPATH = S3_PATH +"ds.parquet"
  val DSBK_FILEPATH = S3_PATH +"dsbk.parquet"
  val VMPG_FILEPATH = S3_PATH +"vmPG.parquet"
  val PP_FILEPATH = S3_PATH +"pp.parquet"
  val PS_FILEPATH = S3_PATH +"ps.parquet"
  val PSG_FILEPATH = S3_PATH +"PSG.parquet"
  val STOREONCE_FILEPATH = S3_PATH +"storeonce.parquet"
  val VM_FILEPATH = S3_PATH +"vm.parquet"
  val VMBK_FILEPATH = S3_PATH +"vmbk.parquet"
  val DO_FILEPATH = S3_PATH +"do.parquet"
  val MSSQL_INSTANCE_FILEPATH = S3_PATH + "mysqlDbInstance.parquet"
  val MSSQL_DATABASE_FILEPATH = S3_PATH + "mysql-db.parquet"
  val MSSQL_PG_FILEPATH = S3_PATH + "mysqlDbPG.parquet"
  val MSSQL_BACKUP_FILEPATH = S3_PATH + "msqlbackup.parquet"
  val MSSQL_SNAPS_FILEPATH = S3_PATH + "mssqlsnaps.parquet"
  val VOLUME_PG_FILEPATH = ""
  val VOLUME_PG_BACKUP_FILEPATH =S3_PATH + "volumePGbackup.parquet"
  val VOLUME_PG_SNAPS_FILEPATH= S3_PATH +"volumePGsnaps.parquet"




/*
  val  S3_PATH = "s3a://panorama-test-1/Bronze/Dec18/GLBR/"
  val AWS_ACCESS_KEY ="AKIA2OSWXPZCFRFL5LXZ"
  val AWS_SECRET_KEY =""
  val AWS_ENDPOINT ="s3.amazonaws.com"


  val MSSQL_INSTANCE_FILEPATH = S3_PATH + "MSSQL-DBINS/partitionKey=2023-12-18-04-25/clvsjkbpo64176erpt5g.parquet/part-00000-16197b41-89b0-4f75-af7a-e53b1e3166d6-c000.snappy.parquet"
  val MSSQL_DATABASE_FILEPATH = S3_PATH + "MSSQL-DB/partitionKey=2023-12-18-04-25/clvsjkbpo64176erpt5g.parquet/part-00000-88e55a85-fb01-42ad-b245-01a664c85f67-c000.snappy.parquet"
  val MSSQL_PG_FILEPATH = S3_PATH + "MSSQL-DBPG/partitionKey=2023-12-18-04-25/clvsjkbpo64176erpt5g.parquet/part-00000-1575575b-f765-4491-85b9-e047de6b6639-c000.snappy.parquet"
  val MSSQL_BACKUP_FILEPATH = S3_PATH + "MSSQL-BK/partitionKey=2023-12-18-04-25/clvsjkbpo64176erpt5g.parquet/part-00000-b7034afc-f2ec-44ad-9ed7-c3100bf3a343-c000.snappy.parquet"
  val MSSQL_SNAPS_FILEPATH = S3_PATH + "/MSSQL-SNP/partitionKey=2023-12-18-04-25/clvsjkbpo64176erpt5g.parquet/part-00000-31965c0e-557b-4ef5-9ae9-037c81c89b31-c000.snappy.parquet"
 
  val VOLUME_PG_FILEPATH = S3_PATH + "VPG/partitionKey=2024-01-08-10-04/cmdshc3po647o12j5r7g.parquet/part-00000-3169aaff-7acc-4b86-88e5-906e3f0b34ab-c000.snappy.parquet"
  val VOLUME_PG_BACKUP_FILEPATH = S3_PATH + "VPG-BK/partitionKey=2024-01-08-10-04/cmdshc3po647o12j5r7g.parquet/part-00000-e2a63b65-8c17-42fb-afa3-2eb8ca8e4103-c000.snappy.parquet"
  val VOLUME_PG_SNAPS_FILEPATH = S3_PATH + "VPG-SNP/partitionKey=2024-01-08-10-04/cmdshc3po647o12j5r7g.parquet/part-00000-00e94bae-ca98-4226-aea8-8341fa1b0da0-c000.snappy.parquet"

 
  val VMBK_FILEPATH = S3_PATH +"VMBK/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-b6342805-5127-4684-a49a-eb89728c08c6-c000.snappy.parquet"
  val VM_FILEPATH = S3_PATH +  "VM/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-f845eae3-5f98-4910-8b38-967c00652969-c000.snappy.parquet"
  val STOREONCE_FILEPATH = S3_PATH +"STOREONCE/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-8dcaa0a8-2ea6-424c-bfa5-e6f28ea4cd16-c000.snappy.parquet"
  val PSG_FILEPATH = S3_PATH +"PSG/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-515a9230-9190-4995-a5e3-7c75707ce0ad-c000.snappy.parquet"  
  val PS_FILEPATH = "s3a://panorama-test-1/Bronze/Dec6/GLBR/PS/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-d568e092-9fc4-4eaa-8e05-740c0c9d840a-c000.snappy.parquet" 
  val PP_FILEPATH = S3_PATH +"PP/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-02e6b346-3d62-4b8a-bbed-1a59a47665be-c000.snappy.parquet"
  val VMPG_FILEPATH = S3_PATH +"VMPG/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-13b469de-4d46-4e5b-ba62-ca183cbb9160-c000.snappy.parquet"
  val DS_FILEPATH = S3_PATH +"DS/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-3e7077dc-f18a-441b-b3d7-5a8cfc7f3c86-c000.snappy.parquet"
  val DSBK_FILEPATH = S3_PATH + "DSBK/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-ec04cf99-0b0b-49cf-b380-11ef75eea2b2-c000.snappy.parquet" //no data
  val DO_FILEPATH = S3_PATH + "DO/partitionKey=2023-12-05-15-56/clnkgcjpo648gpmbtp2g.parquet/part-00000-2832ea5a-1529-4b91-b54f-2445e6f00ed7-c000.snappy.parquet"
  
 


  val VMBK_FILEPATH = S3_PATH +"VMBK/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-a34dc035-4e50-4fbb-912b-f6d8000ac9cc-c000.snappy.parquet"
  val VM_FILEPATH = S3_PATH +  "VM/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-8a505899-e397-46f9-9df2-27614a69adb4-c000.snappy.parquet"
  val STOREONCE_FILEPATH = S3_PATH +"STOREONCE/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-a0c1e6d7-db38-45d6-a905-d10e616b7d32-c000.snappy.parquet"
  val PSG_FILEPATH = S3_PATH +"PSG/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-33377cd1-6bc0-42fb-ae05-9c385ca81894-c000.snappy.parquet"  
  val PS_FILEPATH = S3_PATH +"PS/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-80ad25fe-4f0e-425f-9f23-1903269f2aee-c000.snappy.parquet" 
  val PP_FILEPATH = S3_PATH +"PP/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-cacb1736-9379-47ac-b477-373945873cfe-c000.snappy.parquet"
  val VMPG_FILEPATH = S3_PATH +"VMPG/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-50d219d2-88ba-4bb2-9077-e1a9b017685b-c000.snappy.parquet"
  val DS_FILEPATH = S3_PATH +"DS/partitionKey=2023-11-14-17-25/cl9qr0rpo64f90fmhlrg.parquet/part-00000-a112f228-81f3-40cb-aef9-26675d8af863-c000.snappy.parquet"
  val DSBK_FILEPATH = S3_PATH + "" //no data
  */
}