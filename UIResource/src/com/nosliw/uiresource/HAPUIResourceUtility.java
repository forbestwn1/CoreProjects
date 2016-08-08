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
import com.nosliw.common.utils.HAPFileUtility;

public class HAPUIResourceUtility {

	/*
	 * script file name to store all the script infor for uiresource
	 */
	public static String getUIResourceScriptFileName(String resourceName, HAPUIResourceManager uiResourceMan){
		return HAPFileUtility.buildFullFileName(uiResourceMan.getTempFileLocation(), resourceName, "js");
	}
	
	/*
	 * build expression function name based on id
	 */
	public static String buildExpressionFunctionName(String id){
		return HAPConstant.CONS_UIRESOURCE_UIEXPRESSIONFUNCTION_PREFIX+id;
	}

	/*
	 * build custom tag name based on tag basic name
	 */
	public static String makeCustomTagName(String tag){
		return HAPNamingConversionUtility.createKeyword(tag, HAPConstant.CONS_UIRESOURCE_CUSTOMTAG_TAG_PREFIX);
	}
	
	/*
	 * check whether a tag is custom tag
	 * if yes, return custom tag name
	 * if no, return null
	 */
	public static String isCustomTag(Element ele){
		String tagName = ele.tagName();
		return HAPNamingConversionUtility.getKeyword(tagName, HAPConstant.CONS_UIRESOURCE_CUSTOMTAG_TAG_PREFIX);
	}
	
	/*
	 * check whether attribute have expression value
	 * yes, return expression string
	 * else return null
	 */
	public static String isExpressionAttribute(Attribute attr){
		String value = attr.getValue();
		int start = value.indexOf(HAPConstant.CONS_UIRESOURCE_UIEXPRESSION_TOKEN_OPEN);
		if(start!=-1) {
			return value;
//			int expEnd = value.indexOf(HAPConstant.CONS_UIRESOURCE_UIEXPRESSION_TOKEN_CLOSE, start);
//			if(expEnd!=-1){
//				int expStart = start + HAPConstant.CONS_UIRESOURCE_UIEXPRESSION_TOKEN_OPEN.length();
//				out = value.substring(expStart, expEnd);
//			}
		}
		return null;
	}
	
	public static boolean isDataKeyAttribute(String attribute){
		return attribute.startsWith(HAPConstant.CONS_UIRESOURCE_CUSTOMTAG_TAG_PREFIX+HAPConstant.CONS_UIRESOURCE_ATTRIBUTE_DATABINDING);
	}
	
	public static String makeKeyAttribute(String attribute){
		return HAPConstant.CONS_UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX+attribute;
	}
	
	/*
	 * check whether a attribute name is key attribute: attribute name with some particular prefix
	 * if yes, return basic attribute name
	 * if no, return null
	 */
	public static String isKeyAttribute(String attribute){
		return HAPNamingConversionUtility.getKeyword(attribute, HAPConstant.CONS_UIRESOURCE_CUSTOMTAG_KEYATTRIBUTE_PREFIX);
	}
	
	/*
	 * try to get UI id of this element
	 */
	public static String getUIId(Element ele){	return ele.attr(HAPConstant.CONS_UIRESOURCE_ATTRIBUTE_UIID);	}
	
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
	public static void getAllChildTags(HAPUIResourceBasic resource, Set<HAPUITag> tags){
		for(HAPUITag tag : resource.getUITags()){
			tags.add(tag);
			getAllChildTags(tag, tags);
		}
	}
	
	/*
	 * create script for expression
	 */
	public static String createExpressionScript(HAPUIResourceExpression expression){
		String name = expression.getExpressionId();
		String script = expression.getFunctionScript();
		return name + ":" + script + ",";
	}
}
