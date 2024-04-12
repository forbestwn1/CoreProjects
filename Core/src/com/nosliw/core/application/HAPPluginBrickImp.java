package com.nosliw.core.application;

public class HAPPluginBrickImp implements HAPPluginBrick{

	private HAPInfoBrickType m_brickTypeInfo;
	
	private Class<? extends HAPBrick> m_brickClass;
	
	public HAPPluginBrickImp(HAPInfoBrickType brickTypeInfo, Class<? extends HAPBrick> brickClass) {
		this.m_brickTypeInfo = brickTypeInfo;
		this.m_brickClass = brickClass;
	}
	
	@Override
	public HAPInfoBrickType getBrickTypeInfo() {   return this.m_brickTypeInfo;  }

	@Override
	public HAPBrick newInstance() {
		HAPBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickTypeInfo(m_brickTypeInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}
