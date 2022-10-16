package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPContainerEntity<T extends HAPElementContainer>  extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ELEMENTTYPE = "elementType";

	@HAPAttribute
	public static String ELEMENTBYID = "elementById";

	@HAPAttribute
	public static String ELEMENT = "element";
	
	@HAPAttribute
	public static String EXTRA = "extra";

	private String m_elementType;
	
	//extra info for container
	private HAPEntityInfo m_extraInfo;
	
	private List<T> m_eleArray;
	
	private Map<String, T> m_eleById;

	public HAPContainerEntity(String elementType) {
		this();
		this.m_elementType = elementType;
		this.m_eleArray = new ArrayList<T>();
	}
	
	public HAPContainerEntity() {
		this.m_eleById = new LinkedHashMap<String, T>();
		this.m_eleArray = new ArrayList<T>();
	}

	public String getElementType() {    return this.m_elementType;     }
	public void setElementType(String eleType) {     this.m_elementType = eleType;      }
	
	public HAPEntityInfo getExtraInfo() {    return this.m_extraInfo;     }
	public void setExtraInfo(HAPEntityInfo extraInfo) {    this.m_extraInfo = extraInfo;     }
	
	public void addEntityElement(T eleInfo) {
		this.m_eleById.put(eleInfo.getElementId(), eleInfo);
	}

	public Set<T> getElementInfoByName(String name) {
		Set<T> out = new HashSet<T>();
		for(T ele : this.getAllElementsInfo()) {
			if(name.equals(ele.getElementName()));
		}
		return out;
	}

	public T getElementInfoById(String id) {  return this.m_eleById.get(id);  }

	public List<T> getAllElementsInfo() {  return this.m_eleArray;  }

	public abstract String getContainerType();
	
	public abstract HAPContainerEntity cloneContainerEntity();
	
	protected void cloneToContainer(HAPContainerEntity<T> container) {
		container.m_elementType = this.m_elementType;
		container.m_extraInfo = this.m_extraInfo.cloneEntityInfo();
		for(T eleInfo : this.getAllElementsInfo()) {
			container.addEntityElement((T)eleInfo.cloneContainerElementInfo());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> byIdJsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, this.getContainerType());
		
		for(String id : this.m_eleById.keySet()) {
			byIdJsonMap.put(id, this.m_eleById.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENTBYID, HAPJsonUtility.buildMapJson(byIdJsonMap));
		
		List<String> elesJsonArray = new ArrayList<String>();
		for(T ele : this.getAllElementsInfo()) {
			elesJsonArray.add(ele.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ELEMENT, HAPJsonUtility.buildArrayJson(elesJsonArray.toArray(new String[0])));
	}
}
