package com.nosliw.core.application.division.manual.brick.test.complex.task.script;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.test.complex.task.script.HAPBlockTestComplexTaskScript;
import com.nosliw.core.application.common.withvariable.HAPVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugExecutable;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualBlockTestComplexTaskScript extends HAPManualBrickImp implements HAPBlockTestComplexTaskScript{

	public HAPManualBlockTestComplexTaskScript() {
	}

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(HAPWithVariableDebugExecutable.VARIABLE, new LinkedHashMap<String, HAPVariableInfo>());
	}

	@Override
	public Map<String, Object> getParms() {		return (Map<String, Object>)this.getAttributeValueOfValue(PARM);	}
	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);  }
	
	@Override
	public Map<String, HAPVariableInfo> getVariables() {    return (Map<String, HAPVariableInfo>)this.getAttributeValueOfValue(VARIABLE);  }
	public void addVariable(String name, HAPVariableInfo varInfo) {     this.getVariables().put(name, varInfo);      }

	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(TASKINTERFACE);  }
	public void setTaskInterface(HAPEntityOrReference taskInterface) {   this.setAttributeValueWithBrick(TASKINTERFACE, taskInterface);     }

	@Override
	public HAPEntityOrReference getExpressionInterface() {  return this.getAttributeValueOfBrick(EXPRESSIONINTERFACE);  }
	public void setExpressionInterface(HAPEntityOrReference expressionInterface) {   this.setAttributeValueWithBrick(EXPRESSIONINTERFACE, expressionInterface);     }
	
}
