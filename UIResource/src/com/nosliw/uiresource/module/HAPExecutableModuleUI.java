package com.nosliw.uiresource.module;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;

public class HAPExecutableModuleUI extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String CONTEXTMAPPING = "contextMapping";
	
	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	private HAPDefinitionModuleUI m_moduleUIDefinition;
	
	private String m_id;

	private HAPExecutableUIUnitResource m_page;
	
	// hook up with real data during runtime
	private HAPContext m_contextMapping;

	
	public HAPExecutableModuleUI(HAPDefinitionModuleUI moduleUIDefinition, String id) {
		super(moduleUIDefinition);
		this.m_moduleUIDefinition = moduleUIDefinition;
		this.m_id = id;
	}
	
	public void setContextMapping(HAPContext contextMapping) {   this.m_contextMapping = contextMapping;   }
	public HAPContext getContextMapping() {   return this.m_contextMapping;   }
	
	public void setPage(HAPExecutableUIUnitResource page) {  this.m_page = page;   }
	
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
