//package com.hqgd.pms.util;
//
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.json.JSONObject;
//
//import net.sf.json.JsonConfig;
//
///**
// * <pre>
// * 描述：JSON工具类
// * 作者：fengqianning 
// * 时间：2016年1月8日下午12:03:20
// * 类名: JSONUtil
// * </pre>
// */
//public class JSONUtil {
//	private static final JsonConfig JSON_CONFIG = new JsonConfig();
//
//	public static Map<String, String> jsonStringToHashMap(String jsonString) {
//		return jsonStringToMap(jsonString, new HashMap<String, String>());
//	}
//
//	private static Map<String, String> jsonStringToMap(String jsonString, Map<String, String> map) {
//		try {
//			if (map != null) {
//				JSONObject jsonObject = JSONObject.fromObject(jsonString, JSON_CONFIG);
//				Iterator<Entry<Object, Object>> iterator = jsonObject.entrySet().iterator();
//				while (iterator.hasNext()) {
//					Entry<Object, Object> entry = iterator.next();
//					String value = String.valueOf(entry.getValue());
//					if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 2) {
//						value = value.substring(1, value.length() - 1);
//					}
//					map.put(String.valueOf(entry.getKey()), value);
//				}
//			}
//		} catch (Exception e) {
//			// throw new ParamParseException();
//		}
//		return map;
//	}
//}
