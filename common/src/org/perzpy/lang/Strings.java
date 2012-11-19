package org.perzpy.lang;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作的帮助函数
 *
 * @author perzer
 * @date May 25, 2011
 */
public class Strings {
	/**
	 * 邮箱
	 */
	private static Pattern email_Pattern = Pattern
			.compile("^([\\w-\\.]+)@((\\[[0-9]{1,3}"
					+ "\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
	/**
	 * 段格式
	 */
	private static String partRegx = "\\w+:\"[^\"]*\"";

	/**
	 * 段元素分解
	 */
	private static String partEleRegx = "(\\w+)(:\")([^\"]*)(\")";

	/**
	 * 组格式
	 */
	private static String groupRegx = "\\{((" + partRegx + ",?)+)\\}";

	/**
	 * @param cs 字符串
	 * @return 是不是为空白字符串
	 */
	public static boolean isBlank(CharSequence cs) {
		if (null == cs)
			return true;
		int length = cs.length();
		for (int i = 0; i < length; i++) {
			if (!(Character.isWhitespace(cs.charAt(i))))
				return false;
		}
		return true;
	}

	/**
	 * 去掉字符串前后空白
	 * 
	 * @param cs 字符串
	 * @return 新字符串
	 */
	public static String trim(CharSequence cs) {
		if (null == cs)
			return null;
		if (cs instanceof String)
			return ((String) cs).trim();
		int length = cs.length();
		if (length == 0)
			return cs.toString();
		int l = 0;
		int last = length - 1;
		int r = last;
		for (; l < length; l++) {
			if (!Character.isWhitespace(cs.charAt(l)))
				break;
		}
		for (; r > l; r--) {
			if (!Character.isWhitespace(cs.charAt(r)))
				break;
		}
		if (l > r)
			return "";
		else if (l == 0 && r == last)
			return cs.toString();
		return cs.subSequence(l, r + 1).toString();
	}

	/**
	 * 根据一个正则式，将字符串拆分成数组，空元素将被忽略
	 * 
	 * @param s 字符串
	 * @param regex 正则式
	 * @return 字符串数组
	 */
	public static String[] splitIgnoreBlank(String s, String regex) {
		if (null == s)
			return null;
		String[] ss = s.split(regex);
		List<String> list = new LinkedList<String>();
		for (String st : ss) {
			if (isBlank(st))
				continue;
			list.add(trim(st));
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * 检查一个字符串是否为合法的电子邮件地址
	 * 
	 * @param input 需要检查的字符串
	 * @return true 如果是有效的邮箱地址
	 */
	public static synchronized final boolean isEmail(CharSequence input) {
		return email_Pattern.matcher(input).matches();
	}

	/**
	 * 字符串转换成Map
	 *
	 * @param partStr
	 * @return
	 */
	public static Map str2Map(String str) {
		Map<String, String> map = new HashMap<String, String>();
		Matcher mm = Pattern.compile(partEleRegx).matcher(str);

		while (mm.find()) {
			String eleKey = mm.group().replaceAll(partEleRegx, "$1");

			String eleVal = mm.group().replaceAll(partEleRegx, "$3");

			if (!Strings.isBlank(eleKey)) {
				map.put(eleKey, eleVal);
			}
		}

		return map;
	}

	/**
	 * 字符串转换成List<Map>
	 *
	 * @param mapStr
	 * @return
	 */
	public static List<Map> str2ListMap(String str) {
		List<Map> list = new LinkedList<Map>();
		Matcher m = Pattern.compile(groupRegx).matcher(str);

		while (m.find()) {
			Map map = null;
			String partStr = m.group().replaceAll(groupRegx, "$1");

			map = str2Map(partStr);

			if (map != null) {
				list.add(map);
			}
		}

		return list;
	}
	
	/**
	 * 删除input字符串中的html格式
	 * 
	 * @param input
	 * @return
	 */
	public static String splitAndFilterString(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素,
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}
}
