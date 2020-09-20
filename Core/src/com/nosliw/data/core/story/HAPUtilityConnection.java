package com.nosliw.data.core.story;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.data.core.story.element.connection.HAPConnectionDataIO;

public class HAPUtilityConnection {

	public static HAPConnectionContain newConnectionContain(HAPReferenceElement containerNodeRef, HAPReferenceElement childNodeRef, String childId) {
		HAPConnectionContain out = new HAPConnectionContain();
		HAPConnectionEnd containerEnd = out.getEnd1();
		containerEnd.setNodeRef(containerNodeRef);
		containerEnd.setProfile(HAPConstant.STORYNODE_PROFILE_CONTAINER);
		
		HAPConnectionEnd childEnd = out.getEnd2();
		childEnd.setNodeRef(childNodeRef);
		
		out.setChildId(childId);
		return out;
	}
	
	public static HAPConnectionDataIO newConnectionOnewayDataIO(HAPReferenceElement fromNodeRef, HAPReferenceElement toNodeRef, String fromPath, String toPath) {
		HAPConnectionDataIO out = new HAPConnectionDataIO();
		HAPConnectionEnd fromEnd = out.getEnd1();
		fromEnd.setNodeRef(fromNodeRef);
		fromEnd.setProfile(HAPConstant.STORYNODE_PROFILE_DATAOUT);
		
		HAPConnectionEnd toEnd = out.getEnd2();
		toEnd.setNodeRef(toNodeRef);
		toEnd.setProfile(HAPConstant.STORYNODE_PROFILE_DATAIN);
		
		out.setPath1(fromPath);
		out.setPath2(toPath);
		return out;
	}
}
