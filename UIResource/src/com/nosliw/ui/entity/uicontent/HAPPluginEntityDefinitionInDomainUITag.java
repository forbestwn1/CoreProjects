package com.nosliw.ui.entity.uicontent;

import java.util.LinkedHashMap;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.ui.entity.uitag.HAPDefinitionEntityUITagDefinition;
import com.nosliw.ui.tag.HAPManagerUITag;

public class HAPPluginEntityDefinitionInDomainUITag extends HAPPluginEntityDefinitionInDomainWithUIContent{

	public HAPPluginEntityDefinitionInDomainUITag(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPDefinitionEntityComplexUITag.class, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		HAPDefinitionEntityComplexUITag uiTagEntity = this.getUITagEntity(entityId, parserContext);
		uiTagEntity.setAttributeValueObject(HAPExecutableEntityComplexUITag.ATTRIBUTE, new LinkedHashMap<String, String>());
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUITag uiTagEntity = (HAPDefinitionEntityComplexUITag)this.getEntity(entityId, parserContext);
		
		Element ele = (Element)obj;
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		uiTagEntity.setTagId(customTagName);

		HAPManagerUITag uiTagMan = ((HAPRuntimeEnvironmentImpBrowser)this.getRuntimeEnvironment()).getUIResourceManager().getUITagManager();
		HAPIdEntityInDomain uiTagDefEntityId = uiTagMan.getUITagDefinition(customTagName, parserContext.getGlobalDomain());
		HAPDefinitionEntityUITagDefinition uiTagDefEntity = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(uiTagDefEntityId).getEntity();

		if(uiTagDefEntity.getScriptResourceId()!=null)   uiTagEntity.setScriptResourceId(uiTagDefEntity.getScriptResourceId());
		else  uiTagEntity.setScriptResourceId(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, customTagName));

		uiTagEntity.setAttributeDefinition(uiTagDefEntity.getAttributeDefinition());
		uiTagEntity.setBaseName(uiTagDefEntity.getBaseName());
		uiTagEntity.setParentRelationConfigure(uiTagDefEntity.getParentRelationConfigure());
		uiTagEntity.setChildRelationConfigure(uiTagDefEntity.getChildRelationConfigure());

		//clone value context
		HAPManagerDomainEntityDefinition domainEntityDefMan = this.getRuntimeEnvironment().getDomainEntityDefinitionManager();

		HAPInfoEntityInDomainDefinition valueContextEntityInfo1 = parserContext.getGlobalDomain().getEntityInfoDefinition(uiTagDefEntity.getValueContextEntityId());
		HAPDefinitionEntityValueContext valueContextEntity1 = (HAPDefinitionEntityValueContext)valueContextEntityInfo1.getEntity();
		
		HAPIdEntityInDomain valueContextEntityId = domainEntityDefMan.newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, parserContext);
		HAPDefinitionEntityValueContext valueContextEntity = (HAPDefinitionEntityValueContext)parserContext.getGlobalDomain().getEntityDefinition(valueContextEntityId);

		for(HAPDefinitionWrapperValueStructure valueStructureWrapper1 : valueContextEntity1.getValueStructures()) {

			HAPDefinitionWrapperValueStructure valueStructureWrapper = new HAPDefinitionWrapperValueStructure();
			valueStructureWrapper.setName(valueStructureWrapper1.getName());
			valueStructureWrapper.setGroupType(valueStructureWrapper1.getGroupType());

			HAPInfoEntityInDomainDefinition valueStructureEntityInfo1 = parserContext.getGlobalDomain().getEntityInfoDefinition(valueStructureWrapper1.getValueStructureId());
			HAPDefinitionEntityValueStructure valueStructure1 = (HAPDefinitionEntityValueStructure)valueStructureEntityInfo1.getEntity();

			HAPIdEntityInDomain valueStructureEntityId = domainEntityDefMan.newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, parserContext);
			HAPDefinitionEntityValueStructure valueStructure = (HAPDefinitionEntityValueStructure)parserContext.getGlobalDomain().getEntityDefinition(valueStructureEntityId);
			
			for(String rootName : valueStructure1.getRootNames()) {
				valueStructure.addRoot(valueStructure1.getRootByName(rootName).cloneRoot());
			}
			valueStructureWrapper.setValueStructureId(valueStructureEntityId);
			
			valueContextEntity.addValueStructure(valueStructureWrapper);
		}
		uiTagEntity.setValueContextEntityId(valueContextEntityId);
		
		
		HAPIdEntityInDomain uiContentId = this.parseUIContent(ele, entityId, parserContext);
		HAPUtilityEntityDefinition.buildParentRelation(uiContentId, entityId, uiTagEntity.getChildRelationConfigure(), parserContext);

		//parse 
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiTagEntity.addTagAttribute(eleAttr.getKey(), eleAttr.getValue());
		}

		//add placeholder element to the customer tag's postion and then remove the original tag from html structure 
		String uiId = HAPUtilityUIResourceParser.getUIIdInElement(ele); 
		ele.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_END_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.after("<"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+" style=\"display:none;\" "+HAPConstantShared.UIRESOURCE_ATTRIBUTE_UIID+"="+ uiId +HAPConstantShared.UIRESOURCE_CUSTOMTAG_WRAPER_START_POSTFIX+"></"+HAPConstantShared.UIRESOURCE_TAG_PLACEHOLDER+">");
		ele.remove();
	}
	
	private HAPDefinitionEntityComplexUITag getUITagEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityComplexUITag)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
	}
}
