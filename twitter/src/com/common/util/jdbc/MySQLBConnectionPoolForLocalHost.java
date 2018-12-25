package com.common.util.jdbc;

// java
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;



public class MySQLBConnectionPoolForLocalHost {
	// log file
	private static final Logit log = Logit.getInstance(MySQLBConnectionPoolForLocalHost.class);

	// properties name
	private static final String CONFIG_FILE_NAME = "MySQL_getlovenet_result.properties";

	// data source instance
	public static DataSource dataSource = null;

	static {
		try {
			dataSource = initlize();
		} catch (Exception ex) {
			String errorMsg = " MySQLocalHost.properties initlize failed \n"
					+ ex.getMessage();
			log.error(errorMsg, ex);
		}
	}

	/***************************************************************************
	 * initlize the DB2ConnectionPool.
	 * 
	 * <pre>
	 *  (1) read Driver , ConnectionURL, UserName , Password
	 *      from DB2ConnectionPool.properties .
	 *  (2) use those properties to initized a DataSource,
	 *      and return it .      
	 * </pre>
	 **************************************************************************/
	private static DataSource initlize() throws Exception {
		// 1. read properties
		Properties props = Logit.getProperties(CONFIG_FILE_NAME);
		String driver = props.getProperty("Driver", "");
		String connectionURL = props.getProperty("ConnectionURL", "");
		String userName = props.getProperty("UserName", "");
		String password = props.getProperty("Password", "");
		int maxActive = Integer.parseInt(props.getProperty("maxActive", "10"));
		int maxIdle = Integer.parseInt(props.getProperty("maxIdle", "5"));
		int maxWait = Integer.parseInt(props.getProperty("maxWait", "1000"));

		// 2. loading jdbc driver
		log.info("Loading underlying JDBC driver.");
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw e;
		}
		log.info("Done.");

		// 3.initlize a pooling datasource
		return setupDataSource(connectionURL, // url
				userName, // user name
				password, // password
				maxActive, // max active connection
				maxIdle, // max idle connection
				maxWait); // max wait time

	}

	/***************************************************************************
	 * initlize a pooling DataSource
	 * 
	 * @param String
	 *            connectionURL - he connection url
	 * @param String
	 *            usrName - the user name
	 * @param String
	 *            password - the password
	 **************************************************************************/
	private static DataSource setupDataSource(String connectionURL,
			String usrName, String password, int maxActive, int maxIdle,
			int maxWait) {

		// First, we'll need a ObjectPool that serves as the
		// actual pool of connections.
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		GenericObjectPool connectionPool = new GenericObjectPool(null);
		connectionPool.setMaxActive(maxActive);
		connectionPool.setMaxIdle(maxIdle);
		connectionPool.setMaxWait(maxWait);

		//
		// Next, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
				connectionURL, usrName, password);

		//
		// Now we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);

		//
		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		//
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		return dataSource;

	}

}
