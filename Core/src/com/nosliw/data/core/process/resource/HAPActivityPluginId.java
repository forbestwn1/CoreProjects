package com.nosliw.data.core.process.resource;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPActivityPluginId extends HAPSerializableImp{

	private String m_env;
	private String m_id;
	
	public HAPActivityPluginId(String fullName){
		this.buildObjectByLiterate(fullName);
	}
	
	public HAPActivityPluginId(String id, String env) {
		this.m_id = id;
		this.m_env = env;
	}
	
	public String getEnv() {  return this.m_env;   }
	public String getId(){  return this.m_id;  }

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeLevel1(this.m_env, this.m_id);
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPNamingConversionUtility.parseLevel1(literateValue);
		this.m_env = segs[0];
		this.m_id = segs[1];
		return true;
	}
}
