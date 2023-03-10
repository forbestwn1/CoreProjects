package com.nosliw.data.core.complex;

import org.json.JSONObject;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPResultSolveReference {

	private boolean m_isFromAttachment;
	
	private HAPIdEntityInDomain m_entityId;
	
	private JSONObject m_attachmentAdapter;
	
	private HAPContextProcessor m_context;
	
	public static HAPResultSolveReference newResultFromResource(HAPIdEntityInDomain entityId, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = false;
		out.m_entityId = entityId;
		out.m_context = context;
		return out;
	}
	
	public static HAPResultSolveReference newResultFromAttachment(HAPIdEntityInDomain entityId, JSONObject attachmentAdapter, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = true;
		out.m_entityId = entityId;
		out.m_context = context;
		out.m_attachmentAdapter = attachmentAdapter;
		return out;
	}
	
	public boolean isFromAttachment() {   return this.m_isFromAttachment;     }
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	
	public JSONObject getAttachmentAdapter() {    return this.m_attachmentAdapter;     }
	
	public HAPContextProcessor getContext() {   return this.m_context;    }
	
}
