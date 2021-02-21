# Kafka

### Topics : 
	- A sparticular stream if data
	- Similar to a table in a data base
 	- Topic is identified by a name

 	- Topics are split in partitions	
 	- Partitions are ordered starting with 0 
 	- Each message in partiton has incremental id

## What does message look like?
 	- Suppose we have a fleet of trucks and each truck has gps
 	  kafka will have a topic truck_gps contains all the trucks
 	  truck will send a message to kafka ever n seconds the message 
 	  will contain truck id and lattitude and logitude

### Brokers : 
	- A Kafka cluster is composed with multiple brokers
 	- A bloker contains multiple topics.
 	- Broker has an unique id
 	- A broker will contain a certain topics
 	- Connecting to any broker will be connecting to the entire cluser

### Topic reflication factor : 
	- Kafka is a distributed system
 	- Reflication factor is generally 2 or 3. Mostly it is 3.

### Concept for leader for a partition
	- At any time only one broker can be a leader for a given partition 
	- Only one leader can receive an server data for a partition
	- The other broker will synchronize the data.
	- Therefore each partition will have only one leader and multiple ISR(in-sync replica)

### Producers
	- Producer write data to topics 
	- Producer automatically know to which broker to write to
	- In case producer fail producer will automatically recover and balance load

### Consumers
	- Consumers can read data from topic
	- Knows which broker to read from
	- In case of failure consumer know how to recover
	- Data is read in order from a particular partition. No order while reading from multiple partitions.

### Consumer offset
	- Kafka stores the offsets at which consumer group has been reading(in simple terms book mark)
	- Offsets commited live in a kafka topic name __consumer_offsets
	- When a consumer has consumed data from kafka it must commit the offset.
	- If a consumer dies when it comes back live system should know its last read.

### Kafka Broker Discovery
	- Connecting to any broker will be connecting to the entire cluster
	- Each broker knows about all the brokers, topics and partitions(metadata)
	- Kafka broker is called "bootstrap sever"

## Zookeper
	- Manages brokers(keeps a list of all the brokers)
	- Helps electing the leader election of partitions.
	- Sends notification to kafka in case of any change (new topic, broker dies, broker comes up, delete topic,.....)
	- Kafka won't start without zookeeper.
	- Zookeper doesn't store consumer offset

[Producers and Consumer can bare upto N-1 brokers being down in case of N reflication factor]




#############################################################
## install 
``` brew install kafka```

## start
``` zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties ```
``` kafka-server-start /usr/local/etc/kafka/server.properties ```
- Change default data and log in both the properties file while running for the first time.

## create topics
```kafka-topics --zookeeper localhost:2181 -topic first_topic --create --partitions 3 --replication-factor 2```

##  list all topics
```kafka-topics --zookeeper localhost:2181 --list```

## describe topic 
```kafka-topics --zookeeper localhost:2181 --topic first_topic --describe```

## delete topic
```kafka-topics --zookeeper localhost:2181 --topic second_topic --delete```

```kafka-topics --zookeeper localhost:2181 -topic second_topic --create --partitions 3 --replication-factor 1```

## kafka console producer
```kafka-console-producer -broker-list localhost:9092 --topic first_topic```
```kafka-console-producer -broker-list localhost:9092 --topic first_topic --producer-property acks=all```

- if produce in a topic which doesnot exist kafka will create the topic for you. Initially it will warn 	"LEADER NOT AVAILABLE" then from second message onward it will recover and add LEADER. default partition 	 value = 1 and replication factor 1. You can set default partition value in server.properties

## kafka-console-consumer
```kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic```
- it will only read new messages.

- to read old messages.
 ```kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --from-beginning```

## kafka-console-consumer group
```kafka-console-consumer --bootstrap-server localhost:9092 --topic first_topic --group my-first-application```
- you can give any group appliction
- if you are consuming multiple groups in the same producer. The messages get split in  different instances as they are part of the same group

## kafka-consumer- groups
```kafka-consumer-groups --bootstrap-server localhost:9092 --list```
```kafka-consumer-groups --bootstrap-server localhost:9092 --describe -group my-first-application ```
```kafka-consumer-groups --bootstrap-server localhost:9092 --group my-first-application --reset-offsets --to-earliest --execute --topic first-topic```

[Kafka UI Client : Conduktor]
