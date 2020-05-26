package com.nosliw.servlet.app.story;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.story.HAPStory;

public class HAPNewDesign extends HAPEntityInfoImp{

	private String m_designId;
	
	private String m_director;
	
	private String m_storyId;
	
	private HAPStory m_story;
	
	public void setStory(HAPStory story) {   this.m_story = story;    }
	
	public void setDesignId(String id) {   this.m_designId = id;   }
	
	public void setDirector(String director) {   this.m_director = director;   }
	
	public void setStoryId(String id) {  this.m_storyId = id;   }
}
