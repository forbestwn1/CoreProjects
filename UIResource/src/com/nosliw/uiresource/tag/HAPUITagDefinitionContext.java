package com.nosliw.uiresource.tag;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPJsonUtility;

@HAPEntityWithAttribute
public class HAPUITagDefinitionContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENTS = "elements";
	@HAPAttribute
	public static final String INHERIT = "inherit";

	//whether interit the context from parent
	private boolean m_inherit;

	//scriptExpression : criteria
	Map<String, HAPUITagDefinitionContextElment> m_elements;

	public HAPUITagDefinitionContext(){
		this.m_elements = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_inherit = true;
	}

	public boolean isInherit(){  return this.m_inherit;  }
	public void setInherit(boolean inherit){  this.m_inherit = inherit;   } 
	
	public Map<String, HAPUITagDefinitionContextElment> getElements(){		return this.m_elements;	}
	public void addElement(String name, HAPUITagDefinitionContextElment ele){  this.m_elements.put(name, ele);  }

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INHERIT, this.m_inherit+"");
		typeJsonMap.put(INHERIT, Boolean.class);
		jsonMap.put(ELEMENTS, HAPJsonUtility.buildJson(m_elements, HAPSerializationFormat.JSON));
	}	
}
