package com.nosliw.data.core.domain.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableEntityExpressionScriptGroup;
import com.nosliw.data.core.domain.valuecontext.HAPExecutableEntityValueContext;
import com.nosliw.data.core.domain.valuecontext.HAPValuePortValueContext;
import com.nosliw.data.core.domain.valueport.HAPContainerValuePorts;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemUtility;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityComplex extends HAPExecutableEntity{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	@HAPAttribute
	public static final String ATTACHMENTCONTAINERID = "attachmentContainerId";

	@HAPAttribute
	static final public String VALUECONSTANT = HAPSystemUtility.buildSystemName("valueConstant");  
	@HAPAttribute
	static final public String DATACONSTANT = HAPSystemUtility.buildSystemName("dataConstant");  
	
	@HAPAttribute
	static final public String DATAEEXPRESSIONGROUP = HAPSystemUtility.buildSystemName("dataExpressionGroup");  
	
	@HAPAttribute
	static final public String SCRIPTEEXPRESSIONGROUP = HAPSystemUtility.buildSystemName("scriptExpressionGroup");  

	//script expression without any variable
	@HAPAttribute
	static final public String PLAINSCRIPTEEXPRESSIONGROUP = HAPSystemUtility.buildSystemName("plainScriptExpressionGroup");  

	@HAPAttribute
	static final public String PLAINSCRIPTEEXPRESSIONGROUPVALUE = HAPSystemUtility.buildSystemName("plainScriptExpressionGroupValue");  

	private HAPExecutableEntityValueContext m_valueContext;

	private String m_attachmentContainerId;
	
	private HAPDomainValueStructure m_valueStructureDomain; 
	
	public HAPExecutableEntityComplex() {
		this.init();
	}

	public HAPExecutableEntityComplex(String entityType) {
		super(entityType);
		this.init();
	}

	private void init() {
		this.setAttributeValueObject(VALUECONSTANT, new LinkedHashMap<String, Object>());
		this.getAttribute(VALUECONSTANT).setAttributeAutoProcess(false);
		this.setAttributeValueObject(DATACONSTANT, new LinkedHashMap<String, HAPData>());
		this.getAttribute(DATACONSTANT).setAttributeAutoProcess(false);
		this.setAttributeValueObject(PLAINSCRIPTEEXPRESSIONGROUPVALUE, new LinkedHashMap<String, String>());
		this.getAttribute(PLAINSCRIPTEEXPRESSIONGROUPVALUE).setAttributeAutoProcess(false);
	}
	
//	public HAPIdEntityInDomain getComplexEntityAttributeValue(String attrName) {
//		HAPEmbededExecutable embeded = this.getAttributeEmbeded(attrName);
//		return embeded==null?null:(HAPIdEntityInDomain)embeded.getValue();    
//	}
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }
	
	@Override
	public HAPContainerValuePorts getValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePort(new HAPValuePortValueContext(this, this.m_valueStructureDomain, true));
		out.addValuePorts(this.getOtherValuePorts());
		return out;
	}
	
	protected HAPContainerValuePorts getOtherValuePorts() {   return null;   }
	
	public HAPExecutableEntityComplex getComplexEntityAttributeValue(String attrName) {
		HAPEmbededExecutable embeded = this.getAttributeEmbeded(attrName);
		return embeded==null?null:(HAPExecutableEntityComplex)embeded.getValue();    
	}

	
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
	
	public Map<String, String> getPlainScriptExpressionValues(){   return (Map<String, String>)this.getAttributeValue(PLAINSCRIPTEEXPRESSIONGROUPVALUE);     }
	public void setPlainScriptExpressionValue(String name, String value) {    this.getPlainScriptExpressionValues().put(name, value);      }
	
	public HAPIdEntityInDomain getPlainScriptExpressionGroupEntityId() {    return this.getComplexEntityAttributeValue(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);     }
	public HAPExecutableEntityExpressionScriptGroup getPlainScriptExpressionGroupEntity(HAPContextProcessor processContext) {
		return (HAPExecutableEntityExpressionScriptGroup)this.getAttributeValueEntity(HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		if(this.m_valueContext!=null) jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		super.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		HAPDomainEntityExecutableResourceComplex entityDomainExe = (HAPDomainEntityExecutableResourceComplex)entityDomain;
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		if(this.m_valueContext!=null)  jsonMap.put(VALUECONTEXT, this.m_valueContext.toExpandedString(entityDomainExe.getValueStructureDomain()));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_valueContext!=null)  jsonMap.put(VALUECONTEXT, this.m_valueContext.toResourceData(runtimeInfo).toString());
	}
}
