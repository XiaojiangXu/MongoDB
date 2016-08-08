package com.frank.test;

import java.net.UnknownHostException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

/**
 * <b>function:</b>MongoDB ��ʾ��
 * 
 * @author Frank
 * @createDate 2016-8-4 ����03:17:45
 * @file SimpleTest.java
 * @package com.frank.test
 * @project MongoDB
 * @version 1.0
 */

public class SimpleTest {

	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mg = new Mongo("localhost", 2222);
		// ��ѯ���е�Database
		for (String name : mg.getDatabaseNames()) {
			System.out.println("dbName: " + name);
		}

		DB db = mg.getDB("test");
		// ��ѯ���еľۼ�����
		for (String name : db.getCollectionNames()) {
			System.out.println("collectionName: " + name);
		}

		DBCollection users = db.getCollection("users");

		// ��ѯ���е�����
		DBCursor cur = users.find();
		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
		System.out.println(cur.count());
		System.out.println(cur.getCursorId());
		System.out.println(JSON.serialize(cur));
	}
}