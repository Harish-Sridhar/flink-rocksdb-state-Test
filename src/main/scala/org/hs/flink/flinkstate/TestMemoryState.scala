package org.hs.flink.flinkstate

import org.apache.flink.contrib.streaming.state._
import org.apache.flink.runtime.state.memory.MemoryStateBackend
import org.apache.flink.streaming.api.scala._

object TestMemoryState {
  def main(args: Array[String]) {

    val host = args(0)

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.enableCheckpointing(15000L)

    env
      .setStateBackend(new MemoryStateBackend())

    new RocksDBStateBackendFactory

    val text = env
      .socketTextStream(host, 9999)
    val counts = text.flatMap { _.toLowerCase.split("\\W+") filter { _.nonEmpty } }
      .map { (_, 1L) }
      .keyBy(0)
      .flatMap(new TestSum())
    //.timeWindow(Time.seconds(5))
    //.sum(1)

    counts.print()

    env.execute("Window Stream WordCount")
  }
}
