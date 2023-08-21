package com.nosliw.ui.entity.uicontent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpressionLiterate;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpressionDataScript;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionSegmentExpressionText;
import com.nosliw.data.core.domain.entity.expression.script.HAPUtilityExpressionDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainUIContent extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainUIContent(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, HAPDefinitionEntityComplexUIContent.class, runtimeEnv);
	}

	public static final String SCRIPT = "script";

	private HAPGeneratorId m_idGenerator = new HAPGeneratorId();
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		parseUIDefinitionUnit((Element)obj, entityId, parserContext);
	}
	
	@Override
	public boolean isComplexEntity() {		return true;	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPDefinitionEntityComplexUIContent uiContentEntity = (HAPDefinitionEntityComplexUIContent)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();

		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_NORMALTAGEVENT, new ArrayList<HAPElementEvent>());
		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_CUSTOMTAGEVENT, new ArrayList<HAPElementEvent>());
		
		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_SCRIPTEXPRESSIONINCONTENT, new ArrayList<HAPDefinitionUIEmbededScriptExpressionInContent>());
		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_SCRIPTEXPRESSIONINATTRIBUTE, new ArrayList<HAPDefinitionUIEmbededScriptExpressionInAttribute>());
		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_SCRIPTEXPRESSIONINTAGATTRIBUTE, new ArrayList<HAPDefinitionUIEmbededScriptExpressionInAttribute>());

		uiContentEntity.setAttributeValueObject(HAPDefinitionEntityComplexUIContent.ATTR_ATTRIBUTE, new LinkedHashMap<String, String>());
		
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPDefinitionEntityComplexWithDataExpressionGroup.ATTR_DATAEEXPRESSIONGROUP, parserContext, getRuntimeEnvironment());
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityComplexUIContent.ATTR_SCRIPTEEXPRESSIONGROUP, parserContext, getRuntimeEnvironment());
	}
	
	private void parseUIDefinitionUnit(Element wrapperEle, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext){
		
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		
		//parse value context
		parseValueContext(wrapperEle, uiContentId, parserContext);
		
		//parse value context
		parseAttachment(wrapperEle, uiContentId, parserContext);
		
		//process script block
		this.parseUnitScriptBlocks(wrapperEle, this.getUIContentEntityById(uiContentId, parserContext));

		//process contents within customer ele
		parseDescendantTags(wrapperEle, uiContentId, parserContext);
		
		//parse script in content
		parseChildScriptExpressionInContent(wrapperEle, uiContentId, parserContext);
		
		HAPUtilityUIResourceParser.addSpanToText(wrapperEle);
		
		uiContent.setContent(wrapperEle.html());
	}

	/*
	 * process all the descendant tags under element
	 */
	private void parseDescendantTags(Element ele, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext){
		List<Element> removes = new ArrayList<Element>();
		Elements eles = ele.children();
		for(Element e : eles){
			if(HAPUtilityBasic.isStringEmpty(HAPUtilityUIResourceParser.getUIIdInElement(e))){
				//if tag have no ui id, then create ui id for it
				String id = this.m_idGenerator.generateId();
				e.attr(HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID, id);
			}
			
			boolean ifRemove = parseTag(e, uiContentId, parserContext);
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
	private boolean parseTag(Element ele, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		if(customTagName!=null){
			//process custome tag
			String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele);
			if(customTagName.equals("style")) {
				parseStyle(ele, uiContentId, uiId, parserContext);
			}
			else {
				parseKeyAttributeOnTag(ele, uiContentId, true, parserContext);
				parseScriptExpressionInTagAttribute(ele, uiContentId, true, parserContext);
				
				HAPIdEntityInDomain tagEntityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, ele, parserContext);
				HAPDefinitionEntityComplexUITag uiTag = (HAPDefinitionEntityComplexUITag)parserContext.getGlobalDomain().getEntityInfoDefinition(tagEntityId).getEntity();
				uiTag.setUIId(uiId);
				uiContent.addCustomTag(tagEntityId, uiTag.getChildRelationConfigure());
			}
			return false;
		}
		else{
			//process regular tag
			parseChildScriptExpressionInContent(ele, uiContentId, parserContext);
			//process key attribute
			parseKeyAttributeOnTag(ele, uiContentId, false, parserContext);
			//process elements's attribute that have expression value 
			parseScriptExpressionInTagAttribute(ele, uiContentId, false, parserContext);
			//process all descendant tags under this elment
			parseDescendantTags(ele, uiContentId, parserContext);
			return false;
		}
	}

	/*
	 * process expression in child text content within element 
	 */
	private void parseChildScriptExpressionInContent(Element ele, HAPIdEntityInDomain uiContentId, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		HAPDefinitionEntityExpressionScriptGroup scriptEntityGroupEntity = uiContent.getScriptExpressionGroupEntity(parserContext);

		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			
			StringBuffer newText = new StringBuffer();
			HAPDefinitionExpression expressionDef = HAPUtilityExpressionDefinition.parseDefinitionExpression(text, null, uiContent.getDataExpressionGroupEntity(parserContext), this.getRuntimeEnvironment().getDataExpressionParser());
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
					uiContent.addScriptExpressionInContent(expressionContent);
				}
			}
			
			textNode.after(newText.toString());
			textNode.remove();
		}
	}
	
	/*
	 * process element's attribute that have script expression value
	 */
	private void parseScriptExpressionInTagAttribute(Element ele, HAPIdEntityInDomain uiContentId, boolean isCustomerTag, HAPContextParser parserContext){
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		HAPDefinitionEntityExpressionScriptGroup scriptEntityGroupEntity = uiContent.getScriptExpressionGroupEntity(parserContext);
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		
		//read attributes
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			String eleAttrKey = eleAttr.getKey();
			//replace express attribute value with; create ExpressEle object
			String attrValue = eleAttr.getValue(); 
			if(!HAPUtilityExpressionDefinition.isText(attrValue)) {

				HAPDefinitionExpression expressionDef = HAPUtilityExpressionDefinition.parseDefinitionExpression(attrValue, null, uiContent.getDataExpressionGroupEntity(parserContext), this.getRuntimeEnvironment().getDataExpressionParser());
				String scriptExpressionId = scriptEntityGroupEntity.addExpression(expressionDef);
				
				HAPDefinitionUIEmbededScriptExpressionInAttribute eAttr = new HAPDefinitionUIEmbededScriptExpressionInAttribute(eleAttrKey, uiId, scriptExpressionId);
				if(isCustomerTag)  uiContent.addScriptExpressionInCustomTagAttribute(eAttr);
				else  uiContent.addScriptExpressionInNormalTagAttribute(eAttr);
				ele.attr(eleAttrKey, "");
			}
		}
	}
	
	/*
	 * process key attribute within element 
	 * key attribute means attribute that have predefined meaning within ui resource
	 * isCustomertag : whether this element is a customer tag
	 */
	private void parseKeyAttributeOnTag(Element ele, HAPIdEntityInDomain uiContentId, boolean isCustomerTag, HAPContextParser parserContext){
		
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		
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
							uiContent.addCustomTagEvent(tagEvent);
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

	
	private void parseValueContext(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		List<Element> valueContextEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithValueContext.VALUECONTEXT);
		for(Element valueContextEle : valueContextEles){
			this.parseSimpleEntityAttributeSelf(new JSONObject(valueContextEle.html()), parentEntityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
			break;
		}
		for(Element valueContextEle : valueContextEles)  valueContextEle.remove();
	}
	
	private void parseAttachment(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		List<Element> attachmentEles = HAPUtilityUIResourceParser.getChildElementsByTag(ele, HAPWithAttachment.ATTACHMENT);
		for(Element attachmentEle : attachmentEles){
			this.parseSimpleEntityAttributeSelf(new JSONObject(attachmentEle.html()), parentEntityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
			break;
		}
		for(Element attachmentEle : attachmentEles)  attachmentEle.remove();
	}
	
	/*
	 * process all script blocks under unit
	 */
	private void parseUnitScriptBlocks(Element ele, HAPDefinitionEntityComplexUIContent resource){
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
	private void parseStyle(Element ele, HAPIdEntityInDomain uiContentId, String uiId, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUIContent uiContent = this.getUIContentEntityById(uiContentId, parserContext);
		HAPDefinitionStyle style = new HAPDefinitionStyle(uiId);
		List<TextNode> textNodes = ele.textNodes();
		for(TextNode textNode : textNodes){
			String text = textNode.text();
			style.setDefinition(text);
			break;
		}
		ele.remove();
		uiContent.setStyle(style);
	}

	private HAPDefinitionEntityComplexUIContent getUIContentEntityById(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityComplexUIContent)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
	}
	
	private String generateId() {     return this.m_idGenerator.generateId();      }
}
