package com.nosliw.data.core.domain.entity.expression;

import java.util.List;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoContainerElement;
import com.nosliw.data.core.domain.HAPInfoContainerElementSet;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionSuite extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionSuite(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityExpressionSuite.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityExpressionSuite suiteEntity = (HAPDefinitionEntityExpressionSuite)definitionDomain.getEntityInfo(entityId).getEntity();
		
		//parse element
		JSONObject groupObj = jsonObj.getJSONObject(HAPDefinitionEntityExpressionSuite.GROUP);
		List<HAPInfoContainerElement> eles = HAPUtilityParserEntity.parseComplexContainer(
				groupObj, 
				HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, 
				HAPConstantShared.ENTITYCONTAINER_TYPE_SET, 
				entityId,
				HAPUtilityDomain.createDefaultParentRelationConfigure(),
				HAPUtilityDomain.getContextParse(entityId, definitionDomain),
				this.getRuntimeEnvironment().getDomainEntityManager());
		
		for(HAPInfoContainerElement groupInfo : eles) {
			suiteEntity.addExpressionGroup((HAPInfoContainerElementSet)groupInfo);
		}
	}

}
