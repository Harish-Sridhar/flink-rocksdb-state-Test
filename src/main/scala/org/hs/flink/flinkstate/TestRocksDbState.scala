package org.hs.flink.flinkstate

import java.net.InetAddress

import org.apache.flink.contrib.streaming.state._
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala._

object TestRocksDbState {
  def main(args: Array[String]) {

    val host = args(0)

   /* val properties:Properties = new Properties()
    properties.setProperty("state.backend", "rocksdb")
    properties.setProperty("state.checkpoints.dir", "file:///usr/share/dsh-base-flink/volume/data")
    properties.setProperty("state.backend.rocksdb.localdir", "file:///usr/share/dsh-base-flink/volume/data/rocksdb")
    properties.setProperty("state.checkpoints.num-retained", "1")
    properties.setProperty("state.savepoints.dir", "file:///usr/share/dsh-base-flink/volume/data")
    properties.setProperty("state.backend.local-recovery", "true")



   // properties.setProperty("state.backend.local-recovery", "true")
    //val conf = ConfigurationUtils.createConfiguration(properties)
*/
    val env = StreamExecutionEnvironment.getExecutionEnvironment


    env.enableCheckpointing(15000L)
    // enable externalized checkpoints which are retained after job cancellation
    env.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
    // make sure 500 ms of progress happen between checkpoints
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    // allow job recovery fallback to checkpoint when there is a more recent savepoint
    env.getCheckpointConfig.setPreferCheckpointForRecovery(true);

    val rockdsDbBackend =
      new RocksDBStateBackend("file:///usr/share/dsh-base-flink/volume/data/rocksdb", true)

    env
      .setStateBackend(rockdsDbBackend)


    val text = env
      .socketTextStream(host, 9999)
    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map { (_, 1L) }
      .keyBy(0)
      .flatMap(new TestSum())
      .uid("TestSum")
        .map(a =>
        if(a._1.toLowerCase=="fail"){
          val inetAddress = InetAddress.getLocalHost
          println(s"${inetAddress.getHostName} - ${inetAddress.getHostAddress} - ${a}")
          throw new RuntimeException("Got an Fail message")
        }

        else{
          val inetAddress = InetAddress.getLocalHost
          println(s"${inetAddress.getHostName} - ${inetAddress.getHostAddress} - ${a}")
          a
        }
        )
      //.timeWindow(Time.seconds(5))
      //.sum(1)

    counts.print()

    env.execute("Window Stream WordCount")
  }
}

