package com.nosliw.core.application.division.manual.brick.test.complex.task.script;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugDefinition;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTestComplexTaskScript 
           extends HAPManualDefinitionBrick 
           implements HAPWithBlockInteractiveTask, HAPWithBlockInteractiveExpression, HAPWithVariableDebugDefinition, HAPWithScriptReference{

	public HAPManualDefinitionBlockTestComplexTaskScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_SCRIPT_100);
		this.setAttributeValueWithValue(HAPBlockTestComplexTask.PARM, new LinkedHashMap<String, Object>());
	}

	public void setParm(String name, Object value) {	this.getParms().put(name, value);	}
	public Map<String, Object> getParms(){   return (Map<String, Object>)this.getAttributeValueOfValue(HAPBlockTestComplexTask.PARM);    }
	public Object getParm(String name) {   return this.getParms().get(name);    }

	@Override
	public List<String> getVariables() {    return (List<String>)this.getAttributeValueOfValue(VARIABLE);  }
	public void addVariable(String variable) {    this.getVariables().add(variable);        }

	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);  }
	
	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPWithBlockInteractiveTask.TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPWithBlockInteractiveTask.TASKINTERFACE, taskInterface);       }

	@Override
	public HAPEntityOrReference getExpressionInterface() {   return this.getAttributeValueOfBrick(HAPWithBlockInteractiveExpression.EXPRESSIONINTERFACE);  }
	@Override
	public void setExpressionInterface(HAPEntityOrReference expressionInterface) {    this.setAttributeValueWithBrick(HAPWithBlockInteractiveExpression.EXPRESSIONINTERFACE, expressionInterface);       }

}
