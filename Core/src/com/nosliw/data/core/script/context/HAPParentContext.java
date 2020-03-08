package com.nosliw.data.core.script.context;

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
import com.nosliw.common.utils.HAPConstant;

public class HAPParentContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";
	
	private Map<String, HAPContextStructure> m_elements;
	private List<String> m_eleNames;
	
	public HAPParentContext() {
		this.m_elements = new LinkedHashMap<String, HAPContextStructure>();
		this.m_eleNames = new ArrayList<String>();
	}
	
	static public HAPParentContext createDefault(HAPContextStructure parent) {
		HAPParentContext out = new HAPParentContext();
		if(parent!=null)	out.addContext(null, parent);
		return out;
	}
	
	public HAPParentContext addContext(String name, HAPContextStructure context) {
		if(context==null)  return this;
		if(this.isSelf(name))  return this;   //ignore self parent
		
		if(HAPBasicUtility.isStringEmpty(name))  name = HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT;
		this.m_elements.put(name, context);
		this.m_eleNames.add(name);
		return this;
	}
	
	public boolean isEmpty() {  return this.m_eleNames.isEmpty();  }
	
	public HAPContextStructure getContext(String name) {	return this.m_elements.get(name);	}

	public HAPContextStructure getContext() {	return this.m_elements.get(HAPConstant.DATAASSOCIATION_RELATEDENTITY_DEFAULT);	}

	public List<String> getNames(){  return this.m_eleNames;  }	

	private boolean isSelf(String name) {	return HAPConstant.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name);  }
	
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
			HAPContextStructure ele = HAPParserContext.parseContextStructure(elesJsonObj.getJSONObject((String)key));
			this.addContext((String)key, ele);
		}
		return true;  
	}
}
