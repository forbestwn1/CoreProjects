package com.nosliw.data.core.domain;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoContainerElementList extends HAPInfoContainerElement{

	public static final String INDEX = "index";

	private int m_index;
	
	public HAPInfoContainerElementList(HAPIdEntityInDomain entityId) {
		super(entityId);
	}

	public HAPInfoContainerElementList() {
		this.m_index = -1;
	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_LIST;    }
	
	public int getIndex() {   return this.m_index;    }
	public void setIndex(int index) {    this.m_index = index;     }
	
	@Override
	public HAPInfoContainerElement cloneContainerElementInfo() {
		HAPInfoContainerElementList out = new HAPInfoContainerElementList();
		this.cloneToInfoContainerElement(out);
		out.m_index = this.m_index;
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		Object indexObj = jsonObj.opt(INDEX);
		if(indexObj!=null && indexObj instanceof Integer) {
			this.m_index = (Integer)indexObj;
		}
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		if(this.m_index!=-1) {
			jsonMap.put(INDEX, this.m_index+"");
			typeJsonMap.put(INDEX, Integer.class);
		}
	}
	
	@Override
	protected void toExpanedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainDefinitionEntity entityDefDomain) {
		super.toExpanedJsonMap(jsonMap, typeJsonMap, entityDefDomain);
		jsonMap.put(INDEX, this.m_index+"");
		typeJsonMap.put(INDEX, Integer.class);
	}
}
