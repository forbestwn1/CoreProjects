package com.nosliw.core.application.division.story;

import java.util.List;
import java.util.stream.Collectors;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChangeWrapper;

public class HAPStoryUtilityVariable {

	public static List<HAPStoryNodeVariable> getVariableForChild(HAPEntityOrReference entityOrRef, HAPStoryRequestChangeWrapper changeRequest){
		HAPStoryNode storyNode = (HAPStoryNode)HAPStoryUtilityStory.getStoryElement(entityOrRef, changeRequest.getStory());
		return HAPStoryUtilityStory.getAllChildNodeByType(storyNode, HAPStoryNodeVariable.TYPE, changeRequest.getStory()).stream().filter(childInfo->isVariableForChild(childInfo)).map(childInfo->(HAPStoryNodeVariable)childInfo.getChildNode()).collect(Collectors.toList());
	}

	public static void addVariableToNode(HAPStoryReferenceElement nodeRef, HAPStoryReferenceElement variableRef, HAPInfo setting, HAPStoryRequestChangeWrapper changeRequest) {
		HAPStoryNodeVariable variableNode =  (HAPStoryNodeVariable)changeRequest.getStory().getElement(variableRef);
		changeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(nodeRef, variableRef, variableNode.getName(), setting));
	}
	
	private static boolean isVariableForChild(HAPStoryInfoNodeChild varChildInfo) {
		return true;
	}
	
}
