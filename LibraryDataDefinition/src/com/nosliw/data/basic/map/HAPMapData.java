package com.nosliw.data.basic.map;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.imp.HAPDataImp;

public class HAPMapData extends HAPDataImp{

	private Map<String, HAPData> m_elements;
	
	public HAPMapData(HAPDataType dataType) {
		super(dataType);
		this.m_elements = new LinkedHashMap<String, HAPData>();
	}

	public HAPMapData(HAPDataType dataType, Map<String, HAPData> map) {
		super(dataType);
		this.m_elements = new LinkedHashMap<String, HAPData>();
		this.m_elements.putAll(map);
	}

	public Map<String, HAPData> getMap(){
		return this.m_elements;
	}
	
	public HAPData get(String name){
		return this.m_elements.get(name);
	}
	
	public void put(String name, HAPData data){
		this.m_elements.put(name, data);
	}
	
	public int getSize(){
		return this.m_elements.size();
	}
	
	@Override
	public HAPData cloneData() {
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		return null;
	}

}
