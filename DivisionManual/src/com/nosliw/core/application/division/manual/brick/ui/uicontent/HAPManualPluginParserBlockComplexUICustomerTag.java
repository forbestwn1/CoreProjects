package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.Map;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.parentrelation.HAPManualDefinitionBrickRelation;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.core.application.uitag.HAPUITagValueContextDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockComplexUICustomerTag extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockComplexUICustomerTag(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.UICUSTOMERTAG_100, HAPManualDefinitionBlockComplexUICustomerTag.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentHtml(HAPManualDefinitionBrick brickManualDef, Object obj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUICustomerTag uiCustomerTag = (HAPManualDefinitionBlockComplexUICustomerTag)brickManualDef;
		
		Element ele = (Element)obj;
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		uiCustomerTag.setTagId(customTagName);

		HAPUITagDefinition uiTagDef = this.getRuntimeEnvironment().getUITagManager().getUITagDefinition(customTagName, null);
		HAPUITagValueContextDefinition uiTagDefValueContext = uiTagDef.getValueContext();

		//build value context
		HAPManualDefinitionBrickValueContext valueContextBrick = (HAPManualDefinitionBrickValueContext)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUECONTEXT_100);
		for(HAPWrapperValueStructure uiTagDefValueStructure : uiTagDefValueContext.getValueStructures()) {
			HAPManualDefinitionBrickWrapperValueStructure manualWrapperBrickValueStrucutre = (HAPManualDefinitionBrickWrapperValueStructure)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);

			HAPManualDefinitionBrickValueStructure manualBrickValueStrucutre = (HAPManualDefinitionBrickValueStructure)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTURE_100);
			manualBrickValueStrucutre.setValue(uiTagDefValueStructure.getValueStructure());
			manualWrapperBrickValueStrucutre.setValueStructure(manualBrickValueStrucutre);
			
			manualWrapperBrickValueStrucutre.setGroupType(uiTagDefValueStructure.getGroupType());
			manualWrapperBrickValueStrucutre.setInfo(uiTagDefValueStructure.getInfo());
			manualWrapperBrickValueStrucutre.setName(uiTagDefValueStructure.getName());
		
			valueContextBrick.addValueStructure(manualWrapperBrickValueStrucutre);
		}
		uiCustomerTag.setValueContextBrick(valueContextBrick);
		
		uiCustomerTag.setBase(uiTagDef.getBase());
		
		uiCustomerTag.setScriptResourceId(uiTagDef.getScriptResourceId());
		
		for(HAPManualDefinitionBrickRelation parentRelation : uiTagDef.getParentRelations()) {
			uiCustomerTag.addParentRelation(parentRelation);
		}
		
		Map<String, HAPUITagAttributeDefinition> attrDefs = uiTagDef.getAttributeDefinition();
		for(String attrName : attrDefs.keySet()) {
			uiCustomerTag.addTagAttributeDefinition(attrDefs.get(attrName));
		}
		
		//parse customer tag attribute 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiCustomerTag.addTagAttribute(eleAttr.getKey(), eleAttr.getValue());
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
