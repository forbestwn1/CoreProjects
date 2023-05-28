package com.nosliw.data.core.operand;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	public static String CRITERIA = "criteria";

	private Map<HAPIdVariable, HAPInfoCriteria> m_criteriaInfosById;

	public HAPContainerVariableCriteriaInfo() {
		this.m_criteriaInfosById = new LinkedHashMap<HAPIdVariable, HAPInfoCriteria>();
	}
	
	public void addVariable(HAPIdVariable variableId, HAPInfoCriteria criteria) {
		this.m_criteriaInfosById.put(variableId, criteria);
	}
	
	public Set<HAPIdVariable> getVariablesId(){    return this.m_criteriaInfosById.keySet();     }
	
	public HAPInfoCriteria getVariableCriteriaInfo(HAPIdVariable variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	public Map<HAPIdVariable, HAPInfoCriteria> getVariableCriteriaInfos() {   return this.m_criteriaInfosById; }
	
	@Override
	public HAPContainerVariableCriteriaInfo clone() {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(HAPIdVariable id : this.m_criteriaInfosById.keySet()) {
			out.addVariable(id, this.m_criteriaInfosById.get(id).cloneCriteriaInfo());
		}
		return out;
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		List<String> outList = new ArrayList<String>();
		for(Entry<HAPIdVariable, HAPInfoCriteria> entry : m_criteriaInfosById.entrySet()) {
			Map<String, String> entryMap = new LinkedHashMap<String, String>();
			entryMap.put(VARIABLEID, entry.getKey().toStringValue(HAPSerializationFormat.JSON));
			entryMap.put(CRITERIA, entry.getValue().toStringValue(HAPSerializationFormat.JSON));
			outList.add(HAPUtilityJson.buildMapJson(entryMap));
		}
		
		jsonMap.put(VARIABLES, HAPUtilityJson.buildArrayJson(outList.toArray(new String[0])));
	}

}
