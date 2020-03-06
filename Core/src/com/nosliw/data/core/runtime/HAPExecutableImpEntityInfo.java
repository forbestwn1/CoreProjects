package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;

abstract public class HAPExecutableImpEntityInfo extends HAPExecutableImp  implements HAPEntityInfo{
	
	private HAPEntityInfo m_entityInfo;
	
	public HAPExecutableImpEntityInfo(HAPEntityInfo entityInfo) {
		this.m_entityInfo = entityInfo;
	}
	
	@Override
	public HAPInfo getInfo() {  return this.m_entityInfo.getInfo();  }

	@Override
	public String getName() {  return this.m_entityInfo.getName();  }

	@Override
	public String getDescription() {   return this.m_entityInfo.getDescription();  }

	@Override
	public HAPExecutableImpEntityInfo clone() {
		throw new RuntimeException();
	}
	
	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		HAPEntityInfoUtility.cloneTo(this, entityInfo);
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		HAPErrorUtility.invalid("This entity info is not writable");
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		HAPEntityInfoUtility.buildJsonMap(jsonMap, this);
	}
	
	@Override
	public HAPEntityInfo cloneEntityInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
