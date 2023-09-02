package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityComplex extends HAPExecutableEntity{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	@HAPAttribute
	public static final String ATTACHMENTCONTAINERID = "attachmentContainerId";

	@HAPAttribute
	static final public String VALUECONSTANT = "valueConstant";  
	@HAPAttribute
	static final public String DATACONSTANT = "dataConstant";  
	
	@HAPAttribute
	static final public String DATAEEXPRESSIONGROUP = "dataExpressionGroup";  
	@HAPAttribute
	static final public String SCRIPTEEXPRESSIONGROUP = "scriptExpressionGroup";  
	@HAPAttribute
	static final public String PLAINSCRIPTEEXPRESSIONGROUP = "plainScriptExpressionGroup";  

	private HAPExecutableEntityValueContext m_valueContext;

	private String m_attachmentContainerId;
	
	public HAPExecutableEntityComplex() {}

	public HAPExecutableEntityComplex(String entityType) {
		super(entityType);
		this.setAttributeValueObject(VALUECONSTANT, new LinkedHashMap<String, Object>());
		this.setAttributeValueObject(DATACONSTANT, new LinkedHashMap<String, HAPData>());
	}
	
	public HAPIdEntityInDomain getComplexEntityAttributeValue(String attrName) {   return (HAPIdEntityInDomain)this.getAttributeEmbeded(attrName).getValue();    }
	
	public void setValueContext(HAPExecutableEntityValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPExecutableEntityValueContext getValueContext() {    return this.m_valueContext;    }
	
	public void setAttachmentContainerId(String id) {    this.m_attachmentContainerId = id;    }
	public String getAttachmentContainerId() {    return this.m_attachmentContainerId;    }
	
	public Map<String, Object> getAllValueConstants(){    return (Map<String, Object>)this.getAttributeValue(VALUECONSTANT);      }
	public Object getConstantValue(String name) {   return this.getAllValueConstants().get(name);      }
	public void addValueConstant(String name, Object value) {    this.getAllValueConstants().put(name, value);        }
	
	public Map<String, HAPData> getAllDataConstants(){    return (Map<String, HAPData>)this.getAttributeValue(DATACONSTANT);      }
	public HAPData getConstantData(String name) {   return this.getAllDataConstants().get(name);      }
	public void addDataConstant(String name, HAPData data) {    this.getAllDataConstants().put(name, data);        }
	
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
