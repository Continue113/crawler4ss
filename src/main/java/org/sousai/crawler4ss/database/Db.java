package org.sousai.crawler4ss.database;

import java.io.File;
import java.util.ArrayList;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class Db {
	private Database bd; // 数据源
	private Environment exampleEnv;// 环境对象
	private boolean isrunning = false;// 判断是否运行
	private String dbName = null ;

	public Db(String dbName){
		this.dbName = dbName ;
	}
	/**
	 * 打开数据库方法
	 */
	public void start(String path) {
		if (isrunning) {
			return;
		}
		/******************** 文件处理 ***********************/
		File envDir = new File(path);// 操作文件
		if (!envDir.exists())// 判断文件路径是否存在，不存在则创建
		{
			envDir.mkdir();// 创建
		}

		/******************** 环境配置 ***********************/
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(false); // 不进行事务处理
		envConfig.setAllowCreate(true); // 如果不存在则创建一个
		exampleEnv = new Environment(envDir, envConfig);// 通过路径，设置属性进行创建

		/******************* 创建适配器对象 ******************/
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(false); // 不进行事务处理
		dbConfig.setAllowCreate(true);// 如果不存在则创建一个
		dbConfig.setSortedDuplicates(true);// 数据分类

		bd = exampleEnv.openDatabase(null, dbName, dbConfig); // 使用适配器打开数据库
		isrunning = true; // 设定是否运行
	}

	/**
	 * 关闭数据库方法
	 */
	public void close() {
		if (isrunning&&exampleEnv != null) {
			exampleEnv.cleanLog(); // 在关闭环境前清理下日志
			isrunning = false;
			bd.close();
			exampleEnv.close();
		}
	}

	public boolean isrunning() {
		return isrunning;
	}

	/**
	 * 数据存储方法 set(Here describes this method function with a few words)
	 * 
	 * possible to elect)
	 * 
	 * @param key
	 * @param data
	 *            void
	 */
	public void set(String key, String data) {
		DatabaseEntry keyEntry = new DatabaseEntry();
		DatabaseEntry dataEntry = new DatabaseEntry();
		keyEntry.setData(key.getBytes()); // 存储数据
		dataEntry.setData(data.getBytes());

		OperationStatus status = bd.put(null, keyEntry, dataEntry);// 持久化数据

		if (status != OperationStatus.SUCCESS) {
			throw new RuntimeException("Data insertion got status " + status);
		}
	}

	/*
	 * 执行获取,根据key值获取
	 */
	public String selectByKey(String key) {
		DatabaseEntry theKey = null;
		String value = null ;
		DatabaseEntry theData = new DatabaseEntry();
		try {
			theKey = new DatabaseEntry(key.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (bd.get(null, theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) { // 根据key值，进行数据查询
			// Recreate the data String.
			byte[] retData = theData.getData();
			value = new String(retData);
		}
		
		return value ;

	}

	/**
	 * 查询所有，可遍历数据 selectAll(Here describes this method function with a few
	 * words)
	 * 
	 * possible to elect)
	 * 
	 * void
	 */
	public ArrayList<String> selectAll() {
		ArrayList<String> results = new ArrayList<String>() ;
		Cursor cursor = null;
		cursor = bd.openCursor(null, null);
		DatabaseEntry theKey = null;
		DatabaseEntry theData = null;
		theKey = new DatabaseEntry();
		theData = new DatabaseEntry();

		while (cursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			results.add(new String(theData.getData()));
		}
		cursor.close();
		
		return results ;
	}
	
	/**
	 * 获取key-value对数
	 * 
	 */
	public int size(){
		int num = 0 ;
		Cursor cursor = null;
		cursor = bd.openCursor(null, null);
		DatabaseEntry theKey = null;
		DatabaseEntry theData = null;
		theKey = new DatabaseEntry();
		theData = new DatabaseEntry();

		while (cursor.getNext(theKey, theData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			num++ ;
		}
		cursor.close();
		
		return num ;
	}

	/**
	 * 删除方法 delete(Here describes this method function with a few words)
	 * 
	 * possible to elect)
	 * 
	 * @param key
	 *            void
	 */
	public void delete(String key) {
		DatabaseEntry keyEntry = null;
		try {
			keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		bd.delete(null, keyEntry);
	}
	
	/**
	 * 是否包含某值
	 */

	public boolean contains(Object value){
		return this.selectAll().contains(value) ;
	}
}
