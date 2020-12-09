package com.nosliw.uiresource.page.tag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPUITagQueryResult  extends HAPSerializableImp{

	@HAPAttribute
	public static final String RESULT = "result";

	@HAPAttribute
	public static final String SCORE = "weight";

	private HAPUITagInfo m_result;
	
	private double m_score;
	
	public HAPUITagQueryResult(HAPUITagInfo result, double score) {
		this.m_result = result;
		this.m_score = score;
	}
	
	public HAPUITagInfo getResult() {    return this.m_result;    }
	
	public double getScore() {    return this.m_score;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_result, HAPSerializationFormat.JSON));
		jsonMap.put(SCORE, this.m_score+"");
		typeJsonMap.put(SCORE, Double.class);
	}
	
}
