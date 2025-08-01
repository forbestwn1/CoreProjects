package com.nosliw.core.application.division.story;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryConnectionEnd;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionContain;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChangeWrapper;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.core.xxx.resource.HAPResourceDefinition1;

public class HAPStoryUtilityStory {

	public static String getBuilderId(HAPStoryStory story) {   return null;     }
	public static void setBuilderId(HAPStoryStory story, String builderId) {         }

	static private int index = 0;

	
	//return connection ref
	public static HAPStoryReferenceElement addNodeAsChild(HAPStoryReferenceElement eleRefParent, HAPStoryReferenceElement eleRefChild, String childId, HAPStoryRequestChangeWrapper changeRequest, boolean inheritVar) {
		if(inheritVar){
			HAPStoryStory story = changeRequest.getStory();
			HAPStoryNode parent = (HAPStoryNode)story.getElement(eleRefParent);
			HAPStoryNode child = (HAPStoryNode)story.getElement(eleRefChild);
			
			//variable to child
			List<HAPStoryNodeVariable> varNodesFromParent = HAPStoryUtilityVariable.getVariableForChild(parent, changeRequest);
			for(HAPStoryNodeVariable varNode : varNodesFromParent) {
				HAPStoryUtilityVariable.addVariableToNode(child.getElementId(), varNode.getElementId(), null, changeRequest);
			}
		}

		//build parent-child relation
		return changeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(eleRefParent, eleRefChild, childId, null)).getElement().getElementId();
	}
	
	
	public static String buildStoryElementId(HAPStoryElement ele, String id) {
		return ele.getCategary()+HAPConstantShared.SEPERATOR_LEVEL1+ele.getType()+HAPConstantShared.SEPERATOR_LEVEL1+id;
	}
	
	public static HAPStoryIdElementInfo parseStoryElementId(String id) {
		String[] segs = id.split(HAPConstantShared.SEPERATOR_LEVEL1);
		return new HAPStoryIdElementInfo(segs[0], segs[1], segs[2]);
	}
	
	public static List<HAPStoryElement> getAllElementRelyOnIt(HAPStoryStory story, String eleId){
		return null;
	}
	
	public static String exportBuildResourceDefinition(HAPStoryStory story, HAPResourceDefinition1 resourceDef) {
		String fileName = story.getShowType() + "_" + story.getName() + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentDynamicResourceExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    String fileFullName = directory.getAbsolutePath()+"/"+fileName;
	    String tempFileName = HAPSystemFolderUtility.getDynamicResourceExportFolder() + "/" + "temp.res";
	    String resourceDefStr = resourceDef.toStringValue(HAPSerializationFormat.LITERATE);
	    HAPUtilityFile.writeFile(fileFullName, resourceDefStr);
	    HAPUtilityFile.writeFile(tempFileName, resourceDefStr);
	    return fileFullName;
	}

	public static Set<HAPStoryNode> getAllStoryNodeByType(HAPStoryStory story, String type) {  return getStoryNodeByType(story, type, false);  }
	public static Set<HAPStoryNode> getStoryNodeByType(HAPStoryStory story, String type) {  return getStoryNodeByType(story, type, true);  }
	
	private static Set<HAPStoryNode> getStoryNodeByType(HAPStoryStory story, String type, boolean onlyEnable) {
		Set<HAPStoryNode> out = new HashSet<HAPStoryNode>();
		for(HAPStoryNode node : story.getNodes()) {
			if(type.equals(node.getType())) {
				if(isValid(node, onlyEnable)) {
					out.add(node);
				}
			}
		}
		return out;
	}

	public static List<HAPStoryInfoNodeChild> getAllChildNode(HAPStoryNode parent, HAPStoryStory story) {  return getChildNode(parent, story, false);  }
	public static List<HAPStoryInfoNodeChild> getChildNode(HAPStoryNode parent, HAPStoryStory story) {  return getChildNode(parent, story, true);  }
	private static List<HAPStoryInfoNodeChild> getChildNode(HAPStoryNode parent, HAPStoryStory story, boolean onlyEnable) {
		List<HAPStoryInfoNodeChild> out = new ArrayList<HAPStoryInfoNodeChild>();
		List<HAPStoryConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstantShared.STORYCONNECTION_TYPE_CONTAIN, HAPConstantShared.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPStoryConnectionEnd connectionEnd : childConnectionEnds) {
			HAPStoryConnectionContain containerConnectionEntity = (HAPStoryConnectionContain)story.getConnection(connectionEnd.getConnectionId());
			HAPStoryNode node = (HAPStoryNode)story.getElement(connectionEnd.getNodeRef());
			if(isValid(containerConnectionEntity, onlyEnable) && isValid(node, onlyEnable)) {
				out.add(new HAPStoryInfoNodeChild(node, containerConnectionEntity));
			}
		}
		return out;
	}

	public static List<HAPStoryInfoNodeChild> getAllChildNodeByType(HAPStoryNode parent, String type, HAPStoryStory story) {return getChildNodeByType(parent, type, story, false);	}
	public static List<HAPStoryInfoNodeChild> getChildNodeByType(HAPStoryNode parent, String type, HAPStoryStory story) {return getChildNodeByType(parent, type, story, true);	}
	public static List<HAPStoryInfoNodeChild> getChildNodeByType(HAPStoryNode parent, String type, HAPStoryStory story, boolean onlyEnable) {
		return getChildNode(parent, story, onlyEnable).stream().filter(node->node.getChildNode().getType().equals(type)).collect(Collectors.toList());
	}
	
	public static List<HAPStoryNode> getAllChildNodesById(HAPStoryNode parent, String childId, HAPStoryStory story) {  return getChildNodesById(parent, childId, story, false);  }
	public static List<HAPStoryNode> getChildNodeById(HAPStoryNode parent, String childId, HAPStoryStory story) {  return getChildNodesById(parent, childId, story, true);  }
	private static List<HAPStoryNode> getChildNodesById(HAPStoryNode parent, String childId, HAPStoryStory story, boolean onlyEnable) {
		List<HAPStoryNode> out = new ArrayList<HAPStoryNode>();
		List<HAPStoryConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstantShared.STORYCONNECTION_TYPE_CONTAIN, HAPConstantShared.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPStoryConnectionEnd connectionEnd : childConnectionEnds) {
			HAPStoryConnectionContain containerConnectionEntity = (HAPStoryConnectionContain)story.getConnection(connectionEnd.getConnectionId());
			if(HAPUtilityBasic.isEquals(childId, containerConnectionEntity.getChildId())) {
				HAPStoryNode node = (HAPStoryNode)story.getElement(connectionEnd.getNodeRef());
				if(isValid(containerConnectionEntity, onlyEnable) && isValid(node, onlyEnable)) {
					out.add(node);
				}
			}
		}
		return out;
	}
	
	public static List<HAPStoryConnectionEnd> getConnectionEnd(HAPStoryNode node1, String connectionType, String profile1, String node2Type, String profile2, HAPStoryStory story) {
		List<HAPStoryConnectionEnd> out = new ArrayList<HAPStoryConnectionEnd>();
		for(String connectionId :  node1.getConnections()) {
			HAPStoryConnection connection = story.getConnection(connectionId);
			if(connection.isEnable()) {
				if(connectionType==null || connection.getType().equals(connectionType)){
					HAPStoryConnectionEnd node1End = getConnectionEnd(connection, node1.getId());
					if(profile1==null || profile1.equals(node1End.getProfile())) {
						HAPStoryConnectionEnd node2End = getOtherConnectionEnd(connection, node1.getId());
						if(profile2==null || profile2.equals(node2End.getProfile())) {
							if(node2Type==null || node2Type.equals(story.getElement(node2End.getNodeRef()).getType())){
								out.add(node2End);
							}
						}
					}
				}
			}
		}
		return out;
	}
	
	private static boolean isValid(HAPStoryElement ele, boolean onlyEnable) {
		return !onlyEnable||ele.isEnable();
	}
	
	public static HAPStoryConnectionEnd getOtherConnectionEnd(HAPStoryConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeElementId().getId())) {
			return connection.getEnd2();
		} else {
			return connection.getEnd1();
		}
	}
	
	public static HAPStoryConnectionEnd getConnectionEnd(HAPStoryConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeElementId().getId())) {
			return connection.getEnd1();
		} else {
			return connection.getEnd2();
		}
	}
	
	public static HAPStoryIdElement getElementIdByReference(HAPStoryReferenceElement eleRef, HAPStoryStory story) {
		if(eleRef instanceof HAPStoryIdElement) {
			return (HAPStoryIdElement)eleRef;
		} else if(eleRef instanceof HAPStoryAliasElement) {
			return story.getElementId(((HAPStoryAliasElement)eleRef).getName());
		}
		return null;
	}
	
	public static HAPStoryElement getStoryElement(HAPEntityOrReference entityOrRef, HAPStoryStory story) {
		if(entityOrRef instanceof HAPStoryNode) {
			return (HAPStoryElement)entityOrRef;
		}
		else if(entityOrRef instanceof HAPStoryReferenceElement){
			return story.getElement((HAPStoryReferenceElement)entityOrRef);
		}
		return null;
	}
	
	public static HAPStoryIdElement newNodeId(String id) {   return new HAPStoryIdElement(HAPConstantShared.STORYELEMENT_CATEGARY_NODE, id);    }
}
