package com.nosliw.data.core.story;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityStory {

	public static String getBuilderId(HAPStory story) {   return null;     }
	public static void setBuilderId(HAPStory story, String builderId) {         }

	static private int index = 0;

	public static String buildStoryElementId(HAPStoryElement ele, String id) {
		return ele.getCategary()+HAPConstant.SEPERATOR_LEVEL1+ele.getType()+HAPConstant.SEPERATOR_LEVEL1+id;
	}
	
	public static HAPIdElementInfo parseStoryElementId(String id) {
		String[] segs = id.split(HAPConstant.SEPERATOR_LEVEL1);
		return new HAPIdElementInfo(segs[0], segs[1], segs[2]);
	}
	
	public static List<HAPStoryElement> getAllElementRelyOnIt(HAPStory story, String eleId){
		return null;
	}
	
	public static String exportBuildResourceDefinition(HAPStory story, HAPResourceDefinition resourceDef) {
		String fileName = story.getShowType() + "_" + story.getName() + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentDynamicResourceExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    String fileFullName = directory.getAbsolutePath()+"/"+fileName;
	    String tempFileName = HAPSystemFolderUtility.getDynamicResourceExportFolder() + "/" + "temp.res";
	    String resourceDefStr = resourceDef.toStringValue(HAPSerializationFormat.LITERATE);
	    HAPFileUtility.writeFile(fileFullName, resourceDefStr);
	    HAPFileUtility.writeFile(tempFileName, resourceDefStr);
	    return fileFullName;
	}

	public static Set<HAPStoryNode> getAllStoryNodeByType(HAPStory story, String type) {  return getStoryNodeByType(story, type, false);  }
	public static Set<HAPStoryNode> getStoryNodeByType(HAPStory story, String type) {  return getStoryNodeByType(story, type, true);  }
	
	private static Set<HAPStoryNode> getStoryNodeByType(HAPStory story, String type, boolean onlyEnable) {
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

	public static List<HAPInfoNodeChild> getAllChildNode(HAPStoryNode parent, HAPStory story) {  return getChildNode(parent, story, false);  }
	public static List<HAPInfoNodeChild> getChildNode(HAPStoryNode parent, HAPStory story) {  return getChildNode(parent, story, true);  }
	private static List<HAPInfoNodeChild> getChildNode(HAPStoryNode parent, HAPStory story, boolean onlyEnable) {
		List<HAPInfoNodeChild> out = new ArrayList<HAPInfoNodeChild>();
		List<HAPConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstant.STORYCONNECTION_TYPE_CONTAIN, HAPConstant.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPConnectionEnd connectionEnd : childConnectionEnds) {
			HAPConnectionContain containerConnectionEntity = (HAPConnectionContain)story.getConnection(connectionEnd.getConnectionId());
			HAPStoryNode node = (HAPStoryNode)story.getElement(connectionEnd.getNodeRef());
			if(isValid(containerConnectionEntity, onlyEnable) && isValid(node, onlyEnable)) {
				out.add(new HAPInfoNodeChild(node, containerConnectionEntity));
			}
		}
		return out;
	}
	
	public static List<HAPStoryNode> getAllChildNode(HAPStoryNode parent, String childId, HAPStory story) {  return getChildNode(parent, childId, story, false);  }
	public static List<HAPStoryNode> getChildNode(HAPStoryNode parent, String childId, HAPStory story) {  return getChildNode(parent, childId, story, true);  }
	private static List<HAPStoryNode> getChildNode(HAPStoryNode parent, String childId, HAPStory story, boolean onlyEnable) {
		List<HAPStoryNode> out = new ArrayList<HAPStoryNode>();
		List<HAPConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstant.STORYCONNECTION_TYPE_CONTAIN, HAPConstant.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPConnectionEnd connectionEnd : childConnectionEnds) {
			HAPConnectionContain containerConnectionEntity = (HAPConnectionContain)story.getConnection(connectionEnd.getConnectionId());
			if(HAPBasicUtility.isEquals(childId, containerConnectionEntity.getChildId())) {
				HAPStoryNode node = (HAPStoryNode)story.getElement(connectionEnd.getNodeRef());
				if(isValid(containerConnectionEntity, onlyEnable) && isValid(node, onlyEnable)) {
					out.add(node);
				}
			}
		}
		return out;
	}
	
	public static List<HAPConnectionEnd> getConnectionEnd(HAPStoryNode node1, String connectionType, String profile1, String node2Type, String profile2, HAPStory story) {
		List<HAPConnectionEnd> out = new ArrayList<HAPConnectionEnd>();
		for(String connectionId :  node1.getConnections()) {
			HAPConnection connection = story.getConnection(connectionId);
			if(connection.isEnable()) {
				if(connectionType==null || connection.getType().equals(connectionType)){
					HAPConnectionEnd node1End = getConnectionEnd(connection, node1.getId());
					if(profile1==null || profile1.equals(node1End.getProfile())) {
						HAPConnectionEnd node2End = getOtherConnectionEnd(connection, node1.getId());
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
	
	public static HAPConnectionEnd getOtherConnectionEnd(HAPConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeElementId().getId()))   return connection.getEnd2();
		else return connection.getEnd1();
	}
	
	public static HAPConnectionEnd getConnectionEnd(HAPConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeElementId().getId()))   return connection.getEnd1();
		else return connection.getEnd2();
	}
	
	public static HAPIdElement getElementIdByReference(HAPReferenceElement eleRef, HAPStory story) {
		if(eleRef instanceof HAPIdElement)  return (HAPIdElement)eleRef;
		else if(eleRef instanceof HAPAliasElement)  return story.getElementId(((HAPAliasElement)eleRef).getAlias());
		return null;
	}
	
	public static HAPIdElement newNodeId(String id) {   return new HAPIdElement(HAPConstant.STORYELEMENT_CATEGARY_NODE, id);    }
}
