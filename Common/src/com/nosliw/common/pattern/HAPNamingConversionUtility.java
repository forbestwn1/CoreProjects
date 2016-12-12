package com.nosliw.common.pattern;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPNamingConversionUtility {

	public static String[] splitText(String text, String token){
		return text.split(token);
	}

	public static Map<String, String> parsePropertyValuePairs(String value){
		Map<String, String> out = new LinkedHashMap<String, String>();
		String[] eleStrs = parseElements(value);
		for(String eleStr : eleStrs){
			String[] nameValuePair = parseNameValuePair(eleStr);
			out.put(nameValuePair[0], nameValuePair[1]);
		}
		return out;
	}
	
	public static String cascadeTexts(String part1, String part2, String seperator){
		if(HAPBasicUtility.isStringEmpty(part1))	part1 = "";
		if(HAPBasicUtility.isStringEmpty(part2))	part2 = "";
		return new StringBuffer().append(part1).append(seperator).append(part2).toString();
	}
	
	public static String cascadeNameSegment(String part1, String part2){
		return cascadeTexts(part1, part2, HAPConstant.SEPERATOR_PREFIX);
	}

	
	/*
	 * cascade two part element together
	 */
	public static String cascadePart(String part1, String part2){
		return cascadeTexts(part1, part2, HAPConstant.SEPERATOR_PART);
	}

	/*
	 * get all sub parts from full
	 */
	public static String[] parsePartlInfos(String parts){
		return parts.split(HAPConstant.SEPERATOR_PART);
	}
	
	public static String[] parseNameValuePair(String nameValueStr){
		return nameValueStr.split(HAPConstant.SEPERATOR_NAMEVALUE);
	}
	
	/*
	 * cascade two detail element together
	 */
	public static String cascadeDetail(String detail1, String detail2){
		return cascadeTexts(detail1, detail2, HAPConstant.SEPERATOR_DETAIL);
	}

	/*
	 * get all sub path from full path
	 */
	public static String[] parseDetailInfos(String details){
		return details.split(HAPConstant.SEPERATOR_DETAIL);
	}
	
	/*
	 * cascade two Path element together
	 */
	public static String cascadePath(String path1, String path2){
		return cascadeTexts(path1, path2, HAPConstant.SEPERATOR_PATH);
	}

	public static String[] parseElements(String eles){
		return eles.split(HAPConstant.SEPERATOR_ELEMENT);
	}

	public static String[] parseSegments(String eles){
		return eles.split(HAPConstant.SEPERATOR_SEGMENT);
	}

	public static String cascadeSegments(String seg1, String seg2){
		return cascadeTexts(seg1, seg2, HAPConstant.SEPERATOR_SEGMENT);
	}

	public static String cascadeElement(String[] eles){
		StringBuffer listStr = new StringBuffer();
		listStr.append(HAPConstant.SEPERATOR_ARRAYSTART);
		for(int i=0; i<eles.length; i++){
			listStr.append(eles[i]);
			if(i<eles.length-1)   listStr.append(HAPConstant.SEPERATOR_ELEMENT);  
		}
		listStr.append(HAPConstant.SEPERATOR_ARRAYEND);
		return listStr.toString();
	}
	
	public static String[] parseProperty(String propertyDef){
		return propertyDef.split("=");
	}
	
	/*
	 * get all sub path from full path
	 */
	public static String[] parsePathSegs(String fullPath){
		return fullPath.split("\\"+HAPConstant.SEPERATOR_PATH);
	}
	 
	/*
	 * key word are always satart with "#", this character is the way to judge if it is a keyword
	 * if yes, then return the keyword part
	 * if not, then reutnr null;
	 */
	static public HAPSegmentParser isKeywordPhrase(String name, String seperator){
		if(name.subSequence(0, 1).equals(HAPConstant.SYMBOL_KEYWORD)){
			return new HAPSegmentParser(name.substring(1), seperator);
		}
		else  return null;
	}

	static public HAPSegmentParser isKeywordPhrase(String name){
		return isKeywordPhrase(name, HAPConstant.SEPERATOR_DETAIL);
	}

	static public String getKeyword(String keyword){
		return getKeyword(keyword, HAPConstant.SYMBOL_KEYWORD);
	}
	
	static public String createKeyword(String key){
		return createKeyword(key, HAPConstant.SYMBOL_KEYWORD);
	}

	/*
	 * key word are always start with some special characters
	 */
	static public String getKeyword(String keyword, String keywordSymbol){
		if(keyword.startsWith(keywordSymbol)){
			return keyword.substring(keywordSymbol.length());
		}
		return null;
	}
	
	/*
	 * build keyword format 
	 * if key already have the format, then just return key
	 */
	static public String createKeyword(String key, String keywordSymbol){
		if(key.contains(keywordSymbol))  return key;
		return keywordSymbol + key;
	}
	
	static public String buildPath(String path1, String path2){
		if(HAPBasicUtility.isStringEmpty(path1)){
			return path2;
		}
		if(HAPBasicUtility.isStringEmpty(path2)){
			return path1;
		}
		else{
			return path1 + HAPConstant.SEPERATOR_PATH + path2;
		}
	}
	
	
}
