package cn.kenenjoy

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.flume._


/**
  * Created by hefa on 2017/7/15.
  */
object FlumeEventCount {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      System.err.println(
        "Usage: FlumeEventCount <host> <port>")
      System.exit(1)
    }

    StreamingExamples.setStreamingLogLevels()

    val Array(host,port) = args
    val batchInterval = Milliseconds(2000)
    // Create the context
    val sparkConf = new SparkConf().setAppName("FlumeEventCount").setMaster("local")
    val ssc = new StreamingContext(sparkConf, batchInterval)
    // Create a flume stream
    val stream = FlumeUtils.createStream(ssc, host, port.toInt, StorageLevel.MEMORY_ONLY_SER_2)
    // Print out the count of events received from this server in each batch
    stream.count().map(cnt => "Received " + cnt + " flume events.").print()
    ssc.start()
    ssc.awaitTermination()
    sys.addShutdownHook({
      ssc.stop(true,true)
    })
  }
}
