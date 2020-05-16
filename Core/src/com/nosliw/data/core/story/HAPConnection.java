package com.nosliw.data.core.story;

//connect is between two end
public interface HAPConnection extends HAPStoryElement{

	public static final String END1 = "end1";

	public static final String END2 = "end2";

	HAPConnectionEnd getEnd1();
	
	HAPConnectionEnd getEnd2();

}
