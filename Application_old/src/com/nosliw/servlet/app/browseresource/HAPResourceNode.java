package com.nosliw.servlet.app.browseresource;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceNode extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String RESOURCEID = "resourceId";

	@HAPAttribute
	public static String URL = "url";

	@HAPAttribute
	public static String ISVALID = "isValid";

	@HAPAttribute
	public static String CHILDREN = "children";

	private String m_name;

	private HAPResourceId m_resourceId;
	
	private String m_url;
	
	private HAPResourceNodeContainer m_children;

	private boolean m_isValid;
	
	public HAPResourceNode(String name) {
		this.m_name = name;
		this.m_children = new HAPResourceNodeContainer();
		this.m_isValid = true;
	}
	
	public String getName() {  return this.m_name;   }

	public void setResourceId(HAPResourceId resourceId) {	this.m_resourceId = resourceId;	}
	
	public void setUrl(String url) {	this.m_url = url;	}
	
	public void setIsValid(boolean valid) {    this.m_isValid = valid;    }
	public boolean getIsValid() {    return this.m_isValid;    }

	public void addChild(String type, HAPResourceNode node) {
		this.m_children.addResource(type, node);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
		if(this.m_url!=null)   jsonMap.put(URL, this.m_url);
		if(this.m_resourceId!=null) jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON_FULL));
		jsonMap.put(ISVALID, Boolean.toString(this.m_isValid));
		typeJsonMap.put(ISVALID, Boolean.class);
		if(!this.m_children.isEmpty())	jsonMap.put(CHILDREN, HAPUtilityJson.buildJson(this.m_children, HAPSerializationFormat.JSON));
	}

}
