package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

@HAPEntityWithAttribute
public class HAPExecutableTestComplexScript extends HAPExecutableEntityComplex{

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
	
	
	public HAPExecutableTestComplexScript() {
	}
	
	public void setScript(String script) {		this.setNormalAttributeValueObject(SCRIPT, new HAPJsonTypeScript(script));	}

	public void setScriptName(String scriptName) {		this.setNormalAttributeValueObject(SCRIPTNAME, scriptName);	}
	public String getScriptName() {		return (String)this.getNormalAttributeValue(SCRIPTNAME);	}

	public void setParms(Map<String, Object> parms) {	this.setNormalAttributeValueObject(PARM, parms);	}

	public void setVariables(List<HAPInfoReferenceResolve> vars) {    this.setNormalAttributeValueObject(VARIABLE, vars);	}
	
	public void setUnknowVariable(List<HAPReferenceElementInValueContext> unknowns) {   this.setNormalAttributeValueObject(UNKNOWNVARIABLE, unknowns);   }

	public List<HAPExecutableVariableExpected> getExtendedVariables(){    return (List<HAPExecutableVariableExpected>)this.getNormalAttributeValue(VARIABLEEXTENDED);         }
	public void setExtendedVariables(List<HAPExecutableVariableExpected> vars) {    this.setNormalAttributeValueObject(VARIABLEEXTENDED, vars);	}

	public void setAttachment(List<HAPInfoAttachmentResolve> attachments) {    this.setNormalAttributeValueObject(ATTACHMENT, attachments);	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
		dependency.add(scriptResource);
	}

}
