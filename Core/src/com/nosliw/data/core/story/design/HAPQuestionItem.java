package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPParserElementReference;
import com.nosliw.data.core.story.HAPReferenceElement;

public class HAPQuestionItem extends HAPQuestion{

	@HAPAttribute
	public static final String TARGETREF = "targetRef";

	private HAPReferenceElement m_targetRef;
	
	public HAPQuestionItem() {}
	
	public HAPQuestionItem(String question, HAPReferenceElement targetRef) {
		super(question);
		this.m_targetRef = targetRef;
	}

	@Override
	public String getType() {	return HAPConstant.STORYDESIGN_QUESTIONTYPE_ITEM;	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		HAPParserElementReference.parse(jsonObj.getJSONObject(TARGETREF));
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETREF, HAPJsonUtility.buildJson(this.m_targetRef, HAPSerializationFormat.JSON));
	}

}
