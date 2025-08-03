package com.nosliw.core.application.common.withvariable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.valueport.HAPWithInternalValuePort;
import com.nosliw.core.xxx.application.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.xxx.application.valueport.HAPIdElement;
import com.nosliw.core.xxx.application.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.xxx.application1.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

public class HAPContainerVariableInfo extends HAPSerializableImp{

	public static String VARIABLES = "variables";

	public static String VARIABLEID = "variableId";

	public static String VARIABLENAME = "variableName";

	public static String CRITERIA = "criteria";

	private Map<String, Map<String, HAPIdElement>> m_variableIdByName;
	
	private Map<String, HAPInfoCriteria> m_criteriaInfosByKey;

	private Map<String, HAPIdElement> m_variableIdByKey;
	
	private Map<HAPIdElement, String> m_keyByvariableId;
	
	private HAPDomainValueStructure m_valueStructureDomain;
	
	private int m_nextId = 0;
	
	private HAPWithInternalValuePort m_withInternalValuePort;
	
	public HAPContainerVariableInfo(HAPWithInternalValuePort withInternalValuePort, HAPDomainValueStructure valueStructureDomain) {
		this.m_withInternalValuePort = withInternalValuePort;
		this.m_valueStructureDomain = valueStructureDomain;
		this.m_criteriaInfosByKey = new LinkedHashMap<String, HAPInfoCriteria>();
		this.m_variableIdByName = new LinkedHashMap<String, Map<String, HAPIdElement>>();
		this.m_variableIdByKey = new LinkedHashMap<String, HAPIdElement>();
		this.m_keyByvariableId = new LinkedHashMap<HAPIdElement, String>(); 
	}

	public String addVariable(String variableName, String varIODirection, HAPConfigureResolveElementReference resolveConfigure) {
		HAPIdElement eleId = this.getVariableId(variableName, varIODirection);
		if(eleId==null) {
			eleId = HAPUtilityStructureElementReference.resolveNameFromInternal(variableName, varIODirection, this.m_withInternalValuePort, resolveConfigure, this.m_valueStructureDomain).getElementId();
			Map<String, HAPIdElement> varIdByIoDirection = this.m_variableIdByName.get(variableName);
			if(varIdByIoDirection==null) {
				varIdByIoDirection = new LinkedHashMap<String, HAPIdElement>();
				this.m_variableIdByName.put(variableName, varIdByIoDirection);
			}
			varIdByIoDirection.put(varIODirection, eleId);
		}
		
		String key = this.m_keyByvariableId.get(eleId);
		if(key==null) {
			//variable id not exist
			key = this.m_nextId++ + "";
			this.m_criteriaInfosByKey.put(key, new HAPInfoCriteria());
			this.m_variableIdByKey.put(key, eleId);
			this.m_keyByvariableId.put(eleId, key);
		}
		
		return key;
	}
	
	public boolean isEmpty() {	return this.m_variableIdByName.isEmpty();	}
	
	public HAPInfoCriteria getVaraibleCriteriaInfo(String key) {   return this.m_criteriaInfosByKey.get(key);     }
	public Map<String, HAPInfoCriteria> getVariableCriteriaInfos(){   return this.m_criteriaInfosByKey;     }
	public HAPIdElement getVariableId(String key) {    return this.m_variableIdByKey.get(key);      }
	public Map<String, HAPIdElement> getVariables(){   return this.m_variableIdByKey;      }
	
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
		HAPContainerVariableInfo out = new HAPContainerVariableInfo(this.m_withInternalValuePort, this.m_valueStructureDomain);
		out.m_criteriaInfosByKey.putAll(this.m_criteriaInfosByKey);
		out.m_variableIdByName.putAll(this.m_variableIdByName);
		return out;
	}
	
	
	
	
	
	
	
	public String addVariable(HAPIdElement variableId) {
		if(variableId==null) {
			throw new RuntimeException();
		}
		
		if(m_criteriaInfosByKey.get(variableId)!=null) {
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
			this.m_criteriaInfosByKey.put(variableId, new HAPInfoCriteria());
			return key;
		}
	}
	
	
	
	
	
	public Set<HAPIdElement> getVariablesId(){    return this.m_criteriaInfosByKey.keySet();     }
	
//	public HAPInfoCriteria getVariableCriteriaInfo(HAPIdElement variableId) {     return this.m_criteriaInfosById.get(variableId);     }
	
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		Map<String, String> outMap = new LinkedHashMap<String, String>();

		for(String name : this.m_variableIdByName.keySet()) {
			Map<String, String> entryMap = new LinkedHashMap<String, String>();
			entryMap.put(VARIABLENAME, name);
			entryMap.put(VARIABLEID, m_variableIdByName.get(name).toStringValue(HAPSerializationFormat.JSON));
			entryMap.put(CRITERIA, this.m_criteriaInfosByKey.get(m_variableIdByName.get(name)).toStringValue(HAPSerializationFormat.JSON));
			outMap.put(name, HAPUtilityJson.buildMapJson(entryMap));
		}
		
		jsonMap.put(VARIABLES, HAPUtilityJson.buildMapJson(outMap));
	}

}
