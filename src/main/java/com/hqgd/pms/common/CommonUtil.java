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

	// private ICommParamService commParamService = ApplicationContextUtil
	// .getBean("commParamService", ICommParamService.class);

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
		// String addr = commParamService.getSysParam("SYS_DOCUMENT_SAVE_PATH");
		// if (addr.isEmpty()) {
		// System.out.println("未配置下载文档保存地址，使用默认配置");
		// String os = System.getProperty("os.name");
		// if (os.toLowerCase().startsWith("win")) {
		// addr = "D:\\tmp\\iips_payment\\document";
		// } else {
		// addr = "/tmp/iips_payment/document";
		// }
		// }
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
	 * 
	 * @param obj
	 *            实体对象
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
	 * 
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

	/**
	 * 描述： 作者：姚绒 日期：2019年3月14日 上午8:59:53
	 *
	 */
	public static String getAfterDay(String today) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat("yy-MM-dd").parse(today));
		int date = c.get(Calendar.DATE);
		c.set(Calendar.DATE, date + 1);
		String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		System.out.println(afterDay);
		return afterDay;
	}

	public static String getBeforeDay(String today) throws ParseException {
		Calendar c = Calendar.getInstance();
		c.setTime(new SimpleDateFormat("yy-MM-dd").parse(today));
		int date = c.get(Calendar.DATE);
		c.set(Calendar.DATE, date - 1);
		String afterDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		System.out.println(afterDay);
		return afterDay;
	}

	public static String getCRC(String data) {
		data = data.replace(" ", "");
		int len = data.length();
		if (!(len % 2 == 0)) {
			return "0000";
		}
		int num = len / 2;
		byte[] para = new byte[num];
		for (int i = 0; i < num; i++) {
			int value = Integer.valueOf(data.substring(i * 2, 2 * (i + 1)), 16);
			para[i] = (byte) value;
		}
		return getCRC(para);
	}

	/**
	 * 计算CRC16校验码
	 *
	 * @param bytes
	 *            字节数组
	 * @return {@link String} 校验码
	 * @since 1.0
	 */
	public static String getCRC(byte[] bytes) {
		// CRC寄存器全为1
		int CRC = 0x0000ffff;
		// 多项式校验值
		int POLYNOMIAL = 0x0000a001;
		int i, j;
		for (i = 0; i < bytes.length; i++) {
			CRC ^= ((int) bytes[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		// 结果转换为16进制
		String result = Integer.toHexString(CRC).toUpperCase();
		if (result.length() != 4) {
			StringBuffer sb = new StringBuffer("0000");
			result = sb.replace(4 - result.length(), 4, result).toString();
		}
		//
		// return result.substring(2, 4) + " " + result.substring(0, 2);
		// 交换高低位，低位在前高位在后
		return result.substring(2, 4) + " " + result.substring(0, 2);
	}

	public static byte[] hexStringToByteArray(String hexString) {
		hexString = hexString.replaceAll(" ", "");
		int len = hexString.length();
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
			bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return bytes;
	}

	// 字符串转时间戳
	public static Double str2Time(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 设置要读取的时间字符串格式
		Date date = null;
		try {
			date = format.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 转换为Date类
		Double timestamp = (double) date.getTime();
		return timestamp;
	}

	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/*
	 * 将时间转换为时间戳
	 */
	public static long dateToStamp(String s) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		return ts;
	}
}
