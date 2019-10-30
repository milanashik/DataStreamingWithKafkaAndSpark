/**
 * 
 */
package com.grandChallenge.project.kafkaspark;
import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

/**
 * @author student
 *
 */
public class StreamingJob {
	public static JavaSparkContext sparkContext;
	private static final Pattern SPACE = Pattern.compile(" ");
	/**
	 * 
	 */
	public StreamingJob() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 */


	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws StreamingQueryException, InterruptedException {
		StreamingJob streamJob = new StreamingJob();
		streamJob.start();
	}

	private void start() throws StreamingQueryException, InterruptedException {
		SparkSession spark = SparkSession
			      .builder()
			      .appName("SparkStreamer")
			      .config("spark.master", "local")
			      .getOrCreate();
		
		Dataset<Row> df = spark
				  .readStream()
				  .format("kafka")
				  .option("kafka.bootstrap.servers", "172.17.0.5:9092")
				  .option("subscribe", "twitter")
				  .load();
				df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

			    StreamingQuery query = df.writeStream()
			      .outputMode("append")
			      .format("console")
			      .start();

			    query.awaitTermination();
	}

}
