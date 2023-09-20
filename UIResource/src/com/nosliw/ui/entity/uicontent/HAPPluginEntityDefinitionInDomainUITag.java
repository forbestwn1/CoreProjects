package com.nosliw.ui.entity.uicontent;

import java.util.LinkedHashMap;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceDefinition;
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
		HAPDefinitionEntityComplexUITag uiTagEntity = (HAPDefinitionEntityComplexUITag)parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();

		uiTagEntity.setAttributeValueObject(HAPExecutableEntityComplexUITag.ATTRIBUTE, new LinkedHashMap<String, String>());
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUITag uiTagEntity = (HAPDefinitionEntityComplexUITag)this.getEntity(entityId, parserContext);
		
		HAPManagerUITag uiTagMan = ((HAPRuntimeEnvironmentImpBrowser)this.getRuntimeEnvironment()).getUIResourceManager().getUITagManager();
		Element ele = (Element)obj;
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		uiTagEntity.setTagName(customTagName);

		HAPResourceDefinition tagDefResourceDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, customTagName), parserContext.getGlobalDomain());
		HAPDefinitionEntityUITagDefinition uiTagDefEntity = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(tagDefResourceDef.getEntityId()).getEntity();
		uiTagEntity.setValueContextEntityId(uiTagDefEntity.getValueContextEntityId());
		uiTagEntity.setParentRelationConfigure(uiTagDefEntity.getParentRelationConfigure());
		uiTagEntity.setChildRelationConfigure(uiTagDefEntity.getChildRelationConfigure());
		
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

}
