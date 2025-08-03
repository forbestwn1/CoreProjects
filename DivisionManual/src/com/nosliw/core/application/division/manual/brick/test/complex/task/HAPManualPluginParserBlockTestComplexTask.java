package com.nosliw.core.application.division.manual.brick.test.complex.task;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.xxx.application.valueport.HAPReferenceElement;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTestComplexTask extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockTestComplexTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100, HAPManualDefinitionBlockTestComplexTask.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockTestComplexTask taskEntity = (HAPManualDefinitionBlockTestComplexTask)entityDefinition;
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPBlockTestComplexTask.PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				taskEntity.setParm(parmName, parms.opt(parmName));
			}
		}

		//variables
		JSONArray variablesArray = jsonObj.optJSONArray(HAPBlockTestComplexTask.VARIABLE);
		if(variablesArray!=null) {
			for(int i=0; i<variablesArray.length(); i++) {
				String varName = null;
				Object varDefObj = variablesArray.get(i);
				if(varDefObj instanceof String) {
					varName = (String)varDefObj;
				}
				else if(varDefObj instanceof JSONObject) {
					varName = ((JSONObject)varDefObj).getString(HAPEntityInfo.NAME);
				}
				
				HAPReferenceElement elementRef = new HAPReferenceElement();
				elementRef.buildObject(varDefObj, HAPSerializationFormat.JSON);
				taskEntity.getVariables().put(varName, elementRef);
			}
		}
		
		//task interactive
		this.parseBrickAttributeJson(entityDefinition, jsonObj, HAPBlockTestComplexTask.INTERACTIVETASK, HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, null, parseContext);

		String interactiveTaskResult = (String)jsonObj.opt(HAPBlockTestComplexTask.INTERACTIVETASKRESULT);
		if(interactiveTaskResult!=null) {
			taskEntity.setTaskInteractiveResult(interactiveTaskResult);
		}
		
		//expression interactive
		this.parseBrickAttributeJson(entityDefinition, jsonObj, HAPBlockTestComplexTask.INTERACTIVEEXPRESSION, HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, null, parseContext);

	}
}
