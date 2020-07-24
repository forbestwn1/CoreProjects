package com.nosliw.data.core.story;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.data.core.story.element.connection.HAPConnectionDataIO;

public class HAPUtilityConnection {

	public static HAPConnectionContain newConnectionContain(String containerNodeId, String childNodeId, String childId) {
		HAPConnectionContain out = new HAPConnectionContain();
		HAPConnectionEnd containerEnd = out.getEnd1();
		containerEnd.setNodeId(containerNodeId);
		containerEnd.setProfile(HAPConstant.STORYNODE_PROFILE_CONTAINER);
		
		HAPConnectionEnd childEnd = out.getEnd2();
		childEnd.setNodeId(childNodeId);
		
		out.setChildId(childId);
		return out;
	}
	
	public static HAPConnectionDataIO newConnectionOnewayDataIO(String fromNodeId, String toNodeId, String fromPath, String toPath) {
		HAPConnectionDataIO out = new HAPConnectionDataIO();
		HAPConnectionEnd fromEnd = out.getEnd1();
		fromEnd.setNodeId(fromNodeId);
		fromEnd.setProfile(HAPConstant.STORYNODE_PROFILE_DATAOUT);
		
		HAPConnectionEnd toEnd = out.getEnd2();
		toEnd.setNodeId(toNodeId);
		toEnd.setProfile(HAPConstant.STORYNODE_PROFILE_DATAIN);
		
		out.setPath1(fromPath);
		out.setPath2(toPath);
		return out;
	}
}
