package com.nosliw.data.core.structure;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public class HAPContainerStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String STRUCTURE = "structure";
	
	private Map<String, HAPStructure> m_structures;
	private List<String> m_structureNames;
	
	public HAPContainerStructure() {
		this.m_structures = new LinkedHashMap<String, HAPStructure>();
		this.m_structureNames = new ArrayList<String>();
	}
	
	static public HAPContainerStructure createDefault(HAPValueStructureDefinition parent) {
		HAPContainerStructure out = new HAPContainerStructure();
		if(parent!=null)	out.addStructure(null, parent);
		return out;
	}
	
	public HAPContainerStructure addStructure(String name, HAPStructure structure) {
		if(structure==null)  return this;
		if(this.isSelf(name))  return this;   //ignore self parent
		
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_structures.put(name, structure);
		this.m_structureNames.add(name);
		return this;
	}
	
	public boolean isEmpty() {  return this.m_structureNames.isEmpty();  }
	
	public HAPStructure getStructure(String name) {	return this.m_structures.get(name);	}

	public HAPStructure getStructure() {	return this.m_structures.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}

	public List<String> getStructureNames(){  return this.m_structureNames;  }	

	private boolean isSelf(String name) {	return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name);  }
	
	public HAPContainerStructure cloneStructureContainer() {
		HAPContainerStructure out = new HAPContainerStructure();
		for(String name : this.getStructureNames()) {
			out.addStructure(name, this.getStructure(name));
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(STRUCTURE, HAPJsonUtility.buildJson(this.m_structures, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		JSONObject elesJsonObj = jsonObj.getJSONObject(STRUCTURE);
		for(Object key : elesJsonObj.keySet()) {
			HAPValueStructureDefinition ele = HAPParserStructure.parseValueStructureDefinition(elesJsonObj.getJSONObject((String)key));
			this.addStructure((String)key, ele);
		}
		return true;  
	}
}
