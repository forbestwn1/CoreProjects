package com.nosliw.data.core.domain.entity.uipage;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPScript;
import com.nosliw.data.core.script.expression.imp.literate.HAPUtilityScriptLiterate;
import com.nosliw.uiresource.page.definition.HAPDefinitionStyle;
import com.nosliw.uiresource.page.definition.HAPDefinitionUITag;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPUtilityUIResourceParser;

public class HAPPluginEntityDefinitionInDomainUIUnit extends HAPPluginEntityDefinitionInDomainImp{

	public static final String SCRIPT = "script";

	public HAPPluginEntityDefinitionInDomainUIUnit(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIUNIT, HAPDefinitionEntityComplexUIUnit.class, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPDefinitionEntityComplexWithDataExpressionGroup.ATTR_DATAEEXPRESSIONGROUP, parserContext, getRuntimeEnvironment());
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityComplexUIUnit.ATTR_SCRIPTEEXPRESSIONGROUP, parserContext, getRuntimeEnvironment());
	}

	@Override
	public boolean isComplexEntity() {   return true;  }

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		// TODO Auto-generated method stub
		
	}


	
	private void parseUIDefinitionUnit(HAPIdEntityInDomain uiUnitId, Element unitEle, HAPDefinitionEntityComplexUIUnit parentUIUnit, HAPContextParser parserContext){
		
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		
		//parse value context
		parseValueContext(unitEle, uiUnitId, parserContext);
		
		//parse value context
		parseAttachment(unitEle, uiUnitId, parserContext);
		
		//process script block
		this.parseUnitScriptBlocks(unitEle, this.getUIUnitEntityById(uiUnitId, parserContext));
		
		//process key attribute
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getUnitType()))   parseKeyAttributeOnTag(uiUnitId, unitEle, true, parserContext);

		//process unit element's attribute that have expression value 
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getUnitType()))	parseScriptExpressionInTagAttribute(uiUnitId, unitEle, true, parserContext);
		
		
		
		
		

		//process element's normal attribute
		parseUIUnitAttribute(unitEle, uiUnit);
		
		//parse as component 
		parseComponent(unitEle, uiUnit);
		
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
	 * process element's attribute that have script expression value
	 */
	private void parseScriptExpressionInTagAttribute(HAPIdEntityInDomain uiUnitId, Element ele, boolean isCustomerTag, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String attrValue = eleAttr.getValue(); 
			if(!HAPUtilityScriptLiterate.isText(attrValue)) {
				HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr = new HAPDefinitionUIEmbededScriptExpressionInAttribute(eleAttrKey, uiId, eleAttr.getValue());
				if(isCustomerTag)  uiUnit.addScriptExpressionInCustomTagAttribute(eAttr);
				else  uiUnit.addScriptExpressionInNormalTagAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttributeOnTag(HAPIdEntityInDomain uiUnitId, Element ele, boolean isCustomerTag, HAPContextParser parserContext){
		
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		
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
							uiUnit.addCustomTagEvent(tagEvent);
						}
						else{
							//this attribute blong to regular tag
							HAPElementEvent eleEvent = new HAPElementEvent(uiId, event);
							uiUnit.addNormalTagEvent(eleEvent);
						}
					}
					//remove this attribute from element
					ele.removeAttr(eleAttrName);
				}
			}
		}
	}

	
	private void parseValueContext(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		List<Element> valueContextEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithValueContext.VALUECONTEXT);
		for(Element valueContextEle : valueContextEles){
			this.parseNormalSimpleEntityAttribute(new JSONObject(valueContextEle.html()), parentEntityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
			break;
		}
		for(Element valueContextEle : valueContextEles)  valueContextEle.remove();
	}
	
	private void parseAttachment(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		List<Element> attachmentEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithAttachment.ATTACHMENT);
		for(Element attachmentEle : attachmentEles){
			this.parseNormalSimpleEntityAttribute(new JSONObject(attachmentEle.html()), parentEntityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
			break;
		}
		for(Element attachmentEle : attachmentEles)  attachmentEle.remove();
	}
	
	/*
	 * process all script blocks under unit
	 */
	private void parseUnitScriptBlocks(Element ele, HAPDefinitionEntityComplexUIUnit resource){
		List<Element> scirptEles = new ArrayList<Element>();
		
		scirptEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, SCRIPT);
		for(Element scriptEle : scirptEles){
			HAPJsonTypeScript jsBlock = new HAPJsonTypeScript(scriptEle.html());
			resource.setJSBlock(jsBlock);
			break;
		}
		
		for(Element scriptEle : scirptEles)  scriptEle.remove();
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

	private HAPDefinitionEntityComplexUIUnit getUIUnitEntityById(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityComplexUIUnit)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
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

	private String getElementText(Element ele) {
		return StringEscapeUtils.unescapeHtml(ele.html());
//		childEle.ownText().html()
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
				else if(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT.equals(scriptType)) {
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
	private void parseUIUnitAttribute(HAPIdEntityInDomain uiUnitId, Element unitEle, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		Attributes eleAttrs = unitEle.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiUnit.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	

	
}
