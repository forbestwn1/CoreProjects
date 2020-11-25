package com.nosliw.common.displayresource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPNamingConversionUtility;

@HAPEntityWithAttribute
public class HAPDisplayResourceNode extends HAPDisplayResource{

	@HAPAttribute
	public static String CHILDREN = "children";

	private Map<String, HAPDisplayResource> m_children;

	public HAPDisplayResourceNode() {
		this.m_children = new LinkedHashMap<String, HAPDisplayResource>();
	}
	
	public HAPDisplayResource getResource(String name) {
		if(HAPBasicUtility.isStringEmpty(name))  return this;
		String[] segs = HAPNamingConversionUtility.parsePaths(name);
		return this.getChild(segs, 0); 
	}

	public HAPDisplayResourceNode getResourceNode(String name) {
		if(HAPBasicUtility.isStringEmpty(name))  return this;
		String[] segs = HAPNamingConversionUtility.parsePaths(name);
		HAPDisplayResourceNode out = (HAPDisplayResourceNode)this.getChild(segs, 0);
		if(out==null)  out = new HAPDisplayResourceNode();
		return out;
	}

	public String getValue(String name) {
		HAPDisplayResource child = this.getResource(name);
		if(child!=null && child instanceof HAPDisplayResourceLeaf)  return ((HAPDisplayResourceLeaf)child).getValue();
		else return null;
	}
	
	private HAPDisplayResource getChild(String[] segs, int index) {
		HAPDisplayResource childResource = this.m_children.get(segs[index]);
		if(childResource==null)  return null;
		else {
			index++;
			if(index>=segs.length)   return childResource;
			if(childResource instanceof HAPDisplayResourceNode) {
				return ((HAPDisplayResourceNode)childResource).getChild(segs, index);
			}
			else {
				return null;
			}
		}
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		JSONObject resourceObj = (JSONObject)value;
		for(Object key : resourceObj.keySet()) {
			String name = (String)key;
			Object childResourceObj = resourceObj.get(name);
			HAPDisplayResource childResource = null;
			if(childResourceObj instanceof String) {
				childResource = new HAPDisplayResourceLeaf((String)childResourceObj);
			}
			else {
				childResource = new HAPDisplayResourceNode();
				childResource.buildObject(childResourceObj, format);
			}
			this.m_children.put(name, childResource);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String name : this.m_children.keySet()) {
			jsonMap.put(name, this.m_children.get(name).toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
