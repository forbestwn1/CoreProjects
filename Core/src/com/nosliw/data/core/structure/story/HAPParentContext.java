package com.nosliw.data.core.structure.story;

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
import com.nosliw.data.core.structure.HAPParserContext;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;

public class HAPParentContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPContextStructureValueDefinition> m_elements;
	private List<String> m_eleNames;
	
	public HAPParentContext() {
		this.m_elements = new LinkedHashMap<String, HAPContextStructureValueDefinition>();
		this.m_eleNames = new ArrayList<String>();
	}
	
	static public HAPParentContext createDefault(HAPContextStructureValueDefinition parent) {
		HAPParentContext out = new HAPParentContext();
		if(parent!=null)	out.addContext(null, parent);
		return out;
	}
	
	public HAPParentContext addContext(String name, HAPContextStructureValueDefinition context) {
		if(context==null)  return this;
		if(this.isSelf(name))  return this;   //ignore self parent
		
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_elements.put(name, context);
		this.m_eleNames.add(name);
		return this;
	}
	
	public boolean isEmpty() {  return this.m_eleNames.isEmpty();  }
	
	public HAPContextStructureValueDefinition getContext(String name) {	return this.m_elements.get(name);	}

	public HAPContextStructureValueDefinition getContext() {	return this.m_elements.get(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}

	public List<String> getNames(){  return this.m_eleNames;  }	

	private boolean isSelf(String name) {	return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name);  }
	
	public HAPParentContext cloneParentContext() {
		HAPParentContext out = new HAPParentContext();
		for(String name : this.getNames()) {
			out.addContext(name, this.getContext(name));
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		JSONObject elesJsonObj = jsonObj.getJSONObject(ELEMENT);
		for(Object key : elesJsonObj.keySet()) {
			HAPContextStructureValueDefinition ele = HAPParserContext.parseValueStructureDefinition(elesJsonObj.getJSONObject((String)key));
			this.addContext((String)key, ele);
		}
		return true;  
	}
}
