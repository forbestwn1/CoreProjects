package com.nosliw.data.core.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

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
	protected String buildLiterate(){  return HAPSerializeManager.getInstance().toStringValue(this, HAPSerializationFormat.LITERATE); }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(this.getChildren(), HAPSerializationFormat.JSON));
	}
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){	this.buildJsonMap(jsonMap, typeJsonMap);	}

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataTypeCriteriaImp){
			HAPDataTypeCriteriaImp criteria = (HAPDataTypeCriteriaImp)obj;
			if(criteria.getType().equals(this.getType())){
				out = HAPBasicUtility.isEqualLists(this.m_children, criteria.m_children);
			}
		}
		return out;
	}
}
