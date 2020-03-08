package com.nosliw.data.core.script.context.dataassociation.mapping;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.script.context.dataassociation.HAPOutputStructure;

public class HAPExecutableDataAssociationMapping extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String ASSOCIATION = "association";

	@HAPAttribute
	public static String INPUTDEPENDENCY = "inputDependency";

	private Map<String, HAPExecutableAssociation> m_associations;
	
	private Set<String> m_inputDependency;
	
	public HAPExecutableDataAssociationMapping(HAPDefinitionDataAssociationMapping definition, HAPParentContext input) {
		super(definition, input);
		this.m_associations = new LinkedHashMap<String, HAPExecutableAssociation>();
		this.m_inputDependency = new HashSet<String>();
	}
	
	public Set<String> getInputDependency(){   return this.m_inputDependency;    }
	
	@Override
	public HAPParentContext getInput() {
		HAPParentContext out = new HAPParentContext();
		for(String dependencyName : this.m_inputDependency) {
			out.addContext(dependencyName, super.getInput().getContext(dependencyName));
		}
		return out;
	}

	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
		for(String name : this.m_associations.keySet()) {
			out.addOutputStructure(name, this.m_associations.get(name).getOutputContext());
		}
		return out;
	}

	public void addAssociation(String name, HAPExecutableAssociation association) {   this.m_associations.put(name, association);  }
	public HAPExecutableAssociation getAssociation(String name) {return this.m_associations.get(name);  }
	public HAPExecutableAssociation getAssociation() {return this.m_associations.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);  }
	public Map<String, HAPExecutableAssociation> getAssociations(){ return m_associations;  }
	
	public boolean isEmpty() {   return this.m_associations==null || this.m_associations.isEmpty();   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		JSONObject associationJsonObj = jsonObj.getJSONObject(ASSOCIATION);
		for(Object key : associationJsonObj.keySet()) {
			HAPExecutableAssociation assocation = new HAPExecutableAssociation();
			assocation.buildObject(associationJsonObj.getJSONObject((String)key), HAPSerializationFormat.JSON);
			this.m_associations.put((String)key, assocation);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_associations.keySet()) {
			assocationJsonMap.put(assosName, this.m_associations.get(assosName).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_associations.keySet()) {
			assocationJsonMap.put(assosName, this.m_associations.get(assosName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

}
