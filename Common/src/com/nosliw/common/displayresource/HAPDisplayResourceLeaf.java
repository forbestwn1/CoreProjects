package com.nosliw.common.displayresource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPNamingConversionUtility;

@HAPEntityWithAttribute
public class HAPDisplayResourceLeaf extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUE = "value";

	private String m_value;
	
	public HAPDisplayResourceLeaf(String value) {
		this();
		this.m_value = value;
	}

	public HAPDisplayResourceLeaf() {}
	
	public HAPDisplayResourceLeaf getChild(String name) {
		if(HAPBasicUtility.isStringEmpty(name))  return this;
		String[] segs = HAPNamingConversionUtility.parsePaths(name);
		return this.getChild(segs, 0); 
	}
	
	public String getDisplayValue(String name) {
		HAPDisplayResourceLeaf child = this.getChild(name);
		if(child!=null)  return child.m_value;
		else return null;
	}
	
	private HAPDisplayResourceLeaf getChild(String[] segs, int index) {
		if(index>=segs.length)   return this;
		HAPDisplayResourceLeaf childResource = this.m_children.get(segs[index]);
		if(childResource==null)  return null;
		else {
			index++;
			return childResource.getChild(segs, index);
		}
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		if(value instanceof String)   this.m_value = (String)value;
		else {
			JSONObject resourceObj = (JSONObject)value;
			this.m_value = (String)resourceObj.opt(VALUE);
			
			JSONObject childrenResourceObj = resourceObj.optJSONObject(CHILDREN);
			for(Object key : childrenResourceObj.keySet()) {
				String name = (String)key;
				JSONObject childResourceObj = childrenResourceObj.getJSONObject(name);
				HAPDisplayResourceLeaf childResource = new HAPDisplayResourceLeaf();
				childResource.buildObject(childResourceObj, format);
			}
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUE, this.m_value);
		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(this.m_children, HAPSerializationFormat.JSON));
	}
}
