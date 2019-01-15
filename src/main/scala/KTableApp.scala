import org.apache.kafka.streams.{KafkaStreams, StreamsConfig, Topology}

object KTableApp extends App {

  val appId = this.getClass.getName.replace("$", "")

  import org.apache.kafka.streams.scala._
  import ImplicitConversions._
  import Serdes._

  // Step 1. Create Topology
  // ==> Streams DSL
  // Processor API
  val builder = new StreamsBuilder
  import org.apache.kafka.streams.kstream.Printed
  val console = Printed
    .toSysOut[String, String]
    .withLabel("DEBUG")

  val topic = "exercise1-input"
  val consumed = consumedFromSerde[String, String]
    .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST)
  builder.table[String, String](topic)(consumed)
    .toStream
    .print(console)

  val topology = builder.build()
  println(topology.describe())

  // Step 2. Run the topology
  val props = new java.util.Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092") // FIXME Use scopt for cmd args
  val ks = new KafkaStreams(topology, props)
  ks.start()

}
