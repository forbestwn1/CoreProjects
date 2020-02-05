package com.nosliw.servlet.utils.browseresource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPResourceNodeContainer extends HAPSerializableImp{

	private Map<String, HAPResourceNodeContainerByType> m_children;

	public HAPResourceNodeContainer() {
		this.m_children = new LinkedHashMap<String, HAPResourceNodeContainerByType>();
	}
	
	public void addResource(String type, HAPResourceNode resource) {
		HAPResourceNodeContainerByType byType = this.m_children.get(type);
		if(byType==null) {
			byType = new HAPResourceNodeContainerByType(type);
			this.m_children.put(type, byType);
		}
		byType.addElement(resource);
	}

	public void addTypeResourceContainer(HAPResourceNodeContainerByType byType) {
		this.m_children.put(byType.getType(), byType);
	}

	public boolean isEmpty() {   return this.m_children.isEmpty();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);

		String[] types = this.m_children.keySet().toArray(new String[0]);
		Arrays.sort(types);
		for(String type : types) {
			jsonMap.put(type, this.m_children.get(type).toStringValue(HAPSerializationFormat.JSON));
		}
	}	
}
