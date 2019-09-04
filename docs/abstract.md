Abstract

Everyday huge volumes of data is being generetad from different data sources in different companies. The challenge is not just to process these datas but to stream them fast and analyze them in real-time.

The main aim of this project is to implement a system with different tools in order to maintain a data pipeline which is highly scalable and fault tolerant for a real-time data stream. We are going to use some popular tools like Kafka and Spark for streaming. Kafka is distributed streaming platform and Spark is unified analytics engine. Our data sources are the stocks data. And we are going to use memSQl as our real-time database. 

For data visualization, we can use Tableau. Beside these, we are using docker containers and Kubernetes for managing containers. The data from data source is feed to Kakfa topic using Source Connector and Spark is monitoring the kafka topic as a consumer. We are using pipeline to ingest data into memSQL.








