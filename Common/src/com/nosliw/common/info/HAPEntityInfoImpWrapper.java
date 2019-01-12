package com.nosliw.common.info;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPEntityInfoImpWrapper extends HAPSerializableImp implements HAPEntityInfo{
	
	private HAPEntityInfo m_entityInfo;
	
	public HAPEntityInfoImpWrapper(HAPEntityInfo entityInfo) {
		this.m_entityInfo = entityInfo;
	}
	
	@Override
	public HAPInfo getInfo() {  return this.m_entityInfo.getInfo();  }

	@Override
	public String getName() {  return this.m_entityInfo.getName();  }

	@Override
	public String getDescription() {   return this.m_entityInfo.getDescription();  }

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		entityInfo.setName(this.getName());
		entityInfo.setDescription(this.getDescription());
		entityInfo.setInfo(this.getInfo().cloneInfo());
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		HAPErrorUtility.invalid("This entity info is not writable");
	}
}
