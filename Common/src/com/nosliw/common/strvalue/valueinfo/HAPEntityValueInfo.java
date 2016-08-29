package com.nosliw.common.strvalue.valueinfo;

public class HAPEntityValueInfo {

	private String m_className;
	
	private HAPValueInfoEntity m_valueInfoEntity;
	
	private HAPValueInfoManager m_valueInfoMan;
	
	public HAPEntityValueInfo(HAPValueInfoEntity valueInfoEntity, HAPValueInfoManager valueInfoMan){
		this.m_className = valueInfoEntity.getClassName();
		this.m_valueInfoEntity = valueInfoEntity;
		this.m_valueInfoMan = valueInfoMan;
	}
	
	public String getEntityClassName(){ return this.m_className; }
	public HAPValueInfoEntity getValueInfoEntity(){ return this.m_valueInfoEntity; }
	public HAPValueInfoManager getValueInfoManager(){  return this.m_valueInfoMan;  }
	
}
