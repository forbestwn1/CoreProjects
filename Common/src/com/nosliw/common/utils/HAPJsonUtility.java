package com.nosliw.common.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;


public class HAPJsonUtility {
	
	public static String buildJson(List<?> list, HAPSerializationFormat format){
		if(list==null)  return null;
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(buildObjectJsonValue(data, format));
		}
		return buildArrayJson(arrayJson.toArray(new String[0]));
	}

	public static String buildJson(Object[] list, HAPSerializationFormat format){
		if(list==null)  return null;
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(buildObjectJsonValue(data, format));
		}
		return buildArrayJson(arrayJson.toArray(new String[0]));
	}
	
	public static String buildJson(Set<?> list, HAPSerializationFormat format){
		if(list==null)  return null;
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(buildObjectJsonValue(data, format));
		}
		return buildArrayJson(arrayJson.toArray(new String[0]));
	}

	public static String buildJson(Map<String, ?> map, HAPSerializationFormat format){
		if(map==null)  return null;
		Map<String, String> mapJson = new LinkedHashMap<String, String>();
		for(String key : map.keySet()){
			mapJson.put(key, buildObjectJsonValue(map.get(key), format));
		}
		return buildMapJson(mapJson);
	}

	public static String buildMapJson(Map<String, String> jsonMap){
		return buildMapJson(jsonMap, null);
	}
	
	public static String buildMapJson(Map<String, String> jsonMap, Map<String, Class<?>> typeMap){
		Map<String, String> map = clearMap(jsonMap);
		
		StringBuffer out = new StringBuffer();
		int i = 0;
		out.append("{");
		for(String key : map.keySet()){
			String value = map.get(key);
			
			Class<?> jsonType = null;
			if(typeMap!=null){
				jsonType = typeMap.get(key);
			}
			
			boolean lastOne = i>=map.size()-1;
			String json = buildAttributeJson(key, (String)value, lastOne, jsonType);
			if(json!=null)		out.append(json);
			i++;
		}
		out.append("}");
		return out.toString();
	}

	public static String buildArrayJson(String[] jsons){
		StringBuffer out = new StringBuffer();
		int i = 0;
		out.append("[");
		for(String json : jsons){
			if(json!=null){
				if(i>0)  out.append(",");
				if(json.indexOf("{")==-1)  out.append("\"");
				out.append(json);
				if(json.indexOf("{")==-1)  out.append("\"");
				i++;
			}
		}
		out.append("]");
		return out.toString();
	}

	public static String formatJson(String jsonString){
		try{
			JsonParser parser = new JsonParser();
			JsonElement el = parser.parse(jsonString);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String out = gson.toJson(el); // done		
			
			return out;
		}
		catch(Exception e){
			e.printStackTrace();
			return jsonString;
		}
	}

	private static Map<String, String> clearMap(Map<String, String> jsonMap){
		Map<String, String> out = new LinkedHashMap<String, String>();
		for(String key : jsonMap.keySet()){
			String value = jsonMap.get(key);
			if(value!=null)		out.put(key, value);
		}
		return out;
	}

	private static String buildObjectJsonValue(Object o, HAPSerializationFormat format){
		if(o==null)   return null;
		String out = null;
		if(o instanceof HAPSerializable){
			out = ((HAPSerializable) o).toStringValue(format);
		}
		else if(o instanceof String[]){
			out = buildArrayJson((String[])o);
		}
		else{
			out = o + "";
		}
		return out;
	}
	
	private static String buildAttributeJson(String attr, String value, boolean lastOne, Class<?> type){
		StringBuffer out = new StringBuffer();
		
		String lastString = lastOne?"":",";
		
		if(value==null) return null; //out.append("\"" + attr+ "\""+": undefined" + lastString);
		
		if(String.class==type){
			out.append("\"" + attr+ "\""+":\"" + value + "\""+lastString);
		}
		else if(Integer.class==type){
			out.append("\"" + attr+ "\""+":" + value + ""+lastString);
		}
		else if(Boolean.class==type){
			out.append("\"" + attr+ "\""+":" + value + ""+lastString);
		}
		else if(isObject(value)){
			out.append("\"" + attr+ "\""+":" + value + lastString);
		}
		else if(isArray(value)){
			out.append("\"" + attr+ "\""+":" + value + lastString);
		}
		else{
			out.append("\"" + attr+ "\""+":\"" + value + "\""+lastString);
		}
		out.append("\n");
		return out.toString();
	}

	private static boolean isArray(String value){
		String value1 = value.trim();
		return value1.indexOf("[")==0 && value1.trim().lastIndexOf("]")==value1.trim().length()-1;
	}
	
	private static boolean isObject(String value){
		String value1 = value.trim();
		return value1.indexOf("{")==0 && value1.lastIndexOf("}")==value1.trim().length()-1;
	}
}
