import java.util.Properties

import org.apache.kafka.streams.kstream.{Printed, UnlimitedWindows, Windowed}
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object HelloWorldApp extends App {

  /**
  // Step 1. Build Topology
  // Topology == DAG

  // Kafka Streams
  // Low-Level Processor API
  // ==> High-Level Streams DSL
    */

  import org.apache.kafka.streams.scala._
  import ImplicitConversions._
  import Serdes._

  val builder = new StreamsBuilder
  val input = builder.stream[String, String](topic = "meetup-input")

  // Zamien komunikaty na WIELKIE LITERY
  // Exercise: map
  val mapped = input.mapValues(_.toUpperCase)

  // Na wejściu mamy zdania, np. (1, Ala ma kota)
  // Podziel zdanie na słowa/tokeny
  // Policz wystąpienie słów w zdaniu
  // flatMap -> (1, Ala), (1, ma), (1, kota)
  // groupBy po słowach
  // count
  // Exercise 1: Global counts
  val globalCounts = mapped
    .flatMapValues(_.split("\\s+"))
    .groupBy((_, word) => word)
    .windowedBy(UnlimitedWindows.of)
    .count()
    .toStream
    .mapValues(_.toString)

  val p = Printed
    .toSysOut[Windowed[String], String]
    .withLabel("scala-meetup")
  globalCounts.print(p)

  val topology = builder.build()
  println(topology.describe())

  val appId = this.getClass.getSimpleName.replace("$", "")
  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId)
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, ":9092")
  val ks = new KafkaStreams(topology, props)
  ks.start()

}
