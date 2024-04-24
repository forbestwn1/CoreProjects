package com.nosliw.core.application;

public class HAPPluginBrickImp implements HAPPluginBrick{

	private HAPInfoBrickType m_brickTypeInfo;
	
	private Class<? extends HAPBrick> m_brickClass;
	
	private HAPManagerApplicationBrick m_brickMan;
	
	public HAPPluginBrickImp(HAPInfoBrickType brickTypeInfo, Class<? extends HAPBrick> brickClass, HAPManagerApplicationBrick brickMan) {
		this.m_brickTypeInfo = brickTypeInfo;
		this.m_brickClass = brickClass;
		this.m_brickMan = brickMan;
	}
	
	@Override
	public HAPInfoBrickType getBrickTypeInfo() {   return this.m_brickTypeInfo;  }

	@Override
	public HAPBrick newInstance() {
		HAPBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickTypeInfo(m_brickTypeInfo);
			out.setBrickManager(m_brickMan);
			out.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

}
