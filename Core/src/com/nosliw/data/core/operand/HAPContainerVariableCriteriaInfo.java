package com.nosliw.data.core.operand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.variable.HAPIdVariable;

@HAPEntityWithAttribute
public class HAPContainerVariableCriteriaInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLES = "variables";

	@HAPAttribute
	public static String VARIABLEID = "variableId";

	@HAPAttribute
	public static String VARIABLEKEY = "variableKey";

	@HAPAttribute
	public static String CRITERIA = "criteria";

	private Map<HAPIdVariable, HAPInfoCriteria> m_criteriaInfosById;

	private Map<String, HAPIdVariable> m_variableIdByKey;
	
	private int m_nextId = 0;
	
	public HAPContainerVariableCriteriaInfo() {
		this.m_criteriaInfosById = new LinkedHashMap<HAPIdVariable, HAPInfoCriteria>();
		this.m_variableIdByKey = new LinkedHashMap<String, HAPIdVariable>();
	}
	
	public Map<HAPIdVariable, HAPInfoCriteria> getVariableCriteriaInfos() {   return this.m_criteriaInfosById; }
	
	public HAPIdVariable getVariableId(String key) {    return this.m_variableIdByKey.get(key);     }
	
	public HAPInfoCriteria getVaraibleCriteriaInfo(String key) {   return this.m_criteriaInfosById.get(this.m_variableIdByKey.get(key));     }
	
	public String addVariable(HAPIdVariable variableId) {
		if(m_criteriaInfosById.get(variableId)!=null) {
			//already exist
			for(String key : this.m_variableIdByKey.keySet()) {
				if(variableId.equals(this.m_variableIdByKey.get(key))) {
					return key;
				}
			}
			return null;
		}
		else {
			//brand new
			String key = this.m_nextId + "";
			this.m_nextId++;
			this.m_variableIdByKey.put(key, variableId);
			this.m_criteriaInfosById.put(variableId, new HAPInfoCriteria());
			return key;
		}
	}
	
	public void addVariable(String variableKey, HAPIdVariable variableId, HAPInfoCriteria criteria) {
		this.m_variableIdByKey.put(variableKey, variableId);
		this.m_criteriaInfosById.put(variableId, criteria);
	}
	
	
	
	
	public Set<HAPIdVariable> getVariablesId(){    return this.m_criteriaInfosById.keySet();     }
	
//	public HAPInfoCriteria getVariableCriteriaInfo(HAPIdVariable variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	@Override
	public HAPContainerVariableCriteriaInfo clone() {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(String key : this.m_variableIdByKey.keySet()) {
			out.addVariable(key, this.getVariableId(key), this.getVaraibleCriteriaInfo(key));
		}
		return out;
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		Map<String, String> outMap = new LinkedHashMap<String, String>();

		for(String key : this.m_variableIdByKey.keySet()) {
			Map<String, String> entryMap = new LinkedHashMap<String, String>();
			entryMap.put(VARIABLEKEY, key);
			entryMap.put(VARIABLEID, m_variableIdByKey.get(key).toStringValue(HAPSerializationFormat.JSON));
			entryMap.put(CRITERIA, this.m_criteriaInfosById.get(m_variableIdByKey.get(key)).toStringValue(HAPSerializationFormat.JSON));
			outMap.put(key, HAPUtilityJson.buildMapJson(entryMap));
		}
		
		jsonMap.put(VARIABLES, HAPUtilityJson.buildMapJson(outMap));
	}

}
