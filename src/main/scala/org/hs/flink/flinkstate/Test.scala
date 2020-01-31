package org.hs.flink.flinkstate

import org.apache.flink.core.fs.Path

object Test extends App {


  print(new Path("file:///Test").toUri())
}
