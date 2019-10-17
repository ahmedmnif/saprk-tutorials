package dataframe.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


object CleanMovies {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()


    // Clean data into DataFrame
    val movies = spark.read.textFile("in/movies.dat")
    import spark.implicits._
    val m_id = movies.map(lines => lines.split("::")(0)).toDF("MovieID")
    val m_title = movies.map(lines => lines.split("::")(1)).toDF("Title")
    val m_genre = movies.map(lines => lines.split("::")(2)).toDF("Genres")
    // For appending the dataframes, we need to import monotonically_increasing_id
    import org.apache.spark.sql.functions.monotonically_increasing_id
    val m_res1 = m_id.withColumn("id", monotonically_increasing_id()).join(m_title.withColumn("id", monotonically_increasing_id()), Seq("id")).drop("id")
    val m_result = m_res1.withColumn("id", monotonically_increasing_id()).join(m_genre.withColumn("id", monotonically_increasing_id()), Seq("id")).drop("id")
    // This will give us the valid data with schema
    m_result.show
    // Examples
    m_result.where("MovieID=1").show
    m_result.filter("Genres == 'Action'").show
    m_result.select("Title").show


  }

}
