import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}
import org.apache.kafka.streams.kstream.Printed

object Exercise1App extends App {

  // Boilerplate (is supposed to make your dev life easier :))
  // Bartek says: "Coś za coś" :)
  import org.apache.kafka.streams.scala._
  import Serdes._
  import ImplicitConversions._

  // Step 1. Create Topology
  // ==> Streams DSL
  // Processor API
  val builder = new StreamsBuilder
  val console = Printed
    .toSysOut[String, String]
    .withLabel("DEBUG")
  builder
    .stream[String, String](topic = "exercise1-input")
    .peek((k,v) => println(s"(peek) $k -> $v"))
    .print(console)

  val topology = builder.build()

  // Step 2. Run the topology
  val props = new java.util.Properties()
  val appId = this.getClass.getName.replace("$", "")
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092") // FIXME Use scopt for cmd args
  val ks = new KafkaStreams(topology, props)
  ks.start()

}
