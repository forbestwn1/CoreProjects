package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

//mapping from external name to internal name of component 
public class HAPNameMapping {

	private Map<String, Map<String, String>> m_nameMapping;

	public HAPNameMapping() {
		this.m_nameMapping = new LinkedHashMap<String, Map<String, String>>();
	}

	public static HAPNameMapping newNamingMapping(String literate) {
		HAPNameMapping out = new HAPNameMapping();
		if(HAPUtilityBasic.isStringNotEmpty(literate)) {
			Map<String, String> nameMapping = HAPUtilityNamingConversion.parsePropertyValuePairs(literate);
			if(nameMapping!=null) {
				for(String complexName : nameMapping.keySet()) {
					String[] segs = HAPUtilityNamingConversion.parseNameSegments(complexName);
					String type = segs[0];
					String name = segs[1];
					out.addNameMapping(type, nameMapping.get(complexName), name);
				}
			}
		}
		return out;
	}
	
	public static HAPNameMapping newNamingMapping(JSONObject objJson) {
		HAPNameMapping out = new HAPNameMapping();
		if(objJson!=null) {
			for(Object typeKey : objJson.keySet()) {
				JSONObject byNameJson = objJson.getJSONObject((String)typeKey);
				for(Object nameKey : byNameJson.keySet()) {
					out.addNameMapping((String)typeKey, byNameJson.getString((String)nameKey), (String)nameKey);
				}
			}
		}
		return out;
	}	

	public HAPDefinitionEntityContainerAttachment mapAttachment(HAPDefinitionEntityContainerAttachment original) {
		HAPDefinitionEntityContainerAttachment mapped;
		if(m_nameMapping==null || m_nameMapping.isEmpty())  mapped = original;
		else {
			mapped = new HAPDefinitionEntityContainerAttachment();
			for(String type : m_nameMapping.keySet()) {
				Map<String, String> byParentName = m_nameMapping.get(type);
				for(String parentName : byParentName.keySet()) {
					String childName = byParentName.get(parentName);
					HAPAttachment ele = original.getElement(type, parentName);
					ele = ele.cloneAttachment();
					ele.setName(childName);
					mapped.addAttachment(type, ele);
				}
			}
		}
		return mapped;
	}
	
	public Map<String, ? extends HAPEntityInfo> mapEntity(Map<String, ? extends HAPEntityInfo> parent, String type) {
		if(m_nameMapping!=null && !m_nameMapping.isEmpty()) {
			Map<String, HAPEntityInfo> mappedParentProviders = new LinkedHashMap<String, HAPEntityInfo>();
			Map<String, String> mapping = m_nameMapping.get(type);
			if(mapping!=null && !mapping.isEmpty()) {
				for(String pName : mapping.keySet()) {
					HAPEntityInfoWritable provider = (HAPEntityInfoWritable)parent.get(pName).cloneEntityInfo();
					provider.setName(mapping.get(pName));
					mappedParentProviders.put(mapping.get(pName), provider);
				}
			}
			return mappedParentProviders;
		}
		else {
			return parent;
		}
	}
	
	private void addNameMapping(String type, String parent, String child) {
		Map<String, String> byName = this.m_nameMapping.get(type);
		if(byName==null) {
			byName = new LinkedHashMap<String, String>();
			this.m_nameMapping.put(type, byName);
		}
		byName.put(parent, child);
	}

	public HAPNameMapping cloneNameMapping() {
		HAPNameMapping out = new HAPNameMapping();
		out.m_nameMapping.putAll(this.m_nameMapping);
		return out;
	}
	
}
