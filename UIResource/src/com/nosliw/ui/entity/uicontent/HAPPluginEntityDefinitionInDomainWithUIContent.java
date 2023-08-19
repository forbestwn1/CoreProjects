package com.nosliw.ui.entity.uicontent;

import org.jsoup.nodes.Element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainWithUIContent extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainWithUIContent(String entityType,
			Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
	}

	protected HAPIdEntityInDomain parseUIContent(Element ele, HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return this.parseComplexEntityAttributeSelf(ele, entityId, HAPDefinitionEntityComplexWithUIContent.ATTR_CONTENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, null, null, parserContext);
	}
	
	@Override
	public boolean isComplexEntity() {   return true;  }

}
