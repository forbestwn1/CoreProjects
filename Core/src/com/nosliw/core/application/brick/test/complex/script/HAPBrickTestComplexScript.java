package com.nosliw.core.application.brick.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPBrickTestComplexScript extends HAPBrickComplex{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String SCRIPTNAME = "scriptName";

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
	
	
	public HAPBrickTestComplexScript() {
	}
	
	public void setScript(String script) {		this.setAttributeValueObject(SCRIPT, new HAPJsonTypeScript(script));	}

	public void setScriptName(String scriptName) {   this.setAttributeValueWithValue(SCRIPTNAME, scriptName);  }
	public String getScriptName() {		return (String)this.getAttributeValue(SCRIPTNAME);	}

	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	public void setVariables(List<HAPResultReferenceResolve> vars) {    this.setAttributeValueWithValue(VARIABLE, vars);	}
	
	public void setUnknowVariable(List<HAPReferenceElement> unknowns) {   this.setAttributeValueWithValue(UNKNOWNVARIABLE, unknowns);   }

	public List<HAPExecutableVariableExpected> getExtendedVariables(){    return (List<HAPExecutableVariableExpected>)this.getAttributeValue(VARIABLEEXTENDED);         }
	public void setExtendedVariables(List<HAPExecutableVariableExpected> vars) {    this.setAttributeValueObject(VARIABLEEXTENDED, vars);	}

	public void setAttachment(List<HAPInfoAttachmentResolve> attachments) {    this.setAttributeValueObject(ATTACHMENT, attachments);	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
		dependency.add(scriptResource);
	}

}
