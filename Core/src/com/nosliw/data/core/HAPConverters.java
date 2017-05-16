package com.nosliw.data.core;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPConverters {

	private Map<HAPDataTypeId, HAPRelationship> m_convertors = new LinkedHashMap<HAPDataTypeId, HAPRelationship>();
	
	public HAPConverters(){
	}

	public void addItem(HAPDataTypeId dataTypeId, HAPRelationship relationship){
		this.m_convertors.put(dataTypeId, relationship);
	}

	public Set<HAPRelationship> getRelationships(){
		return new HashSet<HAPRelationship>(this.m_convertors.values());
	}
	
}
