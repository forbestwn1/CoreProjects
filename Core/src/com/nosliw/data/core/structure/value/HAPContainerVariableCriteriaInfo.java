package com.nosliw.data.core.structure.value;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNameMapRoot;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

@HAPEntityWithAttribute
public class HAPContainerVariableCriteriaInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String NAMESBYVARID = "namesByVarId";

	@HAPAttribute
	public static String VARIDBYNAME = "varIdByName";

	private Map<String, Set<String>> m_namesByVarId;
	
	private Map<String, HAPInfoCriteria> m_criteriaInfosById;
	
	private Map<String, String> m_varIdByName;
	
	private int m_index;
	
	public HAPContainerVariableCriteriaInfo() {
		this.m_index = 0;
		m_namesByVarId = new LinkedHashMap<String, Set<String>>();
		m_criteriaInfosById = new LinkedHashMap<String, HAPInfoCriteria>();
		m_varIdByName = new LinkedHashMap<String, String>();
	}
	
	public boolean addVariableCriteriaInfo(HAPInfoCriteria criteriaInfo, String varId, Set<String> aliases) {
		aliases = new HashSet<String>(aliases);
		boolean override = false;
		this.m_criteriaInfosById.put(varId, criteriaInfo);
		this.m_namesByVarId.put(varId, aliases);
		for(String alias : aliases) {
			String oldVarId = this.m_varIdByName.get(alias);
			if(oldVarId!=null) {
				//alias exist already, remove it first
				this.m_varIdByName.remove(alias);
				this.m_namesByVarId.get(oldVarId).remove(alias);
				override = true;
			}
			
			this.m_varIdByName.put(alias, varId);
		}
		return override;
	}

	public void addVariableCriteriaInfo(HAPInfoCriteria criteriaInfo, String id, String alias) {
		Set<String> aliases = new HashSet<String>();
		aliases.add(alias);
		this.addVariableCriteriaInfo(criteriaInfo, id, aliases);
	}

	public HAPUpdateName buildToVarIdNameUpdate() {   return new HAPUpdateNameMapRoot(this.m_varIdByName);   }
	
	public Set<String> getVariableAlias(String varId){		return this.m_namesByVarId.get(varId);	}

	public Set<String> getDataVariableNames(){		return this.m_varIdByName.keySet();	}
	
	public Map<String, HAPInfoCriteria> getVariableCriteriaInfos(){		return this.m_criteriaInfosById;	}
	
	public HAPInfoCriteria getVariableCriteriaInfoByAlias(String name) {		return this.m_criteriaInfosById.get(this.m_varIdByName.get(name));	}
	
	
	public HAPInfoCriteria getVariableCriteriaInfoById(String id) {		return this.m_criteriaInfosById.get(id);	}
	
	public HAPContainerVariableCriteriaInfo buildSubContainer(Set<String> aliases) {
		Set<String> varIds = new HashSet<String>();
		for(String alias : aliases) {
			varIds.add(this.m_varIdByName.get(alias));
		}
		
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(String varId : varIds) {
			out.addVariableCriteriaInfo(this.getVariableCriteriaInfoById(varId), varId, this.getVariableAlias(varId));
		}
		return out;
	}
	
	public HAPContainerVariableCriteriaInfo groupVariables(Set<String> aliases) {
		Map<String, Set<String>> group = new LinkedHashMap<String, Set<String>>();
		for(String alias : aliases) {
			String varId = this.m_varIdByName.get(alias);
			Set<String> groupAliases = group.get(varId);
			if(groupAliases==null) {
				groupAliases = new HashSet<String>();
				group.put(varId, groupAliases);
			}
			groupAliases.add(alias);
		}
		
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(String varId : group.keySet()) {
			out.addVariableCriteriaInfo(this.m_criteriaInfosById.get(varId), varId, group.get(varId));
		}
		return out;
	}
	
	public Set<String> findMissingVariables(Set<String> aliases){
		Set<String> out = new HashSet<String>(this.m_criteriaInfosById.keySet());
		for(String alias : aliases) {
			String varId = this.m_varIdByName.get(alias);
			out.remove(varId);
		}
		return out;
	}
	
	public void updateRootVariableName(HAPUpdateName nameUpdate) {
		{
			Map<String, String> updated = new LinkedHashMap<String, String>();
			for(String name : this.m_varIdByName.keySet()) {
				updated.put(updateAliasRootName(name, nameUpdate), this.m_varIdByName.get(name));
			}
			this.m_varIdByName = updated;
		}
		
		{
			Map<String, Set<String>> updated = new LinkedHashMap<String, Set<String>>();
			for(String varId : this.m_namesByVarId.keySet()) {
				Set<String> oldNames = this.m_namesByVarId.get(varId);
				Set<String> updatedNames = new HashSet<String>();
				for(String oldName : oldNames) {
					updatedNames.add(nameUpdate.getUpdatedName(oldName));
				}
				updated.put(varId, updatedNames);
			}
			this.m_namesByVarId = updated;
		}
	}

	@Override
	public HAPContainerVariableCriteriaInfo clone() {
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		
		for(String varId : this.m_namesByVarId.keySet()) {
			out.m_namesByVarId.put(varId, new HashSet<String>(this.m_namesByVarId.get(varId)));
		}
		
		for(String varId : this.m_criteriaInfosById.keySet()) {
			out.m_criteriaInfosById.put(varId, this.m_criteriaInfosById.get(varId).cloneCriteriaInfo());
		}
		
		out.m_varIdByName.putAll(this.m_varIdByName);
		
		return out;
	}
	
	private String updateAliasRootName(String aliasRootName, HAPUpdateName nameUpdate) {
		HAPComplexPath path = new HAPComplexPath(aliasRootName);
		HAPComplexPath updatedPath = new HAPComplexPath(nameUpdate.getUpdatedName(path.getRootName()), path.getPath());
		return updatedPath.getFullName();
	}
	
	private String generateId() {
		return this.m_index++ + "";
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(VARIDBYNAME, HAPSerializeManager.getInstance().toStringValue(this.m_varIdByName, HAPSerializationFormat.JSON));

		Map<String, String> map = new LinkedHashMap<String, String>();
		for(String varId : this.m_namesByVarId.keySet()) {
			map.put(varId, HAPJsonUtility.buildJson(this.m_namesByVarId.get(varId), HAPSerializationFormat.JSON));
		}
		jsonMap.put(NAMESBYVARID, HAPJsonUtility.buildMapJson(map));
	}
}
