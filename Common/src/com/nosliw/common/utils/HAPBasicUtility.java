package com.nosliw.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HAPBasicUtility {

	public static boolean isEqualMaps(Map m1, Map m2) {
	   if (m1.size() != m2.size())	      return false;
	   for (Object key: m1.keySet()){
		   if(HAPBasicUtility.isEquals(m1.get(key), m2.get(key)))  return false;
	   }
	   return true;
	}	

	public static boolean isEqualLists(List l1, List l2) {
	   if (l1.size() != l2.size())	      return false;
	   for (int i=0; i<l1.size(); i++){
		   if(HAPBasicUtility.isEquals(l1.get(i), l2.get(i)))  return false;
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
}
