# kafka-streams-warsaw-meetup
Exercises from Kafka Streams meetups at Scala Warsaw meetup

```
./bin/zookeeper-server-start.sh config/zookeeper.properties
```

```
// optional step
./bin/kafka-topics.sh \
    --delete \
    --topic exercise1-input \
    --zookeeper :2181
```

```
./bin/kafka-topics.sh \
    --create \
    --topic exercise1-input \
    --partitions 1 \
    --replication-factor 1 \
    --zookeeper :2181
```

```
./bin/kafka-server-start.sh config/server.properties
```

```
./bin/kafka-server-start.sh config/server.properties \ 
    --override log.dirs=/tmp/kafka-2 \ 
    --override port=9192 \ 
    --override broker.id=1
```

```
./bin/kafka-console-producer.sh \
    --broker-list :9092 \
    --topic exercise1-input \
    --property parse.key=true \
    --property key.separator=,
```

```
./bin/kafka-console-consumer.sh \
    --bootstrap-server :9092 \
    --topic exercise1-input \
    --property print.key=true
```

```
cat src/main/resources/exercise1-input.csv | ./bin/kafka-console-producer.sh \
    --broker-list :9092 \
    --topic exercise1-input \
    --property parse.key=true \
    --property key.separator=,
```

## Gotchas

1. [How to define Scala API for Kafka Streams as dependency in build.sbt?
](https://stackoverflow.com/q/53733244/1305344)