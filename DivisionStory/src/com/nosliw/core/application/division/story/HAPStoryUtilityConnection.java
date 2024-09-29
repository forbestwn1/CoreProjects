package com.nosliw.core.application.division.story;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryConnectionEnd;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionContain;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionDataIO;

public class HAPStoryUtilityConnection {

	public static HAPStoryConnectionContain newConnectionContain(HAPStoryReferenceElement containerNodeRef, HAPStoryReferenceElement childNodeRef, String childId) {
		HAPStoryConnectionContain out = new HAPStoryConnectionContain();
		HAPStoryConnectionEnd containerEnd = out.getEnd1();
		containerEnd.setNodeRef(containerNodeRef);
		containerEnd.setProfile(HAPConstantShared.STORYNODE_PROFILE_CONTAINER);
		
		HAPStoryConnectionEnd childEnd = out.getEnd2();
		childEnd.setNodeRef(childNodeRef);
		
		out.setChildId(childId);
		return out;
	}
	
	public static HAPStoryConnectionDataIO newConnectionOnewayDataIO(HAPStoryReferenceElement fromNodeRef, HAPStoryReferenceElement toNodeRef, String fromPath, String toPath) {
		HAPStoryConnectionDataIO out = new HAPStoryConnectionDataIO();
		HAPStoryConnectionEnd fromEnd = out.getEnd1();
		fromEnd.setNodeRef(fromNodeRef);
		fromEnd.setProfile(HAPConstantShared.STORYNODE_PROFILE_DATAOUT);
		
		HAPStoryConnectionEnd toEnd = out.getEnd2();
		toEnd.setNodeRef(toNodeRef);
		toEnd.setProfile(HAPConstantShared.STORYNODE_PROFILE_DATAIN);
		
		out.setPath1(fromPath);
		out.setPath2(toPath);
		return out;
	}
}
