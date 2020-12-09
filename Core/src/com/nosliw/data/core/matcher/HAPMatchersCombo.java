package com.nosliw.data.core.matcher;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPMatchersCombo extends HAPSerializableImp{

	@HAPAttribute
	public static final String MATCHERS = "matchers";
	
	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";

	//matchers from query criteria to ui tag data criteria
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;

	public HAPMatchersCombo() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	public void addMatchers(String name, HAPMatchers matchers) {   
		this.m_matchers.put(name, matchers);    
		this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPJsonUtility.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}

}
