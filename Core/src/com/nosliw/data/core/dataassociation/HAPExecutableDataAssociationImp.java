package com.nosliw.data.core.dataassociation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPExecutableDataAssociationImp  extends HAPExecutableImpEntityInfo implements HAPExecutableDataAssociation{

	private String m_type;
	
	private String m_direction;

	private Set<String> m_fromEntities;
	
	private Set<String> m_toEntities;
	
	public HAPExecutableDataAssociationImp() {
		init();
	}
	
	public HAPExecutableDataAssociationImp(HAPDefinitionDataAssociation definition) {
		super(definition);
		init();
		this.m_direction = definition.getDirection();
		this.m_type = definition.getType();
	}
	
	private void init() {
		this.m_fromEntities = new HashSet<String>();
		this.m_toEntities = new HashSet<String>();
	}
	
	@Override
	public String getType() {  return this.m_type;  }

	@Override
	public String getDireciton() {    return this.m_direction;    }
	
	@Override
	public Set<String> getFromEntities(){   return this.m_fromEntities;    }
	public void addFromEntity(String entityIdPath) {   this.m_fromEntities.add(entityIdPath);    }
	
	@Override
	public Set<String> getToEntities(){    return this.m_toEntities;     }
	public void addToEntity(String entityIdPath) {    this.m_toEntities.add(entityIdPath);     }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		this.m_type = jsonObj.getString(TYPE);
		this.m_direction = jsonObj.getString(DIRECTION);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(DIRECTION, this.m_direction);
		jsonMap.put(FROMENTITY, HAPUtilityJson.buildJsonStringValue(this.m_fromEntities, HAPSerializationFormat.JSON));
		jsonMap.put(TOENTITY, HAPUtilityJson.buildJsonStringValue(this.m_toEntities, HAPSerializationFormat.JSON));
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);

//		Map<String, String> outputFlatMap = new LinkedHashMap<String, String>();
//		Map<String, Class<?>> outputFlatTypeMap = new LinkedHashMap<String, Class<?>>();
//		HAPOutputStructure outputStructure = this.getOutput();
//		for(String name : outputStructure.getNames()) {
//			outputFlatMap.put(name, this.getOutput().getOutputStructure(name).isFlat()+"");
//			outputFlatTypeMap.put(name, Boolean.class);
//		}
//		jsonMap.put(OUTPUT, HAPUtilityJson.buildMapJson(outputFlatMap, outputFlatTypeMap));
//
//
//		Map<String, String> inputFlatMap = new LinkedHashMap<String, String>();
//		Map<String, Class<?>> inputFlatTypeMap = new LinkedHashMap<String, Class<?>>();
//		for(String name : this.getInput().getStructureNames()) {
//			inputFlatMap.put(name, this.m_input.getStructure(name).isFlat()+"");
//			inputFlatTypeMap.put(name, Boolean.class);
//		}
//		jsonMap.put(INPUT, HAPUtilityJson.buildMapJson(inputFlatMap, inputFlatTypeMap));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}

}
