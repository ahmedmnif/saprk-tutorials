package sparkSql.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Sparkdatalake {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    // 2nd method is to read the file directly into a dataFrame and create a temp view// 2nd method is to read the file directly into a dataFrame and create a temp view

    spark.read.textFile("in/movies.dat").createOrReplaceTempView("movies_staging")
    spark.read.textFile("in/ratings.dat").createOrReplaceTempView("ratings_staging")
    spark.read.textFile("in/users.dat").createOrReplaceTempView("users_staging")
    // Create a database to store the tables
    spark.sql("drop database if exists sparkdatalake cascade")
    spark.sql("create database sparkdatalake")
    // Make appropriate schemas for them
    // movies
    spark.sql(
      """ Select
split(value,'::')[0] as movieid,
split(value,'::')[1] as title,
substring(split(value,'::')[1],length(split(value,'::')[1])-4,4) as year,
split(value,'::')[2] as genre
from movies_staging """).write.mode("overwrite").saveAsTable("sparkdatalake.movies")
    // users
    spark.sql(
      """ Select
split(value,'::')[0] as userid,
split(value,'::')[1] as gender,
split(value,'::')[2] as age,
split(value,'::')[3] as occupation,
split(value,'::')[4] as zipcode
from users_staging """).write.mode("overwrite").saveAsTable("sparkdatalake.users")
    // ratings
    spark.sql(
      """ Select
split(value,'::')[0] as userid,
split(value,'::')[1] as movieid,
split(value,'::')[2] as rating,
split(value,'::')[3] as timestamp
from ratings_staging """).write.mode("overwrite").saveAsTable("sparkdatalake.ratings")

  }
}