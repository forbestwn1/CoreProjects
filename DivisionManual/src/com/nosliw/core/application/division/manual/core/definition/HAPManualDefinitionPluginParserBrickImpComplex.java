package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONObject;

import com.nosliw.common.parm.HAPWithParms;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugDefinition;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.HAPWithValueContext;

public class HAPManualDefinitionPluginParserBrickImpComplex extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpComplex(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		super(brickTypeId, brickClass, manualBrickMan, brickMan);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		parseBrickAttributeJson(brickDefinition, jsonObj, HAPWithValueContext.VALUECONTEXT, HAPManualEnumBrickType.VALUECONTEXT_100, null, parseContext);	
		
//		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		
		//parms
        if(brickDefinition instanceof HAPWithParms) {
        	this.parseParmsAttribute((HAPWithParms)brickDefinition, jsonObj);
        }
		
		//task interface
        if(brickDefinition instanceof HAPWithBlockInteractiveTask) {
        	this.parseTaskInterfaceAttribute(brickDefinition, jsonObj, parseContext);
        }
		
        //expression interface
        if(brickDefinition instanceof HAPWithBlockInteractiveExpression) {
        	this.parseExpressionInterfaceAttribute(brickDefinition, jsonObj, parseContext);
        }

        //variable for debug attirbute
        if(brickDefinition instanceof HAPWithVariableDebugDefinition) {
        	this.parseDebugVariable(brickDefinition, jsonObj, parseContext);
        }

        //script resource attribute
        if(brickDefinition instanceof HAPWithScriptReference) {
    		Object scriptObj = jsonObj.opt(HAPWithScriptReference.SCRIPTRESOURCEID);
    		HAPResourceId scriptResourceId = HAPFactoryResourceId.tryNewInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, null, scriptObj, false);
    		brickDefinition.setAttributeValueWithValue(HAPWithScriptReference.SCRIPTRESOURCEID, scriptResourceId);
        }

		this.parseComplexDefinitionContentJson(brickDefinition, jsonObj, parseContext);
	}

	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {}

}
