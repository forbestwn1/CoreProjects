package com.nosliw.data.core.script.context;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
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

	//used to map from local (abc) to global name (public_abc)
	@HAPAttribute
	public static final String ALIAS2ID = "alias2Id";
	
	private HAPContext m_context;
	
	private Map<String, String> m_alias2Id;
	
	public HAPContextFlat() {
		this.m_context = new HAPContext();
		this.m_alias2Id = new LinkedHashMap<String, String>();
	}

	public HAPContextFlat(HAPContext context) {
		this.m_context = context;
		this.m_alias2Id = new LinkedHashMap<String, String>();
	}

	public void addElement(HAPContextDefinitionRoot element, String id) {	this.addElement(element, id, null);	}
	
	public void addElement(HAPContextDefinitionRoot element, String id, Set<String> aliases) {
		this.m_context.addElement(id, element);
		this.m_alias2Id.put(id, id);
		if(aliases!=null) {
			for(String alias : aliases) {
				this.m_alias2Id.put(alias, id);
			}
		}
	}

	public HAPContextDefinitionRoot getElement(String name) {
		String id = this.m_alias2Id.get(name);
		if(id==null)  return null;
		else return this.m_context.getElement(id);
	}
	
	public Set<HAPContextDefinitionRoot> getAllElements(){
		return new HashSet<HAPContextDefinitionRoot>(this.m_context.getElements().values());
	}
	
	public Set<String> getAllNames(){
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_alias2Id.keySet());
		return out;
	}
	
	public void updateRootName(HAPUpdateName nameUpdate) {
		this.m_context.updateRootName(nameUpdate);
		
		//update name in local2Global mapping
		Map<String, String> newMapping = new LinkedHashMap<String, String>();
		for(String name : this.m_alias2Id.keySet()) {
			newMapping.put(nameUpdate.getUpdatedName(name), nameUpdate.getUpdatedName(this.m_alias2Id.get(name)));
		}
		this.m_alias2Id = newMapping;
	}
	
	public Map<String, Object> getConstantValue(){
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, Object> constantsById = this.m_context.getConstantValue();
		for(String id : constantsById.keySet()) {
			Object constantValue = constantsById.get(id);
			for(String alias : this.m_alias2Id.keySet()) {
				if(id.equals(this.m_alias2Id.get(alias))) {
					out.put(alias, constantValue);
				}
			}
		}
		return out;
	}
	
	public Set<String> getAliasById(String id){
		Set<String> out = new HashSet<String>();
		for(String alias : this.m_alias2Id.keySet()) {
			if(id.equals(this.m_alias2Id.get(alias))) {
				out.add(alias);
			}
		}
		return out;
	}
	
	
	
	public HAPContext getContext() {  return this.m_context;  }
	
	public HAPContextFlat cloneContextFlat() {
		HAPContextFlat out = new HAPContextFlat();
		out.m_alias2Id.putAll(this.m_alias2Id);
		out.m_context = this.m_context.cloneContext();
		return null;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ALIAS2ID, HAPJsonUtility.buildMapJson(this.m_alias2Id));
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}
}
