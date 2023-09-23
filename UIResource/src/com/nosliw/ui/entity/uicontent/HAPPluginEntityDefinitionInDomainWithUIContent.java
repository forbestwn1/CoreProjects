package com.nosliw.ui.entity.uicontent;

import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainWithUIContent extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainWithUIContent(String entityType,
			Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	@Override
	protected void parseValueContextAttribute(HAPIdEntityInDomain entityId, Object obj,	HAPContextParser parserContext) {
	}

	@Override
	protected void parseAttachmentAttribute(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
	}

	protected HAPIdEntityInDomain parseUIContent(Element ele, HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return this.parseComplexEntityAttributeSelf(ele, entityId, HAPExecutableEntityComplexWithUIContent.UICONTENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICONTENT, null, null, parserContext);
	}
	
}
