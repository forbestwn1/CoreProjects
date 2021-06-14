package com.nosliw.data.core.dataassociation.mapping;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.dataassociation.HAPOutputStructure;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPExecutableDataAssociationMapping extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String ASSOCIATION = "association";

	@HAPAttribute
	public static String INPUTDEPENDENCY = "inputDependency";

	private Map<String, HAPExecutableAssociation> m_mappings;
	
	private Set<String> m_inputDependency;

	public HAPExecutableDataAssociationMapping() {}
	
	public HAPExecutableDataAssociationMapping(HAPDefinitionDataAssociationMapping definition, HAPContainerStructure input) {
		super(definition, input);
		this.m_mappings = new LinkedHashMap<String, HAPExecutableAssociation>();
		this.m_inputDependency = new HashSet<String>();
	}
	
	public Set<String> getInputDependency(){   return this.m_inputDependency;    }
	
	@Override
	public HAPContainerStructure getInput() {
		HAPContainerStructure out = new HAPContainerStructure();
		for(String dependencyName : this.m_inputDependency) {
			out.addStructure(dependencyName, super.getInput().getStructure(dependencyName));
		}
		return out;
	}

	@Override
	public HAPOutputStructure getOutput() {
		HAPOutputStructure out = new HAPOutputStructure();
		for(String name : this.m_mappings.keySet()) {
			out.addOutputStructure(name, this.m_mappings.get(name).getOutputContext());
		}
		return out;
	}

	public void addMapping(String name, HAPExecutableAssociation mapping) {   this.m_mappings.put(name, mapping);  }
	public HAPExecutableAssociation getMapping(String name) {return this.m_mappings.get(name);  }
	public HAPExecutableAssociation getMapping() {return this.m_mappings.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);  }
	public Map<String, HAPExecutableAssociation> getMappings(){ return m_mappings;  }
	
	public boolean isEmpty() {   return this.m_mappings==null || this.m_mappings.isEmpty();   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		JSONObject associationJsonObj = jsonObj.getJSONObject(ASSOCIATION);
		for(Object key : associationJsonObj.keySet()) {
			HAPExecutableAssociation assocation = new HAPExecutableAssociation();
			assocation.buildObject(associationJsonObj.getJSONObject((String)key), HAPSerializationFormat.JSON);
			this.m_mappings.put((String)key, assocation);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_mappings.keySet()) {
			assocationJsonMap.put(assosName, this.m_mappings.get(assosName).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> assocationJsonMap = new LinkedHashMap<String, String>();
		for(String assosName : this.m_mappings.keySet()) {
			assocationJsonMap.put(assosName, this.m_mappings.get(assosName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ASSOCIATION, HAPJsonUtility.buildMapJson(assocationJsonMap));
	}

}
