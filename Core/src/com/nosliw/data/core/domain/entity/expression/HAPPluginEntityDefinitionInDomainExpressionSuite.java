package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionSuite extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionSuite(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE, HAPDefinitionEntityExpressionSuite.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainEntityDefinitionLocal definitionDomain) {
		HAPDefinitionEntityExpressionSuite suiteEntity = (HAPDefinitionEntityExpressionSuite)definitionDomain.getEntityInfoDefinition(entityId).getEntity();
		
		//parse element
		this.parseContainerComplexAttribute(jsonObj, entityId, HAPDefinitionEntityExpressionSuite.GROUP, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, null, HAPConstantShared.ENTITYCONTAINER_TYPE_SET, HAPUtilityDomain.createDefaultParentRelationConfigure(), definitionDomain);
		
//		JSONObject groupObj = jsonObj.getJSONObject(HAPDefinitionEntityExpressionSuite.GROUP);
//		List<HAPInfoContainerElement> eles = HAPUtilityParserEntity.parseComplexContainer(
//				groupObj, 
//				HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, 
//				HAPConstantShared.ENTITYCONTAINER_TYPE_SET, 
//				entityId,
//				HAPUtilityDomain.createDefaultParentRelationConfigure(),
//				HAPUtilityDomain.getContextParse(entityId, definitionDomain),
//				this.getRuntimeEnvironment().getDomainEntityManager());
//		
//		for(HAPInfoContainerElement groupInfo : eles) {
//			suiteEntity.addExpressionGroup((HAPInfoContainerElementSet)groupInfo);
//		}
	}

}
