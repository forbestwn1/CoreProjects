package com.nosliw.servlet.app.story;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.story.HAPStory;

@HAPEntityWithAttribute
public class HAPNewDesign extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DESIGNID = "designId";

	@HAPAttribute
	public static final String DIRECTOR = "director";

	@HAPAttribute
	public static final String STORYID = "storyId";

	@HAPAttribute
	public static final String STORY = "story";

	private String m_designId;
	
	private String m_director;
	
	private String m_storyId;
	
	private HAPStory m_story;
	
	public void setStory(HAPStory story) {   this.m_story = story;    }
	
	public void setDesignId(String id) {   this.m_designId = id;   }
	
	public void setDirector(String director) {   this.m_director = director;   }
	
	public void setStoryId(String id) {  this.m_storyId = id;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DESIGNID, this.m_designId);
		jsonMap.put(DIRECTOR, this.m_director);
		jsonMap.put(STORYID, this.m_storyId);
		jsonMap.put(STORY, this.m_story.toStringValue(HAPSerializationFormat.JSON));
	
	}


}
