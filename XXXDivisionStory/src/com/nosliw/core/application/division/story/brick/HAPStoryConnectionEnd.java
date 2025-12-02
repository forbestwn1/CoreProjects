package com.nosliw.core.application.division.story.brick;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryParserElementReference;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;

//connect end connect to a profile on a node 
@HAPEntityWithAttribute
public class HAPStoryConnectionEnd extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONNECTIONID = "connectionId";
	@HAPAttribute
	public static final String NODEREF = "nodeRef";
	@HAPAttribute
	public static final String PROFILE = "profile";
	
	private String m_connectionId;
	
	private HAPStoryReferenceElement m_nodeRef;
	
	private String m_profile;

	private boolean m_ifDeleteNode;

	public HAPStoryConnectionEnd() {}

	public HAPStoryConnectionEnd(HAPStoryReferenceElement nodeRef) {
		this.m_nodeRef = nodeRef;
	}
	
	public String getConnectionId() {    return this.m_connectionId;    }
	public void setConnectionId(String connectionId) {   this.m_connectionId = connectionId;    }
	
	public HAPStoryReferenceElement getNodeRef() {   return this.m_nodeRef;   }
	public void setNodeRef(HAPStoryReferenceElement nodeRef) {    this.m_nodeRef = nodeRef;    }
	
	public HAPStoryIdElement getNodeElementId() {   return (HAPStoryIdElement)this.m_nodeRef;     }
	
	public String getProfile() {    return this.m_profile;  }
	public void setProfile(String profile) {    this.m_profile = profile;    }
	
	public HAPStoryConnectionEnd cloneConnectionEnd() {
		HAPStoryConnectionEnd out = new HAPStoryConnectionEnd();
		out.m_connectionId = this.m_connectionId;
		out.m_profile = this.m_profile;
		out.m_ifDeleteNode = this.m_ifDeleteNode;
		if(this.m_nodeRef!=null)   out.m_nodeRef = this.m_nodeRef.cloneElementReference();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_nodeRef = HAPStoryParserElementReference.parse(jsonObj.getJSONObject(NODEREF));
		this.m_profile = (String)jsonObj.opt(PROFILE);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NODEREF, this.m_nodeRef.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CONNECTIONID, this.m_connectionId);
		jsonMap.put(PROFILE, this.m_profile);
	}
}
