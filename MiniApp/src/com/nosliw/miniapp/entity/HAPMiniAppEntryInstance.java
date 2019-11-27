package com.nosliw.miniapp.entity;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;

public class HAPMiniAppEntryInstance  extends HAPSerializableImp{

	private HAPExecutableAppEntry m_entry;
	
	private HAPAppDataInfoContainer m_data;

	public HAPMiniAppEntryInstance() {}
	
	public void setEntry(HAPExecutableAppEntry entry) {  this.m_entry = entry; }
	
	public void setData(HAPAppDataInfoContainer data) {   this.m_data = data;   }

}
