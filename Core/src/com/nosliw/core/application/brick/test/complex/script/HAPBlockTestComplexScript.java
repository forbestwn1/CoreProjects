package com.nosliw.core.application.brick.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPBlockTestComplexScript extends HAPBrickBlockComplex{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String VARIABLE = "variable";
	
	@HAPAttribute
	public static String ATTACHMENT = "attachment";
	
	@HAPAttribute
	public static String UNKNOWNVARIABLE = "unknownVariable";
	
	@HAPAttribute
	public static String VARIABLEEXTENDED = "variableExtended";
	
	
	public HAPBlockTestComplexScript() {
	}
	
	public void setScript(HAPResourceId scriptResourceId) {   this.setAttributeValueWithValue(SCRIPT, scriptResourceId);  }
	public HAPResourceId getScrip() {		return (HAPResourceId)this.getAttributeValueOfValue(SCRIPT);	}

	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	public void setVariables(List<HAPResultReferenceResolve> vars) {    this.setAttributeValueWithValue(VARIABLE, vars);	}
	
	public void setUnknowVariable(List<HAPReferenceElement> unknowns) {   this.setAttributeValueWithValue(UNKNOWNVARIABLE, unknowns);   }

	public List<HAPExecutableVariableExpected> getExtendedVariables(){    return (List<HAPExecutableVariableExpected>)this.getAttributeValueOfValue(VARIABLEEXTENDED);         }
	public void setExtendedVariables(List<HAPExecutableVariableExpected> vars) {    this.setAttributeValueObject(VARIABLEEXTENDED, vars);	}

	public void setAttachment(List<HAPInfoAttachmentResolve> attachments) {    this.setAttributeValueObject(ATTACHMENT, attachments);	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
		dependency.add(scriptResource);
	}

}
