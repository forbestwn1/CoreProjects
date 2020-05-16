package com.nosliw.data.core.story;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.element.connection.ConnectionEntityContain;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityStory {

	public static String getBuilderId(HAPStory story) {   return null;     }
	public static void setBuilderId(HAPStory story, String builderId) {         }

	static private int index = 0;
	
	public static void exportBuildResourceDefinition(HAPStory story, HAPResourceDefinition resourceDef) {
		String fileName = story.getTopicType() + "_" + story.getName() + "_" + index++;

		File directory = new File(HAPSystemFolderUtility.getCurrentDynamicResourceExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    HAPFileUtility.writeFile(directory.getAbsolutePath()+"/"+fileName, resourceDef.toStringValue(HAPSerializationFormat.LITERATE));
	}

	public static Set<HAPStoryNode> getStoryNodeByType(HAPStory story, String type) {
		Set<HAPStoryNode> out = new HashSet<HAPStoryNode>();
		for(HAPStoryNode node : story.getNodes()) {
			if(type.equals(node.getType())) {
				out.add(node);
			}
		}
		return out;
	}

	public static Map<Object, HAPStoryNode> getChildNode(HAPStoryNode parent, HAPStory story) {
		Map<Object, HAPStoryNode> out = new LinkedHashMap<Object, HAPStoryNode>();
		Set<HAPConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstant.CONNECTION_TYPE_CONTAIN, HAPConstant.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPConnectionEnd connectionEnd : childConnectionEnds) {
			ConnectionEntityContain containerConnectionEntity = new ConnectionEntityContain(story.getConnection(connectionEnd.getConnectionId()).getEntity());
			out.put(containerConnectionEntity.getChildId(), story.getNode(connectionEnd.getNodeId()));
		}
		return out;
	}

	
	public static HAPStoryNode getChildNode(HAPStoryNode parent, String childId, HAPStory story) {
		Set<HAPConnectionEnd> childConnectionEnds = getConnectionEnd(parent, HAPConstant.CONNECTION_TYPE_CONTAIN, HAPConstant.STORYNODE_PROFILE_CONTAINER, null, null, story);
		for(HAPConnectionEnd connectionEnd : childConnectionEnds) {
			ConnectionEntityContain containerConnectionEntity = new ConnectionEntityContain(story.getConnection(connectionEnd.getConnectionId()).getEntity());
			if(HAPBasicUtility.isEquals(childId, containerConnectionEntity.getChildId())) {
				return story.getNode(connectionEnd.getNodeId());
			}
		}
		return null;
	}
	
	public static Set<HAPConnectionEnd> getConnectionEnd(HAPStoryNode node1, String connectionType, String profile1, String node2Type, String profile2, HAPStory story) {
		Set<HAPConnectionEnd> out = new HashSet<HAPConnectionEnd>();
		for(String connectionId :  node1.getConnections()) {
			HAPConnection connection = story.getConnection(connectionId);
			if(connectionType==null || connection.getType().equals(connectionType)){
				HAPConnectionEnd node1End = getConnectionEnd(connection, node1.getId());
				if(profile1==null || profile1.equals(node1End.getProfile())) {
					HAPConnectionEnd node2End = getOtherConnectionEnd(connection, node1.getId());
					if(profile2==null || profile2.equals(node2End.getProfile())) {
						if(node2Type==null || node2Type.equals(story.getNode(node2End.getNodeId()).getType())){
							out.add(node2End);
						}
					}
				}
			}
		}
		return out;
	}
	
	
	public static HAPConnectionEnd getOtherConnectionEnd(HAPConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeId()))   return connection.getEnd2();
		else return connection.getEnd1();
	}
	
	public static HAPConnectionEnd getConnectionEnd(HAPConnection connection, String nodeId) {
		if(nodeId.equals(connection.getEnd1().getNodeId()))   return connection.getEnd1();
		else return connection.getEnd2();
	}
	
	
}
