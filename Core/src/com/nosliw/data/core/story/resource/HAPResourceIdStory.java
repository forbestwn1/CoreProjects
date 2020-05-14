package com.nosliw.data.core.story.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdStory  extends HAPResourceIdSimple{

	private HAPStoryId m_storyId; 
	
	public HAPResourceIdStory(){  super(HAPConstant.RUNTIME_RESOURCE_TYPE_TEMPLATE);    }

	public HAPResourceIdStory(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdStory(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdStory(HAPStoryId storyId){
		this();
		init(null, null);
		this.m_storyId = storyId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(storyId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_storyId = new HAPStoryId(id);
	}

	public HAPStoryId getStoryId(){  return this.m_storyId;	}
	
	@Override
	public HAPResourceIdStory clone(){
		HAPResourceIdStory out = new HAPResourceIdStory();
		out.cloneFrom(this);
		return out;
	}

}
