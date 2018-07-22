package com.nosliw.uiresource.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpressionUtility;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.uiresource.HAPIdGenerator;
import com.nosliw.uiresource.context.HAPContextParser;
import com.nosliw.uiresource.page.HAPConstantDef;
import com.nosliw.uiresource.page.HAPContextEntity;
import com.nosliw.uiresource.page.HAPElementEvent;
import com.nosliw.uiresource.page.HAPEmbededScriptExpressionInAttribute;
import com.nosliw.uiresource.page.HAPEmbededScriptExpressionInContent;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */
public class HAPUIResourceParser {

	//for creating ui id
	private HAPIdGenerator m_idGenerator;
	//configuration object
	private HAPConfigure m_setting;
	
	private HAPValueInfoManager m_valueInfoMan;
	private HAPCriteriaParser m_criteriaParser;
	private HAPExpressionSuiteManager m_expressionManager;
	
	public HAPUIResourceParser(HAPConfigure setting, HAPExpressionSuiteManager expressionMan, HAPIdGenerator idGenerator){
		this.m_idGenerator = idGenerator;
		this.m_setting = setting;
		this.m_expressionManager = expressionMan;
		this.m_criteriaParser = HAPCriteriaParser.getInstance();
	}
	
	//resourceUnit : target ui resource object
	//content : html content
	public void parseContent(HAPUIDefinitionUnit resourceUnit, String content){
		try{
			Document doc = Jsoup.parse(content, "UTF-8");
			parseUIDefinitionUnit(resourceUnit, doc.body(), null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public HAPUIDefinitionUnitResource parseContent(String resourceId, String content){
		HAPUIDefinitionUnitResource resource = new HAPUIDefinitionUnitResource(resourceId, content);
		try{
			Document doc = Jsoup.parse(content, "UTF-8");
			parseUIDefinitionResource(resource, doc.body());
			parseUIDefinitionUnit(resource, doc.body(), null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resource;
	}
	
	public HAPUIDefinitionUnitResource parseFile(String fileName){
		HAPUIDefinitionUnitResource resource = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			resource = new HAPUIDefinitionUnitResource(resourceId, source);
			
			Document doc = Jsoup.parse(input, "UTF-8");
			parseUIDefinitionResource(resource, doc.body());
			parseUIDefinitionUnit(resource, doc.body(), null);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return resource;
	}

	private void parseUIDefinitionResource(HAPUIDefinitionUnitResource uiResourceUnit, Element ele){
		this.parseChildCommandBlocks(ele, uiResourceUnit);
	}	
	
	private void parseUIDefinitionUnit(HAPUIDefinitionUnit uiResourceUnit, Element ele, HAPUIDefinitionUnit parentUIResourceUnit){
		//process script block
		this.parseChildScriptBlocks(ele, uiResourceUnit);
		//process constant block
		this.parseChildConstantBlocks(ele, uiResourceUnit);
		//process context block
		this.parseChildContextBlocks(ele, uiResourceUnit);
		//process expression block
		this.parseChildExpressionBlocks(ele, uiResourceUnit);
		
		this.parseChildEventBlocks(ele, uiResourceUnit);
		this.parseChildServiceBlocks(ele, uiResourceUnit);
		
		//process key attribute
		parseKeyAttribute(ele, parentUIResourceUnit, true);

		//process elements's attribute that have expression value 
		parseScriptExpressionInAttribute(ele, parentUIResourceUnit, true);
		
		//process element's normal attribute
		parseCurrentAttribute(ele, uiResourceUnit);
		
		//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
		String uiId = uiResourceUnit.getId();
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.after("<"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstant.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstant.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.remove();
		
		//process contents within customer ele
		parseChildScriptExpressionInContent(ele, uiResourceUnit);
		parseDescendantTags(ele, uiResourceUnit);
		
		HAPUIResourceParserUtility.addSpanToText(ele);
		
		uiResourceUnit.postRead();
		
		uiResourceUnit.setContent(ele.html());
		
	}
	
	private void parseChildEventBlocks(Element ele, HAPUIDefinitionUnit resourceUnit) {
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnit.EVENTS);
		for(Element childEle : childEles){
			try {
				JSONArray eventListJson = new JSONArray(childEle.html());
				for(int i=0; i<eventListJson.length(); i++) {
					JSONObject eventJson = eventListJson.getJSONObject(i);
					HAPContextEntity eventDef = new HAPContextEntity();
					eventDef.buildObject(eventJson, HAPSerializationFormat.JSON);
					resourceUnit.addEventDefinition(eventDef);
				}
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(Element childEle : childEles)  childEle.remove();
	}

	private void parseChildServiceBlocks(Element ele, HAPUIDefinitionUnit resourceUnit) {
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnit.SERVICES);
		for(Element childEle : childEles){
			try {
				JSONArray serviceListJson = new JSONArray(childEle.html());
				for(int i=0; i<serviceListJson.length(); i++) {
					JSONObject serviceJson = serviceListJson.getJSONObject(i);
					HAPContextEntity serviceDef = new HAPContextEntity();
					serviceDef.buildObject(serviceJson, HAPSerializationFormat.JSON);
					resourceUnit.addServiceDefinition(serviceDef);
				}
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(Element childEle : childEles)  childEle.remove();
	}

	private void parseChildCommandBlocks(Element ele, HAPUIDefinitionUnitResource resourceUnit) {
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnitResource.COMMANDS);
		for(Element childEle : childEles){
			try {
				JSONArray commandListJson = new JSONArray(childEle.html());
				for(int i=0; i<commandListJson.length(); i++) {
					JSONObject commandJson = commandListJson.getJSONObject(i);
					HAPContextEntity commandDef = new HAPContextEntity();
					commandDef.buildObject(commandJson, HAPSerializationFormat.JSON);
					resourceUnit.addCommandDefinition(commandDef);
				}
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(Element childEle : childEles)  childEle.remove();
	}
	
	private void parseChildContextBlocks(Element ele, HAPUIDefinitionUnit resourceUnit){
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnit.CONTEXT);
		
		for(Element childEle : childEles){
			try {
				HAPContextParser.parseContextGroup(new JSONObject(childEle.html()), resourceUnit.getContextDefinition());
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(Element childEle : childEles)  childEle.remove();
	}
	
	private void parseChildExpressionBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnit.EXPRESSIONS);

		for(Element childEle : childEles){
			try {
				String content = childEle.html();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					HAPDefinitionExpression expDef = new HAPDefinitionExpression(defsJson.optString(defName));
					resource.addExpressionDefinition(defName, expDef);
				}
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		

		for(Element childEle : childEles)   childEle.remove();
	}
	
	/*
	 * process all constant blocks
	 */
	private void parseChildConstantBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> childEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnit.CONSTANTS);

		for(Element childEle : childEles){
			try {
				String content = childEle.text();
				JSONObject defsJson = new JSONObject(content);
				Iterator<String> defNames = defsJson.keys();
				while(defNames.hasNext()){
					String defName = defNames.next();
					resource.addConstantDef(defName, new HAPConstantDef(defsJson.get(defName)));
				}
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		for(Element childEle : childEles)   childEle.remove();
	}

	/*
	 * process all script blocks
	 */
	private void parseChildScriptBlocks(Element ele, HAPUIDefinitionUnit resource){
		List<Element> scirptEles = new ArrayList<Element>();
		
		scirptEles = HAPUIResourceParserUtility.getChildElementsByTag(ele, HAPUIDefinitionUnitResource.SCRIPT);
		for(Element scriptEle : scirptEles){
			HAPScript jsBlock = new HAPScript(scriptEle.html());
			resource.setJSBlock(jsBlock);
			break;
		}
		
		for(Element scriptEle : scirptEles)  scriptEle.remove();
	}

	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPUIDefinitionUnit resource){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			List<Object> segments = HAPScriptExpressionUtility.discoverUIExpressionInText(text, this.m_expressionManager);
			StringBuffer newText = new StringBuffer();
			for(Object segment : segments){
				if(segment instanceof String){
					newText.append((String)segment);
				}
				else if(segment instanceof HAPScriptExpression){
					HAPScriptExpression scriptExpression = (HAPScriptExpression)segment;
					HAPEmbededScriptExpressionInContent expressionContent = new HAPEmbededScriptExpressionInContent(this.m_idGenerator.createId(), scriptExpression, this.m_expressionManager);
					newText.append("<span "+HAPConstant.UIRESOURCE_ATTRIBUTE_UIID+"="+expressionContent.getUIId()+"></span>");
					resource.addScriptExpressionInContent(expressionContent);
				}
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPUIDefinitionUnit resource){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPBasicUtility.isStringEmpty(HAPUIResourceParserUtility.getUIId(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.createId();
				e.attr(HAPConstant.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, resource);
			if(ifRemove)  removes.add(e);
		}
		
		for(Element remove : removes){
			remove.remove();
		}
	}
	
	/*
	 * process a tag element 
	 * return true : this element should be removed after processing
	 * 		  false : this element should not be removed after processiong
	 */
	private boolean parseTag(Element ele, HAPUIDefinitionUnit resource){
		String customTag = HAPUIResourceParserUtility.isCustomTag(ele);
		if(customTag!=null){
			//process custome tag
			String uiId = HAPUIResourceParserUtility.getUIId(ele); 
			HAPUIDefinitionUnitTag uiTag = new HAPUIDefinitionUnitTag(customTag, uiId);
			parseUIDefinitionUnit(uiTag, ele, resource);
			resource.addUITag(uiTag);
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, resource);
			//process key attribute
			parseKeyAttribute(ele, resource, false);
			//process elements's attribute that have expression value 
			parseScriptExpressionInAttribute(ele, resource, false);
			//process all descendant tags under this elment
			parseDescendantTags(ele, resource);
		}
		return false;
	}

	
	/*
	 * process attribute of Element for resource(UI resource or custom tag)
	 */
	private void parseCurrentAttribute(Element ele, HAPUIDefinitionUnit resource){
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			resource.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process element's attribute that have script expression value
	 * only the first script expression will consider, other text or script expression will be ignored
	 */
	private void parseScriptExpressionInAttribute(Element ele, HAPUIDefinitionUnit resource, boolean isCustomerTag){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object

			HAPEmbededScriptExpressionInAttribute eAttr = new HAPEmbededScriptExpressionInAttribute(eleAttrKey, uiId, eleAttr.getValue(), this.m_expressionManager);
			if(!eAttr.getScriptExpressions().isEmpty()){
				if(isCustomerTag)  resource.addScriptExpressionInTagAttribute(eAttr);
				else  resource.addScriptExpressionInAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttribute(Element ele, HAPUIDefinitionUnit resource, boolean isCustomerTag){
		String uiId = HAPUIResourceParserUtility.getUIId(ele); 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUIResourceParserUtility.isKeyAttribute(eleAttrName);
			
			if(keyAttrName!=null){
				if(keyAttrName.contains(HAPConstant.UIRESOURCE_ATTRIBUTE_EVENT)){
					//process event key attribute
					HAPSegmentParser events = new HAPSegmentParser(eleAttrValue, HAPConstant.SEPERATOR_ELEMENT);
					while(events.hasNext()){
						String event = events.next();
						if(isCustomerTag){
							//this attribute belong to customer tag
							HAPElementEvent tagEvent = new HAPElementEvent(uiId, event);
							resource.addTagEvent(tagEvent);
						}
						else{
							//this attribute blong to regular tag
							HAPElementEvent eleEvent = new HAPElementEvent(uiId, event);
							resource.addElementEvent(eleEvent);
						}
					}
					//remove this attribute from element
					ele.removeAttr(eleAttrName);
				}
			}
		}
	}

	
	
	/*
	 * convert all standard descendant tags that have data key attribute to default custom tag 
	 */
//	private void adjustDescendantTagAccordToDataBinding(Element ele){
//		Elements eles = ele.children();
//		for(Element e : eles){
//			//for each child element
//			String tag = e.tagName();
//			if(HAPUIResourceParserUtility.isCustomTag(e)==null){
//				//for none custom tag
//				Attributes attrs = e.attributes();
//				boolean dataKeyAttr = false;
//				for(Attribute attr : attrs){
//					//check if there is any data key attribute, if so, then this tag should be costom tag
//					//here we cannot just check key attribute as some regular tag may have key attribute for instance "event"
//					dataKeyAttr = HAPUIResourceParserUtility.isDataKeyAttribute(attr.getKey());
//					if(dataKeyAttr)	break;
//				}
//				if(dataKeyAttr){
//					//if have data key attribute, then change the standard tag name to default custome tag name
//					tag = HAPUIResourceParserUtility.makeCustomTagName(tag);
//					e.tagName(tag);
//				}
//			}
//			//do the same on child element
//			adjustDescendantTagAccordToDataBinding(e);
//		}
//	}
	
}
