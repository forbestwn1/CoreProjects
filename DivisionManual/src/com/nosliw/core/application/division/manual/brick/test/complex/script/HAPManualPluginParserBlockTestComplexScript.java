package com.nosliw.core.application.division.manual.brick.test.complex.script;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.brick.test.complex.script.HAPTestTaskTrigguer;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualPluginParserBlockTestComplexScript extends HAPManualDefinitionPluginParserBrickImpComplex{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserBlockTestComplexScript(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, HAPManualDefinitionBlockTestComplexScript.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTestComplexScript scriptEntity = (HAPManualDefinitionBlockTestComplexScript)entityDefinition;
		//script
		Object scriptObj = jsonObj.opt(HAPBlockTestComplexScript.SCRIPT);
		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
		scriptEntity.setScript(scriptResourceId);
		
//		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
//		HAPUtilityEntityDefinition.setEntitySimpleAttributeWithId(entity, HAPManualDefinitionBlockTestComplexTaskScript.ATTR_SCRIPT, scriptResoureDef.getEntityId(), this.getRuntimeEnvironment().getDomainEntityManager());
		
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
				HAPTestTaskTrigguer eventTest = HAPTestTaskTrigguer.parsTestTaskTrigguer(eventArray.getJSONObject(i), m_dataRuleMan);
				scriptEntity.getTaskTrigguers().add(eventTest);
			}
		}
	}

}
