package com.nosliw.uiresource.page.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.expression.HAPScript;
import com.nosliw.data.core.script.expression.literate.HAPUtilityScriptLiterate;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.uiresource.common.HAPIdGenerator;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */ 
public class HAPParserPage {

	public static final String EVENT = "events";
	public static final String ATTACHMENT = "attachment";
	public static final String SERVICE = "services";
	public static final String SERVICE_USE = "use";
	public static final String SERVICE_PROVIDER = "provider";
	public static final String CONTEXT = "contexts";
	public static final String COMMAND = "commands";
	public static final String EXPRESSION = "expressions";
	public static final String SCRIPT = "scripts";
	
	//for creating ui id
	private HAPIdGenerator m_idGenerator;
	//configuration object
	private HAPConfigure m_setting;
	
	public HAPParserPage(HAPConfigure setting, HAPIdGenerator idGenerator){
		this.m_idGenerator = idGenerator;
		this.m_setting = setting;
	}
	
	//resourceUnit : target ui resource object
	//content : html content
	public void parseAndBuildUIDefinition(HAPDefinitionUIUnit resourceUnit, String content){
		try{
			Document doc = Jsoup.parse(content, "UTF-8");
			parseUIDefinitionUnit(resourceUnit, doc.body(), null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public HAPDefinitionUIPage parseFile(String fileName){
		HAPDefinitionUIPage resource = null;
		File input = new File(fileName);
		//use file name as ui resource id
		String resourceId = HAPFileUtility.getFileName(input);
		String source = HAPFileUtility.readFile(input);
		resource = this.parseUIDefinition(resourceId, source);
		return resource;
	}

	public HAPDefinitionUIPage parseUIDefinition(String resourceId, String content){
		HAPDefinitionUIPage resource = new HAPDefinitionUIPage(resourceId, content);
		Document doc = Jsoup.parse(content, "UTF-8");
		this.parseUIDefinitionUnit(resource, doc.body(), null);
		return resource;
	}

	//parse unit (resource, tag)
	private void parseUIDefinitionUnit(HAPDefinitionUIUnit uiUnit, Element unitEle, HAPDefinitionUIUnit parentUIUnit){
		//process script block
		this.parseUnitScriptBlocks(unitEle, uiUnit);
		//process context block
		this.parseUnitContextBlocks(unitEle, uiUnit);
		//process expression block
//		this.parseUnitExpressionBlocks(unitEle, uiUnit);
		
		//parse event definition block
		this.parseUnitEventBlocks(unitEle, uiUnit);
		//parse service definition block
		this.parseUnitServiceBlocks(unitEle, uiUnit);
		//parse command definition block
		this.parseChildCommandBlocks(unitEle, uiUnit);
		
		//parse attachment
		parseAttachmentBlocks(unitEle, uiUnit);
		
		//process key attribute
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getType()))   parseKeyAttributeOnTag(unitEle, parentUIUnit, true);

		//process unit element's attribute that have expression value 
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getType()))	parseScriptExpressionInTagAttribute(unitEle, parentUIUnit, true);
		
		//process element's normal attribute
		parseUIUnitAttribute(unitEle, uiUnit);
		
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getType())) {
			//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
			String uiId = uiUnit.getId();
			unitEle.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
			unitEle.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
			unitEle.remove();
		}
		
		//process contents within customer ele
		parseChildScriptExpressionInContent(unitEle, uiUnit);
		parseDescendantTags(unitEle, uiUnit);
		
		HAPUtilityUIResourceParser.addSpanToText(unitEle);
		
		uiUnit.postRead();
		
		uiUnit.setContent(unitEle.html());
	}

	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPDefinitionUIUnit parentUnit){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPBasicUtility.isStringEmpty(HAPUtilityUIResourceParser.getUIIdInElement(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.createId();
				e.attr(HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, parentUnit);
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
	private boolean parseTag(Element ele, HAPDefinitionUIUnit parentUnit){
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		if(customTagName!=null){
			//process custome tag
			String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele);
			if(customTagName.equals("style")) {
				parseStyle(ele, uiId, parentUnit);
			}
			else {
				HAPDefinitionUITag uiTag = new HAPDefinitionUITag(customTagName, uiId);
				parseUIDefinitionUnit(uiTag, ele, parentUnit);
				parentUnit.addUITag(uiTag);
			}
			return false;
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, parentUnit);
			//process key attribute
			parseKeyAttributeOnTag(ele, parentUnit, false);
			//process elements's attribute that have expression value 
			parseScriptExpressionInTagAttribute(ele, parentUnit, false);
			//process all descendant tags under this elment
			parseDescendantTags(ele, parentUnit);
			return false;
		}
	}

	//parse style 
	private void parseStyle(Element ele, String uiId, HAPDefinitionUIUnit parentUnit) {
		HAPDefinitionStyle style = new HAPDefinitionStyle(uiId);
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			style.setDefinition(text);
			break;
		}
		ele.remove();
		parentUnit.setStyle(style);
	}
	
	private void parseUnitEventBlocks(Element ele, HAPDefinitionUIUnit resourceUnit) {
		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, EVENT);
		for(Element childEle : childEles){
			try {
				JSONArray eventListJson = new JSONArray(childEle.html());
				for(int i=0; i<eventListJson.length(); i++) {
					JSONObject eventJson = eventListJson.getJSONObject(i);
					HAPDefinitionUIEvent eventDef = new HAPDefinitionUIEvent();
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

	private void parseAttachmentBlocks(Element ele, HAPDefinitionUIUnit resourceUnit) {
		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, ATTACHMENT);
		for(Element childEle : childEles){
			try {
				JSONObject attachmentDefJson = new JSONObject(childEle.html());
				HAPAttachmentUtility.parseDefinition(attachmentDefJson, resourceUnit.getAttachmentContainer());
				break;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		for(Element childEle : childEles)  childEle.remove();
	}
	
	private void parseUnitServiceBlocks(Element ele, HAPDefinitionUIUnit resourceUnit) {
		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, SERVICE);
		for(Element childEle : childEles){
			JSONArray serviceUseListJson = new JSONArray(childEle.html());
			if(serviceUseListJson!=null) {
				for(int i=0; i<serviceUseListJson.length(); i++) {
					JSONObject serviceUseJson = serviceUseListJson.getJSONObject(i);
					HAPDefinitionServiceUse serviceUseDef = new HAPDefinitionServiceUse();
					serviceUseDef.buildObject(serviceUseJson, HAPSerializationFormat.JSON);
					resourceUnit.addService(serviceUseDef);
				}
			}
			break;
		}
		for(Element childEle : childEles)  childEle.remove();
	}

	private void parseChildCommandBlocks(Element ele, HAPDefinitionUIUnit resourceUnit) {
		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, COMMAND);
		for(Element childEle : childEles){
			try {
				JSONArray commandListJson = new JSONArray(childEle.html());
				for(int i=0; i<commandListJson.length(); i++) {
					JSONObject commandJson = commandListJson.getJSONObject(i);
					HAPDefinitionUICommand commandDef = new HAPDefinitionUICommand();
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
	
	private void parseUnitContextBlocks(Element ele, HAPDefinitionUIUnit resourceUnit){
		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, CONTEXT);
		
		for(Element childEle : childEles){
			try {
				HAPParserContext.parseContextGroup(HAPJsonUtility.newJsonObject(StringEscapeUtils.unescapeHtml(childEle.html())), (HAPContextGroup)resourceUnit.getContextStructure());
				break;
			} catch (JSONException e) {
				e.printStackTrace();
				System.out.println(childEle.html());
			}
		}
		
		for(Element childEle : childEles)  childEle.remove();
	}
	
//	private void parseUnitExpressionBlocks(Element ele, HAPDefinitionUIUnit resource){
//		List<Element> childEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, EXPRESSION);
//
//		for(Element childEle : childEles){
//			try {
//				String content = childEle.html();
//				JSONObject defsJson = new JSONObject(content);
//				Iterator<String> defNames = defsJson.keys();
//				while(defNames.hasNext()){
//					String defName = defNames.next();
//					resource.addExpressionDefinition(defName, defsJson.optString(defName));
//				}
//				break;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}		
//
//		for(Element childEle : childEles)   childEle.remove();
//	}
	
	/*
	 * process all script blocks under unit
	 */
	private void parseUnitScriptBlocks(Element ele, HAPDefinitionUIUnit resource){
		List<Element> scirptEles = new ArrayList<Element>();
		
		scirptEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, SCRIPT);
		for(Element scriptEle : scirptEles){
			HAPJsonTypeScript jsBlock = new HAPJsonTypeScript(scriptEle.html());
			resource.setJSBlock(jsBlock);
			break;
		}
		
		for(Element scriptEle : scirptEles)  scriptEle.remove();
	}

	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPDefinitionUIUnit resource){
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			
			List<HAPScript> scriptSegs = HAPUtilityScriptLiterate.parseScriptLiterate(text);
			StringBuffer newText = new StringBuffer();
			for(HAPScript scriptSeg : scriptSegs){
				String scriptType = scriptSeg.getType();
				if(HAPConstantShared.SCRIPT_TYPE_SEG_TEXT.equals(scriptType)){
					newText.append(scriptSeg.getScript());
				}
				else if(HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSIONSCRIPT.equals(scriptType)) {
					List<HAPScript> s = new ArrayList<HAPScript>(); 
					s.add(scriptSeg);
					String sStr = HAPUtilityScriptLiterate.buildScriptLiterate(s);
					HAPDefinitionUIEmbededScriptExpressionInContent expressionContent = new HAPDefinitionUIEmbededScriptExpressionInContent(this.m_idGenerator.createId(), sStr);
					newText.append("<span "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+expressionContent.getUIId()+"></span>");
					resource.addScriptExpressionInContent(expressionContent);
				}
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	
	/*
	 * process attribute of ui unit(UI resource or custom tag)
	 */
	private void parseUIUnitAttribute(Element unitEle, HAPDefinitionUIUnit uiUnit){
		Attributes eleAttrs = unitEle.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiUnit.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process element's attribute that have script expression value
	 */
	private void parseScriptExpressionInTagAttribute(Element ele, HAPDefinitionUIUnit resource, boolean isCustomerTag){
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String attrValue = eleAttr.getValue();
			if(!HAPUtilityScriptLiterate.isText(attrValue)) {
				HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr = new HAPDefinitionUIEmbededScriptExpressionInAttribute(eleAttrKey, uiId, eleAttr.getValue());
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
	private void parseKeyAttributeOnTag(Element ele, HAPDefinitionUIUnit resource, boolean isCustomerTag){
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrValue = eleAttr.getValue();
			String eleAttrName = eleAttr.getKey();
			String keyAttrName = HAPUtilityUIResourceParser.isKeyAttribute(eleAttrName);
			
			if(keyAttrName!=null){
				if(keyAttrName.contains(HAPConstantShared.UIRESOURCE_ATTRIBUTE_EVENT)){
					//process event key attribute
					HAPSegmentParser events = new HAPSegmentParser(eleAttrValue, HAPConstantShared.SEPERATOR_ELEMENT);
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
}
