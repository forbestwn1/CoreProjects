package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute(baseName="UIRESOURCE")
public class HAPContextElement {

	@HAPAttribute
	public static final String CRITERIA  = "criteria";

	@HAPAttribute
	public static final String CHILDREN  = "children";
	
	@HAPAttribute
	public static final String DEFAULT = "default";
	
	private HAPDataTypeCriteria m_criteria;
	
	private Map<String, HAPDataTypeCriteria> m_children;
	
	private String m_default;
	
	public HAPContextElement(){
		this.m_children = new LinkedHashMap<String, HAPDataTypeCriteria>();
	}
	
	public void setDefault(String defau){
		this.m_default = defau;
	}

	public void setCriteria(HAPDataTypeCriteria criteria){
		this.m_criteria = criteria;
	}
	
	public void addChild(String path, HAPDataTypeCriteria criteria){
		this.m_children.put(path, criteria);
	}
	
}
