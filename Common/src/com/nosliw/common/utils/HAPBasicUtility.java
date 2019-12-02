package com.nosliw.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HAPBasicUtility {

	public static String addVersionToUrl(String url, String version) {
		return url+"?version="+version;
	}
	
	public static String toString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}
	
	public static String getRawTypeName(Type type){
		if(type instanceof ParameterizedType){
			Class cs = (Class)((ParameterizedType)type).getRawType();
			return cs.getName();
		}
		else if(type instanceof Class){
			return ((Class)type).getName();
		}
		return null;
	}
	
	public static String getParameterizedType(Type type){
		String out = null;
        if (type instanceof ParameterizedType)
        {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();

            if (actualTypes != null && actualTypes.length == 1)
            {
                out = ((Class)actualTypes[0]).getName();
            }
        }
        return out;
	}
	
	public static boolean isEqualMaps(Map m1, Map m2) {
	   if (m1.size() != m2.size())	      return false;
	   if (!m1.keySet().equals(m2.keySet()))  return false;
	   for (Object key: m1.keySet()){
		   if(!HAPBasicUtility.isEquals(m1.get(key), m2.get(key)))  return false;
	   }
	   return true;
	}	

	public static boolean isEqualLists(List l1, List l2) {
	   if (l1.size() != l2.size())	      return false;
	   for (int i=0; i<l1.size(); i++){
		   if(!HAPBasicUtility.isEquals(l1.get(i), l2.get(i)))  return false;
	   }
	   return true;
	}	

	public static boolean isEqualSets(Set s1, Set s2) {
	   if (s1.size() != s2.size())	      return false;
	   for(Object v1 : s1){
		   if(!s2.contains(v1))  return false;
	   }
	   return true;
	}	
	
	public static boolean toBoolean(String value)
	{
		if("true".equals(value) || "yes".equals(value))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean isStringEmpty(String str){
		return str==null || str.isEmpty();
	}
	
	public static boolean isStringNotEmpty(String str){
		return str!=null && !str.isEmpty();
	}
	
	/*
	 * trim
	 */
	public static String cleanString(String str){
		return str.replaceAll("\\s+","").trim();
	}

	public static boolean isEquals(Object obj1, Object obj2){
		if(obj1==null && obj2==null)  return true;
		if(obj1!=null && obj2!=null){
			return obj1.equals(obj2);
		}
		return false;
	}
	
	public static boolean isEqualSet(Set obj1, Set obj2){
		if(obj1==null && obj2==null)  return true;
		if(obj1!=null && obj2!=null){
			if(obj1.size()!=obj2.size())  return false;
			for(Object ele1 : obj1){
				if(!obj2.contains(ele1))   return false;
			}
			return true;
		}
		return false;
	}
	
	
	public static String wildcardToRegex(String wildcard){
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch(c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                    // escape special regexp-characters
                case '(': case ')': case '[': case ']': case '$':
                case '^': case '.': case '{': case '}': case '|':
                case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return(s.toString());
    }
	
	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
	}	
	
	public static String upperCaseFirstLetter(String name){
		if(HAPBasicUtility.isStringEmpty(name))  return name;
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static <T, S> Map<S, T> reverseMapping(Map<T, S> mapping){
		Map<S, T> out = new LinkedHashMap<S, T>();
		for(T t : mapping.keySet()){
			out.put(mapping.get(t), t);
		}
		return out;
	}
}
