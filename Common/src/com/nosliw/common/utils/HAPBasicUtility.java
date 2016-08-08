package com.nosliw.common.utils;

import java.util.Set;

public class HAPBasicUtility {

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
	
}
