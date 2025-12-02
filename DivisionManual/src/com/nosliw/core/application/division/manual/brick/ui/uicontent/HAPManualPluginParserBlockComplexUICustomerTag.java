package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPWithUIContent;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPInfoStructureInWrapper;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.common.structure.HAPValueStructureImp;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinitionImp;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.entity.uitag.HAPManagerUITag;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinition;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttribute;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttributeVariable;
import com.nosliw.core.application.valueport.HAPReferenceElement;

public class HAPManualPluginParserBlockComplexUICustomerTag extends HAPManualDefinitionPluginParserBrickImpComplex{

	private HAPManagerUITag m_uiTagMan;
	
	public HAPManualPluginParserBlockComplexUICustomerTag(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerUITag uiTagMan) {
		super(HAPEnumBrickType.UICUSTOMERTAG_100, HAPManualDefinitionBlockComplexUICustomerTag.class, manualDivisionEntityMan, brickMan);
		this.m_uiTagMan = uiTagMan;
	}

	@Override
	protected void parseDefinitionContentHtml(HAPManualDefinitionBrick brickManualDef, Object obj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUICustomerTag uiCustomerTag = (HAPManualDefinitionBlockComplexUICustomerTag)brickManualDef;
		
		Element ele = (Element)obj;
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		uiCustomerTag.setTagId(customTagName);

		HAPUITagDefinition uiTagDef = this.m_uiTagMan.getUITagDefinition(customTagName, null);

		//tag definition
		uiCustomerTag.setUITagDefinition(uiTagDef);

		//attribute definition
		Map<String, HAPUITagDefinitionAttribute> attrDefs = uiTagDef.getAttributeDefinition();
		for(String attrName : attrDefs.keySet()) {
			uiCustomerTag.addTagAttributeDefinition(attrDefs.get(attrName));
		}
		
		//parse customer tag attribute 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			if(uiTagDef.getAttributeDefinition().get(eleAttr.getKey())!=null) {
				uiCustomerTag.addTagAttribute(eleAttr.getKey(), eleAttr.getValue());
			}
		}

		//build value context from ui tag definition
		HAPManualUtilityValueContext.buildValueContextBrickFromValueContext(uiCustomerTag.getValueContextBrick(), uiTagDef.getValueContext(), getManualDivisionBrickManager());
		
		//build value structure for variable from attribute
		Map<String, HAPValueStructure> attrValueStructures = new LinkedHashMap<String, HAPValueStructure>();
		Map<String, HAPUITagDefinitionAttribute> attrs = uiCustomerTag.getTagAttributeDefinitions();
		for(String attrName : attrs.keySet()) {
			HAPUITagDefinitionAttribute attr = attrs.get(attrName);
			if(HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE.equals(attr.getType())) {
				String attrValue = uiCustomerTag.getTagAttributes().get(attrName);
				if(attrValue!=null) {
					HAPUITagDefinitionAttributeVariable varAttr = (HAPUITagDefinitionAttributeVariable)attr;
					
					HAPElementStructureLeafRelativeForValue attrEle = new HAPElementStructureLeafRelativeForValue();
					HAPElementStructureLeafData d = new HAPElementStructureLeafData();
					d.setDataDefinition(varAttr.getDataDefinition());
					attrEle.setDefinition(d);
					attrEle.setReference(new HAPReferenceElement(attrValue));
					
					HAPRootInStructure rootEle = new HAPRootInStructure();
					rootEle.setName(attrName);
					rootEle.setDefinition(attrEle);
					
					
					HAPValueStructure valueStructure = attrValueStructures.get(varAttr.getScope());
					if(valueStructure==null) {
						valueStructure = new HAPValueStructureImp();
						attrValueStructures.put(varAttr.getScope(), valueStructure);
					}
					valueStructure.addRoot(rootEle);
				}
			}
		}
		
		for(String scope : attrValueStructures.keySet()) {
			HAPWrapperValueStructureDefinitionImp vsWrapper = new HAPWrapperValueStructureDefinitionImp(attrValueStructures.get(scope));
			HAPInfoStructureInWrapper wrapperInfo = new HAPInfoStructureInWrapper();
			wrapperInfo.setScope(scope);
			vsWrapper.setStructureInfo(wrapperInfo);
			HAPManualUtilityValueContext.addValueStuctureWrapperToValueContextBrick(vsWrapper, uiCustomerTag.getValueContextBrick(), getManualDivisionBrickManager());
		}
		
		//base
		uiCustomerTag.setBase(uiTagDef.getBase());
		
		//script
		uiCustomerTag.setScriptResourceId(uiTagDef.getScriptResourceId());
		
		//parent relation
		for(HAPManualDefinitionBrickRelation parentRelation : uiTagDef.getParentRelations()) {
			uiCustomerTag.addParentRelation(parentRelation);
		}
		
		//embeded content
		this.parseBrickAttribute(uiCustomerTag, ele, HAPWithUIContent.UICONTENT, HAPEnumBrickType.UICONTENT_100, null, HAPSerializationFormat.HTML, parseContext);
		//enhance value context in embeded content
		HAPManualUtilityValueContext.buildValueContextBrickFromValueContext(uiCustomerTag.getUIContent().getValueContextBrick(), uiTagDef.getValueContextEmbeded(), getManualDivisionBrickManager());
		//enhance constant in embeded content
		for(HAPDefinitionConstant constant : HAPManualUtilityUITag.buildConstantDefinitions(uiCustomerTag.getUITagDefinition(), uiCustomerTag.getTagAttributes()).values()) {
			uiCustomerTag.getUIContent().addConstantFromParent(constant);
		}
		
		//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		ele.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.remove();
		

		
		

		
		
/*		
		
		HAPManagerUITag uiTagMan = ((HAPRuntimeEnvironmentImpBrowser)this.getRuntimeEnvironment()).getUIResourceManager().getUITagManager();
		HAPIdEntityInDomain uiTagDefEntityId = uiTagMan.getUITagDefinition(customTagName, parserContext.getGlobalDomain());
		HAPDefinitionEntityUITagDefinition uiTagDefEntity = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(uiTagDefEntityId).getEntity();

		if(uiTagDefEntity.getScriptResourceId()!=null) {
			uiTagEntity.setScriptResourceId(uiTagDefEntity.getScriptResourceId());
		} else {
			uiTagEntity.setScriptResourceId(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, customTagName));
		}

		uiTagEntity.setAttributeDefinition(uiTagDefEntity.getAttributeDefinition());
		uiTagEntity.setBaseName(uiTagDefEntity.getBaseName());
		uiTagEntity.setParentRelationConfigure(uiTagDefEntity.getParentRelationConfigure());
		uiTagEntity.setChildRelationConfigure(uiTagDefEntity.getChildRelationConfigure());

		//clone value context
		HAPManagerDomainEntityDefinition domainEntityDefMan = this.getRuntimeEnvironment().getDomainEntityDefinitionManager();

		HAPInfoEntityInDomainDefinition valueContextEntityInfo1 = parserContext.getGlobalDomain().getEntityInfoDefinition(uiTagDefEntity.getValueContextEntityId());
		HAPManualDefinitionBrickValueContext valueContextEntity1 = (HAPManualDefinitionBrickValueContext)valueContextEntityInfo1.getEntity();
		
		HAPIdEntityInDomain valueContextEntityId = domainEntityDefMan.newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, parserContext);
		HAPManualDefinitionBrickValueContext valueContextEntity = (HAPManualDefinitionBrickValueContext)parserContext.getGlobalDomain().getEntityDefinition(valueContextEntityId);

		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper1 : valueContextEntity1.getValueStructures()) {

			HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper = new HAPManualDefinitionBrickWrapperValueStructure();
			valueStructureWrapper.setName(valueStructureWrapper1.getName());
			valueStructureWrapper.setGroupType(valueStructureWrapper1.getGroupType());

			HAPInfoEntityInDomainDefinition valueStructureEntityInfo1 = parserContext.getGlobalDomain().getEntityInfoDefinition(valueStructureWrapper1.getValueStructureId());
			HAPManualDefinitionBrickValueStructure valueStructure1 = (HAPManualDefinitionBrickValueStructure)valueStructureEntityInfo1.getEntity();

			HAPIdEntityInDomain valueStructureEntityId = domainEntityDefMan.newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext);
			HAPManualDefinitionBrickValueStructure valueStructure = (HAPManualDefinitionBrickValueStructure)parserContext.getGlobalDomain().getEntityDefinition(valueStructureEntityId);
			
			for(String rootName : valueStructure1.getRootNames()) {
				valueStructure.addRoot(valueStructure1.getRootByName(rootName).cloneRoot());
			}
			valueStructureWrapper.setValueStructureId(valueStructureEntityId);
			
			valueContextEntity.addValueStructure(valueStructureWrapper);
		}
		uiTagEntity.setValueContextEntityId(valueContextEntityId);
		
		
		HAPIdEntityInDomain uiContentId = this.parseUIContent(ele, entityId, parserContext);
		HAPUtilityEntityDefinition.buildParentRelation(uiContentId, entityId, uiTagEntity.getChildRelationConfigure(), parserContext);
*/
		
	}

}
