package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPUIAppEntryId extends HAPSerializableImp{

	private String m_appId;
	private String m_entry;
	
	private HAPUIAppEntryId() {}
	
	public HAPUIAppEntryId(String id, String entry){
		this.m_appId = id;
		this.m_entry = entry;
	}
	
	public String getAppId(){  return this.m_appId;  }
	
	public String getEntry() {  return this.m_entry;  }

	@Override
	protected String buildLiterate(){
		return HAPUtilityNamingConversion.cascadeLevel1(m_appId, m_entry);
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPUtilityNamingConversion.parseLevel1(literateValue);
		this.m_appId = segs[0];
		this.m_entry = segs[1];
		return true;
	}

	public static HAPUIAppEntryId buildUIAppEntryId(String literateValue) {
		HAPUIAppEntryId out = new HAPUIAppEntryId();
		out.buildObjectByLiterate(literateValue);
		return out;
	}
}
