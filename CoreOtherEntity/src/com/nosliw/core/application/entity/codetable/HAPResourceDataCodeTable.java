package com.nosliw.core.application.entity.codetable;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValueImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPResourceDataCodeTable  extends HAPResourceDataJSValueImp{

	private HAPCodeTable m_codeTable;
	
	public HAPResourceDataCodeTable(HAPCodeTable codeTable){
		this.m_codeTable = codeTable;
	}
	
	public HAPCodeTable getCodeTable(){ return this.m_codeTable;  }
	
	@Override
	public String getValue() {
		return this.m_codeTable.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		return null;
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		return null;
	}

}
