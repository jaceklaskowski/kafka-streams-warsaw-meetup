
object KStreamApp extends App {

  // Boilerplate (is supposed to make your dev life easier :))
  // Bartek says: "Coś za coś" :)
  import org.apache.kafka.streams.scala._
  import Serdes._
  import ImplicitConversions._

  // Step 1. Create Topology
  // ==> Streams DSL
  // Processor API
  val builder = new StreamsBuilder
  import org.apache.kafka.streams.kstream.Printed
  val console = Printed
    .toSysOut[String, String]
    .withLabel("DEBUG")

  val topic = "exercise1-input" // FIXME Use scopt for cmd args
  import org.apache.kafka.streams.Topology
  val consumed = consumedFromSerde[String, String]
    .withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST)
  builder
    .stream[String, String](topic)(consumed)
    .peek((k,v) => println(s"(peek) $k -> $v"))
    .print(console)

  // Will this work?
  // No!
  // Invalid topology: Topic exercise1-input has already been registered by another source.
  // You cannot use the same input
//  builder.table[String, String](topic)
//    .toStream
//    .print(console)

  val topology = builder.build()
  println(topology.describe())

  // Step 2. Run the topology
  val props = new java.util.Properties()
  val appId = this.getClass.getName.replace("$", "")
  import org.apache.kafka.streams.StreamsConfig
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092") // FIXME Use scopt for cmd args
  import org.apache.kafka.streams.KafkaStreams
  val ks = new KafkaStreams(topology, props)
  ks.start()

}
