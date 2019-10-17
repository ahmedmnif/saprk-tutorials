package rdd.movielens

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

object MoviesStartingWithLetterOrDigit {

  def main(args: Array[String]) {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setAppName("count").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val movies_rdd = sc.textFile("in/movies.dat")
    val movies = movies_rdd.map(lines => lines.split("::")(1))
    val string_flat = movies.map(lines => lines.split(" ")(0))
    // check for the first character for a letter then find the count
    val movies_letter = string_flat.filter(word => Character.isLetter(word.head)).map(word => (word.head.toUpper, 1))
    val movies_letter_count = movies_letter.reduceByKey((k, v) => k + v).sortByKey()
    // check for the first character for a digit then find the count
    val movies_digit = string_flat.filter(word => Character.isDigit(word.head)).map(word => (word.head, 1))
    val movies_digit_count = movies_digit.reduceByKey((k, v) => k + v).sortByKey()
    // Union the partitions into a same file
    val result = movies_digit_count.union(movies_letter_count).repartition(1).saveAsTextFile("out/result-csv")

  }
}