package com.nosliw.core.application.division.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPStoryIdElement extends HAPSerializableImp implements HAPStoryReferenceElement{

	@HAPAttribute
	public static final String CATEGARY = "categary";

	@HAPAttribute
	public static final String ID = "id";

	private String m_categary;
	
	private String m_id;
	
	public HAPStoryIdElement() {}
	
	public HAPStoryIdElement(String categary, String id) {
		this.m_categary = categary;
		this.m_id = id;
	}
	
	@Override
	public String getEntityOrReferenceType() {  return HAPConstantShared.REFERENCE;  }

	public String getCategary() {    return this.m_categary;     }
	
	public String getId() {    return this.m_id;    }

	@Override
	public HAPStoryReferenceElement cloneElementReference() {
		HAPStoryIdElement out = new HAPStoryIdElement();
		out.m_categary = this.m_categary;
		out.m_id = this.m_id;
		return out;
	}

	@Override
	protected String buildLiterate(){
		return HAPUtilityNamingConversion.cascadeLevel2(new String[]{this.getCategary(), this.getId()});
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] segs = HAPUtilityNamingConversion.parseLevel2(literateValue);
		this.m_categary = segs[0];
		this.m_id = segs[1];
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Object categaryObj = jsonObj.opt(CATEGARY);
		if(categaryObj!=null)  this.m_categary = (String)categaryObj;
		Object idObj = jsonObj.opt(ID);
		if(idObj!=null)  this.m_id = (String)idObj;
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
		if(obj instanceof HAPStoryIdElement) {
			HAPStoryIdElement eleId = (HAPStoryIdElement)obj;
			if(HAPUtilityBasic.isEquals(this.m_categary, eleId.m_categary)) {
				if(HAPUtilityBasic.isEquals(this.m_id, eleId.m_id)) {
					out = true;
				}
			}
		}
		return out;
	}
}
