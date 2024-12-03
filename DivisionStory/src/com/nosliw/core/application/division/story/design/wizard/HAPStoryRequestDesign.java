package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryParserChange;

@HAPEntityWithAttribute
public class HAPStoryRequestDesign extends HAPSerializableImp{

	@HAPAttribute
	public static final String DESIGNID = "designId";
	@HAPAttribute
	public static final String ANSWER = "answer";
	@HAPAttribute
	public static final String EXTRACHANGES = "extraChanges";
	@HAPAttribute
	public static final String STEPCUSOR = "stepCursor";
	
	private List<HAPStoryAnswer> m_answer;
	
	private List<HAPStoryChangeItem> m_extraChanges;

	private String m_designId;
	
	private int m_stepCursor = -1;
	
	public HAPStoryRequestDesign() {
		this.m_answer = new ArrayList<HAPStoryAnswer>();
		this.m_extraChanges = new ArrayList<HAPStoryChangeItem>();
	}
	
	public HAPStoryRequestDesign(String designId) {
		this();
		this.m_designId = designId;
	}
	
	public void setDesignId(String designId) {   this.m_designId = designId;     }
	
	public int getStepCursor() {   return this.m_stepCursor;    }
	public void setStepCursor(int stepCursor) {   this.m_stepCursor = stepCursor;    }
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addAnswer(HAPStoryAnswer answer) {  this.m_answer.add(answer);   }
	public List<HAPStoryAnswer> getAnswers(){    return this.m_answer;    }
	
	public List<HAPStoryChangeItem> getExtraChanges(){   return this.m_extraChanges;    }
	public void addExtraChange(HAPStoryChangeItem change) {   this.m_extraChanges.add(change);   }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject parms = (JSONObject)json;
		
		this.m_designId = parms.optString(DESIGNID);

		JSONArray answerArray = parms.optJSONArray(ANSWER);
		for(int i=0; i<answerArray.length(); i++) {
			JSONObject answerObj = answerArray.getJSONObject(i);
			HAPStoryAnswer answer = new HAPStoryAnswer();
			answer.buildObject(answerObj, HAPSerializationFormat.JSON);
			this.m_answer.add(answer);
		}
		
		JSONArray extraChangesArray = parms.optJSONArray(EXTRACHANGES);
		if(extraChangesArray!=null) {
			for(int i=0; i<extraChangesArray.length(); i++) {
				JSONObject changeObj = extraChangesArray.getJSONObject(i);
				HAPStoryChangeItem changeItem = HAPStoryParserChange.parseChangeItem(changeObj);
				this.m_extraChanges.add(changeItem);
			}
		}
		
		int stepCursor = parms.optInt(STEPCUSOR, -1);
		this.setStepCursor(stepCursor);
		
		return true;
	}
}
