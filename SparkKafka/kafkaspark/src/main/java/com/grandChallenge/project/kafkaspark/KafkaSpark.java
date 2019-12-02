package com.grandChallenge.project.kafkaspark;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grandChallenge.project.kafkaspark.HashTagsUtils;

import scala.Tuple2;

public class KafkaSpark {

	private static JavaSparkContext jsc;
	
	public KafkaSpark(String sparkName, List<String> topicList, int threadsNum) throws InterruptedException {
		SparkConf sparkConf = new SparkConf()
				.setAppName(sparkName)
				.setMaster("local[*]");
		JavaStreamingContext sc = new JavaStreamingContext(sparkConf, Durations.seconds(500));
		sparkStreaming(topicList, threadsNum, sc);
	}

	
	private void sparkStreaming(List<String> topicList, int threadsNum, JavaStreamingContext sc)
			throws InterruptedException {
		// Map<String, Integer> topics = new HashMap<>();
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

		lines.count().map(cnt -> "Popular hash tags (" + cnt + " total tweets):").print();

//
		lines.print();

		lines.flatMap(HashTagsUtils::hashTagsFromTweet).mapToPair(hashTag -> new Tuple2<>(hashTag, 1))
				.reduceByKey(Integer::sum)

				.mapToPair(Tuple2::swap).foreachRDD(rrdd -> {

					System.out.println("\n\n\n---------------------------------------------------------------\n\n\n");
					List<Tuple2<Integer, String>> sorted;
					JavaPairRDD<Integer, String> counts = rrdd.sortByKey(false);
					sorted = counts.collect();
					sorted.forEach(record -> System.out.println(String.format(" %s (%d)", record._2, record._1)));
				});

		/*
		 * stream.foreachRDD(rdd -> { rdd.foreach(message -> {
		 * System.out.println("\n\n\n\n\n\nHere"); System.out.println(message); }); });
		 * 
		 * JavaPairDStream<String, Integer> countOfMessageKeys = stream
		 * .map((ConsumerRecord<String, String> record) -> record.key())
		 * .mapToPair((String s) -> new Tuple2<>(s, 1)).reduceByKey((Integer i1, Integer
		 * i2) -> i1 + i2);
		 * 
		 * countOfMessageKeys.print();
		 */
		sc.start();
		sc.awaitTermination();

	}

}
