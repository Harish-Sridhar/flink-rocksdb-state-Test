name := "FlinkStates"

version := "0.1"

scalaVersion := "2.12.4"

val flinkVersion = "1.9.0"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % Compile,
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % Compile,
  "org.apache.flink" %% "flink-statebackend-rocksdb" % flinkVersion,
  "ch.qos.logback"      % "logback-classic"             % "1.2.3"
)

mainClass := Some("org.hs.flink.flinkstate.TestRocksDbState")