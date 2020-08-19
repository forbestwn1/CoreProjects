package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

public class HAPRequestDesign {

	private List<HAPAnswer> m_answer;

	private String m_designId;
	
	public HAPRequestDesign(String buildId) {
		this.m_answer = new ArrayList<HAPAnswer>();
	}
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addAnswer(HAPAnswer answer) {  this.m_answer.add(answer);   }

	public List<HAPAnswer> getAnswers(){    return this.m_answer;    }
}
