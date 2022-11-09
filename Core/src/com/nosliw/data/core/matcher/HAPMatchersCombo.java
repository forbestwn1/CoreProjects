package com.nosliw.data.core.matcher;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPMatchersCombo extends HAPSerializableImp{

	@HAPAttribute
	public static final String MATCHERS = "matchers";
	
	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";

	//matchers from query criteria to ui tag data criteria
	private HAPMatchers m_matchers;

	private HAPMatchers m_reverseMatchers;

	public HAPMatchersCombo() {	}

	public HAPMatchersCombo(HAPMatchers matchers) {
		this.setMatchers(matchers);
	}
	
	public HAPMatchers getMatchers() {   return this.m_matchers;  }
	public HAPMatchers getReverseMatchers() {    return this.m_reverseMatchers;    }
	
	public void setMatchers(HAPMatchers matchers) {   
		this.m_matchers = matchers;
		this.m_reverseMatchers = HAPMatcherUtility.reversMatchers(matchers);
	}

	public HAPMatchersCombo cloneMatchers() {
		return new HAPMatchersCombo(this.m_matchers.cloneMatchers());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_matchers!=null){
			jsonMap.put(MATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPUtilityJson.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		JSONObject jsonValue = (JSONObject)value;
		this.m_matchers = new HAPMatchers();
		this.m_matchers.buildObject(jsonValue.getJSONObject(MATCHERS), HAPSerializationFormat.JSON);
		this.m_reverseMatchers = new HAPMatchers();
		this.m_reverseMatchers.buildObject(jsonValue.getJSONObject(REVERSEMATCHERS), HAPSerializationFormat.JSON);
		return true;
	}
}
