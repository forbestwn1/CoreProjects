package com.nosliw.miniapp.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPMiniAppSettingData extends HAPSerializableImp{

	private Map<String, List<HAPSettingData>> m_data;
	
	public HAPMiniAppSettingData() {
		this.m_data = new LinkedHashMap<String, List<HAPSettingData>>();
	}
	
	public void addData(HAPSettingData data) {
		String dataName = data.getName();
		List<HAPSettingData> dataByName = this.m_data.get(dataName);
		if(dataByName==null) {
			dataByName = new ArrayList<HAPSettingData>();
			this.m_data.put(dataName, dataByName);
		}
		dataByName.add(data);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		for(String dataName : this.m_data.keySet()) {
			jsonMap.put(dataName, HAPJsonUtility.buildJson(this.m_data.get(dataName), HAPSerializationFormat.JSON));
		}
	}
}
