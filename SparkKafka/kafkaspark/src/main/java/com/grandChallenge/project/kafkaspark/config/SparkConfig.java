package com.grandChallenge.project.kafkaspark.config;

import org.apache.spark.SparkConf;

public class SparkConfig {

	public SparkConf sparkConf(String sparkName) {
        return new SparkConf()
				.setAppName(sparkName)
				.setMaster("local[*]");
                
    }
}
