package com.nosliw.data.core.entity.division.manual.test.complex.script;

import org.json.JSONObject;

import com.nosliw.data.core.entity.HAPEnumEntityType;
import com.nosliw.data.core.entity.division.manual.HAPContextParse;
import com.nosliw.data.core.entity.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.entity.division.manual.HAPPluginParserEntityImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpTestComplexScript extends HAPPluginParserEntityImpComplex{

	public HAPPluginParserEntityImpTestComplexScript(HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100, HAPDefinitionEntityTestComplexScript.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualEntity entityDefinition, JSONObject jsonObj, HAPContextParse parseContext) {
		HAPDefinitionEntityTestComplexScript scriptEntity = (HAPDefinitionEntityTestComplexScript)entityDefinition;
		//script
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityTestComplexScript.ATTR_SCRIPTNAME);
		scriptEntity.setScriptName(scriptName);
		
//		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
//		HAPUtilityEntityDefinition.setEntitySimpleAttributeWithId(entity, HAPDefinitionEntityTestComplexScript.ATTR_SCRIPT, scriptResoureDef.getEntityId(), this.getRuntimeEnvironment().getDomainEntityManager());
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPDefinitionEntityTestComplexScript.ATTR_PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				scriptEntity.setParm(parmName, parms.opt(parmName));
			}
		}
	}

}
