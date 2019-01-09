package com.hqgd.pms.common;

import java.io.File;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * 描述：静态公共方法 作者：姚绒 日期：2018年11月19日 下午3:22:03
 *
 */

public class CommonUtil {
	
//	private ICommParamService commParamService = ApplicationContextUtil
//			.getBean("commParamService", ICommParamService.class);

	/**
	 * 描述：将字符型的枚举值转换为对应的枚举类值 作者：姚绒 日期：2018年11月19日 下午3:21:46 @param enumType @param
	 * nameStr @return T @throws
	 */
	public static <T extends Enum<T>> T convertStringToEnum(Class<T> enumType, String nameStr) {
		T oneEnum = Enum.valueOf(enumType, nameStr);
		return oneEnum;
	}

	/**
	 * 描述：获取一个key按升序排列的TreeMap<String,String> 作者：姚绒 日期：2018年11月19日 下午3:21:33 @return
	 * TreeMap<String,String> @throws
	 */
	public static TreeMap<String, String> getNewSimpleTreeMap() {
		return getNewSimpleTreeMap(true);
	}

	/**
	 * 描述：获取一个key按指定顺序排列的TreeMap<String,String> 作者：姚绒 日期：2018年11月19日
	 * 下午3:21:17 @param flag @return TreeMap<String,String> @throws
	 */
	public static TreeMap<String, String> getNewSimpleTreeMap(boolean flag) {
		return getNewTreeMap(flag);
	}

	/**
	 * 描述：获取一个按key升序排列的TreeMap<K,V> 作者：姚绒 日期：2018年11月19日 下午3:21:00 @return
	 * TreeMap<K,V> @throws
	 */
	public static <K extends Comparable<K>, V> TreeMap<K, V> getNewTreeMap() {
		return getNewTreeMap(true);
	}

	/**
	 * 描述：获取一个key按指定顺序排序的TreeMap<K,V> 作者：姚绒 日期：2018年11月19日 下午3:20:47 @param
	 * flag @return TreeMap<K,V> @throws
	 */
	public static <K extends Comparable<K>, V> TreeMap<K, V> getNewTreeMap(boolean flag) {
		TreeMap<K, V> treeMap = null;
		if (flag) {
			treeMap = new TreeMap<K, V>(new Comparator<K>() {
				public int compare(K key1, K key2) {
					return key1.compareTo(key2);
				}
			});
		} else {
			treeMap = new TreeMap<K, V>(new Comparator<K>() {
				public int compare(K key1, K key2) {
					return key2.compareTo(key1);
				}
			});
		}
		return treeMap;
	}

	/**
	 * 描述：获取当前服务器的时间 作者：姚绒 日期：2018年11月19日 下午3:20:34 @return Date @throws
	 */
	public static Date getLocalCurrentTime() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 描述：根据设置的时间格式获取当前服务器时间 作者：姚绒 日期：2018年11月19日 下午3:20:23 @param
	 * formatString @return String @throws
	 */
	public static String getFormatCurrentTime(String formatString) {
		SimpleDateFormat sf = new SimpleDateFormat(formatString);
		return sf.format(getLocalCurrentTime());
	}

	/**
	 * 描述：根据设置的时间格式转换Date对象 作者：姚绒 日期：2018年11月19日 下午3:20:10 @param
	 * formatString @param date @return String @throws
	 */
	public static String getFormatCurrentTime(String formatString, Date date) {
		SimpleDateFormat sf = new SimpleDateFormat(formatString);
		return sf.format(date);
	}

	/**
	 * 描述：获取当前服务器时间(时间格式：yyyy-MM-dd) 作者：姚绒 日期：2018年11月19日 下午3:19:57 @return
	 * String @throws
	 */
	public static String getSimpleFormatCurrentDate() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(getLocalCurrentTime());
	}

	/**
	 * 描述：获取当前服务器时间(时间格式：yyyy-MM-dd HH:mm:ss) 作者：姚绒 日期：2018年11月19日 下午3:19:45 @return
	 * String @throws
	 */
	public static String getSimpleFormatTimestamp() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(getLocalCurrentTime());
	}

	/**
	 * 描述：将Date转化为字符串的时间格式(yyyy-MM-dd HH:mm:ss) 作者：姚绒 日期：2018年11月19日
	 * 下午3:19:33 @param date @return String @throws
	 */
	public static String getSimpleFormatTimestampByDate(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	/**
	 * 描述：将字符串的时间格式转化为Date(yyyy-MM-dd HH:mm:ss) 作者：姚绒 日期：2018年11月19日
	 * 下午3:19:03 @param simpleFormatTimestamp @return @throws ParseException
	 * Date @throws
	 */
	public static Date getDateBySimpleFormatTimestamp(String simpleFormatTimestamp) throws ParseException {
		if (simpleFormatTimestamp.isEmpty() || simpleFormatTimestamp.length() < 10) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.parse(simpleFormatTimestamp);
	}

	/**
	 * 描述：将字符串的时间格式转化为Date(yyyyMMddHHmmss) 作者：姚绒 日期：2018年11月19日 下午3:18:51 @param
	 * noFormatTimestamp @return @throws ParseException Date @throws
	 */
	public static Date getDateByNoFormatTimestamp(String noFormatTimestamp) throws ParseException {
		if (noFormatTimestamp.isEmpty() || noFormatTimestamp.length() < 10) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.parse(noFormatTimestamp);
	}

	/**
	 * 描述：将字符串的时间formatTimestamp格式转化为Date 作者：姚绒 日期：2018年11月19日 下午3:18:39 @param
	 * formatTimestamp @param formatString @return @throws ParseException
	 * Date @throws
	 */
	public static Date getDateByFormatTimestamp(String formatTimestamp, String formatString) throws ParseException {
		if (formatTimestamp.isEmpty() || formatTimestamp.length() < 10) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat(formatString);
		return sf.parse(formatTimestamp);
	}

	/**
	 * 描述：获取当前服务器时间(时间格式：yyyyMMddHHmmss) 作者：姚绒 日期：2018年11月19日 下午3:18:26 @return
	 * String @throws
	 */
	public static String getNoFormatTimestamp() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(getLocalCurrentTime());
	}

	/**
	 * 描述：将Date转化为字符串的时间格式(时间格式：yyyyMMddHHmmss) 作者：姚绒 日期：2018年11月19日
	 * 下午3:18:12 @param date @return String @throws
	 */
	public static String getNoFormatTimestamp(Date date) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sf.format(date);
	}

	/**
	 * 描述：根据字符和指定的个数生成字符序列 作者：姚绒 日期：2018年11月19日 下午3:17:58 @param character @param
	 * number @return String @throws
	 */
	public static String getCharSequence(char character, int number) {
		StringBuilder charSequence = new StringBuilder();
		for (int i = 0; i < number; i++) {
			charSequence.append(character);
		}
		return charSequence.toString();
	}

	/**
	 * 描述：生成固定长度的随机数 作者：姚绒 日期：2018年11月19日 下午3:17:42 @param numberLength @return
	 * String @throws
	 */
	public static String getRandomNumberByLength(Integer numberLength) {
		StringBuilder randomStrBuilder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < numberLength; i++) {
			randomStrBuilder.append(random.nextInt(10));
		}
		return randomStrBuilder.toString();
	}

	/**
	 * 描述：分析url协议类型 作者：姚绒 日期：2018年11月19日 下午3:17:23 @param url @return String @throws
	 */
	public static String analysisUrlModel(String url) {
		String result = "HTTP";
		if (!url.isEmpty() && url.indexOf(":") > 0) {
			String backModel = url.substring(0, url.indexOf(":"));
			if ("HTTPS".equalsIgnoreCase(backModel.trim())) {
				result = "HTTPS";
			}
		}
		return result;
	}

	
	public String getDocumentSavePath() {
//		String addr = commParamService.getSysParam("SYS_DOCUMENT_SAVE_PATH");
//		if (addr.isEmpty()) {
//			System.out.println("未配置下载文档保存地址，使用默认配置");
//			String os = System.getProperty("os.name");
//			if (os.toLowerCase().startsWith("win")) {
//				addr = "D:\\tmp\\iips_payment\\document";
//			} else {
//				addr = "/tmp/iips_payment/document";
//			}
//		}
		String addr = "C:\\Users\\yr\\Desktop\\PMS";
		File file = new File(addr);
		if (!file.exists()) {
			file.mkdirs();
		}
		System.out.println("getDocumentSavePath addr：" + addr);
		return addr;
	}
	
	/**
     * 实体对象转成Map
     * @param obj 实体对象
     * @return
     */
	public static Map<String, Object> object2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<? extends Object> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

	/**
     * 截取字符串str中指定字符 strStart、strEnd之间的字符串
     * 
     * @param string
     * @param str1
     * @param str2
     * @return
     */
    public static String subString(String str, String strStart, String strEnd) {
 
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);
 
        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return str;
        }
        if (strEndIndex < 0) {
            return str;
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }
    
    /**
	 * get postfix of the path
	 * @param path
	 * @return
	 */
	public static String getPostfix(String path) {
		if (path == null || Common.EMPTY.equals(path.trim())) {
			return Common.EMPTY;
		}
		if (path.contains(Common.POINT)) {
			return path.substring(path.lastIndexOf(Common.POINT) + 1, path.length());
		}
		return Common.EMPTY;
	}
}
