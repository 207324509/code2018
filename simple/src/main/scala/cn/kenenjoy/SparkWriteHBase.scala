package cn.kenenjoy

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 编写程序向HBase写入数据
  * Created by hefa on 2017/7/14.
  */
object SparkWriteHBase {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("SparkWriteHBase").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val conf = HBaseConfiguration.create()

    var jobConf = new JobConf(conf)
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, "student")
    jobConf.setOutputFormat(classOf[TableOutputFormat])
    jobConf.setOutputKeyClass(classOf[ImmutableBytesWritable])
    jobConf.setOutputValueClass(classOf[Result])

    val indataRDD = sc.makeRDD(Array("1,张三,M,26", "2,李四,M,27")) //构建两行记录
    val rdd = indataRDD.map(_.split(',')).map { arr => {
      val put = new Put(Bytes.toBytes(arr(0))) //行健的值
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(arr(1))) //info:name列的值
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("gender"), Bytes.toBytes(arr(2))) //info:gender列的值
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes( arr(3).toInt)) //info:age列的值
      (new ImmutableBytesWritable, put)
    }
    }
    rdd.saveAsHadoopDataset(jobConf)

    sc.stop()

  }
}
