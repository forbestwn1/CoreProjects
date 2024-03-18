package com.nosliw.core.application.division.manual.brick.test.complex.script;

import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpTestComplexScript extends HAPPluginParserBrickImpComplex{

	public HAPPluginParserEntityImpTestComplexScript(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, HAPDefinitionEntityTestComplexScript.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualBrick entityDefinition, JSONObject jsonObj, HAPManualContextParse parseContext) {
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
