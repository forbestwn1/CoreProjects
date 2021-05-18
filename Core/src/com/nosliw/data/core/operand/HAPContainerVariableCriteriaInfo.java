package com.nosliw.data.core.operand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

@HAPEntityWithAttribute
public class HAPContainerVariableCriteriaInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLES = "variables";

	private Map<String, HAPInfoCriteria> m_criteriaInfosById;

	public HAPContainerVariableCriteriaInfo() {
		this.m_criteriaInfosById = new LinkedHashMap<String, HAPInfoCriteria>();
	}
	
	public void addVariable(String id, HAPInfoCriteria criteria) {
		this.m_criteriaInfosById.put(id, criteria);
	}
	
	public Set<String> getVariablesName(){    return this.m_criteriaInfosById.keySet();     }
	
	public HAPInfoCriteria getVariableCriteriaInfo(String variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	public Map<String, HAPInfoCriteria> getVariableCriteriaInfos() {   return this.m_criteriaInfosById; }
	
	@Override
	public HAPContainerVariableCriteriaInfo clone() {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(String id : this.m_criteriaInfosById.keySet()) {
			out.addVariable(id, this.m_criteriaInfosById.get(id).cloneCriteriaInfo());
		}
		return out;
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(VARIABLES, HAPSerializeManager.getInstance().toStringValue(this.m_criteriaInfosById, HAPSerializationFormat.JSON));
	}

}
