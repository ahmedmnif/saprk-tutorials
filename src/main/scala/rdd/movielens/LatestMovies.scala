package rdd.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object LatestMovies {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val movies_rdd = sc.textFile("in/movies.dat")
    val movie_nm = movies_rdd.map(lines => lines.split("::")(1))
    val year = movie_nm.map(lines => lines.substring(lines.lastIndexOf("(") + 1, lines.lastIndexOf(")")))
    val latest = year.max
    val latest_movies = movie_nm.filter(lines => lines.contains("(" + latest + ")")).saveAsTextFile("out/latestMovies")

  }
}
