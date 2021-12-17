package com.nosliw.data.core.expression.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPUtilityComplexParser;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;

public class HAPPluginEntityDefinitionInDomainExpressionSuite extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainExpressionSuite() {
		super(HAPDefinitionExpressionSuite.class);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,	HAPContextParser parserContext) {
		HAPDefinitionExpressionSuite entity = (HAPDefinitionExpressionSuite)this.getEntity(entityId, parserContext);
		
		//parse container
		HAPUtilityComplexParser.parseContentInComplexContainerEntity(entity, jsonObj, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, parserContext);
	}

}
