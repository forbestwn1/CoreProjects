package com.nosliw.miniapp.entity;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.uiresource.application.HAPExecutableMiniAppEntry;

public class HAPMiniAppEntryInstance  extends HAPSerializableImp{

	private HAPExecutableMiniAppEntry m_entry;
	
	private HAPMiniAppSettingData m_data;

	public HAPMiniAppEntryInstance() {}
	
	public void setEntry(HAPExecutableMiniAppEntry entry) {  this.m_entry = entry; }
	
	public void setData(HAPMiniAppSettingData data) {   this.m_data = data;   }

}
