package com.nosliw.data.core.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPOutputStructure extends HAPSerializableImp{

	@HAPAttribute
	public static String CONTEXTSTRUCTURE = "contextStructure";

	private Map<String, HAPValueStructure> m_contextStructrue;

	public HAPOutputStructure() {
		this.m_contextStructrue = new LinkedHashMap<String, HAPValueStructure>();
	}

	public Set<String> getNames(){   return this.m_contextStructrue.keySet();   };
	
	public Map<String, HAPValueStructure> getOutputStructures() {		return this.m_contextStructrue;	}
	
	public HAPValueStructure getOutputStructure(String name) {   return this.m_contextStructrue.get(name);   }

	public HAPValueStructure getOutputStructure() {	return this.m_contextStructrue.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}
	
	public void addOutputStructure(String name, HAPValueStructure structure) {   this.m_contextStructrue.put(name, structure);   }

	public void addOutputStructure(HAPValueStructure structure) {   this.m_contextStructrue.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT, structure);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		for(Object key : jsonObj.keySet()) {
			this.m_contextStructrue.put((String)key, HAPParserContext.parseValueStructure(jsonObj.getJSONObject((String)key)));
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(CONTEXTSTRUCTURE, HAPJsonUtility.buildJson(this.m_contextStructrue, HAPSerializationFormat.JSON));
	}

}
