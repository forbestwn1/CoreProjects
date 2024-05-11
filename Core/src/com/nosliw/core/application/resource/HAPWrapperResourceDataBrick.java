package com.nosliw.core.application.resource;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPWrapperResourceData;

public class HAPWrapperResourceDataBrick implements HAPWrapperResourceData{

	private HAPBundle m_bundle;
	
	private HAPManagerApplicationBrick m_brickMan;
	
	public HAPWrapperResourceDataBrick(HAPBundle bundle, HAPManagerApplicationBrick brickMan) {
		this.m_bundle = bundle;
		this.m_brickMan = brickMan;
	}
	
	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		if(path.getLength()>=2) {
			throw new RuntimeException();
		}
		return this.m_bundle.getExportResourceData(path.toString(), m_brickMan);
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceData getResourceData() {
		return this.m_bundle.getExportResourceData(null, m_brickMan);
	}

}
