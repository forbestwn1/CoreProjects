package com.nosliw.core.application.common.valueport;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

public class HAPContainerVariableInfo extends HAPSerializableImp{

	public static String VARIABLES = "variables";

	public static String VARIABLEID = "variableId";

	public static String VARIABLENAME = "variableName";

	public static String CRITERIA = "criteria";

	private Map<HAPIdElement, HAPInfoCriteria> m_criteriaInfosById;

	private Map<String, Map<String, HAPIdElement>> m_variableIdByName;
	
	private int m_nextId = 0;
	
	private HAPWithInternalValuePort m_withInternalValuePort;
	
	public HAPContainerVariableInfo(HAPWithInternalValuePort withInternalValuePort) {
		this.m_withInternalValuePort = withInternalValuePort;
		this.m_criteriaInfosById = new LinkedHashMap<HAPIdElement, HAPInfoCriteria>();
		this.m_variableIdByName = new LinkedHashMap<String, Map<String, HAPIdElement>>();
	}

	public HAPIdElement addVariable(String variableName, String varIODirection, HAPConfigureResolveElementReference resolveConfigure) {
		HAPIdElement out = this.getVariableId(variableName, varIODirection);
		if(out==null) {
			out = HAPUtilityStructureElementReference.resolveNameFromInternal(variableName, varIODirection, resolveConfigure, this.m_withInternalValuePort).getElementId();
			Map<String, HAPIdElement> varIdByIoDirection = this.m_variableIdByName.get(variableName);
			if(varIdByIoDirection==null) {
				varIdByIoDirection = new LinkedHashMap<String, HAPIdElement>();
				this.m_variableIdByName.put(variableName, varIdByIoDirection);
			}
			varIdByIoDirection.put(varIODirection, out);
		}
		
		if(this.m_criteriaInfosById.get(out)==null) {
			this.m_criteriaInfosById.put(out, new HAPInfoCriteria());
		}
		
		return out;
	}
	
	public boolean isEmpty() {	return this.m_variableIdByName.isEmpty();	}
	
	public Map<HAPIdElement, HAPInfoCriteria> getVariableCriteriaInfos() {   return this.m_criteriaInfosById; }

	public HAPInfoCriteria getVaraibleCriteriaInfo(HAPIdElement varId) {   return this.m_criteriaInfosById.get(varId);     }

	public void addVariable(HAPIdElement variableId, HAPInfoCriteria criteria) {
		this.m_criteriaInfosById.put(variableId, criteria);
	}
	
	private HAPIdElement getVariableId(String variableName, String ioDirection) {
		HAPIdElement out = null;
		Map<String, HAPIdElement> varIdByIoDirection = this.m_variableIdByName.get(variableName);
		if(varIdByIoDirection!=null) {
			out = varIdByIoDirection.get(ioDirection);
		}
		return out;
	}
	
	@Override
	public HAPContainerVariableInfo clone() {
		HAPContainerVariableInfo out = new HAPContainerVariableInfo(this.m_withInternalValuePort);
		out.m_criteriaInfosById.putAll(this.m_criteriaInfosById);
		out.m_variableIdByName.putAll(this.m_variableIdByName);
		return out;
	}
	
	
	
	
	
	
	
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
	
	
	
	
	
	public Set<HAPIdElement> getVariablesId(){    return this.m_criteriaInfosById.keySet();     }
	
//	public HAPInfoCriteria getVariableCriteriaInfo(HAPIdElement variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	
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
