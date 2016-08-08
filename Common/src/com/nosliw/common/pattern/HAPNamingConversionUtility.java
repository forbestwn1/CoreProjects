package com.nosliw.common.pattern;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

public class HAPNamingConversionUtility {

	public static String[] splitText(String text, String token){
		return text.split(token);
	}

	public static String cascadeTexts(String part1, String part2, String seperator){
		if(HAPBasicUtility.isStringEmpty(part1))	part1 = "";
		if(HAPBasicUtility.isStringEmpty(part2))	part2 = "";
		return new StringBuffer().append(part1).append(seperator).append(part2).toString();
	}
	

	/*
	 * cascade two part element together
	 */
	public static String cascadePart(String part1, String part2){
		return cascadeTexts(part1, part2, HAPConstant.CONS_SEPERATOR_PART);
	}

	/*
	 * get all sub parts from full
	 */
	public static String[] parsePartlInfos(String parts){
		return parts.split(HAPConstant.CONS_SEPERATOR_PART);
	}
	
	/*
	 * cascade two detail element together
	 */
	public static String cascadeDetail(String detail1, String detail2){
		return cascadeTexts(detail1, detail2, HAPConstant.CONS_SEPERATOR_DETAIL);
	}

	/*
	 * get all sub path from full path
	 */
	public static String[] parseDetailInfos(String details){
		return details.split(HAPConstant.CONS_SEPERATOR_DETAIL);
	}
	
	/*
	 * cascade two Path element together
	 */
	public static String cascadePath(String path1, String path2){
		return cascadeTexts(path1, path2, HAPConstant.CONS_SEPERATOR_PATH);
	}

	/*
	 * get all sub path from full path
	 */
	public static String[] parsePathSegs(String fullPath){
		return fullPath.split("\\"+HAPConstant.CONS_SEPERATOR_PATH);
	}
	 
	public static HAPPath parsePath(String path){
		HAPPath out = null;
		int index = path.lastIndexOf(HAPConstant.CONS_SEPERATOR_PATH);
		if(index==-1){
			//name only
			out = new HAPPath(path, null, null);
		}
		else{
			String name = path.substring(index+1);
			String p = path.substring(0, index);
			String[] pathSegs = parsePathSegs(p);
			out = new HAPPath(name, p, pathSegs);
		}
		return out;
	}
	
	/*
	 * key word are always satart with "#", this character is the way to judge if it is a keyword
	 * if yes, then return the keyword part
	 * if not, then reutnr null;
	 */
	static public HAPSegmentParser isKeywordPhrase(String name, String seperator){
		if(name.subSequence(0, 1).equals(HAPConstant.CONS_SYMBOL_KEYWORD)){
			return new HAPSegmentParser(name.substring(1), seperator);
		}
		else  return null;
	}

	static public HAPSegmentParser isKeywordPhrase(String name){
		return isKeywordPhrase(name, HAPConstant.CONS_SEPERATOR_DETAIL);
	}

	static public String getKeyword(String keyword){
		return getKeyword(keyword, HAPConstant.CONS_SYMBOL_KEYWORD);
	}
	
	static public String createKeyword(String key){
		return createKeyword(key, HAPConstant.CONS_SYMBOL_KEYWORD);
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
		else{
			return path1 + HAPConstant.CONS_SEPERATOR_PATH + path2;
		}
	}
	
	
}
