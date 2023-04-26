package com.nosliw.data.core.domain.entity.dataassociation.mapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPDefinitionValueMapping extends HAPSerializableImp{

	@HAPAttribute
	public static final String MAPPING = "mapping";
	
	private List<HAPItemValueMapping<HAPReferenceElementInValueContext>> m_items;

	public HAPDefinitionValueMapping() {
		this.m_items = new LinkedList<HAPItemValueMapping<HAPReferenceElementInValueContext>>();
	}
	
//	public void addMapping(String path, HAPElementStructure structureEle) {
//		HAPRootStructure root = null;
//		HAPComplexPath cPath = new HAPComplexPath(path);
//		root = this.m_items.get(cPath.getRoot());
//		if(root==null) {
//			root = new HAPRootStructure();
//			this.m_items.put(cPath.getRoot(), root);
//		}
//		HAPUtilityStructure.setDescendant(root, cPath.getPath(), structureEle);
//	}
	
	public void addItem(HAPItemValueMapping<HAPReferenceElementInValueContext> item) { 	this.m_items.add(item); 	}
	
	public List<HAPItemValueMapping<HAPReferenceElementInValueContext>> getItems(){   return this.m_items;    }

	public boolean isEmpty() {   return this.getItems().isEmpty();   }
	
	public HAPDefinitionValueMapping cloneValueMapping() {
		HAPDefinitionValueMapping out = new HAPDefinitionValueMapping();
		for(HAPItemValueMapping item : this.m_items) {
			out.addItem(item.cloneValueMappingItem());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			Object mappingObj = jsonObj.opt(MAPPING);
			if(mappingObj==null)   mappingObj = jsonObj;
			
			List<HAPItemValueMapping<HAPReferenceElementInValueContext>> items = HAPParserValueMapping.parses(mappingObj);
			this.m_items.addAll(items);
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
