package com.nosliw.data.core.script.context;

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

//flat context represent result of flat a context group to context
//one root in context group named Abc will generate two element in context: local Abc and global Abc___categary
//A has local relative parent of A__categary
//Also, we can find global name by local name
//flat context is back compatible with context
@HAPEntityWithAttribute
public class HAPContextFlat extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONTEXT = "context";

	@HAPAttribute
	public static String NAMESBYVARID = "namesByVarId";

	@HAPAttribute
	public static String VARIDBYNAME = "varIdByName";
	
	private HAPContext m_context;
	
	private Map<String, String> m_varIdByName;
	
	private Map<String, Set<String>> m_namesByVarId;
	
	public HAPContextFlat() {
		this.m_context = new HAPContext();
		this.m_varIdByName = new LinkedHashMap<String, String>();
		this.m_namesByVarId = new LinkedHashMap<String, Set<String>>();
	}

	public HAPContextFlat(HAPContext context) {
		this.m_context = context;
		this.m_varIdByName = new LinkedHashMap<String, String>();
	}

	public void addElement(HAPContextDefinitionRoot element, String id) {	this.addElement(element, id, null);	}
	
	public void addElement(HAPContextDefinitionRoot element, String id, Set<String> aliases) {
		this.m_context.addElement(id, element);
		Set<String> allNames = new HashSet<String>(aliases);
		allNames.add(id);
		this.m_namesByVarId.put(id, allNames);
		for(String name : allNames) {
			this.m_varIdByName.put(name, id);
		}
	}

	public HAPContextDefinitionRoot getElement(String name) {
		String id = this.m_varIdByName.get(name);
		if(id==null)  return null;
		else return this.m_context.getElement(id);
	}
	
	public Set<HAPContextDefinitionRoot> getAllElements(){
		return new HashSet<HAPContextDefinitionRoot>(this.m_context.getElements().values());
	}
	
	public Set<String> getAllNames(){
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_varIdByName.keySet());
		return out;
	}
	
	public void updateRootName(HAPUpdateName nameUpdate) {
		this.m_context.updateRootName(nameUpdate);
		
		{
			Map<String, String> updated = new LinkedHashMap<String, String>();
			for(String name : this.m_varIdByName.keySet()) {
				updated.put(updateAliasRootName(name, nameUpdate), nameUpdate.getUpdatedName(this.m_varIdByName.get(name)));
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
	
	private String updateAliasRootName(String aliasRootName, HAPUpdateName nameUpdate) {
		HAPComplexPath path = new HAPComplexPath(aliasRootName);
		HAPComplexPath updatedPath = new HAPComplexPath(nameUpdate.getUpdatedName(path.getRootName()), path.getPath());
		return updatedPath.getFullName();
	}

	public Map<String, Object> getConstantValue(){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, Object> constantsById = this.m_context.getConstantValue();
		for(String id : constantsById.keySet()) {
			Object constantValue = constantsById.get(id);
			for(String alias : this.m_varIdByName.keySet()) {
				if(id.equals(this.m_varIdByName.get(alias))) {
					out.put(alias, constantValue);
				}
			}
		}
		return out;
	}
	
	public Set<String> getAliasById(String id){
		Set<String> out = new HashSet<String>();
		for(String alias : this.m_varIdByName.keySet()) {
			if(id.equals(this.m_varIdByName.get(alias))) {
				out.add(alias);
			}
		}
		return out;
	}
	
	public HAPContext getContext() {  return this.m_context;  }
	
	public HAPContextFlat cloneContextFlat() {
		HAPContextFlat out = new HAPContextFlat();
		out.m_varIdByName.putAll(this.m_varIdByName);
		for(String varId : this.m_namesByVarId.keySet()) {
			out.m_namesByVarId.put(varId, new HashSet<String>(this.m_namesByVarId.get(varId)));
		}
		out.m_context = this.m_context.cloneContext();
		return null;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VARIDBYNAME, HAPSerializeManager.getInstance().toStringValue(this.m_varIdByName, HAPSerializationFormat.JSON));

		Map<String, String> map = new LinkedHashMap<String, String>();
		for(String varId : this.m_namesByVarId.keySet()) {
			map.put(varId, HAPJsonUtility.buildJson(this.m_namesByVarId.get(varId), HAPSerializationFormat.JSON));
		}
		jsonMap.put(NAMESBYVARID, HAPJsonUtility.buildMapJson(map));
	}
}
