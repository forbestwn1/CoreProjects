package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;

abstract public class HAPExecutableImpEntityInfoWrapper extends HAPExecutableImp  implements HAPEntityInfo{
	
	private HAPEntityInfo m_entityInfo;

	public HAPExecutableImpEntityInfoWrapper() {}

	public HAPExecutableImpEntityInfoWrapper(HAPEntityInfo entityInfo) {
		this.m_entityInfo = entityInfo;
	}
	
	@Override
	public String getId() {  return this.m_entityInfo.getId(); }

	@Override
	public HAPInfo getInfo() {  return this.m_entityInfo.getInfo();  }

	@Override
	public String getName() {  return this.m_entityInfo.getName();  }

	@Override
	public String getDescription() {   return this.m_entityInfo.getDescription();  }

	@Override
	public void setId(String id) {   this.m_entityInfo.setId(id); }

	@Override
	public void setName(String name) {  this.m_entityInfo.setName(name);  }

	@Override
	public void setDescription(String description) {  this.m_entityInfo.setDescription(description);  }

	@Override
	public void setInfo(HAPInfo info) {  this.m_entityInfo.setInfo(info);  }


	@Override
	public HAPExecutableImpEntityInfoWrapper clone() {
		throw new RuntimeException();
	}
	
	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		HAPUtilityEntityInfo.cloneTo(this, entityInfo);
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		HAPErrorUtility.invalid("This entity info is not writable");
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);
	}
	
	@Override
	public HAPEntityInfo cloneEntityInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
