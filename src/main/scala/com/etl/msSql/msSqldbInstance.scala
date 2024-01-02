package com.etl.msSql
import io.delta.tables._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, SQLContext}
import org.apache.spark.sql.types._ 
import org.apache.spark.sql.Encoders
import com.etl.Constants._
import com.etl.SparkObject
import com.model.{
  MsSqlInstance 
}


object MsSqlInstanceResource extends App with SparkObject {
  val msSqlInstanceJob = new MsSqlInstanceResource(spark)
  msSqlInstanceJob.run
}
  
class MsSqlInstanceResource(spark: SparkSession) {
  private def run: Unit = {
  val psData = spark.read.format("parquet").load(MSSQL_INSTANCE_FILEPATH)
  import spark.implicits._
val flattenedDf = psData.select(
                                      col("id"),
                                      col("name"),
                                      col("customerId").alias("customer_id"),
                                      col("applicationHostInfo.id").alias("application_host_id")
                                      
)

val msSqlInstance  = flattenedDf.withColumn("time_stamp",current_timestamp()).as[MsSqlInstance]
msSqlInstance.write.format("delta").mode("Overwrite").save(S3_PATH + "delta-tables/MsSqlInstance")

/*reading from delta table
val msSqlInstanceTable  = spark.read.format("delta").load(S3_PATH + "delta-tables/MsSqlInstance")
msSqlInstanceTable.show()*/
   
 }
  
  }
