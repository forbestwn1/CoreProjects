package com.nosliw.data.core.domain.entity.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutableWithValue;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;

@HAPEntityWithAttribute
public class HAPExecutableTestComplexScript extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityTestComplexScript.ENTITY_TYPE;

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String SCRIPTNAME = "scriptName";

	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String VARIABLE = "variable";
	
	public HAPExecutableTestComplexScript() {
		super(HAPDefinitionEntityTestComplexScript.ENTITY_TYPE);
	}
	
	public void setScript(String script) {		this.setNormalAttribute(SCRIPT, new HAPEmbededExecutableWithValue(new HAPJsonTypeScript(script)));	}

	public void setScriptName(String scriptName) {		this.setNormalAttribute(SCRIPTNAME, new HAPEmbededExecutableWithValue(scriptName));	}
	public String getScriptName() {		return (String)this.getNormalAttributeContentWithValue(SCRIPTNAME).getValue();	}

	public void setParms(Map<String, Object> parms) {	this.setNormalAttribute(PARM, new HAPEmbededExecutableWithValue(parms));	}

	public void setVariables(List<HAPInfoReferenceResolve> vars) {    this.setNormalAttribute(VARIABLE, new HAPEmbededExecutableWithValue(vars));	}
	
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
		dependency.add(scriptResource);
	}

}
