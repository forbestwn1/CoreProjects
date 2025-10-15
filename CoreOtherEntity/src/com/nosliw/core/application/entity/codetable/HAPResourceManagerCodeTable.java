package com.nosliw.core.application.entity.codetable;

import org.springframework.stereotype.Component;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@Component
public class HAPResourceManagerCodeTable implements HAPPluginResourceManager{

	private HAPManagerCodeTable m_codeTableManager;
	
	public HAPResourceManagerCodeTable(HAPManagerCodeTable codeTableManager, HAPManagerResource rootResourceMan) {
		this.m_codeTableManager = codeTableManager;
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdCodeTable codeTableResourceId = new HAPResourceIdCodeTable(simpleResourceId); 
		HAPCodeTable codeTable = this.m_codeTableManager.getCodeTable(codeTableResourceId.getCodeTableId());
		if(codeTable==null) {
			return null;
		}
		return new HAPResourceDataCodeTable(codeTable);
	}

}
