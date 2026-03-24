package com.nosliw.core.application.entity.brickcriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPCriteriaBrick extends HAPSerializableImp{

	public final static String CRITERIATYPE = "criteriaType"; 

	public final static String RESTRAIN = "restrain"; 

	private String m_criteriaType;
	
	private List<HAPRestrainBrick> m_restrains;
	
	public HAPCriteriaBrick(String criteriaType) {
		this.m_criteriaType = criteriaType;
		this.m_restrains = new ArrayList<HAPRestrainBrick>();
	}
	
	public String getCriteriaType() {		return this.m_criteriaType;  	}

	public List<HAPRestrainBrick> getRestains(){	return this.m_restrains;	}
	
	public void addRestrain(HAPRestrainBrick restrain) {   this.m_restrains.add(restrain);      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CRITERIATYPE, this.m_criteriaType);
		jsonMap.put(RESTRAIN, HAPManagerSerialize.getInstance().toStringValue(m_restrains, HAPSerializationFormat.JSON));
	}
}
