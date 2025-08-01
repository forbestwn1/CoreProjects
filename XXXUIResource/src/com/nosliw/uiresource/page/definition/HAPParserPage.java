package com.nosliw.uiresource.page.definition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.xxx.resource.HAPParserResourceEntity;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPScript;
import com.nosliw.data.core.script.expression1.imp.literate.HAPUtilityScriptLiterate;

/*
 * This is a utility class that process ui resource file and create ui resource object
 * the id index start with 1 every processing start so that for same ui resource, we would get same result
 */ 
public class HAPParserPage implements HAPParserResourceEntity{
	public static final String COMPONENT = "component";

	public static final String EXPRESSION = "expressions";
	public static final String SCRIPT = "script";
	
	//for creating ui id
	private HAPGeneratorId m_idGenerator;
	//configuration object
	private HAPConfigure m_setting;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPParserPage(HAPConfigure setting, HAPGeneratorId idGenerator, HAPRuntimeEnvironment runtimeEnv){
		this.m_idGenerator = idGenerator;
		this.m_setting = setting;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public HAPDefinitionUIUnitPage parseFile(File file) {
		//use file name as ui resource id
		String resourceId = HAPUtilityFile.getFileName(file);
		String source = HAPUtilityFile.readFile(file);
		return this.parseUIDefinition(resourceId, source);
	}

	@Override
	public HAPDefinitionUIUnitPage parseContent(String content) {  return this.parseUIDefinition(null, content);  }

	@Override
	public HAPDefinitionUIUnitPage parseJson(JSONObject jsonObj) {
		// TODO Auto-generated method stub
		return null;
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
	
	public HAPDefinitionUIUnitPage parseFile(String fileName){
		File file = new File(fileName);
		return this.parseFile(file);
	}

	public HAPDefinitionUIUnitPage parseUIDefinition(String resourceId, String content){
		HAPDefinitionUIUnitPage resource = new HAPDefinitionUIUnitPage(resourceId, content);
		Document doc = Jsoup.parse(content, "UTF-8");
		this.parseUIDefinitionUnit(resource, doc.body(), null);
		return resource;
	}

	//parse unit (resource, tag)
	private void parseUIDefinitionUnit(HAPDefinitionUIUnit uiUnit, Element unitEle, HAPDefinitionUIUnit parentUIUnit){
		//process script block
		this.parseUnitScriptBlocks(unitEle, uiUnit);

		//parse as component 
		parseComponent(unitEle, uiUnit);
		
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

	private void parseComponent(Element parentEle, HAPDefinitionUIUnit uiUnit) {
		List<Element> componentEles = HAPUtilityUIResourceParser.getChildElementsByTag(parentEle, COMPONENT);
		JSONObject componentObj = null;
		if(componentEles.size()>0) {
			Element componentEle = componentEles.get(0);
			componentObj = new JSONObject(componentEle.html());
		}
		HAPParserEntityComponent.parseComponentEntity(uiUnit, componentObj, this.m_runtimeEnv.getTaskManager());
		for(Element ele : componentEles)  ele.remove();
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPDefinitionUIUnit parentUnit){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPUtilityBasic.isStringEmpty(HAPUtilityUIResourceParser.getUIIdInElement(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.generateId();
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

	private String getElementText(Element ele) {
		return StringEscapeUtils.unescapeHtml(ele.html());
//		childEle.ownText().html()
	}
	
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
				if(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT.equals(scriptType)){
					newText.append(scriptSeg.getScript());
				}
				else if(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX.equals(scriptType)) {
					List<HAPScript> s = new ArrayList<HAPScript>(); 
					s.add(scriptSeg);
					String sStr = HAPUtilityScriptLiterate.buildScriptLiterate(s);
					HAPDefinitionUIEmbededScriptExpressionInContent expressionContent = new HAPDefinitionUIEmbededScriptExpressionInContent(this.m_idGenerator.generateId(), sStr);
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
			uiUnit.addTagAttribute(eleAttr.getKey(), eleAttr.getValue());
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
