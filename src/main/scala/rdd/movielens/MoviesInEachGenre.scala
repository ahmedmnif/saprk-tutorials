package rdd.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object MoviesInEachGenre {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val movies_rdd = sc.textFile("in/movies.dat")
    val genre = movies_rdd.map(lines => lines.split("::")(2))
    val flat_genre = genre.flatMap(lines => lines.split("\\|"))
    val genre_kv = flat_genre.map(k => (k, 1))
    val genre_count = genre_kv.reduceByKey((k, v) => (k + v))
    val genre_sort = genre_count.sortByKey()
    genre_sort.saveAsTextFile("out/result-csv")

  }
}