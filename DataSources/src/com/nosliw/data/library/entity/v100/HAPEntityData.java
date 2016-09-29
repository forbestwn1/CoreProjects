package com.nosliw.data.library.entity.v100;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataImp;
import com.nosliw.data.HAPDataType;

public class HAPEntityData extends HAPDataImp{

	Map<String, HAPData> m_attributes;
	
	public HAPEntityData(HAPDataType dataType) {
		super(dataType);
		this.m_attributes = new LinkedHashMap<String, HAPData>();
	}

	public HAPEntityData setAttribute(String attribute, HAPData data){
		this.m_attributes.put(attribute, data);
		return this;
	}
	
	public HAPData getAttribute(String name){
		return this.m_attributes.get(name);
	}

	
	@Override
	public HAPData cloneData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toDataStringValue(String format) {
		return null;
	}

}
