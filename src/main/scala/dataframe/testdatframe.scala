package dataframe

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext, SparkFiles}
import org.apache.spark.sql.SparkSession

object testdatframe {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("collect").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val filePath = "ftp://parkeondev:eqx#1234@193.70.9.83/test-spark.xlsx"
    sc.addFile(filePath)
    var fileName = SparkFiles.get(filePath.split("/").last)
    var file = sc.textFile(fileName)


  }

}
