//package com.hqgd.pms.util;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//
//import org.json.JSONObject;
//import org.springframework.boot.configurationprocessor.json.JSONArray;
//
//
//
//import net.sf.json.JsonConfig;
//import net.sf.json.processors.JsonValueProcessor;
//
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
//
//	private static final JsonConfig JSON_CONFIG = new JsonConfig();
//
//	static {
//		JSON_CONFIG.registerJsonValueProcessor(Date.class,
//				new JSONUtil.DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将对象转换为JSONObject
//	 * 作者：fengqianning 
//	 * 时间：2016年5月19日下午4:37:11
//	 * @param object
//	 * @return
//	 * returnType：JSONObject
//	 * </pre>
//	 */
//	public static JSONObject objectToJsonObject(Object object) {
//		return JSONObject.fromObject(object, JSON_CONFIG);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将对象转换为JSONArray
//	 * 作者：fengqianning 
//	 * 时间：2016年5月19日下午4:37:17
//	 * @param object
//	 * @return
//	 * returnType：JSONArray
//	 * </pre>
//	 */
//	public static JSONArray objectToJSONArray(Object object) {
//		return JSONArray.fromObject(object, JSON_CONFIG);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：实体转换jsonObject
//	 * 作者：fengqianning 
//	 * 时间：2016年5月10日上午10:48:40
//	 * @param entity
//	 * @return
//	 * returnType：JSONObject
//	 * </pre>
//	 */
//	public static JSONObject entityToJsonObject(Object entity) {
//		return JSONObject.fromObject(entity, JSON_CONFIG);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：实体转换jsonString 
//	 * 作者：fengqianning 
//	 * 时间：2016年5月10日上午10:49:25
//	 * @param entity
//	 * @return
//	 * returnType：String
//	 * </pre>
//	 */
//	public static String entityToJsonString(Object entity) {
//		return entityToJsonObject(entity).toString();
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将map转为JSONObject对象
//	 * 作者：fengqianning 
//	 * 时间：2016年1月10日下午3:35:14
//	 * @param map
//	 * @return
//	 * returnType：JSONObject
//	 * </pre>
//	 */
//	public static <K, V> JSONObject mapToJsonObject(Map<K, V> map) {
//		return JSONObject.fromObject(map, JSON_CONFIG);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将map转为jsonString字符串
//	 * 作者：fengqianning 
//	 * 时间：2016年1月10日下午3:32:50
//	 * @param map
//	 * @return
//	 * returnType：String
//	 * </pre>
//	 */
//	public static <K, V> String mapToJsonString(Map<K, V> map) {
//		return mapToJsonObject(map).toString();
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将List转为JSONArray对象
//	 * 作者：fengqianning 
//	 * 时间：2016年4月28日上午9:56:39
//	 * @param list
//	 * @return
//	 * returnType：JSONArray
//	 * </pre>
//	 */
//	public static <T> JSONArray ListToJSONArray(List<T> list) {
//		return JSONArray.fromObject(list, JSON_CONFIG);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将List转为jsonString字符串
//	 * 作者：fengqianning 
//	 * 时间：2016年4月28日上午9:57:16
//	 * @param list
//	 * @return
//	 * returnType：String
//	 * </pre>
//	 */
//	public static <T> String ListToJsonString(List<T> list) {
//		return ListToJSONArray(list).toString();
//	}
//
//	/**
//	 * <pre>
//	 * 描述：校验jsonString是否是完整的JSONObject格式
//	 * 作者：fengqianning 
//	 * 时间：2016年1月8日下午4:16:53
//	 * @param jsonString
//	 * @return
//	 * returnType：boolean
//	 * </pre>
//	 */
//	public static boolean checkJsonObjectStringFormat(String jsonObjectString) {
//		boolean flag = true;
//		try {
//			JSONObject.fromObject(jsonObjectString);
//		} catch (Exception e) {
//			flag = false;
//		}
//		return flag;
//	}
//	
//	/**
//	 * <pre>
//	 * 描述：校验jsonString是否是完整的JSONArray格式
//	 * 作者：fengqianning
//	 * 时间：2017年3月16日下午8:31:16
//	 * @param jsonArrayString
//	 * @return
//	 * returnType：boolean
//	 * </pre>
//	*/
//	public static boolean checkJsonArrayStringFormat(String jsonArrayString) {
//		boolean flag = true;
//		try {
//			JSONArray.fromObject(jsonArrayString);
//		} catch (Exception e) {
//			flag = false;
//		}
//		return flag;
//	}
//	
//	/**
//	 * <pre>
//	 * 描述：将jsonString转换成HashMap<K,V>
//	 * 作者：fengqianning 
//	 * 时间：2016年1月9日下午3:49:42
//	 * @param jsonString
//	 * @return HashMap<K, V>
//	 * @throws ParamParseException
//	 * returnType：Map<K,V>
//	 * </pre>
//	 */
//	public static Map<String, String> jsonStringToHashMap(String jsonString) {
//		return jsonStringToMap(jsonString, new HashMap<String, String>());
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将jsonString转换成按key升序排列的TreeMap<K,V>
//	 * 作者：fengqianning 
//	 * 时间：2016年1月9日下午3:50:14
//	 * @param jsonString
//	 * @return TreeMap<K,V>
//	 * @throws ParamParseException
//	 * returnType：Map<K,V>
//	 * </pre>
//	 */
//	public static Map<String, String> jsonStringToTreeMap(String jsonString) {
//		return jsonStringToTreeMap(jsonString, true);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将jsonString转换成TreeMap<K,V>
//	 * 作者：fengqianning 
//	 * 时间：2016年1月9日下午3:52:34
//	 * @param jsonString
//	 * @param flag :true按key升序排列的TreeMap<K,V>;false按key降序排列的TreeMap<K,V>
//	 * @return TreeMap<K,V>
//	 * @throws ParamParseException
//	 * returnType：Map<K,V>
//	 * </pre>
//	 */
//	public static Map<String, String> jsonStringToTreeMap(String jsonString,
//			boolean flag) {
//		Map<String, String> map = new TreeMap<String, String>();
//		return jsonStringToMap(jsonString, map);
//	}
//
//	/**
//	 * <pre>
//	 * 描述：将jsonString转换成Map<String, String>
//	 * 作者：fengqianning 
//	 * 时间：2016年1月8日下午4:23:45
//	 * @param jsonString
//	 * @param map
//	 * @return 
//	 * returnType：Map<String, String>
//	 * </pre>
//	 */
//	@SuppressWarnings("unchecked")
//	private static Map<String, String> jsonStringToMap(String jsonString,
//			Map<String, String> map)  {
//		try {
//			if (map != null) {
//				JSONObject jsonObject = JSONObject.fromObject(jsonString,
//						JSON_CONFIG);
//				Iterator<Entry<Object, Object>> iterator = jsonObject
//						.entrySet().iterator();
//				while (iterator.hasNext()) {
//					Entry<Object, Object> entry = iterator.next();
//					String value = String.valueOf(entry.getValue());
//					if (value.startsWith("\"") && value.endsWith("\"")
//							&& value.length() > 2) {
//						value = value.substring(1, value.length() - 1);
//					}
//					map.put(String.valueOf(entry.getKey()), value);
//				}
//			}
//		} catch (Exception e) {
////			throw new ParamParseException();
//		}
//		return map;
//	}
//
//	private static class DateJsonValueProcessor implements JsonValueProcessor {
//		public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
//		private DateFormat dateFormat;
//
//		public DateJsonValueProcessor(String datePattern) {
//			try {
//				dateFormat = new SimpleDateFormat(datePattern);
//			} catch (Exception e) {
//				dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
//			}
//		}
//
//		public Object processArrayValue(Object value, JsonConfig jsonConfig) {
//			return process(value);
//		}
//
//		public Object processObjectValue(String key, Object value,
//				JsonConfig jsonConfig) {
//			return process(value);
//		}
//
//		private Object process(Object value) {
//			if (value == null) {
//				return value;
//			}
//			return dateFormat.format((Date) value);
//		}
//	}
//
//	public static boolean checkIsJson(String jsonString) {
//		if (TextUtil.isEmpty(jsonString)) {
//			return false;
//		}
//		try {
//			JsonUtil.json2Map(jsonString);
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
//}
