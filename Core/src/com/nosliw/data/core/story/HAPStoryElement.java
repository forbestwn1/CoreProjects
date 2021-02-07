package com.nosliw.data.core.story;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
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
	public static final String ELEMENTSTATUS = "eleStatus";

	@HAPAttribute
	public static final String DISPLAYRESOURCE = "displayResource";

	@HAPAttribute
	public static final String EXTRA = "extra";

	public static String[] getRootAttribute() {
		return new String[] {ENABLE};
	}
	
	HAPIdElement getElementId();
	
	String getCategary();
	
	String getType();

	Object getExtra();
	
	HAPDisplayResourceNode getDisplayResource();
	
	HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv);
	Object getValueByPath(String path);
	
	//configuration for element, for ui purpose
	HAPStatus getElementStatus();

	boolean isEnable();

	void appendToStory(HAPStory story);   

	HAPStoryElement cloneStoryElement();
}
