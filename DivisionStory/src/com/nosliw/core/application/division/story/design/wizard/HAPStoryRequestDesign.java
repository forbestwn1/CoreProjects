package com.nosliw.core.application.division.story.design.wizard;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;

public class HAPStoryRequestDesign {

	private List<HAPStoryAnswer> m_answer;
	
	private List<HAPStoryChangeItem> m_extraChanges;

	private String m_designId;
	
	private int m_stepCursor = -1;
	
	public HAPStoryRequestDesign(String designId) {
		this.m_designId = designId;
		this.m_answer = new ArrayList<HAPStoryAnswer>();
		this.m_extraChanges = new ArrayList<HAPStoryChangeItem>();
	}
	
	public int getStepCursor() {   return this.m_stepCursor;    }
	public void setStepCursor(int stepCursor) {   this.m_stepCursor = stepCursor;    }
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addAnswer(HAPStoryAnswer answer) {  this.m_answer.add(answer);   }
	public List<HAPStoryAnswer> getAnswers(){    return this.m_answer;    }
	
	public List<HAPStoryChangeItem> getExtraChanges(){   return this.m_extraChanges;    }
	public void addExtraChange(HAPStoryChangeItem change) {   this.m_extraChanges.add(change);   }
}
