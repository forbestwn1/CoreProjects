package com.nosliw.data.core.dataassociation.mirror;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociationImp;
import com.nosliw.data.core.dataassociation.HAPOutputStructure;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPExecutableDataAssociationMirror extends HAPExecutableDataAssociationImp{

	@HAPAttribute
	public static String OUTPUT1 = "output";

	private HAPOutputStructure m_output;

	public HAPExecutableDataAssociationMirror() {}

	public HAPExecutableDataAssociationMirror(HAPDefinitionDataAssociationMirror definition, HAPContainerStructure input, HAPContainerStructure output) {
		super(definition, input, output);
		this.m_output = new HAPOutputStructure();
	}
	
	@Override
	public HAPOutputStructure getOutput() {  return this.m_output;  }
	
	public void addOutputStructure(String name, HAPValueStructure context) {
		this.m_output.addOutputStructure(name, context);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		this.m_output = new HAPOutputStructure();
		this.m_output.buildObject(jsonObj.getJSONObject(OUTPUT1), HAPSerializationFormat.JSON);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OUTPUT1, this.m_output.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
	}
}
