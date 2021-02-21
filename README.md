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
