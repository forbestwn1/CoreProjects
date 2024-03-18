package com.nosliw.data.core.dataassociation.mapping1;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPParserStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;

public class HAPValueMapping extends HAPSerializableImp{

	@HAPAttribute
	public static final String MAPPING = "mapping";
	
	private Map<String, HAPRootStructure> m_items;

	public HAPValueMapping() {
		this.m_items = new LinkedHashMap<String, HAPRootStructure>();
	}
	
	public void addMapping(String path, HAPElementStructure structureEle) {
		HAPRootStructure root = null;
		HAPComplexPath cPath = new HAPComplexPath(path);
		root = this.m_items.get(cPath.getRoot());
		if(root==null) {
			root = new HAPRootStructure();
			this.m_items.put(cPath.getRoot(), root);
		}
		HAPUtilityStructure.setDescendant(root, cPath.getPath(), structureEle);
	}
	
	public void addItem(String targetName, HAPRootStructure item) {
		this.m_items.put(targetName, item);
	}
	
	public Map<String, HAPRootStructure> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }
	
	public HAPValueMapping cloneValueMapping() {
		HAPValueMapping out = new HAPValueMapping();
		for(String targetName : this.m_items.keySet()) {
			out.addItem(targetName, this.m_items.get(targetName).cloneRoot());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			JSONObject jsonMapping = jsonObj.optJSONObject(MAPPING);
			if(jsonMapping==null)  jsonMapping = jsonObj;
			for(Object key : jsonMapping.keySet()) {
				String name = (String)key;
				HAPRootStructure root = HAPParserStructure.parseStructureRootFromJson(jsonMapping.getJSONObject(name));
				this.addItem(name, root);
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAPPING, HAPUtilityJson.buildJson(this.m_items, HAPSerializationFormat.JSON));
	}
	
}
