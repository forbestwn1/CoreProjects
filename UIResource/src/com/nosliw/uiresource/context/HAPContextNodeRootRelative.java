package com.nosliw.uiresource.context;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatchers;

/**
 * Context element that based on context element on parent
 * When tag has its own variable definition which is differnt from defintion from parent, 
 * we should treat through two variables as different variables 
 * And matcher is needed to do convert between these two variables  
 */
public class HAPContextNodeRootRelative extends HAPContextNode implements HAPContextNodeRoot{

	@HAPAttribute
	public static final String PATH = "path";
	
	//relative path from parent context
	private HAPContextPath m_path;

	//variable full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;
	
	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE;	}

	public void setPath(HAPContextPath path){
		this.m_path = path;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, this.getType());
	}
}
