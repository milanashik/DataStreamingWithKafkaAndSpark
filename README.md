# DataStreamingWithKafkaAndSpark
Real Time Data Streaming using Kafka and Spark

## Abstract

Everyday huge volumes of data are being generated from different data sources in different companies. The challenge is not just to process this data but to stream it fast and analyze it in real-time.

The main aim of this project is to implement a system with different tools in order to maintain a data pipeline that is highly scalable and fault-tolerant for a real-time data stream. We are going to use some currently popular tools like Kafka and Spark for streaming. Kafka is a distributed streaming platform and Spark is a unified analytics engine. We are going to use different data from different data sources. And we are going to use memSQl as our real-time database.

For data visualization, we can use Tableau. Besides these, we are using Docker containers and Kubernetes for managing containers. The data from the data source is feed to a Kafka topic using Source Connector, and Spark is monitoring the Kafka topic as a consumer. We are using the pipeline to ingest data into memSQL.

We are using the best tools and practices that are known today. At the end of the project, we expect to get hands-on experiences on these tools and technologies.

## Running the project
 ### Step by step Instructions:
  - Setup Docker Container for kafka and memSQl.
  https://github.com/milanashik/DataStreamingWithKafkaAndSpark/wiki/Week6-Docker-Installation-kafka,-kafka-connect-and-memSQL
  
  - Setup Kafka Connector to twitter
  https://github.com/milanashik/DataStreamingWithKafkaAndSpark/wiki/Week6-Docker-Installation-kafka,-kafka-connect-and-memSQL#kafka-connector-for-twitter
  
  - Verify twitter data is received in kafka topic. You can create kafka topic from control center.
  ```
  docker-compose exec broker kafka-console-consumer --bootstrap-server localhost:29092 --topic twitter
  ```
  
  
  
  
