package com.nosliw.common.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.ConsString;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import com.nosliw.common.serialization.HAPJsonTypeUnchange;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager; 
 
/**
 * Collection of JSON Utility methods. 
 *  
 */ 
 
public class HAPRhinoDataUtility 
{ 
    /**
     * Takes a java object and converts it to a native java script object 
     *  
     * @param  jsonString       a valid json string 
     * @return NativeObject     the created native JS object that represents the JSON object 
     */ 
    public static ScriptableObject toRhinoScriptableObjectFromObject(Object obj) throws Exception 
    { 
        // Parse JSON string 
		String jsonString = HAPSerializeManager.getInstance().toStringValue(obj, HAPSerializationFormat.JSON);
		Object json = null;

		try{json = new JSONObject(jsonString);}catch(Exception e){}

		if(json==null){
    		try{json = new JSONArray(jsonString);}catch(Exception e){}
		}
		if(json==null)  json = jsonString;
		
		// Create native object
		return toSciptableObjectFromJson(json);
    } 
     
    private static ScriptableObject toSciptableObjectFromJson(Object json) throws Exception{
		ScriptableObject out = null;
    	if(json instanceof JSONObject){
    		JSONObject jsonObject = (JSONObject)json;
            NativeObject object = new NativeObject(); 
            Iterator<String> keys = jsonObject.keys(); 
            while (keys.hasNext()) 
            { 
                String key = keys.next(); 
                Object value = jsonObject.get(key); 
                if (value instanceof JSONObject || value instanceof JSONArray)                object.put(key, object, toSciptableObjectFromJson(value)); 
                else            object.put(key, object, value); 
            }  
            return object;
    	}
    	else if(json instanceof JSONArray){
    		JSONArray jsonArray = (JSONArray)json;
        	int length = jsonArray.length();
        	NativeArray array = new NativeArray(length);
        	for(int i=0; i<length; i++){
        		Object value = jsonArray.get(i);
                if (value instanceof JSONObject || value instanceof JSONArray)            array.put(i, array, toSciptableObjectFromJson(value)); 
                else           array.put(i, array, value); 
        	}
        	return array;
    	}
    	return out;
    }
    
    public static String toJSONStringValue(Object object, Context context, Scriptable scope){
    	String json = (String)NativeJSON.stringify(context, scope, object, null, null);
    	return json;
    }
	
	
    /**
     * Converts a given JavaScript native object and converts it to the relevant JSON string. 
     *  
     * @param nativeObject            JavaScript object 
     * @return String           JSON       
     */ 
    public static Object toJson(Object nativeObject) throws Exception
    { 
    	Object out = null;
    	if(nativeObject instanceof String){
    		out = nativeObject.toString();
    	}
    	else if(nativeObject instanceof ConsString){
    		out = nativeObject.toString();
    	}
    	else if(nativeObject instanceof Integer){
    		out = nativeObject;
    	}
    	else if(nativeObject instanceof Boolean){
    		out = nativeObject;
    	}
    	else if(nativeObject instanceof Double){
    		out = nativeObject;
    	}
    	else if (nativeObject instanceof NativeArray) 
        { 
            out = new JSONArray(nativeArrayToJSONString((NativeArray)nativeObject)); 
        } 
        else if (nativeObject instanceof NativeObject) 
        {  
        	String kkkk = nativeObjectToJSONString((NativeObject)nativeObject);
        	try {
                out = new JSONObject(kkkk); 
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        else if(nativeObject instanceof Function){
        	out = new HAPFunctionType(Context.toString(nativeObject));
        }
        else if(nativeObject instanceof NativeJavaObject){
        	Object javaObj = ((NativeJavaObject)nativeObject).unwrap();
        	out = toJson(javaObj);
        	if(out==null){
            	String jsonStr = HAPSerializeManager.getInstance().toStringValue(javaObj, HAPSerializationFormat.JSON);
            	if(javaObj instanceof List || javaObj instanceof Set)  out = new JSONArray(jsonStr);
            	else out = new JSONObject(jsonStr);
        	}
        }
        else if(nativeObject instanceof Undefined) {
//        	throw new HAPCommonException("Undefined Value!!!!");
        }
    	return out; 
    } 
     
    /**
     * Build a JSON string for a native object 
     *  
     * @param nativeObject 
     * @param json 
     */ 
    private static String nativeObjectToJSONString(NativeObject nativeObject) throws Exception 
    { 
    	Map<String, String> mapJson = new LinkedHashMap<String, String>();
    	Map<String, Class<?>> mapTypeJson = new LinkedHashMap<String, Class<?>>();
    	
        Object[] ids = nativeObject.getIds(); 
        for (Object id : ids) 
        { 
            String key = id.toString();
            Object value = nativeObject.get(key, nativeObject);
            Object json = toJson(value);
            mapJson.put(key, json+"");
          
            if(!(json instanceof String) && json!=null)      mapTypeJson.put(key, json.getClass()); 
        } 
     
        return HAPJsonUtility.buildMapJson(mapJson, mapTypeJson);  
    } 
     
    /**
     * Build JSON string for a native array 
     *  
     * @param nativeArray 
     */ 
    private static String nativeArrayToJSONString(NativeArray nativeArray) throws Exception 
    { 
        Object[] propIds = nativeArray.getIds(); 
        if (isArray(propIds) == true) 
        {
        	List<String> jsonArray = new ArrayList<String>();
        	Class typeClass = null;
            for (int i=0; i<propIds.length; i++) 
            { 
                Object propId = propIds[i]; 
                if (propId instanceof Integer) 
                { 
                    Object value = nativeArray.get((Integer)propId, nativeArray);
                    Object json = toJson(value);
                    jsonArray.add(json+"");
                    if(typeClass==null)  typeClass = json.getClass();
                } 
            } 
            return HAPJsonUtility.buildArrayJson(jsonArray.toArray(new String[0]), typeClass);
        } 
        else 
        { 
        	Map<String, String> mapJson = new LinkedHashMap<String, String>();
        	Map<String, Class<?>> mapTypeJson = new LinkedHashMap<String, Class<?>>();
            for (Object propId : propIds) 
            { 
            	String key = propId.toString();
                Object value = nativeArray.get(key, nativeArray);
                Object json = toJson(value);
                mapJson.put(key, json+"");
                mapTypeJson.put(key, json.getClass()); 
            } 
            return HAPJsonUtility.buildMapJson(mapJson, mapTypeJson);  
        }
    } 
     
    /**
     * Look at the id's of a native array and try to determine whether it's actually an Array or a HashMap 
     *  
     * @param ids       id's of the native array 
     * @return boolean  true if it's an array, false otherwise (ie it's a map) 
     */ 
    static private boolean isArray(Object[] ids) 
    { 
        boolean result = true; 
        for (Object id : ids) 
        { 
            if (id instanceof Integer == false) 
            { 
               result = false; 
               break; 
            } 
        } 
        return result; 
    } 
     
    /**
     * Convert value to JSON string 
     *  
     * @param value 
     */ 
    private static String valueToJSONString(Object value) throws Exception 
    {
		 JSONObject json = new JSONObject(); 
	        if (value instanceof IdScriptableObject && 
	            ((IdScriptableObject)value).getClassName().equals("Date") == true) 
	        { 
	            // Get the UTC values of the date 
	            Object year = NativeObject.callMethod((IdScriptableObject)value, "getUTCFullYear", null); 
	            Object month = NativeObject.callMethod((IdScriptableObject)value, "getUTCMonth", null); 
	            Object date = NativeObject.callMethod((IdScriptableObject)value, "getUTCDate", null); 
	            Object hours = NativeObject.callMethod((IdScriptableObject)value, "getUTCHours", null); 
	            Object minutes = NativeObject.callMethod((IdScriptableObject)value, "getUTCMinutes", null); 
	            Object seconds = NativeObject.callMethod((IdScriptableObject)value, "getUTCSeconds", null); 
	            Object milliSeconds = NativeObject.callMethod((IdScriptableObject)value, "getUTCMilliseconds", null); 
	             
	            // Build the JSON object to represent the UTC date 
	            
	            json.put("zone","UTC"); 
	     json.put("year",year); 
	     json.put("month",month); 
	     json.put("date",date); 
	     json.put("hours",hours); 
	     json.put("minutes",minutes); 
	     json.put("seconds",seconds); 
	     json.put("milliseconds",milliSeconds); 
	     return json.toString(); 
	        } 
	        else if (value instanceof NativeJavaObject) 
	        { 
	            Object javaValue = Context.jsToJava(value, Object.class); 
	            return javaValue.toString(); 
	        } 
	        else if (value instanceof NativeArray) 
	        { 
	            // Output the native array 
	            return nativeArrayToJSONString((NativeArray)value); 
	        } 
	        else if (value instanceof NativeObject) 
	        { 
	            // Output the native object 
	            return nativeObjectToJSONString((NativeObject)value); 
	        } 
	        else if( value instanceof Function){
	        	return Context.toString(value);
	        }
	        else 
	        { 
	           return value.toString();  
	        } 
    }     

}
//class for storing js function string
class HAPFunctionType implements HAPJsonTypeUnchange{
	private String m_content;
	public HAPFunctionType(String str){    		this.m_content = str;    	}
	@Override
	public String toString(){  return this.m_content;  }
}
