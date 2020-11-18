package com.nosliw.uiresource.page.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;

public class HAPUtilityUIResourceParser {

	public static final String CUSTOMTAG_PREFIX = "nosliw-";
	
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
	public static String getUIIdInElement(Element ele){	return ele.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID);	}
	
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
	public static void getAllChildTags(HAPDefinitionUIUnit uiUnit, Set<HAPDefinitionUITag> tags){
		for(HAPDefinitionUITag tag : uiUnit.getUITags()){
			tags.add(tag);
			getAllChildTags(tag, tags);
		}
	}
	
	public static List<Element> getChildElementsByTag(Element parent, String tagName){
		List<Element> out = new ArrayList<Element>();
		Elements childEles = parent.children(); 
		for(int i=0; i<childEles.size(); i++){
			Element childEle = childEles.get(i);
			String childTagName = childEle.tag().getName();
			if(tagName.equals(childTagName)){
				out.add(childEle);
			}
		}
		return out;
	}
}
