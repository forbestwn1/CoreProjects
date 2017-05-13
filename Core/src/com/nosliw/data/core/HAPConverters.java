package com.nosliw.data.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPConverters {

	private Map<HAPDataTypeId, HAPRelationship> m_convertors = new LinkedHashMap<HAPDataTypeId, HAPRelationship>();
	
	public HAPConverters(){
	}

	public void addItem(HAPDataTypeId dataTypeId, HAPRelationship relationship){
		this.m_convertors.put(dataTypeId, relationship);
	}
	
}
