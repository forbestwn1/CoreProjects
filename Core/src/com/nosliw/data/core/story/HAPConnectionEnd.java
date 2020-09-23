package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

//connect end connect to a profile on a node 
@HAPEntityWithAttribute
public class HAPConnectionEnd extends HAPSerializableImp{

	@HAPAttribute
	public static final String CONNECTIONID = "connectionId";
	@HAPAttribute
	public static final String NODEREF = "nodeRef";
	@HAPAttribute
	public static final String PROFILE = "profile";
	
	private String m_connectionId;
	
	private HAPReferenceElement m_nodeRef;
	
	private String m_profile;

	private boolean m_ifDeleteNode;

	public HAPConnectionEnd() {}

	public HAPConnectionEnd(HAPReferenceElement nodeRef) {
		this.m_nodeRef = nodeRef;
	}
	
	public String getConnectionId() {    return this.m_connectionId;    }
	public void setConnectionId(String connectionId) {   this.m_connectionId = connectionId;    }
	
	public HAPReferenceElement getNodeRef() {   return this.m_nodeRef;   }
	public void setNodeRef(HAPReferenceElement nodeRef) {    this.m_nodeRef = nodeRef;    }
	
	public HAPIdElement getNodeElementId() {   return (HAPIdElement)this.m_nodeRef;     }
	
	public String getProfile() {    return this.m_profile;  }
	public void setProfile(String profile) {    this.m_profile = profile;    }
	
	public HAPConnectionEnd cloneConnectionEnd() {
		HAPConnectionEnd out = new HAPConnectionEnd();
		out.m_connectionId = this.m_connectionId;
		out.m_profile = this.m_profile;
		out.m_ifDeleteNode = this.m_ifDeleteNode;
		if(this.m_nodeRef!=null)   out.m_nodeRef = this.m_nodeRef.cloneElementReference();
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_nodeRef = HAPParserElementReference.parse(jsonObj.getJSONObject(NODEREF));
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
