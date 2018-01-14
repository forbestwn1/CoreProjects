package com.nosliw.uiresource.tag;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPUITagDefinitionContext extends HAPSerializableImp{

	@HAPAttribute
	public static final String INHERIT = "inherit";

	//whether interit the context from parent
	private boolean m_inherit;

	//public context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_publicEles;

	//public context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_internalEles;
	
	//public context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_privateEles;

	//excluded context element definition
	private Map<String, HAPUITagDefinitionContextElment> m_excludedEles;
	
	public HAPUITagDefinitionContext(){
		this.m_publicEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_internalEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_privateEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_excludedEles = new LinkedHashMap<String, HAPUITagDefinitionContextElment>();
		this.m_inherit = true;
	}

	public boolean isInherit(){  return this.m_inherit;  }
	public void setInherit(boolean inherit){  this.m_inherit = inherit;   } 

	public Map<String, HAPUITagDefinitionContextElment> getElements(String type){
		Map<String, HAPUITagDefinitionContextElment> out = null;
		switch(type){
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC:
			out = this.m_publicEles;
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL:
			out = this.m_internalEles;
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE:
			out = this.m_privateEles;
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED:
			out = this.m_excludedEles;
			break;
		}
		return out;	
	}

	public void addElement(String name, HAPUITagDefinitionContextElment ele, String type){
		switch(type){
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC:
			this.m_publicEles.put(name, ele);
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_INTERNAL:
			this.m_internalEles.put(name, ele);
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE:
			this.m_privateEles.put(name, ele);
			break;
		case HAPConstant.UIRESOURCE_CONTEXTTYPE_EXCLUDED:
			this.m_excludedEles.put(name, ele);
			break;
		}
	}
	
	
	public Map<String, HAPUITagDefinitionContextElment> getPublicElements(){		return this.m_publicEles;	}
	public void addPublicElement(String name, HAPUITagDefinitionContextElment ele){  this.m_publicEles.put(name, ele);  }

	public Map<String, HAPUITagDefinitionContextElment> getInternalElements(){		return this.m_internalEles;	}
	public void addInternalElement(String name, HAPUITagDefinitionContextElment ele){  this.m_internalEles.put(name, ele);  }
	
	public Map<String, HAPUITagDefinitionContextElment> getPrivateElements(){		return this.m_privateEles;	}
	public void addPrivateElement(String name, HAPUITagDefinitionContextElment ele){  this.m_privateEles.put(name, ele);  }

	public Map<String, HAPUITagDefinitionContextElment> getExcludedElements(){		return this.m_excludedEles;	}
	public void addExcludedElement(String name, HAPUITagDefinitionContextElment ele){  this.m_excludedEles.put(name, ele);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INHERIT, this.m_inherit+"");
		typeJsonMap.put(INHERIT, Boolean.class);
		jsonMap.put(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, HAPJsonUtility.buildJson(m_publicEles, HAPSerializationFormat.JSON));
	}	
}
