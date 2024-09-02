package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.brick.test.complex.script.HAPExecutableVariableExpected;
import com.nosliw.core.application.brick.test.complex.script.HAPInfoAttachmentResolve;
import com.nosliw.core.application.brick.test.complex.task.HAPTestEvent;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualBlockTestComplexScript extends HAPManualBrickImp implements HAPBlockTestComplexScript{

	public HAPManualBlockTestComplexScript() {
		this.setAttributeValueWithValue(HAPBlockTestComplexScript.EVENT, new ArrayList<HAPTestEvent>());
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	public HAPResourceId getScrip() {		return (HAPResourceId)this.getAttributeValueOfValue(SCRIPT);	}
	public void setScript(HAPResourceId scriptResourceId) {   this.setAttributeValueWithValue(SCRIPT, scriptResourceId);  }

	@Override
	public Map<String, Object> getParms() {		return (Map<String, Object>)this.getAttributeValueOfValue(PARM);	}
	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	@Override
	public List<HAPResultReferenceResolve> getVariables() {   return (List<HAPResultReferenceResolve>)this.getAttributeValueOfValue(VARIABLE);	}
	public void setVariables(List<HAPResultReferenceResolve> vars) {    this.setAttributeValueWithValue(VARIABLE, vars);	}
	
	@Override
	public List<HAPReferenceElement> getUnknownVariables() {    return (List<HAPReferenceElement>)this.getAttributeValueOfValue(ATTACHMENT);	}
	public void setUnknowVariable(List<HAPReferenceElement> unknowns) {   this.setAttributeValueWithValue(UNKNOWNVARIABLE, unknowns);   }

	@Override
	public List<HAPExecutableVariableExpected> getExtendedVariables(){    return (List<HAPExecutableVariableExpected>)this.getAttributeValueOfValue(VARIABLEEXTENDED);         }
	public void setExtendedVariables(List<HAPExecutableVariableExpected> vars) {    this.setAttributeValueWithValue(VARIABLEEXTENDED, vars);	}

	@Override
	public List<HAPInfoAttachmentResolve> getAttachments(){    return (List<HAPInfoAttachmentResolve>)this.getAttributeValueOfValue(ATTACHMENT);         }
	public void setAttachment(List<HAPInfoAttachmentResolve> attachments) {    this.setAttributeValueWithValue(ATTACHMENT, attachments);	}

	public List<HAPTestEvent> getEvents(){    return (List<HAPTestEvent>)this.getAttributeValueOfValue(HAPBlockTestComplexScript.EVENT);     }
	
}
