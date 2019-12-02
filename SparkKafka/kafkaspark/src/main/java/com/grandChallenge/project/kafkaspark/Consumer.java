/**
 * 
 */
package com.grandChallenge.project.kafkaspark;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandChallenge.project.kafkaspark.config.DatabaseConfig;

import scala.Tuple2;

/**
 * @author student
 *
 */
public class Consumer implements Serializable {
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	int id;
	Connection conn = null;
	Statement stmt = null;

	public Consumer(String sparkName, List<String> topicList, int threadsNum) throws InterruptedException, SQLException {
		SparkConf sparkConf = new SparkConf().setAppName(sparkName).setMaster("local[*]");
		JavaStreamingContext sc = new JavaStreamingContext(sparkConf, Durations.seconds(500));
		storeSparkToDatabase(topicList, threadsNum, sc);
	}

	private void storeSparkToDatabase(List<String> topicList, int threadsNum, JavaStreamingContext sc)
			throws InterruptedException, SQLException {
		Collection<String> topics = topicList;

		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "172.17.0.3:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);

		JavaInputDStream<ConsumerRecord<String, String>> stream = KafkaUtils.createDirectStream(sc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams));

		JavaDStream<String> lines = stream.map(x -> x.value());
		lines.flatMap(HashTagsUtils::hashTagsFromTweet).mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
				.reduceByKey(Integer::sum)

				.mapToPair(Tuple2::swap).foreachRDD(rrdd -> {

					System.out.println("\n\n\n---------------------------------------------------------------\n\n\n");
					List<Tuple2<Integer, String>> sorted;
					JavaPairRDD<Integer, String> counts = rrdd.sortByKey(false);
					sorted = counts.collect();
					Gson gson = new GsonBuilder().create();
					

					try {
						Class.forName(DatabaseConfig.JDBC_DRIVER);

						conn = DriverManager.getConnection(DatabaseConfig.DB_URL, DatabaseConfig.dbUserName,
								DatabaseConfig.dbUserPassword);
						 stmt = conn.createStatement();
						LocalDateTime dt = LocalDateTime.now();
						sorted.forEach(record -> {
							id = generateUniqueId();
							String sql = "Insert into tb_popularTweets(id, Tweet, Count, dt) values (" + id + ", \""
									+ record._2 + "\", " + record._1 + ", \"" + dtf.format(dt) +"\")";
							
							try {
								stmt.executeUpdate(sql);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						});
					} catch (Exception se) {
						se.printStackTrace();
					} finally {
						try {
							assert conn != null;
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
						try {
							conn.close();
						} catch (SQLException se) {
							se.printStackTrace();
						}
					}

					// System.out.println(String.format(" %s (%d)", record._2, record._1)));
				});

		
		sc.start();
		sc.awaitTermination();

	}

	public static int generateUniqueId() {
		UUID idOne = UUID.randomUUID();
		String str = "" + idOne;
		int uid = str.hashCode();
		String filterStr = "" + uid;
		str = filterStr.replaceAll("-", "");
		return Integer.parseInt(str);
	}

}
