package com.nosliw.data.core.story;

//connect is between two end
public interface HAPConnection extends HAPStoryElement{

	HAPConnectionEnd getEnd1();
	
	HAPConnectionEnd getEnd2();

}
