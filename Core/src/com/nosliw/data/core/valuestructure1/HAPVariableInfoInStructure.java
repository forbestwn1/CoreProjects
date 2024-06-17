package com.nosliw.data.core.valuestructure1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.common.structure.HAPInfoAlias;
import com.nosliw.core.application.common.structure.HAPInfoVariable;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

//store all variable in structure
@HAPEntityWithAttribute
public class HAPVariableInfoInStructure extends HAPSerializableImp{

	@HAPAttribute
	public static String NAMESBYVARID = "namesByVarId";

	@HAPAttribute
	public static String VARIDBYNAME = "varIdByName";

	private Map<String, List<HAPInfoAlias>> m_rootNamesById;
	
	private Map<String, HAPInfoCriteria> m_criteriaInfosById;
	
	private Map<String, String> m_rootIdByName;
	
	
	
	private int m_index;
	
	public HAPVariableInfoInStructure() {
		this.m_index = 0;
		m_rootNamesById = new LinkedHashMap<String, List<HAPInfoAlias>>();
		m_criteriaInfosById = new LinkedHashMap<String, HAPInfoCriteria>();
		m_rootIdByName = new LinkedHashMap<String, String>();
	}
	
	public boolean addVariableCriteriaInfo(HAPInfoVariable varInfo) {
		return this.addVariableCriteriaInfo(varInfo.getCriteriaInfo(), varInfo.getIdPath().getFullName(), varInfo.getRootAliases());
	}
	
	public boolean addVariableCriteriaInfo(HAPInfoCriteria criteriaInfo, String varId, List<HAPInfoAlias> rootAliasesInfo) {
		Set<String> rootAliases = new HashSet<String>();
		for(HAPInfoAlias rootAliasInfo : rootAliasesInfo)  rootAliases.add(rootAliasInfo.getName());
		
		boolean override = false;
		HAPComplexPath varIdPath = new HAPComplexPath(varId);
		this.m_criteriaInfosById.put(varId, criteriaInfo);
		this.m_rootNamesById.put(varIdPath.getRoot(), rootAliasesInfo);
		for(String rootAlias : rootAliases) {
			String oldVarId = this.m_rootIdByName.get(rootAlias);
			if(oldVarId!=null) {
				//alias exist already, remove it first
				this.m_rootIdByName.remove(rootAlias);
				this.m_rootNamesById.get(oldVarId).remove(rootAlias);
				override = true;
			}
			
			this.m_rootIdByName.put(rootAlias, varIdPath.getRoot());
		}
		return override;
	}

	public void addVariableCriteriaInfo(HAPInfoCriteria criteriaInfo, String id, String alias) {
		List<HAPInfoAlias> aliases = new ArrayList<HAPInfoAlias>();
		aliases.add(new HAPInfoAlias(alias, 0));
		this.addVariableCriteriaInfo(criteriaInfo, id, aliases);
	}

	public HAPInfoVariable getVariableInfoByAlias(String name) {
		HAPComplexPath namePath = new HAPComplexPath(name);
		HAPComplexPath idPath = new HAPComplexPath(this.m_rootIdByName.get(namePath.getRoot()), namePath.getPath());
		return new HAPInfoVariable(this.m_criteriaInfosById.get(idPath.getFullName()), idPath, this.m_rootNamesById.get(idPath.getRoot()));
	}
	
	public Set<HAPInfoVariable> getAllVariables(){
		Set<HAPInfoVariable> out = new HashSet<HAPInfoVariable>();
		for(String varId : this.m_criteriaInfosById.keySet()) {
			HAPComplexPath idPath = new HAPComplexPath(varId);
			out.add(new HAPInfoVariable(this.m_criteriaInfosById.get(varId), idPath, this.m_rootNamesById.get(idPath.getRoot())));
		}
		return out;
	}
	
	public HAPInfoVariable getVariableInfoById(String varId){
		HAPComplexPath idPath = new HAPComplexPath(varId);
		return new HAPInfoVariable(this.m_criteriaInfosById.get(varId), idPath, this.m_rootNamesById.get(idPath.getRoot()));
	}
	
	public Map<String, HAPInfoCriteria> getVariableCriteriaInfos(){		return this.m_criteriaInfosById;	}
	
	public HAPInfoCriteria getVariableCriteriaInfoById(String id) {		return this.m_criteriaInfosById.get(id);	}
	
/*	
	public HAPUpdateName buildToVarIdNameUpdate() {   return new HAPUpdateNameMapRoot(this.m_varIdByName);   }

	public Set<String> getDataVariableNames(){		return this.m_varIdByName.keySet();	}
	
	public HAPContainerVariableCriteriaInfo buildSubContainer(Set<String> aliases) {
		Set<String> varIds = new HashSet<String>();
		for(String alias : aliases) {
			varIds.add(this.m_varIdByName.get(alias));
		}
		
		HAPContainerVariableCriteriaInfo out = new HAPContainerVariableCriteriaInfo();
		for(String varId : varIds) {
			out.addVariableCriteriaInfo(this.getVariableCriteriaInfoById(varId), varId, this.getVariableInfoById(varId));
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

*/
/*	
	@Override
	public HAPDefinitionContainerVariableCriteriaInfo clone() {
		HAPDefinitionContainerVariableCriteriaInfo out = new HAPDefinitionContainerVariableCriteriaInfo();
		
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
		HAPComplexPath updatedPath = new HAPComplexPath(nameUpdate.getUpdatedName(path.getRootName()), path.getPathStr());
		return updatedPath.getFullName();
	}
	
	private String generateId() {
		return this.m_index++ + "";
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(VARIDBYNAME, HAPManagerSerialize.getInstance().toStringValue(this.m_varIdByName, HAPSerializationFormat.JSON));

		Map<String, String> map = new LinkedHashMap<String, String>();
		for(String varId : this.m_namesByVarId.keySet()) {
			map.put(varId, HAPJsonUtility.buildJson(this.m_namesByVarId.get(varId), HAPSerializationFormat.JSON));
		}
		jsonMap.put(NAMESBYVARID, HAPJsonUtility.buildMapJson(map));
	}
*/	
}
