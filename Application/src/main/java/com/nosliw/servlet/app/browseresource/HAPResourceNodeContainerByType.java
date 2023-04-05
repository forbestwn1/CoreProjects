package com.nosliw.servlet.app.browseresource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPResourceNodeContainerByType extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ELEMENTS = "elements";

	private String m_type;
	
	private List<HAPResourceNode> m_elements;

	public HAPResourceNodeContainerByType(String type) {
		this.m_type = type;
		this.m_elements = new ArrayList<HAPResourceNode>();
	}
	
	public String getType() {
		return this.m_type;
	}
	
	public void addElement(HAPResourceNode ele) {
		this.m_elements.add(ele);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		
		Collections.sort(this.m_elements, new Comparator<HAPResourceNode>(){
			@Override
			public int compare(HAPResourceNode arg0, HAPResourceNode arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		});
		jsonMap.put(ELEMENTS, HAPUtilityJson.buildJson(this.m_elements, HAPSerializationFormat.JSON));
	}
}
