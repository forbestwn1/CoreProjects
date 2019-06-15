package com.nosliw.miniapp.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPMiniAppSettingData extends HAPSerializableImp{

	@HAPAttribute
	public static final String OWNERINFO = "ownerInfo";

	@HAPAttribute
	public static final String DATABYNAME = "dataByName";

	private HAPOwnerInfo m_ownerInfo;
	
	private Map<String, HAPSettingData> m_dataByName;
	
	public HAPMiniAppSettingData(HAPOwnerInfo ownerInfo) {
		this.m_ownerInfo = ownerInfo;
		this.m_dataByName = new LinkedHashMap<String, HAPSettingData>();
	}

	public HAPOwnerInfo getOwnerInfo() {   return this.m_ownerInfo;   }
	
	public void addData(HAPSettingData data) {	this.m_dataByName.put(data.getName(), data);	}
	
	public Map<String, HAPSettingData> getDatas(){  return this.m_dataByName;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OWNERINFO, HAPJsonUtility.buildJson(this.m_ownerInfo, HAPSerializationFormat.JSON));
		jsonMap.put(DATABYNAME, HAPJsonUtility.buildJson(this.m_dataByName, HAPSerializationFormat.JSON));
	}
}
