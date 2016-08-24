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
import com.nosliw.common.serialization.HAPStringable;


public class HAPJsonUtility {
	public static String getListObjectJson(List list, String format){
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(getObjectJsonValue(data, format));
		}
		return getArrayJson(arrayJson.toArray(new String[0]));
	}

	public static String getArrayObjectJson(Object[] list, String format){
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(getObjectJsonValue(data, format));
		}
		return getArrayJson(arrayJson.toArray(new String[0]));
	}
	
	public static String getSetObjectJson(Set list, String format){
		List<String> arrayJson = new ArrayList<String>();
		for(Object data : list){
			arrayJson.add(getObjectJsonValue(data, format));
		}
		return getArrayJson(arrayJson.toArray(new String[0]));
	}

	public static String getMapObjectJson(Map map, String format){
		Map mapJson = new LinkedHashMap();
		for(Object key : map.keySet()){
			mapJson.put(key, getObjectJsonValue(map.get(key), format));
		}
		return getMapJson(mapJson);
	}

	private static String getObjectJsonValue(Object o, String format){
		String out = null;
		if(o instanceof HAPStringable){
			out = ((HAPStringable) o).toStringValue(format);
		}
		else if(o instanceof String[]){
			out = getArrayJson((String[])o);
		}
		else{
			out = o + "";
		}
		return out;
	}
	
	private static Map<String, String> clearMap(Map<String, String> jsonMap){
		Map<String, String> out = new LinkedHashMap<String, String>();
		for(String key : jsonMap.keySet()){
			String value = jsonMap.get(key);
			if(value!=null)		out.put(key, value);
		}
		return out;
	}

	public static String getMapJson(Map<String, String> jsonMap){
		return getMapJson(jsonMap, null);
	}
	
	public static String getMapJson(Map<String, String> jsonMap, Map<String, Class> typeMap){
		Map<String, String> map = clearMap(jsonMap);
		
		StringBuffer out = new StringBuffer();
		int i = 0;
		out.append("{");
		for(String key : map.keySet()){
			String value = map.get(key);
			
			Class jsonType = null;
			if(typeMap!=null){
				jsonType = typeMap.get(key);
			}
			
			boolean lastOne = i>=map.size()-1;
			String json = getAttributeJson(key, (String)value, lastOne, jsonType);
			if(json!=null)		out.append(json);
			i++;
		}
		out.append("}");
		return out.toString();
	}

	private static String getAttributeJson(String attr, String value, boolean lastOne, Class type){
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

	public static String getArrayJson(String[] jsons){
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

	private static boolean isArray(String value){
		String value1 = value.trim();
		return value1.indexOf("[")==0 && value1.trim().lastIndexOf("]")==value1.trim().length()-1;
	}
	
	private static boolean isObject(String value){
		String value1 = value.trim();
		return value1.indexOf("{")==0 && value1.lastIndexOf("}")==value1.trim().length()-1;
	}
	
	public static String formatJson(String jsonString){
		try{
//			JsonReader reader = new JsonReader(new StringReader(jsonString));
//			reader.setLenient(true);
			
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

}
