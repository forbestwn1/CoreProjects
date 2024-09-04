package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualBlockTestComplexTask extends HAPManualBrickImp implements HAPBlockTestComplexTask{

	public HAPManualBlockTestComplexTask() {
	}

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VARIABLE, new LinkedHashMap<String, HAPResultReferenceResolve>());
	}

	@Override
	public HAPResourceId getScrip() {		return (HAPResourceId)this.getAttributeValueOfValue(SCRIPT);	}
	public void setScript(HAPResourceId scriptResourceId) {   this.setAttributeValueWithValue(SCRIPT, scriptResourceId);  }

	@Override
	public Map<String, Object> getParms() {		return (Map<String, Object>)this.getAttributeValueOfValue(PARM);	}
	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	@Override
	public Map<String, HAPResultReferenceResolve> getVariables() {    return (Map<String, HAPResultReferenceResolve>)this.getAttributeValueOfValue(VARIABLE);  }

}
 