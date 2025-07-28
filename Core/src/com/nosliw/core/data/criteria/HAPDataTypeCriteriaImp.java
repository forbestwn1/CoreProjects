package com.nosliw.core.data.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.HAPDataTypeId;

public abstract class HAPDataTypeCriteriaImp extends HAPSerializableImp implements HAPDataTypeCriteria{

	private List<HAPDataTypeCriteria> m_children;
	
	public HAPDataTypeCriteriaImp(){
		this.m_children = new ArrayList<HAPDataTypeCriteria>();
	}
	
	@Override
	public List<HAPDataTypeCriteria> getChildren(){  return this.m_children;  }

	public void addChild(HAPDataTypeCriteria criteria){ this.m_children.add(criteria);  }
	public void addChildren(List<HAPDataTypeCriteria> criterias){ this.m_children.addAll(criterias);  }
	
	@Override
	public boolean validate(HAPDataTypeCriteria criteria, HAPDataTypeHelper dataTypeHelper) {
		return this.getValidDataTypeId(dataTypeHelper).containsAll(criteria.getValidDataTypeId(dataTypeHelper));
	}

	@Override
	public boolean validate(HAPDataTypeId dataTypeId, HAPDataTypeHelper dataTypeHelper) {
		return this.getValidDataTypeId(dataTypeHelper).contains(dataTypeId);
	}
	
	@Override
	protected String buildLiterate(){  return HAPManagerSerialize.getInstance().toStringValue(this, HAPSerializationFormat.LITERATE); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(CHILDREN, HAPUtilityJson.buildJson(this.getChildren(), HAPSerializationFormat.JSON));
	}
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){	this.buildJsonMap(jsonMap, typeJsonMap);	}

	@Override
	public String toString() {		return this.toStringValue(HAPSerializationFormat.LITERATE);	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaImp){
			HAPDataTypeCriteriaImp criteria = (HAPDataTypeCriteriaImp)obj;
			if(criteria.getType().equals(this.getType())){
				out = HAPUtilityBasic.isEqualLists(this.m_children, criteria.m_children);
			}
		}
		return out;
	}
}
