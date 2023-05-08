package com.nosliw.data.core.domain.entity.decoration.script1;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableDecorationScript extends HAPExecutableEntityComplex{

	public static final String ENTITY_TYPE = HAPDefinitionEntityDecorationScript.ENTITY_TYPE;

	@HAPAttribute
	public static final String SCRIPT = "script";

	@HAPAttribute
	public static String SCRIPTNAME = "scriptName";

	private HAPJsonTypeScript m_script;

	public HAPExecutableDecorationScript() {
		super(ENTITY_TYPE);
	}

	public void setScript(String script) {		this.setNormalAttributeObject(SCRIPT, new HAPEmbededExecutable(new HAPJsonTypeScript(script)));	}

	public void setScriptName(String scriptName) {		this.setNormalAttributeObject(SCRIPTNAME, new HAPEmbededExecutable(scriptName));	}
	public String getScriptName() {		return (String)this.getNormalAttributeValue(SCRIPTNAME);	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
		dependency.add(scriptResource);
	}
}
