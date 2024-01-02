package com.etl
//import scala.math.random
//import io.delta.tables._
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.SparkSession
 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SparkSession, SQLContext}
import io.delta.tables._

import org.apache.spark.sql.functions._
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.spark.sql.types._
import java.time._
import org.apache.log4j.Logger
import org.apache.spark.sql.Encoders
import com.etl.Constants._

trait SparkObject  {
 
   
     val spark = SparkSession
      .builder()
      .appName("Quickstart")
      .master("local[*]")
      //delta lake configuration
      .config("spark.sql.extensions", "io.delta.sql.DeltaSparkSessionExtension")
      .config(
        "spark.sql.catalog.spark_catalog",
        "org.apache.spark.sql.delta.catalog.DeltaCatalog"
      )
      .config("spark.hadoop.fs.s3a.aws.credentials.provider", "org.apache.hadoop.fs.s3a.SimpleAWSCredentialsProvider")
      .getOrCreate()
    
spark.sparkContext
    .hadoopConfiguration.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")      
spark.sparkContext
    .hadoopConfiguration.set("fs.s3a.access.key", AWS_ACCESS_KEY)
spark.sparkContext
    .hadoopConfiguration.set("fs.s3a.secret.key", AWS_SECRET_KEY)
spark.sparkContext
    .hadoopConfiguration.set("fs.s3a.endpoint", AWS_ENDPOINT)
spark.sparkContext.hadoopConfiguration.set("fs.s3a.path.style.access", "true")
  
}


