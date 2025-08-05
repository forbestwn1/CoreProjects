package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpressionParser;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImp;
import com.nosliw.core.xxx.application1.HAPWithValueContext;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPElementEvent;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPUIEmbededScriptExpressionInAttribute;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPUIEmbededScriptExpressionInContent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockComplexUIContent extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualPluginParserBlockComplexUIContent(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.UICONTENT_100, HAPManualDefinitionBlockComplexUIContent.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentHtml(HAPManualDefinitionBrick brickManualDef, Object obj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUIContent uiContent = (HAPManualDefinitionBlockComplexUIContent)brickManualDef;
		
		Element element = (Element)obj;
		
		//parse value context
		parseValueContext(element, brickManualDef, parseContext);

		parseDescendantTags(element, uiContent, parseContext);
		
		//parse script expression in content
		parseChildScriptExpressionInContent(element, uiContent, parseContext);

		
		HAPUtilityUIResourceParser.addSpanToText(element);
		uiContent.setHtml(element.html());
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPManualDefinitionBlockComplexUIContent uiContent, HAPManualDefinitionContextParse parserContext){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPUtilityBasic.isStringEmpty(HAPUtilityUIResourceParser.getUIIdInElement(e))){
				//if tag have no ui id, then create ui id for it
				String id = uiContent.generateId();
				e.attr(HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, uiContent, parserContext);
			if(ifRemove) {
				removes.add(e);
			}
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
	private boolean parseTag(Element ele, HAPManualDefinitionBlockComplexUIContent uiContent, HAPManualDefinitionContextParse parserContext){
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		if(customTagName!=null){
			//process custome tag
			String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele);
			if(customTagName.equals("style")) {
//				parseStyle(ele, uiContentId, uiId, parserContext);
			}
			else {
//				parseKeyAttributeOnTag(ele, uiContentId, true, parserContext);
				parseScriptExpressionInTagAttribute(ele, uiContent, true, parserContext);
				
				HAPManualDefinitionBlockComplexUICustomerTag uiCustomerTag = (HAPManualDefinitionBlockComplexUICustomerTag)this.getManualDivisionBrickManager().parseBrickDefinition(ele, HAPEnumBrickType.UICUSTOMERTAG_100, HAPSerializationFormat.HTML, parserContext);
				uiCustomerTag.setUIId(uiId);
				uiContent.addCustomerTag(uiCustomerTag);
			}
			return false;
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, uiContent, parserContext);
			//process key attribute
			parseKeyAttributeOnTag(ele, uiContent, false, parserContext);
			//process elements's attribute that have expression value 
			parseScriptExpressionInTagAttribute(ele, uiContent, false, parserContext);
			//process all descendant tags under this elment
			parseDescendantTags(ele, uiContent, parserContext);
			return false;
		}
	}

	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttributeOnTag(Element ele, HAPManualDefinitionBlockComplexUIContent uiContent, boolean isCustomerTag, HAPManualDefinitionContextParse parserContext){
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
							uiContent.addCustomerTagEvent(tagEvent);
						}
						else{
							//this attribute blong to regular tag
							HAPElementEvent eleEvent = new HAPElementEvent(uiId, event);
							uiContent.addNormalTagEvent(eleEvent);
						}
					}
					//remove this attribute from element
					ele.removeAttr(eleAttrName);
				}
			}
		}
	}

	/*
	 * process element's attribute that have script expression value
	 */
	private void parseScriptExpressionInTagAttribute(Element ele, HAPManualDefinitionBlockComplexUIContent uiContent, boolean isCustomerTag, HAPManualDefinitionContextParse parserContext){
		HAPDefinitionContainerScriptExpression scriptEntityGroupEntity = uiContent.getScriptExpressions();
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String attrValue = eleAttr.getValue(); 
			if(!HAPManualUtilityScriptExpressionParser.isText(attrValue)) {
				String scriptExpressionId = scriptEntityGroupEntity.addScriptExpression(attrValue);
				HAPUIEmbededScriptExpressionInAttribute eAttr = new HAPUIEmbededScriptExpressionInAttribute(eleAttrKey, uiId, scriptExpressionId);
				if(isCustomerTag) {
					uiContent.addScriptExpressionInCustomerTagAttribute(eAttr);
				} else {
					uiContent.addScriptExpressionInNormalTagAttribute(eAttr);
				}
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPManualDefinitionBlockComplexUIContent uiContent, HAPManualDefinitionContextParse parseContext){
		HAPDefinitionContainerScriptExpression scriptEntityGroupEntity = uiContent.getScriptExpressions();

		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			StringBuffer newText = new StringBuffer();
			
			String text = textNode.text();
			if(HAPManualUtilityScriptExpressionParser.isText(text)) {
				newText.append(text);
			}
			else {
				String scriptExpressionId = scriptEntityGroupEntity.addScriptExpression(text);
				HAPUIEmbededScriptExpressionInContent expressionContent = new HAPUIEmbededScriptExpressionInContent(uiContent.generateId(), scriptExpressionId);
				newText.append("<span "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+expressionContent.getUIId()+"></span>");
				uiContent.addScriptExpressionInContent(expressionContent);
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	private void parseValueContext(Element ele, HAPManualDefinitionBrick brickManualDef, HAPManualDefinitionContextParse parseContext) {
		List<Element> valueContextEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithValueContext.VALUECONTEXT);
		for(Element valueContextEle : valueContextEles){
			parseBrickAttributeSelfJson(brickManualDef, new JSONObject(valueContextEle.html()), HAPWithValueContext.VALUECONTEXT, HAPManualEnumBrickType.VALUECONTEXT_100, null, parseContext);	
			break;
		}
		for(Element valueContextEle : valueContextEles) {
			valueContextEle.remove();
		}
	}
	

	

	
	
	
	
	
	@Override
	protected void parseDefinitionContentHtml(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		//parse value context
		parseValueContext((Element)obj, entityId, parserContext);

		//parse attachment
		parseAttachment((Element)obj, entityId, parserContext);

		parseUIDefinitionUnit((Element)obj, entityId, parserContext);
	}
	
	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPDefinitionEntityComplexUIContent uiContentEntity = (HAPDefinitionEntityComplexUIContent)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();

		uiContentEntity.setAttributeValueObject(HAPExecutableEntityComplexUIContent.NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		uiContentEntity.setAttributeValueObject(HAPExecutableEntityComplexUIContent.CUSTOMTAGEVENT, new ArrayList<HAPElementEvent>());
		
		uiContentEntity.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPUIEmbededScriptExpressionInContent>());
		uiContentEntity.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
		uiContentEntity.setAttributeValueObject(HAPExecutableEntityComplexUIContent.SCRIPTEXPRESSIONINTAGATTRIBUTE, new ArrayList<HAPUIEmbededScriptExpressionInAttribute>());
	}
	
	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {	
		super.setupAttributeForComplexEntity(entityId, parserContext);
		//create customer tag container attribute
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPExecutableEntityComplexUIContent.CUSTOMERTAG, HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICUSTOMERTAG, null, parserContext, getRuntimeEnvironment());
		HAPUtilityEntityContainer.newSimpleEntityContainerAttribute(entityId, HAPExecutableEntityComplexUIContent.SERVICE, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, parserContext, getRuntimeEnvironment());
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPExecutableEntityComplexUIContent.SCRIPT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, null, parserContext, getRuntimeEnvironment());
	}
	
	private void parseUIDefinitionUnit(Element wrapperEle, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		
		//parse service
		this.parseService(wrapperEle, uiContentId, parserContext);
		
		//parse script block
		this.parseUnitScriptBlocks(wrapperEle, this.getUIContentEntityById(uiContentId, parserContext), parserContext);

		//parse contents within customer ele
		parseDescendantTags(wrapperEle, uiContentId, parserContext);
		
		//parse script in content
		parseChildScriptExpressionInContent(wrapperEle, uiContentId, parserContext);
		
		HAPUtilityUIResourceParser.addSpanToText(wrapperEle);
		
		uiContent.setHtml(wrapperEle.html());
	}


	/*
	 * process all script blocks under unit
	 */
	private void parseUnitScriptBlocks(Element ele, HAPDefinitionEntityComplexUIContent resource, HAPContextParser parserContext){
		List<Element> scirptEles = new ArrayList<Element>();
		
		HAPDefinitionEntityContainerComplex scriptContainerEntity = (HAPDefinitionEntityContainerComplex)resource.getAttributeValueEntity(HAPExecutableEntityComplexUIContent.SCRIPT, parserContext);
		
		scirptEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPExecutableEntityComplexUIContent.SCRIPT);
		for(Element scriptEle : scirptEles){
			String scriptHtml = scriptEle.html();
			HAPIdEntityInDomain scriptTaskGroupEntityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, scriptHtml, HAPSerializationFormat.JAVASCRIPT, parserContext);
			HAPUtilityEntityContainer.addComplexElementAttribute(resource.getAttributeValueEntityId(HAPExecutableEntityComplexUIContent.SCRIPT) , scriptTaskGroupEntityId, parserContext);
//			scriptContainerEntity.addElementAttribute(scriptTaskGroupEntityId);
		}
		
		for(Element scriptEle : scirptEles) {
			scriptEle.remove();
		}
	}

	
	private void parseService(Element ele, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);

		List<Element> serviceEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPExecutableEntityComplexUIContent.SERVICE);
		for(Element serviceEle : serviceEles) {
			JSONArray serviceArray = new JSONArray(serviceEle.html());
			for(int i=0; i<serviceArray.length(); i++) {
				JSONObject serviceObj = serviceArray.getJSONObject(i);
				this.parseSimpleEntityAttributeSelfJson(serviceObj, uiContent.getAttributeValueEntityId(HAPExecutableEntityComplexUIContent.SERVICE), null, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, null, parserContext);
			}
		}
		for(Element serviceEle : serviceEles) {
			serviceEle.remove();
		}
	}

	
	private void parseAttachment(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		List<Element> attachmentEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithAttachment.ATTACHMENT);
		for(Element attachmentEle : attachmentEles){
			this.parseSimpleEntityAttributeSelfJson(new JSONObject(attachmentEle.html()), parentEntityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
			break;
		}
		for(Element attachmentEle : attachmentEles) {
			attachmentEle.remove();
		}
	}
	
	//parse style 
	private void parseStyle(Element ele, HAPIdEntityInDomain uiContentId, String uiId, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		HAPDefinitionStyle style = new HAPDefinitionStyle(uiId);
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			style.setTask(text);
			break;
		}
		ele.remove();
		uiContent.setStyle(style);
	}

	private HAPDefinitionEntityComplexUIContent getUIContentEntityById(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityComplexUIContent)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
	}


}
