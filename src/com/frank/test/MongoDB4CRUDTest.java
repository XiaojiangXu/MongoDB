package com.frank.test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.QueryOperators;
import com.mongodb.util.JSON;

/**
 * <b>function:</b>ʵ��MongoDB��CRUD����
 * 
 * @author Frank
 * @createDate 2016-8-4 ����03:21:23
 * @file MongoDB4CRUDTest.java
 * @package com.frank.test
 * @project MongoDB
 * @version 1.0
 */

public class MongoDB4CRUDTest {

	private Mongo mg = null;
	private DB db;
	private DBCollection users;

	@Before
	public void init() {
		try {
			// mg = new Mongo();

			mg = new Mongo("localhost", 2222);

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (MongoException e) {

			e.printStackTrace();

		}

		// ��ȡtest DB�����Ĭ��û�д�����mongodb���Զ�����

		db = mg.getDB("test");

		// ��ȡusers DBCollection�����Ĭ��û�д�����mongodb���Զ�����

		users = db.getCollection("users");

	}

	@After
	public void destory() {

		if (mg != null)

			mg.close();

		mg = null;

		db = null;

		users = null;

		System.gc();

	}

	/**
	 * <b>function:</b> ��ѯ��������
	 * 
	 * @author Frank
	 * @createDate 2011-6-2 ����03:22:40
	 */

	private void queryAll() {

		print("��ѯusers���������ݣ�");

		// db�α�

		DBCursor cur = users.find();

		while (cur.hasNext()) {

			print(cur.next());

		}

	}

	@Test
	public void add() {

		// �Ȳ�ѯ��������

		queryAll();

		print("count: " + users.count());

		DBObject user = new BasicDBObject();

		user.put("name", "frank");

		user.put("age", 31);

		// users.save(user)���棬getN()��ȡӰ������

		// print(users.save(user).getN());

		// ��չ�ֶΣ���������ֶΣ���Ӱ����������

		user.put("sex", "��");

		print(users.save(user).getN());

		// ��Ӷ������ݣ�����Array����

		print(users.insert(user, new BasicDBObject("name", "tom")).getN());

		// ���List����

		List<DBObject> list = new ArrayList<DBObject>();

		list.add(user);

		DBObject user2 = new BasicDBObject("name", "lucy");

		user.put("age", 22);

		list.add(user2);

		// ���List����

		print(users.insert(list).getN());

		// ��ѯ�����ݣ������Ƿ���ӳɹ�

		print("count: " + users.count());

		queryAll();

	}

	@Test
	public void remove() {

		queryAll();

		print("ɾ��id = 4de73f7acd812d61b4626a77��"
				+ users.remove(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).getN());

		print("remove age >= 24: " + users.remove(new BasicDBObject("age", new BasicDBObject("$gte", 24))).getN());

	}

	@Test
	public void modify() {

		print("�޸ģ�" + users.update(new BasicDBObject("_id", new ObjectId("57a3ff8e2041151d45d40013")),
				new BasicDBObject("age", 99)).getN());

		print("�޸ģ�" + users.update(

				new BasicDBObject("_id", new ObjectId("57a3fff720415ba15799b0e1")),

				new BasicDBObject("age", 100),

				true, // ������ݿⲻ���ڣ��Ƿ����

				false// �����޸�

		).getN());

		print("�޸ģ�" + users.update(

				new BasicDBObject("name", "haha"),

				new BasicDBObject("name", "dingding"),

				false, // ������ݿⲻ���ڣ��Ƿ����

				false// falseֻ�޸ĵ�һ����true����ж����Ͳ��޸�

		).getN());

		// �����ݿⲻ���ھͲ��޸ġ���������ݣ����ж������ݾͲ��޸�

		print("�޸Ķ�����" + users.updateMulti(new BasicDBObject("_id", new ObjectId("4dde23616be7c19df07db42c")),
				new BasicDBObject("name", "199")));

	}

	// @Test
	public void query() {

		// ��ѯ����

		// queryAll();

		// ��ѯid = 4de73f7acd812d61b4626a77

		print("find id = 4de73f7acd812d61b4626a77: "
				+ users.find(new BasicDBObject("_id", new ObjectId("4de73f7acd812d61b4626a77"))).toArray());

		// ��ѯage = 24

		print("find age = 24: " + users.find(new BasicDBObject("age", 24)).toArray());

		// ��ѯage >= 24

		print("find age >= 24: " + users.find(new BasicDBObject("age", new BasicDBObject("$gte", 24))).toArray());

		print("find age <= 24: " + users.find(new BasicDBObject("age", new BasicDBObject("$lte", 24))).toArray());

		print("��ѯage!=25��" + users.find(new BasicDBObject("age", new BasicDBObject("$ne", 25))).toArray());

		print("��ѯage in 25/26/27��"
				+ users.find(new BasicDBObject("age", new BasicDBObject(QueryOperators.IN, new int[] { 25, 26, 27 })))
						.toArray());

		print("��ѯage not in 25/26/27��"
				+ users.find(new BasicDBObject("age", new BasicDBObject(QueryOperators.NIN, new int[] { 25, 26, 27 })))
						.toArray());

		print("��ѯage exists ����"
				+ users.find(new BasicDBObject("age", new BasicDBObject(QueryOperators.EXISTS, true))).toArray());

		print("ֻ��ѯage���ԣ�" + users.find(null, new BasicDBObject("age", true)).toArray());

		print("ֻ�����ԣ�" + users.find(null, new BasicDBObject("age", true), 0, 2).toArray());

		print("ֻ�����ԣ�" + users.find(null, new BasicDBObject("age", true), 0, 2, Bytes.QUERYOPTION_NOTIMEOUT).toArray());

		// ֻ��ѯһ�����ݣ�����ȥ��һ��

		print("findOne: " + users.findOne());

		print("findOne: " + users.findOne(new BasicDBObject("age", 26)));

		print("findOne: " + users.findOne(new BasicDBObject("age", 26), new BasicDBObject("name", true)));

		// ��ѯ�޸ġ�ɾ��

		print("findAndRemove ��ѯage=25�����ݣ�����ɾ��: " + users.findAndRemove(new BasicDBObject("age", 25)));

		// ��ѯage=26�����ݣ������޸�name��ֵΪAbc

		print("findAndModify: " + users.findAndModify(new BasicDBObject("age", 26), new BasicDBObject("name", "Abc")));

		print("findAndModify: " + users.findAndModify(new BasicDBObject("age", 28), // ��ѯage=28������
				new BasicDBObject("name", true), // ��ѯname����
				new BasicDBObject("age", true), // ����age����
				false, // �Ƿ�ɾ����true��ʾɾ��
				new BasicDBObject("name", "Abc"), // �޸ĵ�ֵ����name�޸ĳ�Abc
				true, true));

		queryAll();
	}

	// @Test
	public void testOthers() {

		DBObject user = new BasicDBObject();

		user.put("name", "green");

		user.put("age", 24);

		// JSON ����ת��

		print("serialize: " + JSON.serialize(user));

		// �����л�

		print("parse: " + JSON.parse("{ \"name\" : \"green\" , \"age\" : 24}"));

		print("�ж�user Collection�Ƿ����: " + db.collectionExists("user"));

		// ��������ھʹ���

		if (!db.collectionExists("user")) {

			DBObject options = new BasicDBObject();

			options.put("size", 20);

			options.put("capped", 20);

			options.put("max", 20);

			print(db.createCollection("users", options));

		}

		// ����dbΪֻ��

		db.setReadOnly(true);

		// ֻ������д������

		db.getCollection("user").save(user);

	}

	public void print(Object o) {

		System.out.println(o);

	}

}