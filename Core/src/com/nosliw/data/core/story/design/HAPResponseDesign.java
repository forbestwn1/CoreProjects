package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPResponseDesign extends HAPSerializableImp{

	@HAPAttribute
	public static final String ANSWER = "answer";

	@HAPAttribute
	public static final String ANSWEREXTEND = "answerExtend";

	@HAPAttribute
	public static final String STEP = "step";

	private List<HAPAnswer> m_answer;

	private List<HAPAnswer> m_answerExtend;

	private HAPDesignStep m_step;
	
	public HAPResponseDesign(List<HAPAnswer> answer, HAPDesignStep step) {
		this.m_answer = new ArrayList<HAPAnswer>();
		this.m_answerExtend = new ArrayList<HAPAnswer>();
		this.m_answer.addAll(answer);
		this.m_step = step;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, HAPAnswer> answerMap = new LinkedHashMap<String, HAPAnswer>();
		for(HAPAnswer answer : this.m_answer) {
			answerMap.put(answer.getQuestionId(), answer);
		}
		jsonMap.put(ANSWER, HAPJsonUtility.buildJson(answerMap, HAPSerializationFormat.JSON));
		jsonMap.put(ANSWEREXTEND, HAPJsonUtility.buildJson(m_answerExtend, HAPSerializationFormat.JSON));
		jsonMap.put(STEP, HAPJsonUtility.buildJson(this.m_step, HAPSerializationFormat.JSON));
	}
}
