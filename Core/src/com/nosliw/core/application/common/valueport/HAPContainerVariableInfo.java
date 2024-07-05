package com.nosliw.core.application.common.valueport;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

@HAPEntityWithAttribute
public class HAPContainerVariableInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String VARIABLES = "variables";

	@HAPAttribute
	public static String VARIABLEID = "variableId";

	@HAPAttribute
	public static String VARIABLENAME = "variableName";

	@HAPAttribute
	public static String CRITERIA = "criteria";

	private Map<HAPIdElement, HAPInfoCriteria> m_criteriaInfosById;

	private Map<String, HAPIdElement> m_variableIdByName;
	
	private int m_nextId = 0;
	
	private HAPWithInternalValuePort m_withInternalValuePort;
	
	public HAPContainerVariableInfo(HAPWithInternalValuePort withInternalValuePort) {
		this.m_criteriaInfosById = new LinkedHashMap<HAPIdElement, HAPInfoCriteria>();
		this.m_variableIdByName = new LinkedHashMap<String, HAPIdElement>();
	}

	public HAPIdElement addVariable(String variableName, String varIODirection, HAPConfigureResolveElementReference resolveConfigure) {
		HAPIdElement out = this.m_variableIdByName.get(variableName);
		if(out==null) {
			out = HAPUtilityStructureElementReference.resolveNameFromInternal(variableName, varIODirection, resolveConfigure, this.m_withInternalValuePort).getElementId();
		}
		return out;
	}
	
	public boolean isEmpty() {	return this.m_variableIdByName.isEmpty();	}
	
	public Map<HAPIdElement, HAPInfoCriteria> getVariableCriteriaInfos() {   return this.m_criteriaInfosById; }
	
	public HAPIdElement getVariableId(String key) {    return this.m_variableIdByName.get(key);     }
	
	public HAPInfoCriteria getVaraibleCriteriaInfo(String key) {   return this.m_criteriaInfosById.get(this.m_variableIdByName.get(key));     }
	
	public String addVariable(HAPIdElement variableId) {
		if(variableId==null) {
			throw new RuntimeException();
		}
		
		if(m_criteriaInfosById.get(variableId)!=null) {
			//already exist
			for(String key : this.m_variableIdByName.keySet()) {
				if(variableId.equals(this.m_variableIdByName.get(key))) {
					return key;
				}
			}
			return null;
		}
		else {
			//brand new
			String key = this.m_nextId + "";
			this.m_nextId++;
			this.m_variableIdByName.put(key, variableId);
			this.m_criteriaInfosById.put(variableId, new HAPInfoCriteria());
			return key;
		}
	}
	
	public void addVariable(String variableKey, HAPIdElement variableId, HAPInfoCriteria criteria) {
		this.m_variableIdByName.put(variableKey, variableId);
		this.m_criteriaInfosById.put(variableId, criteria);
	}
	
	
	
	
	public Set<HAPIdElement> getVariablesId(){    return this.m_criteriaInfosById.keySet();     }
	
//	public HAPInfoCriteria getVariableCriteriaInfo(HAPIdElement variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	@Override
	public HAPContainerVariableInfo clone() {
		HAPContainerVariableInfo out = new HAPContainerVariableInfo();
		for(String key : this.m_variableIdByName.keySet()) {
			out.addVariable(key, this.getVariableId(key), this.getVaraibleCriteriaInfo(key));
		}
		return out;
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		Map<String, String> outMap = new LinkedHashMap<String, String>();

		for(String name : this.m_variableIdByName.keySet()) {
			Map<String, String> entryMap = new LinkedHashMap<String, String>();
			entryMap.put(VARIABLENAME, name);
			entryMap.put(VARIABLEID, m_variableIdByName.get(name).toStringValue(HAPSerializationFormat.JSON));
			entryMap.put(CRITERIA, this.m_criteriaInfosById.get(m_variableIdByName.get(name)).toStringValue(HAPSerializationFormat.JSON));
			outMap.put(name, HAPUtilityJson.buildMapJson(entryMap));
		}
		
		jsonMap.put(VARIABLES, HAPUtilityJson.buildMapJson(outMap));
	}

}
