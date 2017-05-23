package com.seke.autocomplete.lib;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to handle JSON. Created by ckcz123 on 2017/3/6.
 */
public class JSON {
	enum TYPE {
		INTEGER, DOUBLE, BOOLEAN, STRING, JSON, LIST
	}

	private TYPE type;
	private int intValue;
	private double doubleValue;
	private boolean boolValue;
	private String stringValue;
	private List<JSON> listValue;

	HashMap<String, JSON> map;

	/**
	 * Default constructor with a map.
	 */
	public JSON() {
		type = TYPE.JSON;
		map = new HashMap<>();
	}

	/**
	 * Constructor with a int value.
	 * 
	 * @param value
	 */
	public JSON(int value) {
		type = TYPE.INTEGER;
		intValue = value;
	}

	/**
	 * Constructor with a double value.
	 * 
	 * @param value
	 */
	public JSON(double value) {
		type = TYPE.DOUBLE;
		doubleValue = value;
	}

	/**
	 * Constructor with a boolean value.
	 * 
	 * @param value
	 */
	public JSON(boolean value) {
		type = TYPE.BOOLEAN;
		boolValue = value;
	}

	/**
	 * <p>
	 * Constructor with a string value.
	 * </p>
	 * <p>
	 * <b>Note: </b> If you want to decode a json string, use
	 * JSON.decode(String) instead of new JSON(String).
	 * </p>
	 * 
	 * @param value
	 */
	public JSON(String value) {
		type = TYPE.STRING;
		stringValue = encodeString(value);
	}

	/**
	 * <p>
	 * Constructor with another JSON object.
	 * </p>
	 * <p>
	 * <b>Note: </b>It's not deep copy: please use JSON.deepCopy(JSON) if you
	 * want.
	 * </p>
	 * 
	 * @param another
	 */
	public JSON(JSON another) {
		type = another.type;
		intValue = another.intValue;
		doubleValue = another.doubleValue;
		boolValue = another.boolValue;
		stringValue = another.stringValue;
		listValue = another.listValue;
		map = another.map;
	}

	/**
	 * Constructor with a map value
	 * 
	 * @param map
	 */
	public JSON(Map<String, JSON> map) {
		type = TYPE.JSON;
		this.map = new HashMap<>(map);
	}

	/**
	 * <p>
	 * Constructor with a list value.
	 * </p>
	 * 
	 * @param list
	 */
	public JSON(List<JSON> list) {
		type = TYPE.LIST;
		listValue = new ArrayList<>(list);
	}

	/**
	 * Return true if it's a basic type (int, double, boolean, string).
	 * 
	 * @return true if it's a basic type
	 */
	public boolean isBasicType() {
		return type == TYPE.INTEGER || type == TYPE.BOOLEAN
				|| type == TYPE.DOUBLE || type == TYPE.STRING;
	}

	/**
	 * Get int value if it's an integer or it can parse to an integer, else
	 * return null
	 * 
	 * @return an integer, or null
	 */
	public Integer getInt() {
		if (type == TYPE.INTEGER)
			return intValue;
		if (type == TYPE.DOUBLE)
			return (int) doubleValue;
		if (type == TYPE.BOOLEAN)
			return boolValue ? 1 : 0;
		if (type == TYPE.STRING) {
			try {
				return Integer.parseInt(stringValue);
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	/**
	 * Get double value if it's a double or it can parse to a double, else
	 * return null
	 * 
	 * @return a double, or null
	 */
	public Double getDouble() {
		if (type == TYPE.DOUBLE)
			return doubleValue;
		if (type == TYPE.INTEGER)
			return intValue + 0.0;
		if (type == TYPE.BOOLEAN)
			return boolValue ? 1.0 : 0.0;
		if (type == TYPE.STRING) {
			try {
				return Double.parseDouble(stringValue);
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	/**
	 * Get boolean value if it's a boolean or it can parse to a boolean, else
	 * return null
	 * 
	 * @return a boolean, or null
	 */
	public Boolean getBoolean() {
		if (type == TYPE.BOOLEAN)
			return boolValue;
		if (type == TYPE.DOUBLE)
			return doubleValue != 0;
		if (type == TYPE.INTEGER)
			return intValue != 0;
		if (type == TYPE.STRING) {
			try {
				return Boolean.parseBoolean(stringValue);
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	/**
	 * Get string value if it's a string or it can parse to a string, else
	 * return null
	 * 
	 * @return a string, or null
	 */
	public String getString() {
		if (type == TYPE.STRING)
			return decodeString(stringValue);
		if (type == TYPE.INTEGER)
			return String.valueOf(intValue);
		if (type == TYPE.DOUBLE)
			return String.valueOf(doubleValue);
		if (type == TYPE.BOOLEAN)
			return String.valueOf(boolValue);
		return null;
	}

	/**
	 * If it's a list, return it; else return null
	 * 
	 * @return a list, or null
	 */
	public List<JSON> getList() {
		if (type == TYPE.LIST)
			return listValue;
		return null;
	}

	/**
	 * Get an integer by key, or null if no such key exists
	 * 
	 * @param key
	 * @return integer, or null
	 */
	public Integer getInt(String key) {
		if (type != TYPE.JSON)
			return null;
		JSON json = map.get(key);
		return json == null ? null : json.getInt();
	}

	/**
	 * Get an integer by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @param defaultValue
	 * @return integer, or defaultValue
	 */
	public int getInt(String key, int defaultValue) {
		Integer integer = getInt(key);
		if (integer == null)
			return defaultValue;
		return integer;
	}

	/**
	 * Get a double by key, or null if no such key exists
	 * 
	 * @param key
	 * @return double, or null
	 */
	public Double getDouble(String key) {
		if (type != TYPE.JSON)
			return null;
		JSON json = map.get(key);
		return json == null ? null : json.getDouble();
	}

	/**
	 * Get a double by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @param defaultValue
	 * @return double, or defaultValue
	 */
	public double getDouble(String key, double defaultValue) {
		Double d = getDouble(key);
		if (d == null)
			return defaultValue;
		return d;
	}

	/**
	 * Get a boolean by key, or null if no such key exists
	 * 
	 * @param key
	 * @return boolean, or null
	 */
	public Boolean getBoolean(String key) {
		if (type != TYPE.JSON)
			return null;
		JSON json = map.get(key);
		return json == null ? null : json.getBoolean();
	}

	/**
	 * Get a boolean by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @param defaultValue
	 * @return boolean, or defaultValue
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		Boolean b = getBoolean(key);
		if (b == null)
			return defaultValue;
		return b;
	}

	/**
	 * Get a string by key, or null if no such key exists
	 * 
	 * @param key
	 * @return string, or null
	 */
	public String getString(String key) {
		if (type != TYPE.JSON)
			return null;
		JSON json = map.get(key);
		return json == null ? null : decodeString(json.getString());
	}

	/**
	 * Get a string by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @param defaultValue
	 * @return string, or defaultValue
	 */
	public String getString(String key, String defaultValue) {
		String s = getString(key);
		if (s == null)
			return defaultValue;
		return s;
	}

	/**
	 * Get a list by key, or null if no such key exists
	 * 
	 * @param key
	 * @return list, or null
	 */
	public List<JSON> getList(String key) {
		if (type != TYPE.JSON)
			return null;
		JSON json = map.get(key);
		return json == null ? null : json.getList();
	}

	/**
	 * Get a list by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @return list, or defaultValue
	 */
	public List<JSON> getList(String key, List<JSON> defaultValue) {
		List<JSON> list = getList(key);
		if (list == null)
			return defaultValue;
		return list;
	}

	/**
	 * Get JSON object by key, or null if no such key exists
	 * 
	 * @param key
	 * @return JSON object, or null
	 */
	public JSON getJson(String key) {
		if (type != TYPE.JSON)
			return null;
		return map.get(key);
	}

	/**
	 * Get JSON object by key, or defaultValue if no such key exists
	 * 
	 * @param key
	 * @param defaultValue
	 * @return JSON object, or defaultValue;
	 */
	public JSON getJson(String key, JSON defaultValue) {
		JSON json = getJson(key);
		return json == null ? defaultValue : json;
	}

	/**
	 * If it's a list, return the JSON object at the specified position.
	 * 
	 * @param index
	 * @return JSON object, or null
	 */
	public JSON getJson(int index) {
		List<JSON> list = getList();
		if (list == null)
			return null;
		if (index >= list.size())
			return null;
		return list.get(index);
	}

	/**
	 * If it's a list or map, return its size; else return 0
	 * 
	 * @return length, or 0
	 */
	public int size() {
		if (type == TYPE.LIST)
			return listValue.size();
		if (type == TYPE.JSON)
			return map.size();
		return 0;
	}

	/**
	 * Set this JSON object to an integer value.
	 * 
	 * @param value
	 */
	public void set(int value) {
		type = TYPE.INTEGER;
		intValue = value;
	}

	/**
	 * Set this JSON object to a double value.
	 * 
	 * @param value
	 */
	public void set(double value) {
		type = TYPE.DOUBLE;
		doubleValue = value;
	}

	/**
	 * Set this JSON object to a boolean value.
	 * 
	 * @param value
	 */
	public void set(boolean value) {
		type = TYPE.BOOLEAN;
		boolValue = value;
	}

	/**
	 * Set this JSON object to a string value.
	 * 
	 * @param value
	 */
	public void set(String value) {
		type = TYPE.STRING;
		stringValue = encodeString(value);
	}

	/**
	 * Set this JSON object to a list value;
	 * 
	 * @param list
	 */
	public void set(List<JSON> list) {
		type = TYPE.LIST;
		listValue = new ArrayList<>(list);
	}

	/**
	 * Put an integer value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, int value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * Put a double value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, double value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * Put a boolean value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, boolean value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * Put a string value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, String value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * Put a JSON value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, JSON value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * Put a list value to the given key.
	 * 
	 * @param key
	 * @param value
	 * @return true if success.
	 */
	public boolean put(String key, List<JSON> value) {
		if (type != TYPE.JSON)
			return false;
		map.put(key, new JSON(value));
		return true;
	}

	/**
	 * <p>
	 * Decode a string to json, using `"` to mark strings.
	 * </p>
	 * <p>
	 * We assume there will be no character `"` (%22 instead) or `'` (%27
	 * instead) in the string.
	 * </p>
	 * 
	 * @param string
	 * @return a valid JSON object, or null if it's invalid
	 */
	public static JSON decode(String string) {
		return decode(string, false);
	}

	/**
	 * <p>
	 * Decode a string to json.
	 * </p>
	 * <p>
	 * If useSingleQuote=true, then use `'` instead of `"` to mark strings.
	 * </p>
	 * <p>
	 * We assume there will be no character `"` (%22 instead) or `'` (%27
	 * instead) in the string.
	 * </p>
	 * 
	 * @param string
	 * @param useSingleQuote
	 * @return a valid JSON object, or null if it's invalid
	 */
	public static JSON decode(String string, boolean useSingleQuote) {
		if (string == null)
			return null;
		char quote = useSingleQuote ? '\'' : '"';

		string = string.trim();
		if (string.length() == 0)
			return new JSON();

		// judge if it's a basic type
		if (string.charAt(0) != '[' && string.charAt(0) != '{') {
			return decodeBasicType(string, "" + quote);
		}

		// if it's a list
		if (string.charAt(0) == '[') {
			return decodeListType(string, useSingleQuote);
		}

		// it's a map
		if (string.charAt(string.length() - 1) != '}')
			return null;
		string = string.substring(1, string.length() - 1);
		boolean inQuote = false;
		int a = 0, b = 0;

		// state 0: key; state 1: `:`; state 2: value
		int state = 0;
		String key = null;
		StringBuilder builder = new StringBuilder();
		HashMap<String, JSON> map = new HashMap<>();

		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (Character.isWhitespace(c)) {
				if (inQuote)
					builder.append(c);
				continue;
			}

			// check `:`
			if (state == 1) {
				if (c != ':')
					return null;
				state = 2;
				continue;
			}

			// check `,`
			if (state == 3) {
				if (c != ',')
					return null;
				state = 0;
				continue;
			}

			if (c == quote) {
				inQuote = !inQuote;
				if (inQuote)
					continue;
				if (state == 0) {
					state = 1;
					key = builder.toString();
					builder = new StringBuilder();
				} else {
					state = 3;
					map.put(key, new JSON(builder.toString()));
					builder = new StringBuilder();
					key = null;
				}
				continue;
			}

			if (inQuote) {
				builder.append(c);
				continue;
			}

			if (key == null || state != 2)
				return null;
			if (c == '[' || c == '{') {
				int np = i;
				for (int j = i; j < string.length(); j++) {
					c = string.charAt(j);
					if (Character.isWhitespace(c))
						continue;
					if (c == quote)
						inQuote = !inQuote;
					if (inQuote)
						continue;
					if (c == '{')
						a++;
					if (c == '}')
						a--;
					if (c == '[')
						b++;
					if (c == ']')
						b--;
					if (a < 0 || b < 0)
						return null;
					if (a == 0 && b == 0) {
						np = j + 1;
						break;
					}
				}
				if (inQuote || a != 0 || b != 0)
					return null;
				JSON j = JSON.decode(string.substring(i, np), useSingleQuote);
				// if (j==null) return null;
				map.put(key, j);
				i = np - 1;
				state = 3;
				continue;
			}

			// others: true/false/numbers
			int p = string.indexOf(',', i);
			if (p == -1)
				p = string.length();
			String tmp = string.substring(i, p);
			tmp = tmp.trim();
			if ("true".equals(tmp))
				map.put(key, new JSON(true));
			else if ("false".equals(tmp))
				map.put(key, new JSON(false));
			else {
				if (tmp.isEmpty())
					return null;
				if (!tmp.matches("[+-]?\\d*(.\\d*)?"))
					return null;
				try {
					if (tmp.contains("."))
						map.put(key, new JSON(Double.parseDouble(tmp)));
					else
						map.put(key, new JSON(Integer.parseInt(tmp)));
				} catch (Exception e) {
					return null;
				}
			}
			state = 3;
			i = p - 1;
		}
		if (inQuote || state == 1 || state == 2)
			return null;
		return new JSON(map);
	}

	/**
	 * Decode string to a basic JSON object
	 * 
	 * @return
	 */
	private static JSON decodeBasicType(String string, String quote) {
		// Integer
		try {
			int x = Integer.parseInt(string);
			if (String.valueOf(x).equals(string))
				return new JSON(x);
		} catch (Exception ignore) {
		}

		// Double
		try {
			double x = Double.parseDouble(string);
			if (String.valueOf(x).equals(string))
				return new JSON(x);
		} catch (Exception ignore) {
		}

		// Boolean
		try {
			boolean b = Boolean.parseBoolean(string);
			if (String.valueOf(b).equals(string))
				return new JSON(b);
		} catch (Exception ignore) {
		}

		// String, remove `"`
		return new JSON(string.replace(quote, ""));
	}

	/**
	 * Decode string to a list JSON object
	 * 
	 * @return
	 */
	private static JSON decodeListType(String string, boolean useSingleQuote) {
		char quote = useSingleQuote ? '\'' : '"';
		if (string.charAt(string.length() - 1) != ']')
			return null;
		string = string.substring(1, string.length() - 1).trim();
		if (string.isEmpty())
			return new JSON(new ArrayList<>());
		boolean inQuote = false;
		int a = 0, b = 0, pos = 0;

		List<JSON> ans = new ArrayList<>();
		ArrayList<String> arrayList = new ArrayList<>();
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (Character.isWhitespace(c))
				continue;
			if (c == quote)
				inQuote = !inQuote;
			if (inQuote)
				continue;
			if (c == '{')
				a++;
			if (c == '}')
				a--;
			if (c == '[')
				b++;
			if (c == ']')
				b--;
			if (a < 0 || b < 0)
				return null;
			if (c == ',' && a == 0 && b == 0) {
				arrayList.add(string.substring(pos, i));
				pos = i + 1;
			}
		}
		arrayList.add(string.substring(pos));
		for (String s : arrayList) {
			JSON j = JSON.decode(s, useSingleQuote);
			// if (j==null) return null;
			ans.add(j);
		}
		return new JSON(ans);
	}

	/**
	 * Deep copy a json object.
	 * 
	 * @param another
	 * @return a JSON object
	 */
	public static JSON deepCopy(JSON another) {
		if (another == null)
			return null;
		if (another.isBasicType())
			return new JSON(another);
		JSON json = new JSON();
		if (another.type == TYPE.JSON) {
			for (Map.Entry<String, JSON> entry : another.map.entrySet()) {
				json.map.put(entry.getKey(), JSON.deepCopy(entry.getValue()));
			}
		}
		if (another.type == TYPE.LIST) {
			json.type = TYPE.LIST;
			json.listValue = new ArrayList<>();
			for (JSON js : another.listValue) {
				json.listValue.add(JSON.deepCopy(js));
			}
		}
		return json;
	}

	/**
	 * Encode JSON object to human readable style.
	 * 
	 * @param json
	 * @see #toStringHumanReadable()
	 */
	public static String toStringHumanReadable(JSON json) {
		return json == null ? "null" : json.toStringHumanReadable();
	}

	/**
	 * Encode JSON object to string. Special characters such as `"`, `\n`, or
	 * non-ascii will be url-encoded.
	 * 
	 * @param json
	 * @see #toString()
	 */
	public static String toString(JSON json) {
		return toString(json, true);
	}

	/**
	 * Encode JSON object to string. If needUrlEncoded, special characters such
	 * as `"`, `\n`, or non-ascii will be url-encoded.
	 * 
	 * @param json
	 * @param needUrlEncode
	 *            whether to encode
	 * @return an encoded string
	 * @see #toString(boolean)
	 */
	public static String toString(JSON json, boolean needUrlEncode) {
		if (json == null)
			return "";
		return json.toString(needUrlEncode);
	}

	/**
	 * Encode JSON object to string. Special characters such as `"`, `\n`, or
	 * non-ascii will be url-encoded.
	 * 
	 * @return
	 */
	public String toString() {
		return toString(true);
	}

	/**
	 * <p>
	 * Show a human-readable structure of the json object. Strings will be
	 * decoded, so there may be extra `"`, `\n` or other special characters.
	 * </p>
	 * <p>
	 * Use <code>{@link #toString()}</code> to encode JSON to a standard JSON
	 * string.
	 * </p>
	 * 
	 * @return
	 */
	public String toStringHumanReadable() {
		return toStringHumanReadable("", false);
	}

	/**
	 * Print human-visible string with prefix.
	 * 
	 * @param prefix
	 * @return
	 */
	private String toStringHumanReadable(String prefix, boolean newLine) {
		if (isBasicType()) {
			if (type == TYPE.STRING) {
				return (newLine ? prefix : "") + "\""
						+ decodeString(stringValue) + "\"";
			}
			return (newLine ? prefix : "") + getString();
		}
		if (type == TYPE.LIST) {
			StringBuilder builder = new StringBuilder();
			if (listValue.size() == 0)
				return (newLine ? prefix : "") + "[]";
			if (newLine)
				builder.append(prefix);
			builder.append("[");
			boolean added = false;
			for (JSON j : listValue) {
				if (added)
					builder.append(",");
				added = true;
				builder.append("\n");
				builder.append(j.toStringHumanReadable(prefix + "  ", true));
			}
			builder.append("\n").append(prefix).append("]");
			return builder.toString();
		}
		if (map.size() == 0)
			return (newLine ? prefix : "") + "{}";
		ArrayList<String> list = new ArrayList<>();
		for (Map.Entry<String, JSON> entry : map.entrySet()) {
			StringBuilder builder = new StringBuilder(prefix + "  ");
			builder.append(String.format("\"%s\": ", entry.getKey()));
			if (entry.getValue() == null)
				builder.append("null");
			else {
				char[] spaces = new char[builder.length()];
				Arrays.fill(spaces, ' ');
				builder.append(entry.getValue().toStringHumanReadable(
						new String(spaces), false));
			}
			list.add(builder.toString());
		}
		return String.format("%s{\n%s\n%s}", newLine ? prefix : "",
				String.join(",\n", list), prefix);
	}

	/**
	 * Encode JSON to string. If needUrlEncoded, special characters such as `"`,
	 * `\n`, or non-ascii will be url-encoded.
	 * 
	 * @param needUrlEncode
	 *            whether to encode
	 * @return an encoded string
	 */
	public String toString(boolean needUrlEncode) {
		if (isBasicType()) {
			if (type == TYPE.STRING) {
				if (needUrlEncode)
					return "\"" + stringValue + "\"";
				else
					return "\"" + decodeString(stringValue) + "\"";
			}
			return getString();
		}
		if (type == TYPE.LIST) {
			if (listValue.size() == 0)
				return "[]";
			StringBuilder builder = new StringBuilder();
			builder.append("[");
			for (JSON j : listValue) {
				builder.append(j.toString(needUrlEncode));
				builder.append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("]");
			return builder.toString();
		}
		if (map.size() == 0)
			return "{}";
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (Map.Entry<String, JSON> entry : map.entrySet()) {
			builder.append("\"").append(entry.getKey()).append("\"")
					.append(":");
			if (entry.getValue() == null)
				builder.append("null");
			else
				builder.append(entry.getValue().toString(needUrlEncode));
			builder.append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}");
		return builder.toString();
	}

	/**
	 * Encode a string
	 * 
	 * @param string
	 * @return encoded string
	 */
	private static String encodeString(String string) {
		if (string == null)
			return null;
		try {
			// try to decode before encode
			try {
				string = decodeString(string);
			} catch (Exception ignore) {
			}
			String s = URLEncoder.encode(string, "utf-8");
			return s.replace("+", "%20");
		} catch (Exception ignore) {
		}
		return string;
	}

	/**
	 * Decode a string
	 * 
	 * @param string
	 * @return decoded string
	 */
	private static String decodeString(String string) {
		if (string == null)
			return null;
		try {
			return URLDecoder.decode(string.replace("+", "%2B"), "utf-8");
		} catch (Exception ignore) {
		}
		return string;
	}

}
