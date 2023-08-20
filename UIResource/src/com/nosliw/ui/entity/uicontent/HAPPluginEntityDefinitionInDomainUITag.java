package com.nosliw.ui.entity.uicontent;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.ui.entity.uitag.HAPDefinitionEntityUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPPluginEntityDefinitionInDomainUITag extends HAPPluginEntityDefinitionInDomainWithUIContent{

	public HAPPluginEntityDefinitionInDomainUITag(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPDefinitionEntityComplexUITag.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityComplexUITag uiTagEntity = (HAPDefinitionEntityComplexUITag)this.getEntity(entityId, parserContext);
		
		HAPManagerUITag uiTagMan = ((HAPRuntimeEnvironmentImpBrowser)this.getRuntimeEnvironment()).getUIResourceManager().getUITagManager();
		Element ele = (Element)obj;
		String customTagName = HAPUtilityUIResourceParser.isCustomTag(ele);
		uiTagEntity.setTagName(customTagName);

		
		Attributes eleAttrs = ele.attributes();
		for(Attribute eleAttr : eleAttrs){
			uiTagEntity.addAttribute(eleAttr.getKey(), eleAttr.getValue());
		}

		HAPResourceDefinition tagDefResourceDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, customTagName), parserContext.getGlobalDomain());
		HAPDefinitionEntityUITagDefinition uiTagDefEntity = (HAPDefinitionEntityUITagDefinition)parserContext.getGlobalDomain().getEntityInfoDefinition(tagDefResourceDef.getEntityId()).getEntity();
		uiTagEntity.setValueContextEntity(uiTagDefEntity.getValueContextEntityId());
		uiTagEntity.setParentRelationConfigure(uiTagDefEntity.getParentRelationConfigure());
		uiTagEntity.setChildRelationConfigure(uiTagDefEntity.getChildRelationConfigure());
		
		HAPIdEntityInDomain uiContentId = this.parseUIContent(ele, entityId, parserContext);
		HAPUtilityEntityDefinition.buildParentRelation(uiContentId, entityId, uiTagEntity.getChildRelationConfigure(), parserContext);
		
	}

}
