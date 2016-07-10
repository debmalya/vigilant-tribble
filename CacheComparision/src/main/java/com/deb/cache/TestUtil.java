/**
 * Copyright 2015-2016 Knowesis Pte Ltd.
 * 
 */
package com.deb.cache;

import java.util.Random;

import com.couchbase.client.java.document.JsonDocument;


/**
 * @author debmalyajash
 *
 */
public class TestUtil {
	
	public static JsonDocument subscriberDocument = TestUtil.createJsonDocument();
	
	public static final long ONE_BILLION = 1000000000L;

	public static final String serverIp = "192.168.251.171";
	/**
	 * 
	 */
	private static final int SEED_FOR_RANDOM_MSISDN_GENERATION = 100000000;

	private static Random random = new Random(SEED_FOR_RANDOM_MSISDN_GENERATION);

	public static final String JSON_VALUE = "{\"a\":\"A\"}";

	public static final String MSISDN = "83766916";

	public static final String MSISDN_JSON_STRING = "84033572";

	public static final String SUBSCRIBER_INDICATOR = "{\"docType\":\"subscriberIndicators\","
			+ "\"NAME-L\":\"Tester John Doe\"," + "\"PREFERRED_NAME-L\":\"Jim\"," + "\"TITLE-L\":\"Mr.\","
			+ "\"ACCOUNT_NUMBER-L\":\"30863023\"," + "\"ADDRESS-L\":\"83, 73rd St, Brooklyn, NY 11209\","
			+ "\"ACCOUNT_TYPE-L\":\"Postpaid\"," + "\"ACCOUNT_CLASS_LifeTime-L\":\"ACCOUNT_CLASS\","
			+ "\"CUSTOMER_GROUP-L\":\"Residential\"," + "\"TARIFF_PLAN-L\":\"Standard Package\","
			+ "\"AGE_GROUP-L\":\"40-50\"," + "\"GENDER-L\":\"Male\"," + "\"TWITTER_USERNAME-L\":\"@James_Smith\","
			+ "\"EMAIL-L\":\"James.Smith@bogustel.com\"," + "\"PREFERRED_WORKOUT_METHOD-L\":\"WALKING\","
			+ "\"DAILY_CALORIE_BURN_GOAL-L\":120,"
			+ "\"DAILY_WORKOUT_SERIES-L\":{\"1461283200000\":{\"workoutMethod\":"
			+ "\"WALKING\",\"calorieBurnt\":60," + "\"startTime\":17,\"duration\":20,"
			+ "\"caloriGoalAchievementPercentage\":50}," + "\"1461369600000\":{\"workoutMethod\":\"WALKING\","
			+ "\"calorieBurnt\":50," + "\"startTime\":8," + "\"duration\":15,"
			+ "\"caloriGoalAchievementPercentage\":41}," + "\"1461456000000\":" + "{\"workoutMethod\":\"WALKING\","
			+ "\"calorieBurnt\":100,\"startTime\":7,"
			+ "\"duration\":45,\"caloriGoalAchievementPercentage\":83}," + "\"1461542400000\":"
			+ "{\"workoutMethod\":\"WALKING\"," + "\"calorieBurnt\":80,\"startTime\":8,"
			+ "\"duration\":35," + "\"caloriGoalAchievementPercentage\":66}" + "},"
			+ "\"AVERAGE_CALORIE_ACHIEVEMENT_PERCENTAGE-L\":60," + "\"DAYS_SINCE_LAST_WORKOUT-L\":2" + "}";
	/*
	 * 
	 * 
	 * "DAYS_SINCE_LAST_WORKOUT-L": 2 }";
	 */
	public static final String GSON_STR = "{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"GlossTerm\":\"Standard Generalized Markup Language\",\"GlossSee\":\"markup\",\"SortAs\":\"SGML\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"ID\":\"SGML\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\"}}}}}";
	/**
	 * Couchbase JSON string.
	 */
	public static final String CSON_STR = "{\"glossary\":{\"title\":\"example glossary\",\"GlossDiv\":{\"title\":\"S\",\"GlossList\":{\"GlossEntry\":{\"GlossTerm\":\"Standard Generalized Markup Language\",\"GlossSee\":\"markup\",\"SortAs\":\"SGML\",\"GlossDef\":{\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\",\"GlossSeeAlso\":[\"GML\",\"XML\"]},\"ID\":\"SGML\",\"Acronym\":\"SGML\",\"Abbrev\":\"ISO 8879:1986\"}}}}}";

	/**
	 * JSON string.
	 */
	public static final String JSON_STR = "{" + "\"glossary\":{" + "\"title\":\"example glossary\"," + "\"GlossDiv\":{"
			+ "\"title\":\"S\"," + "\"GlossList\":{" + "\"GlossEntry\":{" + "\"ID\":\"SGML\"," + "\"SortAs\":\"SGML\","
			+ "\"GlossTerm\":\"Standard Generalized Markup Language\"," + "\"Acronym\":\"SGML\","
			+ "\"Abbrev\":\"ISO 8879:1986\"," + "\"GlossDef\":{"
			+ "\"para\":\"A meta-markup language, used to create markup languages such as DocBook.\","
			+ "\"GlossSeeAlso\":[\"GML\",\"XML\"]" + "}," + "\"GlossSee\":\"markup\"" + "}" + "}" + "}" + "}" + "}";



	public static final com.couchbase.client.java.document.json.JsonObject CSON_OBJ = createCsonObject(SUBSCRIBER_INDICATOR);
	/**
	 * 
	 */
	public static final int ONE_MILLION = 1000000;


	
	public static JsonDocument createJsonDocument() {		
		return JsonDocument.create(generateRandomMSISDN(), CSON_OBJ);
		
	}

	/**
	 * To generate random MSISDN number.
	 * 
	 * @return random MSISDN number.
	 */
	public static String generateRandomMSISDN() {
		if (random == null) {
			random = new Random(SEED_FOR_RANDOM_MSISDN_GENERATION);
		}
		return String.valueOf(random.nextInt());
	}

	/**
	 * Create couchbase JsonObject from json string.
	 * 
	 * @param csonString
	 *            json string to be converted.
	 * @return Couchbase JsonObject.
	 */
	public static final com.couchbase.client.java.document.json.JsonObject createCsonObject(final String csonString) {
		return com.couchbase.client.java.document.json.JsonObject.fromJson(csonString);
	}

	

	/**
	 * @param subscriberIndicator
	 * @return
	 */
	public static JsonDocument createJsonDocument(String subscriberIndicator) {
		
		JsonDocument document = JsonDocument.create(generateRandomMSISDN(),CSON_OBJ);
		
		return document;
	}
	
	/**
	 * To test cache with one billion put requests.
	 * @param cache
	 */
	public static void billionaire(Cache cache) {
		for (long i = 0; i < TestUtil.ONE_BILLION; i++) {
			try {
				cache.put(TestUtil.generateRandomMSISDN(), subscriberDocument);
			} catch (Throwable th) {
				System.err.println("!! ERR, Sorry can not continue after " + i + " !!");
				break;
			}
		}
	}
}
