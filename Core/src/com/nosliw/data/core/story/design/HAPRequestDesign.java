package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.story.change.HAPChangeItem;

public class HAPRequestDesign {

	private List<HAPAnswer> m_answer;
	
	private List<HAPChangeItem> m_extraChanges;

	private String m_designId;
	
	private int m_stepCursor = -1;
	
	public HAPRequestDesign(String designId) {
		this.m_designId = designId;
		this.m_answer = new ArrayList<HAPAnswer>();
		this.m_extraChanges = new ArrayList<HAPChangeItem>();
	}
	
	public int getStepCursor() {   return this.m_stepCursor;    }
	public void setStepCursor(int stepCursor) {   this.m_stepCursor = stepCursor;    }
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addAnswer(HAPAnswer answer) {  this.m_answer.add(answer);   }
	public List<HAPAnswer> getAnswers(){    return this.m_answer;    }
	
	public List<HAPChangeItem> getExtraChanges(){   return this.m_extraChanges;    }
	public void addExtraChange(HAPChangeItem change) {   this.m_extraChanges.add(change);   }
}
