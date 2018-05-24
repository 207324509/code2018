package cn.kenenjoy;

import java.io.File
import javax.jdo.JDOException

import com.facebook.fb303.FacebookService
import org.antlr.runtime.RecognitionException
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException
import org.apache.hadoop.hive.ql.metadata.SessionHiveMetaStoreClient
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.thrift.TException
import org.datanucleus.exceptions.ClassNotResolvedException

object SparkHiveExample {

  case class Record(key: Int, value: String)

  def main(args: Array[String]) {
    val warehouseLocation = "spark-warehouse"
    val spark = SparkSession
      .builder()
      .master("local[1]")
      .appName("Spark Hive Example")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._
    import spark.sql

    // Queries are expressed in HiveQL
    sql("SELECT * FROM sparktest.student").show()

    val studentRDD = spark.sparkContext.parallelize(Array("3 Rongcheng M 26","4 Guanhua M 27")).map(_.split(" "))

    val schema = StructType(List(StructField("id", IntegerType, true),StructField("name", StringType, true),StructField("gender", StringType, true),StructField("age", IntegerType, true)))

    val rowRDD = studentRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))

    val studentDF = spark.createDataFrame(rowRDD, schema)

    studentDF.show()

//    studentDF.registerTempTable("tempTable")
    studentDF.createTempView("tempTable")

//    sql("insert into sparktest.student select * from tempTable")

    spark.stop()
  }
}
