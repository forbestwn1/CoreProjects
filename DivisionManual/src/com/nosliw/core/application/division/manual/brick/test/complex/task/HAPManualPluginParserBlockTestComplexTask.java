package com.nosliw.core.application.division.manual.brick.test.complex.task;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTestComplexTask extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTestComplexTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100, HAPManualDefinitionBlockTestComplexTask.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTestComplexTask scriptEntity = (HAPManualDefinitionBlockTestComplexTask)entityDefinition;
		//script
		Object scriptObj = jsonObj.opt(HAPBlockTestComplexScript.SCRIPT);
		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
		scriptEntity.setScript(scriptResourceId);
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPBlockTestComplexScript.PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				scriptEntity.setParm(parmName, parms.opt(parmName));
			}
		}
		
	}

}
