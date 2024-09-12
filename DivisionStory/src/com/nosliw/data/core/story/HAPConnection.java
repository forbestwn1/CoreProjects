package com.nosliw.data.core.story;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

//connect is between two end
@HAPEntityWithAttribute
public interface HAPConnection extends HAPStoryElement{

	@HAPAttribute
	public static final String END1 = "end1";

	@HAPAttribute
	public static final String END2 = "end2";

	HAPConnectionEnd getEnd1();
	
	HAPConnectionEnd getEnd2();

}
