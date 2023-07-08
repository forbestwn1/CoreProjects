package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPResultSolveReference {

	private boolean m_isFromAttachment;
	
	private HAPIdEntityInDomain m_entityId;
	
	private HAPContextProcessor m_context;
	
	public static HAPResultSolveReference newResultFromResource(HAPIdEntityInDomain entityId, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = false;
		out.m_entityId = entityId;
		out.m_context = context;
		return out;
	}
	
	public static HAPResultSolveReference newResultFromAttachment(HAPIdEntityInDomain entityId, HAPContextProcessor context) {
		HAPResultSolveReference out = new HAPResultSolveReference();
		out.m_isFromAttachment = true;
		out.m_entityId = entityId;
		out.m_context = context;
		return out;
	}
	
	public boolean isFromAttachment() {   return this.m_isFromAttachment;     }
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	
	public HAPContextProcessor getContext() {   return this.m_context;    }
	
}
