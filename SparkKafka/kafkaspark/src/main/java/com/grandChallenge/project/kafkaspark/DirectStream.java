package com.grandChallenge.project.kafkaspark;

import java.util.ArrayList;
import java.util.List;

public class DirectStream {
	public static void main(String[] args) throws Exception {
		List<String> topicList = new ArrayList<>();

		topicList.add("twitter");
		//KafkaSpark kafkaStream = new KafkaSpark("KafkaSparkStreaming", topicList, 1);
		
		Consumer kafkaConsumer = new Consumer("KafkaSparkStreaming", topicList, 1);
	}
}
