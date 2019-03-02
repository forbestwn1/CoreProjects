package com.nosliw.uiresource.application;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationWithTarget;
import com.nosliw.uiresource.module.HAPExecutableModule;

public class HAPExecutableAppModule extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static final String OUTPUTMAPPING = "outputMapping";

	private HAPExecutableModule m_module;
	
	private HAPExecutableDataAssociationWithTarget m_inputMapping;
	
	private HAPExecutableDataAssociationWithTarget m_outputMapping;

	public HAPExecutableAppModule(HAPDefinitionAppModule def) {
		super(def);
	}

	public void setModule(HAPExecutableModule module) {  this.m_module = module;  }
	
	public void setInputMapping(HAPExecutableDataAssociationWithTarget inputMapping) {    this.m_inputMapping = inputMapping;   }
	
	public void setOutputMapping(HAPExecutableDataAssociationWithTarget outputMapping) {   this.m_outputMapping = outputMapping;   }
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
