package com.simmya.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.springframework.beans.factory.annotation.Autowired;



public class DbUtil {

	@Autowired
	private static DataSource dataSource;
	public static void setDataSource(DataSource dataSource) {
		DbUtil.dataSource = dataSource;
	}

	private DbUtil() {
		
	}
	
	public static DataSource getDataSource() {
		return dataSource;
	}

	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public static <T> T getPojo(String sql, Class<T> clazz) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new BeanHandler<T>(clazz));
	}
	
	public static <T> List<T> getPojoList(String sql, Class<T> clazz) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new BeanListHandler<T>(clazz));
	}
	
	public static Map<String, Object> getMap(String sql) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		Map<String, Object> map = runner.query(sql, new MapHandler());
		if (map == null)
			map = Collections.emptyMap();
		return map;
	}
	
	
	public static Map<String, Object> getMap(String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		Map<String, Object> map = runner.query(sql, new MapHandler(), params);
		if (map == null)
			map = Collections.emptyMap();
		return map;
	}
	
	public static List<Map<String, Object>> getMapList(String sql) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new MapListHandler());
	}
	
	public static List<Map<String, Object>> getMapList(String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new MapListHandler(), params);
	}
	
	public static <T> List<T> getColumnList(String sql, String column, Class<T> type) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		return runner.query(sql, new ColumnListHandler<T>(column));
	}
	
	public static void update(String sql, Object... params) throws SQLException {
		QueryRunner runner = new QueryRunner(dataSource);
		runner.update(sql, params);
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) throws SQLException {
		if (rs != null)
			rs.close();
		if (stmt != null)
			stmt.close();
		if (conn != null)
			conn.close();
	}
	
}