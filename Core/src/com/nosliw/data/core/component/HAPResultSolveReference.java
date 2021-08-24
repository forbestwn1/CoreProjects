package com.nosliw.data.core.component;

import org.json.JSONObject;

public class HAPResultSolveReference {

	private boolean m_isFromAttachment;
	
	private Object m_entity;
	
	private JSONObject m_attachmentAdapter;
	
	private HAPContextProcessor m_context;
	
	public static HAPResultSolveReference newResultFromResource(Object entity, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = false;
		out.m_entity = entity;
		out.m_context = context;
		return out;
	}
	
	public static HAPResultSolveReference newResultFromAttachment(Object entity, JSONObject attachmentAdapter, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = true;
		out.m_entity = entity;
		out.m_context = context;
		out.m_attachmentAdapter = attachmentAdapter;
		return out;
	}
	
	public boolean isFromAttachment() {   return this.m_isFromAttachment;     }
	
	public Object getEntity() {    return this.m_entity;     }
	
	public JSONObject getAttachmentAdapter() {    return this.m_attachmentAdapter;     }
	
	public HAPContextProcessor getContext() {   return this.m_context;    }
	
}
