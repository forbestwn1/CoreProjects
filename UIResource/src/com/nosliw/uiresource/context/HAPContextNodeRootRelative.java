package com.nosliw.uiresource.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatcherUtility;
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

	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";
	
	//relative path from parent context
	private HAPContextPath m_path;

	//variable full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;
	
	public HAPContextNodeRootRelative() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE;	}

	public void setPath(HAPContextPath path){
		this.m_path = path;
	}
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.m_path.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, this.getType());
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPJsonUtility.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}
}
