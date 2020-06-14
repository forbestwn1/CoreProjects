package com.nosliw.uiresource.page.execute;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

@HAPEntityWithAttribute
public class HAPExecutableUIUnit extends HAPExecutableImp{

	@HAPAttribute
	public static final String ID = "id";

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";

	@HAPAttribute
	public static final String BODYUNIT = "bodyUnit";

	protected HAPExecutableUIUnit m_parent;

	private String m_type;
	
	private String m_id;
	
	private HAPDefinitionUIUnit m_uiUnitDefinition;

	//store all the constant attribute for this domain
	//for customer tag, they are the tag's attribute
	//for resource, they are the attribute of body
	private Map<String, String> m_attributes;

	private HAPExecutableUIBody m_body;

	public HAPExecutableUIUnit(HAPDefinitionUIUnit uiUnitDefinition, String id) {
		this.m_uiUnitDefinition = uiUnitDefinition;
		this.m_type = this.m_uiUnitDefinition.getType();
		this.m_id = id;
		this.m_attributes = new LinkedHashMap<String, String>();
		Map<String, String> attrs = this.m_uiUnitDefinition.getAttributes();
		for(String attrName : attrs.keySet()) {
			this.addAttribute(attrName, attrs.get(attrName));
		}
		this.m_body = new HAPExecutableUIBody(this.m_uiUnitDefinition, this);
	}

	public String getType() {  return this.m_type;  }
	public String getId() {   return this.m_id;    }
	public Map<String, String> getAttributes(){   return this.m_attributes;    }
	public void addAttribute(String name, String value) {   this.m_attributes.put(name, value);   }
	public HAPExecutableUIBody getBody() {   return this.m_body;   }
	
	public HAPDefinitionUIUnit getUIUnitDefinition() {	return this.m_uiUnitDefinition;	}
	
	public HAPExecutableUIUnit getParent() {  return this.m_parent;   }
	public void setParent(HAPExecutableUIUnit parent) {		this.m_parent = parent;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.getId());
		jsonMap.put(TYPE, String.valueOf(this.getType()));
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildMapJson(this.m_uiUnitDefinition.getAttributes()));
		jsonMap.put(BODYUNIT, this.m_body.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		jsonMap.put(BODYUNIT, this.m_body.toResourceData(runtimeInfo).toString());
	}

}
