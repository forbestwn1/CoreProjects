package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPParserValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPOutputStructure extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";

	private Map<String, HAPValueStructure> m_valueStructrue;

	public HAPOutputStructure() {
		this.m_valueStructrue = new LinkedHashMap<String, HAPValueStructure>();
	}

	public Set<String> getNames(){   return this.m_valueStructrue.keySet();   };
	
	public Map<String, HAPValueStructure> getOutputStructures() {		return this.m_valueStructrue;	}
	
	public HAPValueStructure getOutputStructure(String name) {   return this.m_valueStructrue.get(name);   }

	public HAPValueStructure getOutputStructure() {	return this.m_valueStructrue.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}
	
	public void addOutputStructure(String name, HAPValueStructure structure) {   this.m_valueStructrue.put(name, structure);   }

	public void addOutputStructure(HAPValueStructure structure) {   this.m_valueStructrue.put(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT, structure);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		for(Object key : jsonObj.keySet()) {
			this.m_valueStructrue.put((String)key, HAPParserValueStructure.parseValueStructure(jsonObj.getJSONObject((String)key)));
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUESTRUCTURE, HAPUtilityJson.buildJson(this.m_valueStructrue, HAPSerializationFormat.JSON));
	}

}
