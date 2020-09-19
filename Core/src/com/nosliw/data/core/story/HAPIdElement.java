package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPIdElement extends HAPSerializableImp implements HAPReferenceElement{

	@HAPAttribute
	public static final String CATEGARY = "categary";

	@HAPAttribute
	public static final String ID = "id";

	private String m_categary;
	
	private String m_id;
	
	public HAPIdElement() {}
	
	public HAPIdElement(String categary, String id) {
		this.m_categary = categary;
		this.m_id = id;
	}
	
	@Override
	public String getEntityOrReferenceType() {  return HAPConstant.REFERENCE;  }

	public String getCategary() {    return this.m_categary;     }
	
	public String getId() {    return this.m_id;    }

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeLevel2(new String[]{this.getCategary(), this.getId()});
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPNamingConversionUtility.parseLevel2(literateValue);
		this.m_categary = segs[0];
		this.m_id = segs[1];
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_categary = jsonObj.getString(CATEGARY);
		this.m_id = jsonObj.getString(ID);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPIdElement) {
			HAPIdElement eleId = (HAPIdElement)obj;
			if(HAPBasicUtility.isEquals(this.m_categary, eleId.m_categary)) {
				if(HAPBasicUtility.isEquals(this.m_id, eleId.m_id)) {
					out = true;
				}
			}
		}
		return out;
	}
}
