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
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpressionLiterate;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpressionDataScript;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpressionText;
import com.nosliw.data.core.domain.entity.expression.script.HAPUtilityExpressionDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPUtilityUIResourceParser;

public class HAPPluginEntityDefinitionInDomainUIUnit extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainUIUnit(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	public static final String SCRIPT = "script";

	private HAPGeneratorId m_idGenerator = new HAPGeneratorId();
	

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


	
	private void parseUIDefinitionUnit(Element unitEle, HAPIdEntityInDomain uiUnitId, HAPContextParser parserContext){
		
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		
		//parse value context
		parseValueContext(unitEle, uiUnitId, parserContext);
		
		//parse value context
		parseAttachment(unitEle, uiUnitId, parserContext);
		
		//process script block
		this.parseUnitScriptBlocks(unitEle, this.getUIUnitEntityById(uiUnitId, parserContext));
		
		//process key attribute
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getUnitType()))   parseKeyAttributeOnTag(unitEle, uiUnitId, true, parserContext);

		//process unit element's attribute that have expression value 
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getUnitType()))	parseScriptExpressionInTagAttribute(unitEle, uiUnitId, true, parserContext);
		
		//parse script in content
		parseChildScriptExpressionInContent(unitEle, uiUnitId, parserContext);
		
		if(HAPConstantShared.UIRESOURCE_TYPE_TAG.equals(uiUnit.getUnitType())) {
			//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
			String uiId = uiUnitId.getEntityId();
			unitEle.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
			unitEle.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
			unitEle.remove();
		}
		
		//process element's normal attribute
		parseUIUnitAttribute(unitEle, uiUnitId, parserContext);
		
		
		

		//parse as component 
//		parseComponent(unitEle, uiUnit);
		
		//process contents within customer ele
		parseDescendantTags(unitEle, uiUnitId, parserContext);
		
		HAPUtilityUIResourceParser.addSpanToText(unitEle);
		
//		uiUnit.postRead();
		
		uiUnit.setContent(unitEle.html());
	}

	/*
	 * process attribute of ui unit(UI resource or custom tag)
	 */
	private void parseUIUnitAttribute(Element unitEle, HAPIdEntityInDomain uiUnitId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		Attributes eleAttrs = unitEle.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiUnit.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}
	}
	
	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPIdEntityInDomain uiUnitId, HAPContextParser parserContext){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPUtilityBasic.isStringEmpty(HAPUtilityUIResourceParser.getUIIdInElement(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.generateId();
				e.attr(HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, uiUnitId, parserContext);
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
	private boolean parseTag(Element ele, HAPIdEntityInDomain uiUnitId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		if(customTagName!=null){
			//process custome tag
			String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele);
			if(customTagName.equals("style")) {
				parseStyle(ele, uiUnitId, uiId, parserContext);
			}
			else {
				HAPIdEntityInDomain tagEntityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, parserContext);
				HAPDefinitionEntityComplexUITag uiTag = (HAPDefinitionEntityComplexUITag)parserContext.getGlobalDomain().getEntityInfoDefinition(tagEntityId).getEntity();
				uiTag.setUIId(uiId);
				uiTag.setTagName(customTagName);
				parseUIDefinitionUnit(ele, tagEntityId, parserContext);
				uiUnit.addCustomTag(tagEntityId);
			}
			return false;
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, uiUnitId, parserContext);
			//process key attribute
			parseKeyAttributeOnTag(ele, uiUnitId, false, parserContext);
			//process elements's attribute that have expression value 
			parseScriptExpressionInTagAttribute(ele, uiUnitId, false, parserContext);
			//process all descendant tags under this elment
			parseDescendantTags(ele, uiUnitId, parserContext);
			return false;
		}
	}

	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPIdEntityInDomain uiUnitId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		HAPDefinitionEntityExpressionScriptGroup scriptEntityGroupEntity = uiUnit.getScriptExpressionGroupEntity(parserContext);

		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			
			StringBuffer newText = new StringBuffer();
			HAPDefinitionExpression expressionDef = HAPUtilityExpressionDefinition.parseDefinitionExpression(text, null, uiUnit.getDataExpressionGroupEntity(parserContext), this.getRuntimeEnvironment().getDataExpressionParser());
			for(HAPDefinitionSegmentExpression segment : expressionDef.getSegments()) {
				String segmentType = segment.getType();
				if(segmentType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT)) {
					HAPDefinitionSegmentExpressionText textSegment = (HAPDefinitionSegmentExpressionText)segment;
					newText.append(textSegment.getContent());
				}
				else if(segmentType.equals(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT)) {
					HAPDefinitionExpressionLiterate literateExpression = new HAPDefinitionExpressionLiterate();
					literateExpression.addSegmentDataScript((HAPDefinitionSegmentExpressionDataScript)segment);
					String scriptExpressionId = scriptEntityGroupEntity.addExpression(expressionDef);

					HAPDefinitionUIEmbededScriptExpressionInContent expressionContent = new HAPDefinitionUIEmbededScriptExpressionInContent(this.generateId(), scriptExpressionId);
					newText.append("<span "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+expressionContent.getUIId()+"></span>");
					uiUnit.addScriptExpressionInContent(expressionContent);
				}
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	/*
	 * process element's attribute that have script expression value
	 */
	private void parseScriptExpressionInTagAttribute(Element ele, HAPIdEntityInDomain uiUnitId, boolean isCustomerTag, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		HAPDefinitionEntityExpressionScriptGroup scriptEntityGroupEntity = uiUnit.getScriptExpressionGroupEntity(parserContext);
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String attrValue = eleAttr.getValue(); 
			if(!HAPUtilityExpressionDefinition.isText(attrValue)) {

				HAPDefinitionExpression expressionDef = HAPUtilityExpressionDefinition.parseDefinitionExpression(attrValue, null, uiUnit.getDataExpressionGroupEntity(parserContext), this.getRuntimeEnvironment().getDataExpressionParser());
				String scriptExpressionId = scriptEntityGroupEntity.addExpression(expressionDef);
				
				HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr = new HAPDefinitionUIEmbededScriptExpressionInAttribute(eleAttrKey, uiId, scriptExpressionId);
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
	private void parseKeyAttributeOnTag(Element ele, HAPIdEntityInDomain uiUnitId, boolean isCustomerTag, HAPContextParser parserContext){
		
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
	private void parseStyle(Element ele, HAPIdEntityInDomain uiUnitId, String uiId, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUIUnit uiUnit = this.getUIUnitEntityById(uiUnitId, parserContext);
		HAPDefinitionStyle style = new HAPDefinitionStyle(uiId);
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			style.setDefinition(text);
			break;
		}
		ele.remove();
		uiUnit.setStyle(style);
	}

	private HAPDefinitionEntityComplexUIUnit getUIUnitEntityById(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityComplexUIUnit)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
	}
	
	private String generateId() {     return this.m_idGenerator.generateId();      }
	
	
	
	
	
	
	
	
	
	
	
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
	

	private String getElementText(Element ele) {
		return StringEscapeUtils.unescapeHtml(ele.html());
//		childEle.ownText().html()
	}
	

	
}
