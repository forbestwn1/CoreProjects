package com.nosliw.data.core.domain.container;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPEmbededWithExecutable;

public class HAPInfoExecutableContainerElementList extends HAPInfoExecutableContainerElement implements HAPInfoContainerElementList<HAPEmbededWithExecutable>{

	public static final String INDEX = "index";

	private int m_index;
	
	public HAPInfoExecutableContainerElementList(HAPEmbededWithExecutable embededEntity, String elementId) {
		super(embededEntity, elementId);
		this.m_index = -1;
	}

	public HAPInfoExecutableContainerElementList() {	
		this.m_index = -1;
	}

	@Override
	public String getInfoType() {  return HAPConstantShared.ENTITYCONTAINER_TYPE_EXECUTABLE_LIST;    }

	@Override
	public int getIndex() {   return this.m_index;    }
	public void setIndex(int index) {    this.m_index = index;     }

	@Override
	public HAPInfoExecutableContainerElementList cloneContainerElementInfo() {
		HAPInfoExecutableContainerElementList out = new HAPInfoExecutableContainerElementList();
		this.cloneToInfoContainerElement(out);
		return out;
	}
	
	protected void cloneToInfoContainerElement(HAPInfoExecutableContainerElementList containerEleInfo) {
		super.cloneToInfoContainerElement(containerEleInfo);
		containerEleInfo.m_index = this.m_index;
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
}
