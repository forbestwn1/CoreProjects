package com.nosliw.common.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HAPUtilityNamingConversion {

	/**
	 * Cascade components, every elements count 
	 */
	public static String cascadeComponents(String[] parts, String seperator){
		StringBuffer out = new StringBuffer();
		
		for(int i=0; i<parts.length; i++){
			String part = parts[i];
			if(i>=1){
				out.append(seperator);
			}
			if(part!=null)			out.append(part);
		}
		return out.toString();
	}
	
	/**
	 * Cascade two components 
	 */
	public static String cascadeComponents(String part1, String part2, String seperator){
		return cascadeComponents(new String[]{part1, part2}, seperator);
	}

	/**
	 * Cascade element, only handle component that is not empty 
	 */
	public static String cascadeElements(String[] parts, String seperator){
		StringBuffer out = new StringBuffer();
		
		int k = 0;
		for(int i=0; i<parts.length; i++){
			String part = parts[i];
			if(HAPBasicUtility.isStringNotEmpty(part)){
				if(k>=1){
					out.append(seperator);
				}
				out.append(part);
				k++;
			}
		}
		return out.toString();
	}
	
	public static String cascadeElements(String part1, String part2, String seperator){
		return cascadeElements(new String[]{part1, part2}, seperator);
	}
	
	public static String[] splitTextByComponents(String text, String token){
		return text.split(token);
	}

	public static String[] splitTextByElements(String text, String token){
		if(HAPBasicUtility.isStringEmpty(text)){
			return new String[0];
		}
		
		String[] split = text.split(token);
		List<String> out = new ArrayList<String>();
		for(String ele : split){
			if(HAPBasicUtility.isStringNotEmpty(ele)){
				out.add(ele);
			}
		}
		return out.toArray(new String[0]);
	}
	
	
	public static String cascadeLevel1(String seg1, String seg2){		return cascadeComponents(seg1, seg2, HAPConstantShared.SEPERATOR_LEVEL1);	}
	public static String cascadeLevel1(String[] segs){	return cascadeComponents(segs, HAPConstantShared.SEPERATOR_LEVEL1);	}
	public static String[] parseLevel1(String eles){		return splitTextByComponents(eles, HAPConstantShared.SEPERATOR_LEVEL1);	}

	public static String cascadeLevel2(String seg1, String seg2){		return cascadeComponents(seg1, seg2, HAPConstantShared.SEPERATOR_LEVEL2);	}
	public static String cascadeLevel2(String[] segs){	return cascadeComponents(segs, HAPConstantShared.SEPERATOR_LEVEL2);	}
	public static String[] parseLevel2(String eles){		return splitTextByComponents(eles, "\\"+HAPConstantShared.SEPERATOR_LEVEL2);	}

	public static String cascadeLevel3(String seg1, String seg2){		return cascadeComponents(seg1, seg2, HAPConstantShared.SEPERATOR_LEVEL3);	}
	public static String cascadeLevel3(String[] segs){	return cascadeComponents(segs, HAPConstantShared.SEPERATOR_LEVEL3);	}
	public static String[] parseLevel3(String eles){		return splitTextByComponents(eles, "\\"+HAPConstantShared.SEPERATOR_LEVEL3);	}

	public static String cascadeSegments1(String seg1, String seg2){		return cascadeComponents(seg1, seg2, HAPConstantShared.SEPERATOR_SEGMENT);	}
	public static String[] parseSegments1(String eles){		return splitTextByComponents(eles, HAPConstantShared.SEPERATOR_SEGMENT);	}
	
	public static String cascadePath(String path1, String path2){		return cascadeElements(path1, path2, HAPConstantShared.SEPERATOR_PATH);	}
	public static String cascadePath(String[] paths){		return cascadeElements(paths, HAPConstantShared.SEPERATOR_PATH);	}
	public static String[] parsePaths(String paths){		return splitTextByElements(paths, "\\"+HAPConstantShared.SEPERATOR_PATH);	}

	public static String cascadeComponentPath(String path1, String path2){		return cascadeComponents(path1, path2, HAPConstantShared.SEPERATOR_PATH);	}
	public static String cascadeComponentPath(String[] paths){		return cascadeComponents(paths, HAPConstantShared.SEPERATOR_PATH);	}
	public static String[] parseComponentPaths(String paths){		return splitTextByComponents(paths, "\\"+HAPConstantShared.SEPERATOR_PATH);	}
	
	public static String cascadePart(String part1, String part2){		return cascadeComponents(part1, part2, HAPConstantShared.SEPERATOR_PART);	}
	public static String[] parseParts(String parts){		return splitTextByComponents(parts, HAPConstantShared.SEPERATOR_PART);	}

	public static String cascadeNameValuePair(String name, String value){		return cascadeComponents(name, value, HAPConstantShared.SEPERATOR_NAMEVALUE);	}
	public static String[] parseNameValuePair(String nameValueStr){		return splitTextByComponents(nameValueStr, HAPConstantShared.SEPERATOR_NAMEVALUE);	}
	
	public static String cascadeDetail(String detail1, String detail2){	return cascadeComponents(detail1, detail2, HAPConstantShared.SEPERATOR_DETAIL);	}
	public static String cascadeDetail(String[] details){	return cascadeComponents(details, HAPConstantShared.SEPERATOR_DETAIL);	}
	public static String[] parseDetails(String details){		return splitTextByComponents(details, "\\"+HAPConstantShared.SEPERATOR_DETAIL);	}
	
	public static String cascadeNameSegment(String part1, String part2){		return cascadeComponents(part1, part2, HAPConstantShared.SEPERATOR_PREFIX);	}
	public static String[] parseNameSegments(String nameSegs){		return splitTextByComponents(nameSegs, HAPConstantShared.SEPERATOR_PREFIX);	}

	public static String cascadeElement(String ele1, String ele2){		return cascadeComponents(ele1, ele2, HAPConstantShared.SEPERATOR_ELEMENT);	}
	public static String cascadeElements(String[] eles){		return cascadeComponents(eles, HAPConstantShared.SEPERATOR_ELEMENT);	}
	public static String[] parseElements(String eles){		return splitTextByElements(eles, HAPConstantShared.SEPERATOR_ELEMENT);	}

	public static String cascadeProperty(String name, String value){	return cascadeComponents(name, value, HAPConstantShared.SEPERATOR_DETAIL);	}
	public static String[] parseProperty(String propertyDef){	return splitTextByComponents(propertyDef, HAPConstantShared.SEPERATOR_NAMEVALUE);	}

	
	public static Map<String, String> parsePropertyValuePairs(String value){
		Map<String, String> out = new LinkedHashMap<String, String>();
		String[] eleStrs = parseElements(value);
		for(String eleStr : eleStrs){
			String[] nameValuePair = parseNameValuePair(eleStr);
			out.put(nameValuePair[0], nameValuePair[1]);
		}
		return out;
	}
	
	public static String cascadePropertyValuePairs(Map<String, String> valuePairs) {
		String out = null;
		if(valuePairs!=null && !valuePairs.isEmpty()) {
			List<String> pairs = new ArrayList<String>();
			for(String key : valuePairs.keySet()) {
				pairs.add(cascadeNameValuePair(key, valuePairs.get(key)));
			}
			out = cascadeElements(pairs.toArray(new String[0]));
		}
		return out;
	}
	
	public static String cascadeElementArray(String[] eles){
		StringBuffer listStr = new StringBuffer();
		listStr.append(HAPConstantShared.SEPERATOR_ARRAYSTART);
		for(int i=0; i<eles.length; i++){
			listStr.append(eles[i]);
			if(i<eles.length-1)   listStr.append(HAPConstantShared.SEPERATOR_ELEMENT);  
		}
		listStr.append(HAPConstantShared.SEPERATOR_ARRAYEND);
		return listStr.toString();
	}
	
	static public String buildPath(String path1, String path2){
		if(HAPBasicUtility.isStringEmpty(path1)){
			return path2;
		}
		if(HAPBasicUtility.isStringEmpty(path2)){
			return path1;
		}
		else{
			return path1 + HAPConstantShared.SEPERATOR_PATH + path2;
		}
	}
	

	 
	/*
	 * key word are always satart with "#", this character is the way to judge if it is a keyword
	 * if yes, then return the keyword part
	 * if not, then reutnr null;
	 */
	static public HAPSegmentParser isKeywordPhrase(String name, String seperator){
		if(name.subSequence(0, 1).equals(HAPConstantShared.SYMBOL_KEYWORD)){
			return new HAPSegmentParser(name.substring(1), seperator);
		}
		else  return null;
	}

	static public HAPSegmentParser isKeywordPhrase(String name){
		return isKeywordPhrase(name, HAPConstantShared.SEPERATOR_DETAIL);
	}

	static public String getKeyword(String keyword){
		return getKeyword(keyword, HAPConstantShared.SYMBOL_KEYWORD);
	}
	
	static public String createKeyword(String key){
		return createKeyword(key, HAPConstantShared.SYMBOL_KEYWORD);
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
}
