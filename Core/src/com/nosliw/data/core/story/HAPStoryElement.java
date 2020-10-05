package com.nosliw.data.core.story;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.data.core.story.change.HAPChangeResult;

@HAPEntityWithAttribute
public interface HAPStoryElement extends HAPEntityInfo, HAPEntityOrReference{

	@HAPAttribute
	public static final String CATEGARY = "categary";
	
	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String ENABLE = "enable";

	@HAPAttribute
	public static final String STATUS = "status";

	public static String[] getRootAttribute() {
		return new String[] {ENABLE};
	}
	
	HAPIdElement getElementId();
	
	String getCategary();
	
	String getType();

	HAPChangeResult patch(String path, Object value);
	Object getValueByPath(String path);
	
	//configuration for element, for ui purpose
	HAPStatus getStatus();

	boolean isEnable();

	void appendToStory(HAPStory story);   

	HAPStoryElement cloneStoryElement();
}
