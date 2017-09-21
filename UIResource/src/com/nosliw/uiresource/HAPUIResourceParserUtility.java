package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPExpressionManager;

public class HAPUIResourceParserUtility {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";
	
	public static final String CUSTOMTAG_PREFIX = "nosliw-";
	
	/**
	 * parse text to build ui expression list
	 * @param text
	 * @param idGenerator
	 * @param expressionMan
	 * @return a list of text and ui expression object
	 */
	public static List<Object> parseUIExpression(
			String text, 
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan){
		List<Object> out = new ArrayList<Object>();
		int start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(text.substring(0, start));
			int expEnd = text.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = text.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			String uiId = idGenerator.createId();
			HAPScriptExpression uiExpression = new HAPScriptExpression(uiId, expression, expressionMan);
			out.add(uiExpression);
			//keep searching the rest
			text=text.substring(end);
			start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		}
		if(!HAPBasicUtility.isStringEmpty(text)){
			out.add(text);
		}
		return out;
	}
	
	/*
	 * check whether attribute have expression value
	 * yes, return expression string
	 * else return null
	 */
	public static String isExpressionAttribute(Attribute attr){
		String out = null;
		String value = attr.getValue();
		
//		int start = value.indexOf(HAPUIResourceParser.UIEXPRESSION_TOKEN_OPEN);
//		if(start!=-1) {
//			int expEnd = value.indexOf(HAPUIResourceParser.UIEXPRESSION_TOKEN_CLOSE, start);
//			if(expEnd!=-1){
//				int expStart = start + HAPUIResourceParser.UIEXPRESSION_TOKEN_OPEN.length();
//				out = value.substring(expStart, expEnd);
//			}
//		}
		return out;
	}
	
	
	/*
	 * build expression function name based on id
	 */
	public static String buildExpressionFunctionName(String id){
		return HAPConstant.UIRESOURCE_UIEXPRESSIONFUNCTION_PREFIX+id;
	}

	/*
	 * build custom tag name based on tag basic name
	 */
	public static String makeCustomTagName(String tag){
		return HAPNamingConversionUtility.createKeyword(tag, CUSTOMTAG_PREFIX);
	}
	
	/*
	 * check whether a tag is custom tag
	 * if yes, return custom tag name
	 * if no, return null
	 */
	public static String isCustomTag(Element ele){
		String tagName = ele.tagName();
		return HAPNamingConversionUtility.getKeyword(tagName, CUSTOMTAG_PREFIX);
	}
	
	public static boolean isDataKeyAttribute(String attribute){
		return attribute.startsWith(CUSTOMTAG_PREFIX+HAPConstant.UIRESOURCE_ATTRIBUTE_DATABINDING);
	}
	
	public static String makeKeyAttribute(String attribute){
		return HAPConstant.UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX+attribute;
	}
	
	/*
	 * check whether a attribute name is key attribute: attribute name with some particular prefix
	 * if yes, return basic attribute name
	 * if no, return null
	 */
	public static String isKeyAttribute(String attribute){
		return HAPNamingConversionUtility.getKeyword(attribute, HAPConstant.UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX);
	}
	
	/*
	 * try to get UI id of this element
	 */
	public static String getUIId(Element ele){	return ele.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID);	}
	
	public static String spanText(String text){
		return "<span>"+text+"</span>";
	}
	
	/*
	 * put span structure around text
	 */
	public static void addSpanToText(Element ele){
		List<TextNode> textNodes = new ArrayList<TextNode>();
		collectTextNodes(ele, textNodes);

		for(TextNode textNode : textNodes){
			String t1 = textNode.text();
			String t = t1.replaceAll("\\n+\\t+", "");
			t = StringUtils.stripToEmpty(t);
			if(HAPBasicUtility.isStringNotEmpty(t)){
				String text = spanText(t1);
				textNode.after(text);
				textNode.remove();
			}
			else{
				Node preNode = textNode.previousSibling();
				Node nextNode = textNode.nextSibling();
				if((preNode!=null&&"span".equals(preNode.nodeName())) || 
						(nextNode!=null&&"span".equals(nextNode.nodeName())))
				{
						String text = spanText(t1);
						textNode.after(text);
						textNode.remove();
				}
			}
		}
	}

	/*
	 * collect all text nodes under element
	 */
	public static void collectTextNodes(Element ele, List<TextNode> outputTextNodes){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			outputTextNodes.add(textNode);
		}

		Elements eles = ele.children();
		for(Element e : eles){
			collectTextNodes(e, outputTextNodes);
		}
	}
	
	/*
	 * find all child customer tags
	 */
	public static void getAllChildTags(HAPUIDefinitionUnit resource, Set<HAPUIDefinitionUnitTag> tags){
		for(HAPUIDefinitionUnitTag tag : resource.getUITags()){
			tags.add(tag);
			getAllChildTags(tag, tags);
		}
	}
	
}
