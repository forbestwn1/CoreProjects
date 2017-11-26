package com.nosliw.uiresource.tag;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPUITagDefinitionContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String PUBLIC = "public";
	@HAPAttribute
	public static final String PRIVATE = "private";
	@HAPAttribute
	public static final String INHERIT = "inherit";

	//whether interit the context from parent
	private boolean m_inherit;

	//public context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_publicEles;

	//public context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_privateEles;
	
	public HAPUITagDefinitionContext(){
		this.m_publicEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_privateEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_inherit = true;
	}

	public boolean isInherit(){  return this.m_inherit;  }
	public void setInherit(boolean inherit){  this.m_inherit = inherit;   } 

	public Map<String, HAPUITagDefinitionContextElment> getElements(String type){
		Map<String, HAPUITagDefinitionContextElment> out = null;
		switch(type){
		case PUBLIC:
			out = this.m_publicEles;
			break;
		case PRIVATE:
			out = this.m_privateEles;
			break;
		}
		return out;	
	}

	public void addElement(String name, HAPUITagDefinitionContextElment ele, String type){
		switch(type){
		case PUBLIC:
			this.m_publicEles.put(name, ele);
			break;
		case PRIVATE:
			this.m_privateEles.put(name, ele);
			break;
		}
	}
	
	
	public Map<String, HAPUITagDefinitionContextElment> getPublicElements(){		return this.m_publicEles;	}
	public void addPublicElement(String name, HAPUITagDefinitionContextElment ele){  this.m_publicEles.put(name, ele);  }

	public Map<String, HAPUITagDefinitionContextElment> getPrivateElements(){		return this.m_privateEles;	}
	public void addPrivateElement(String name, HAPUITagDefinitionContextElment ele){  this.m_privateEles.put(name, ele);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INHERIT, this.m_inherit+"");
		typeJsonMap.put(INHERIT, Boolean.class);
		jsonMap.put(PUBLIC, HAPJsonUtility.buildJson(m_publicEles, HAPSerializationFormat.JSON));
	}	
}
