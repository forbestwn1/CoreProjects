package com.nosliw.ui.entity.uicontent;

import org.jsoup.nodes.Element;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.xxx.application1.division.manual.HAPManualBrick;
import com.nosliw.core.xxx.application1.division.manual.executable.HAPInfoBrickType;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocalComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoParentComplex;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainWithUIContent extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainWithUIContent(String entityType,
			Class<? extends HAPManualBrick> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	protected HAPIdEntityInDomain parseUIContent(Element ele, HAPIdEntityInDomain parentEntityId, HAPContextParser parserContext) {
		HAPManualBrick entity = parserContext.getCurrentDomain().getEntityInfoDefinition(parentEntityId).getEntity();
		HAPIdEntityInDomain contentEntityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, ele, HAPSerializationFormat.HTML, parserContext);
		entity.setAttribute(HAPExecutableEntityComplexWithUIContent.UICONTENT, new HAPEmbededDefinition(contentEntityId), new HAPInfoBrickType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, true));

		//parent relation
		HAPInfoParentComplex parentInfo = new HAPInfoParentComplex();
		parentInfo.setParentId(parentEntityId);
		((HAPDomainEntityDefinitionLocalComplex)parserContext.getCurrentDomain()).buildComplexParentRelation(contentEntityId, parentInfo);
		
		return contentEntityId;
	}
}
