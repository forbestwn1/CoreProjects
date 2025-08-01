package com.nosliw.data.core.codetable;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.xxx.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerCodeTable extends HAPResourceManagerImp{

	private HAPManagerCodeTable m_codeTableManager;
	
	public HAPResourceManagerCodeTable(HAPManagerCodeTable codeTableManager, HAPManagerResource rootResourceMan) {
		super(rootResourceMan);
		this.m_codeTableManager = codeTableManager;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdCodeTable codeTableResourceId = new HAPResourceIdCodeTable((HAPResourceIdSimple)resourceId); 
		HAPCodeTable codeTable = this.m_codeTableManager.getCodeTable(codeTableResourceId.getCodeTableId());
		if(codeTable==null)  return null;
		HAPResourceDataCodeTable resourceData = new HAPResourceDataCodeTable(codeTable);
		
		return new HAPResource(resourceId, resourceData, HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
