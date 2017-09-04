package com.nosliw.uiresource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute(baseName="UIRESOURCE")
public class HAPContextElement {

	@HAPAttribute
	public static final String CRITERIAS  = "criterias";
	
	@HAPAttribute
	public static final String DEFAULT = "default";
	
	private Map<String, HAPDataTypeCriteria> m_criterias;
	
	private String m_default;
	
	public HAPContextElement(){
		this.m_criterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
	}
	
	public void setDefault(String defau){
		this.m_default = defau;
	}

	public void addCriteria(String path, HAPDataTypeCriteria criteria){
		this.m_criterias.put(path, criteria);
	}
	
}
