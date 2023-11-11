package com.nosliw.common.serialization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HAPUtilityJson {

	final private static String TOKEN_UNCHANGED_START = "AAAAAAAA";
	final private static String TOKEN_UNCHANGED_END = "EEEEEEEE";

	public static void buildJsonMap(String key, Object value, Map<String, String> jsonMap, Map<String, Class<?>> typeMap, HAPSerializationFormat format) {
		jsonMap.put(key, buildJson(value, format));
		if(value instanceof Integer)   typeMap.put(key, Integer.class);
		else if(value instanceof Boolean)   typeMap.put(key, Boolean.class);
		else if(value instanceof Double)   typeMap.put(key, Double.class);
	}
	
	public static String buildJsonStringValue(Object o, HAPSerializationFormat format) {
		String out;
		if(o instanceof String) {
			out = "\""+(String)o+"\"";
		}
		else {
			out = buildJson(o, format);
		}
		return out;
	}
	
	public static String buildJson(Object o, HAPSerializationFormat format) {
		if (o == null)
			return null;
		String out = null;
		if (o instanceof HAPSerializable) {
			out = ((HAPSerializable) o).toStringValue(format);
		} else if (o instanceof List) {
			List<String> arrayJson = new ArrayList<String>();
			for (Object data : (List) o)
				arrayJson.add(buildJson(data, format));
			out = buildArrayJson(arrayJson.toArray(new String[0]));
		} else if (o instanceof Set) {
			List<String> arrayJson = new ArrayList<String>();
			for (Object data : (Set) o)
				arrayJson.add(buildJson(data, format));
			out = buildArrayJson(arrayJson.toArray(new String[0]));
		} else if (o instanceof Object[]) {
			List<String> arrayJson = new ArrayList<String>();
			for (Object data : (Object[]) o)
				arrayJson.add(buildJson(data, format));
			out = buildArrayJson(arrayJson.toArray(new String[0]));
		} else if (o instanceof Map) {
			Map<String, String> mapJson = new LinkedHashMap<String, String>();
			Map<String, Class<?>> typeMap = new LinkedHashMap<String, Class<?>>();
			Map<String, ?> mapValue = (Map) o;
			for (String key : mapValue.keySet()) {
				buildJsonMap(key, mapValue.get(key), mapJson, typeMap, format);
				
//				Object value = mapValue.get(key);
//				mapJson.put(key, buildJson(value, format));
//				if(value instanceof Integer)   typeMap.put(key, Integer.class);
//				else if(value instanceof Boolean)   typeMap.put(key, Boolean.class);
//				else if(value instanceof Double)   typeMap.put(key, Double.class);
			}
			out = buildMapJson(mapJson, typeMap);
		} else {
			out = o + "";
		}
		return out;
	}

	public static String buildMapJson(Map<String, String> jsonMap) {
		return buildMapJson(jsonMap, null);
	}

	public static String buildMapJson(Map<String, String> jsonMap, Map<String, Class<?>> typeMap) {
		Map<String, String> map = clearMap(jsonMap);

		StringBuffer out = new StringBuffer();
		int i = 0;
		out.append("{");
		for (String key : map.keySet()) {
			String value = map.get(key);

			Class<?> jsonType = null;
			if (typeMap != null) {
				jsonType = typeMap.get(key);
			}

			boolean lastOne = i >= map.size() - 1;
			String json = buildAttributeJson(key, value, lastOne, jsonType);
			if (json != null)
				out.append(json);
			i++;
		}
		out.append("}");
		return out.toString();
	}

	public static String buildArrayJson(String[] jsons) {
		return buildArrayJson(jsons, null);
	}

	public static String buildArrayJson(String[] jsons, Class eleType) {
		StringBuffer out = new StringBuffer();
		int i = 0;
		out.append("[");
		for (String json : jsons) {
			if (json != null) {
				if (i > 0)
					out.append(",");

				if (eleType == Boolean.class || eleType == Integer.class || eleType == Double.class) {
					out.append(json);
				} else {
					if (json.indexOf("{") == -1)
						out.append("\"");
					out.append(json);
					if (json.indexOf("{") == -1)
						out.append("\"");
				}

				i++;
			}
		}
		out.append("]");
		return out.toString();
	}

	public static String formatJson(String jsonString) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(jsonString);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String out = gson.toJson(el); // done

			return out;
		} catch (Exception e) {
			System.out.println(jsonString);
			e.printStackTrace();
			return jsonString;
		}
	}

	public static Object toJsonObject(String jsonString) {
		if(isObject(jsonString)) return new JSONObject(jsonString);
		if(isArray(jsonString)) return new JSONArray(jsonString);
		return jsonString;
	}
	
	private static Map<String, String> clearMap(Map<String, String> jsonMap) {
		Map<String, String> out = new LinkedHashMap<String, String>();
		for (String key : jsonMap.keySet()) {
			String value = jsonMap.get(key);
			if (value != null)
				out.put(key, value);
		}
		return out;
	}

	private static String buildAttributeJson(String attr, String value, boolean lastOne, Class<?> type) {
		StringBuffer out = new StringBuffer();
		String lastString = lastOne ? "" : ",";

		if (value == null)
			return null; // out.append("\"" + attr+ "\""+": undefined" + lastString);

		if (type != null && HAPJsonTypeUnchange.class.isAssignableFrom(type)) {
			// treat the value as it is
			out.append("\"" + attr + "\"" + ":\"" + TOKEN_UNCHANGED_START + HAPUtilityJson.escape(value)
					+ TOKEN_UNCHANGED_END + "\"" + lastString);
		} else if (type != null && HAPJsonTypeAsItIs.class.isAssignableFrom(type)) {
			// treat the value as it is
			out.append("\"" + attr + "\"" + ":" + value + lastString);
		} else if (String.class == type) {
			out.append("\"" + attr + "\"" + ":\"" + value + "\"" + lastString);
		} else if (Double.class == type) {
			out.append("\"" + attr + "\"" + ":" + value + "" + lastString);
		} else if (Integer.class == type) {
			out.append("\"" + attr + "\"" + ":" + value + "" + lastString);
		} else if (Boolean.class == type) {
			out.append("\"" + attr + "\"" + ":" + value + "" + lastString);
		} else if (isObject(value)) {
			out.append("\"" + attr + "\"" + ":" + value + lastString);
		} else if (isArray(value)) {
			out.append("\"" + attr + "\"" + ":" + value + lastString);
		} else if (JsonObject.class == type) {
			out.append("\"" + attr + "\"" + ":" + value + lastString);
		} else if (JSONObject.class == type || JSONArray.class == type) {
			out.append("\"" + attr + "\"" + ":" + value + lastString);
		} else {
			out.append("\"" + attr + "\"" + ":\"" + value + "\"" + lastString);
		}
		out.append("\n");
		return out.toString();
	}

	private static boolean isArray(String value) {
		String value1 = value.trim();
		return value1.indexOf("[") == 0 && value1.trim().lastIndexOf("]") == value1.trim().length() - 1;
	}

	private static boolean isObject(String value) {
		String value1 = value.trim();
		return value1.indexOf("{") == 0 && value1.lastIndexOf("}") == value1.trim().length() - 1;
	}

	public static String escape(String input) {
		return StringEscapeUtils.escapeJson(input);
	}

	public static String unescape(String input) {
		String out = input.replace("\"" + TOKEN_UNCHANGED_START, "");
		out = out.replace(TOKEN_UNCHANGED_END + "\"", "");
		out = StringEscapeUtils.unescapeJson(out);
		return out;
	}

	public static JSONObject newJsonObject(String jsonStr) {
		try {
			return new JSONObject(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
