package com.nosliw.uiresource.page.tag;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.matcher.HAPMatchersCombo;

@HAPEntityWithAttribute
public class HAPUITagInfo extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ATTRIBUTES = "attributes";

	@HAPAttribute
	public static final String MATCHERS = "matchers";
	
	private String m_tag;
	
	private Map<String, String> m_attributes;

	private HAPMatchersCombo m_matchers;

	public HAPUITagInfo() {
		this.m_attributes = new LinkedHashMap<String, String>();
		this.m_matchers = new HAPMatchersCombo();
	}

	public HAPUITagInfo(HAPUITagDefinition tagDef) {
		this();
		tagDef.cloneToEntityInfo(this);
	}
	
	public Map<String, String> getAttributes(){   return this.m_attributes;    }

	public void addMatchers(String name, HAPMatchers matchers) {   
		this.m_matchers.addMatchers(name, matchers);    
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTES, HAPJsonUtility.buildJson(this.m_attributes, HAPSerializationFormat.JSON));
		jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}
	
//	@Override
//	protected boolean buildObjectByJson(Object json){
//		JSONObject jsonObj = (JSONObject)json;
//		this.m_tag = jsonObj.getString(TAG);
//		JSONObject attrsObj = jsonObj.optJSONObject(ATTRIBUTES);
//		if(attrsObj!=null) {
//			for(Object key : attrsObj.keySet()) {
//				this.m_attributes.put((String)key, attrsObj.getString((String)key));
//			}
//		}
//		return true;  
//	}	
}
