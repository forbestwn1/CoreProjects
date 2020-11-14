package com.nosliw.data.core.story.design;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPReferenceElementWrapper;
import com.nosliw.data.core.story.HAPStory;

public class HAPQuestionItem extends HAPQuestion{

	@HAPAttribute
	public static final String TARGETREF = "targetRef";

	@HAPAttribute
	public static final String ISMANDATORY = "isMandatory";

	private HAPReferenceElementWrapper m_targetRef;
	
	private boolean m_isMandatory = false;
	
	public HAPQuestionItem() {}
	
	public HAPQuestionItem(String question, HAPReferenceElement targetRef) {
		super(question);
		this.m_targetRef = new HAPReferenceElementWrapper(targetRef);
	}

	public HAPQuestionItem(String question, HAPReferenceElement targetRef, boolean isMandatory) {
		this(question, targetRef);
		this.m_isMandatory = isMandatory;
	}

	@Override
	public void processAlias(HAPStory story) {  this.m_targetRef.processAlias(story);  }

	@Override
	public String getType() {	return HAPConstant.STORYDESIGN_QUESTIONTYPE_ITEM;	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_targetRef = new HAPReferenceElementWrapper();
		this.m_targetRef.buildObject(jsonObj.getJSONObject(TARGETREF), HAPSerializationFormat.JSON);
		
		this.m_isMandatory = jsonObj.getBoolean(ISMANDATORY);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TARGETREF, HAPJsonUtility.buildJson(this.m_targetRef, HAPSerializationFormat.JSON));
		jsonMap.put(ISMANDATORY, this.m_isMandatory+"");
		typeJsonMap.put(ISMANDATORY, Boolean.class);
	}
}
