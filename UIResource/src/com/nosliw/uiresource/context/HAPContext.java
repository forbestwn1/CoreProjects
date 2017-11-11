package com.nosliw.uiresource.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	private Map<String, HAPContextNodeRoot> m_elements;
	
	public HAPContext(){
		this.m_elements = new LinkedHashMap<String, HAPContextNodeRoot>();
	}
	
	public void addElement(String name, HAPContextNodeRoot rootEle){
		this.m_elements.put(name, rootEle);
	}

	public Map<String, HAPContextNodeRoot> getElements(){  return this.m_elements;  }
	
	public void hardMergeWith(HAPContext context){
		Map<String, HAPContextNodeRoot> eles = context.getElements();
		for(String rootName : eles.keySet()){
			this.m_elements.put(rootName, eles.get(rootName));
		}
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		for(String rootName : this.m_elements.keySet()){
			jsonMap.put(rootName, this.m_elements.get(rootName).toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
