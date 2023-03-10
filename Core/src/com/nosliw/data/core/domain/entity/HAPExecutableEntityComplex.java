package com.nosliw.data.core.domain.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityComplex extends HAPExecutableEntity{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	@HAPAttribute
	public static final String ATTACHMENTCONTAINERID = "attachmentContainerId";
	
	private HAPExecutableEntityValueContext m_valueContext;

	private String m_attachmentContainerId;
	
	public HAPExecutableEntityComplex(String entityType) {
		super(entityType);
	}
	
	public void setValueContext(HAPExecutableEntityValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPExecutableEntityValueContext getValueContext() {    return this.m_valueContext;    }
	
	public void setAttachmentContainerId(String id) {    this.m_attachmentContainerId = id;    }
	public String getAttachmentContainerId() {    return this.m_attachmentContainerId;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		super.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		HAPDomainEntityExecutableResourceComplex entityDomainExe = (HAPDomainEntityExecutableResourceComplex)entityDomain;
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toExpandedString(entityDomainExe.getValueStructureDomain()));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toResourceData(runtimeInfo).toString());
	}
}
