package com.nosliw.data.core.criteria;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;

public abstract class HAPDataTypeCriteriaImp extends HAPSerializableImp implements HAPDataTypeCriteria{

	public HAPDataTypeCriteriaImp(){
	}
	
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
	}
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){	this.buildJsonMap(jsonMap, typeJsonMap);	}

//	protected HAPDataTypeCriteria buildCriteriaByIds1(Set<HAPDataTypeId> ids){
//	HAPDataTypeCriteria out = null;
//	if(ids.size()==1){
//		for(HAPDataTypeId id : ids){
//			out = new HAPDataTypeCriteriaElementId(id);
//		}
//	}
//	else{
//		out = new HAPDataTypeCriteriaElementIds(ids);
//	}
//	return out;
//}

}
