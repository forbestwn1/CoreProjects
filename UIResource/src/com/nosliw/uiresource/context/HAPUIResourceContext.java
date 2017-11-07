package com.nosliw.uiresource.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPUIResourceContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	
	private Map<String, HAPUIResourceContextNodeRoot> m_elements;
	
	public HAPUIResourceContext(){
		this.m_elements = new LinkedHashMap<String, HAPUIResourceContextNodeRoot>();
	}
	
	public void addElement(String name, HAPUIResourceContextNodeRoot rootEle){
		this.m_elements.put(name, rootEle);
	}

	public Map<String, HAPUIResourceContextNodeRoot> getElements(){  return this.m_elements;  }
	
	public void hardMergeWith(HAPUIResourceContext context){
		Map<String, HAPUIResourceContextNodeRoot> eles = context.getElements();
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
