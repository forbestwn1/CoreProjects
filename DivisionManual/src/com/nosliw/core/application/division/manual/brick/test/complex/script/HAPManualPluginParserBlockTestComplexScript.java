package com.nosliw.core.application.division.manual.brick.test.complex.script;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPTestTaskTrigguer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTestComplexScript extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTestComplexScript(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, HAPManualDefinitionBlockTestComplexScript.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTestComplexScript scriptEntity = (HAPManualDefinitionBlockTestComplexScript)entityDefinition;
		//script
		Object scriptObj = jsonObj.opt(HAPBlockTestComplexScript.SCRIPT);
		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
		scriptEntity.setScript(scriptResourceId);
		
//		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
//		HAPUtilityEntityDefinition.setEntitySimpleAttributeWithId(entity, HAPManualDefinitionBlockTestComplexTask.ATTR_SCRIPT, scriptResoureDef.getEntityId(), this.getRuntimeEnvironment().getDomainEntityManager());
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPBlockTestComplexScript.PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				scriptEntity.setParm(parmName, parms.opt(parmName));
			}
		}
		
		//event
		JSONArray eventArray = jsonObj.optJSONArray(HAPBlockTestComplexScript.TASKTRIGGUER);
		if(eventArray!=null) {
			for(int i=0; i<eventArray.length(); i++) {
				HAPTestTaskTrigguer eventTest = new HAPTestTaskTrigguer();
				eventTest.buildObject(eventArray.getJSONObject(i), HAPSerializationFormat.JSON);
				scriptEntity.getTaskTrigguers().add(eventTest);
			}
		}
	}

}
