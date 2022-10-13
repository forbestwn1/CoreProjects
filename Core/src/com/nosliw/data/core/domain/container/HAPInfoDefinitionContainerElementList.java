package com.nosliw.data.core.domain.container;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPEmbededWithId;

public class HAPInfoDefinitionContainerElementList extends HAPInfoDefinitionContainerElement implements HAPInfoContainerElementList<HAPEmbededWithId>{

	private int m_index;
	
	public HAPInfoDefinitionContainerElementList(HAPEmbededWithId embededWithId) {
		super(embededWithId);
		this.m_index = -1;
	}

	public HAPInfoDefinitionContainerElementList() {
		this.m_index = -1;
	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_DEFINITION_LIST;    }

	@Override
	public int getIndex() {   return this.m_index;    }
	public void setIndex(int index) {    this.m_index = index;     }
	
	public HAPInfoDefinitionContainerElementList cloneContainerElementInfoList() {	return this.cloneContainerElementInfo();	}

	@Override
	public HAPInfoDefinitionContainerElementList cloneContainerElementInfo() {
		HAPInfoDefinitionContainerElementList out = new HAPInfoDefinitionContainerElementList();
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
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_index!=-1) {
			jsonMap.put(INDEX, this.m_index+"");
			typeJsonMap.put(INDEX, Integer.class);
		}
	}
	
	@Override
	protected void toExpanedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		super.toExpanedJsonMap(jsonMap, typeJsonMap, entityDomain);
		jsonMap.put(INDEX, this.m_index+"");
		typeJsonMap.put(INDEX, Integer.class);
	}
}
