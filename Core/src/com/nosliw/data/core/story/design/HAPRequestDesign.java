package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

public class HAPRequestDesign {

	private List<HAPAnswer> m_answer;

	private String m_designId;
	
	private int m_stepCursor = -1;
	
	public HAPRequestDesign(String designId) {
		this.m_answer = new ArrayList<HAPAnswer>();
		this.m_designId = designId;
	}
	
	public int getStepCursor() {   return this.m_stepCursor;    }
	public void setStepCursor(int stepCursor) {   this.m_stepCursor = stepCursor;    }
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addAnswer(HAPAnswer answer) {  this.m_answer.add(answer);   }

	public List<HAPAnswer> getAnswers(){    return this.m_answer;    }
}
