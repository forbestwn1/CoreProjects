package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPContainerEntityImp<T extends HAPInfoContainerElement>  extends HAPSerializableImp implements HAPContainerEntity<T>{

	private Map<String, T> m_eleByName;
	private Map<HAPIdEntityInDomain, T> m_eleById;

	public HAPContainerEntityImp() {
		this.m_eleByName = new LinkedHashMap<String, T>();
		this.m_eleById = new LinkedHashMap<HAPIdEntityInDomain, T>();
	}
	
	@Override
	public void addEntityElement(T eleInfo) {
		this.m_eleByName.put(eleInfo.getElementName(), eleInfo);
		this.m_eleById.put(eleInfo.getEmbededElementEntity().getEntityId(), eleInfo);
	}

	@Override
	public T getElementInfoByName(String name) {  return this.m_eleByName.get(name);  }

	@Override
	public T getElementInfoById(HAPIdEntityInDomain id) {  return this.m_eleById.get(id);  }

	@Override
	public List<T> getAllElementsInfo() {  return new ArrayList(this.m_eleById.values());  }

	protected void cloneToContainer(HAPContainerEntity<T> container) {
		for(T eleInfo : this.getAllElementsInfo()) {
			container.addEntityElement((T)eleInfo.cloneContainerElementInfo());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> eleArray = new ArrayList<String>();
		for(T ele : this.getAllElementsInfo()) {
			eleArray.add(ele.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildArrayJson(eleArray.toArray(new String[0])));
	}
	
	@Override
	public String toExpandedJsonString(HAPDomainDefinitionEntity entityDefDomain) {
		List<String> eleArray = new ArrayList<String>();
		for(T ele : this.getAllElementsInfo()) {
			eleArray.add(ele.toExpandedJsonString(entityDefDomain));
		}
		return HAPJsonUtility.buildArrayJson(eleArray.toArray(new String[0]));
	}

}
