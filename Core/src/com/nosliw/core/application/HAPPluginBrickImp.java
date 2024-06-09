package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginBrickImp implements HAPPluginBrick{

	private HAPInfoBrickType m_brickTypeInfo;
	
	private Class<? extends HAPBrick> m_brickClass;
	
	private HAPRuntimeEnvironment m_runtimeEnv; 
	
	public HAPPluginBrickImp(HAPInfoBrickType brickTypeInfo, Class<? extends HAPBrick> brickClass, HAPRuntimeEnvironment runtimeEnv) {
		this.m_brickTypeInfo = brickTypeInfo;
		this.m_brickClass = brickClass;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public HAPInfoBrickType getBrickTypeInfo() {   return this.m_brickTypeInfo;  }

	@Override
	public HAPBrick newInstance() {
		HAPBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickTypeInfo(m_brickTypeInfo);
			out.setRuntimeEnvironment(m_runtimeEnv);
			out.setTreeNodeInfo(new HAPInfoTreeNode(new HAPPath(), null));
			out.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick){
		return new ArrayList<HAPInfoExportResource>();
	}

}
