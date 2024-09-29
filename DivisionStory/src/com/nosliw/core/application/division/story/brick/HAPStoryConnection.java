package com.nosliw.core.application.division.story.brick;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

//connect is between two end
@HAPEntityWithAttribute
public interface HAPStoryConnection extends HAPStoryElement{

	@HAPAttribute
	public static final String END1 = "end1";

	@HAPAttribute
	public static final String END2 = "end2";

	HAPStoryConnectionEnd getEnd1();
	
	HAPStoryConnectionEnd getEnd2();

}
