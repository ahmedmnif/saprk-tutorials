package rdd.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object ListOfDistinctGenres {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val sc = new SparkContext(conf)


    val movies_rdd = sc.textFile("in/movies.dat")
    val genres = movies_rdd.map(lines => lines.split("::")(2))
    val testing = genres.flatMap(line => line.split('|'))
    val genres_distinct_sorted = testing.distinct().sortBy(_ (0))
    genres_distinct_sorted.saveAsTextFile("out/result")


  }


}
