package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPStoryResponseDesign extends HAPSerializableImp{

	@HAPAttribute
	public static final String ANSWER = "answer";

	@HAPAttribute
	public static final String ANSWEREXTEND = "answerExtend";

	@HAPAttribute
	public static final String STEP = "step";

	private List<HAPStoryAnswer> m_answer;

	private List<HAPStoryAnswer> m_answerExtend;

	private HAPStoryDesignStep m_step;
	
	public HAPStoryResponseDesign(List<HAPStoryAnswer> answer, HAPStoryDesignStep step) {
		this.m_answer = new ArrayList<HAPStoryAnswer>();
		this.m_answerExtend = new ArrayList<HAPStoryAnswer>();
		this.m_answer.addAll(answer);
		this.m_step = step;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, HAPStoryAnswer> answerMap = new LinkedHashMap<String, HAPStoryAnswer>();
		for(HAPStoryAnswer answer : this.m_answer) {
			answerMap.put(answer.getQuestionId(), answer);
		}
		jsonMap.put(ANSWER, HAPUtilityJson.buildJson(answerMap, HAPSerializationFormat.JSON));
		jsonMap.put(ANSWEREXTEND, HAPUtilityJson.buildJson(m_answerExtend, HAPSerializationFormat.JSON));
		jsonMap.put(STEP, HAPUtilityJson.buildJson(this.m_step, HAPSerializationFormat.JSON));
	}
}
