package com.nosliw.miniapp.entity;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.miniapp.data.HAPMiniAppSettingData;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;

public class HAPMiniAppEntryInstance  extends HAPSerializableImp{

	private HAPExecutableAppEntry m_entry;
	
	private HAPMiniAppSettingData m_data;

	public HAPMiniAppEntryInstance() {}
	
	public void setEntry(HAPExecutableAppEntry entry) {  this.m_entry = entry; }
	
	public void setData(HAPMiniAppSettingData data) {   this.m_data = data;   }

}
