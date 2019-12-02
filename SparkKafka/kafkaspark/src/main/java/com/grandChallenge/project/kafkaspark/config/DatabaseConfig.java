/**
 * 
 */
package com.grandChallenge.project.kafkaspark.config;

/**
 * @author student
 *
 */
public class DatabaseConfig {

	public static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
	public static final String DB_URL = "jdbc:mariadb://172.17.0.5:3306/db_twitter";
	public static final String dbUserName = "root";
	public static final String dbUserPassword = "";
	public static final String tblName = "tb_tweets";
}
