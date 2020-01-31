package org.hs.flink.flinkstate

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector

class TestSum extends RichFlatMapFunction[(String, Long), (String, Long)] {

  private var sum: ValueState[Long] = _

  override def flatMap(input: (String, Long), out: Collector[(String, Long)]): Unit = {

    // access the state value
    val currentSum = sum.value


    // update the count
    val newSum = currentSum + 1

    // update the state
    sum.update(newSum)

    out.collect((input._1,newSum))

  }

  override def open(parameters: Configuration): Unit = {
    sum = getRuntimeContext.getState(
      new ValueStateDescriptor[Long]("sum", classOf[Long])
    )
  }
}
