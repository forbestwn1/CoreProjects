package com.nosliw.data.core.domain.container;

import java.util.Map;

import com.nosliw.data.core.domain.HAPEmbeded;

public abstract class HAPInfoContainerElementList<T extends HAPEmbeded> extends HAPInfoContainerElement<T>{

	public static final String INDEX = "index";

	private int m_index;
	
	public HAPInfoContainerElementList() {}
	
	public HAPInfoContainerElementList(T embeded) {
		super(embeded);
	}
	
	public int getIndex() {   return this.m_index;    }
	public void setIndex(int index) {    this.m_index = index;     }

	protected void cloneToInfoContainerElement(HAPInfoContainerElementList containerEleInfo) {
		super.cloneToInfoContainerElement(containerEleInfo);
		containerEleInfo.m_index = this.m_index;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INDEX, this.m_index+"");
		typeJsonMap.put(INDEX, Integer.class);
	}

}