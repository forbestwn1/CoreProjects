package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainEntityExecutableResourceComplex;
import com.nosliw.data.core.domain.HAPEmbededExecutableWithId;
import com.nosliw.data.core.domain.container.HAPContainerEntityExecutable;
import com.nosliw.data.core.domain.entity.valuestructure.HAPExecutableEntityComplexValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityComplex extends HAPExecutableEntity{

	@HAPAttribute
	public static final String VALUESTRUCTURECOMPLEX = "valueStructureComplex";
	@HAPAttribute
	public static final String ATTACHMENTCONTAINERID = "attachmentContainerId";
	@HAPAttribute
	public static final String CHILDREN = "children";
	
	private HAPExecutableEntityComplexValueStructure m_valueStructureComplex;

	private String m_attachmentContainerId;
	
	//simple attribute by name, complex entity type
	private Map<String, HAPEmbededExecutableWithId> m_complexAttributesNormal;
	
	//container attribute by name, complex entity type
	private Map<String, HAPContainerEntityExecutable> m_complexAttributeContainer;
	
	public HAPExecutableEntityComplex(String entityType) {
		super(entityType);
		this.m_complexAttributesNormal = new LinkedHashMap<String, HAPEmbededExecutableWithId>();
		this.m_complexAttributeContainer = new LinkedHashMap<String, HAPContainerEntityExecutable>();
	}
	
	public void setValueStructureComplex(HAPExecutableEntityComplexValueStructure valueStructureComplex) {     this.m_valueStructureComplex = valueStructureComplex;      }
	public HAPExecutableEntityComplexValueStructure getValueStructureComplex() {    return this.m_valueStructureComplex;    }
	
	public void setAttachmentContainerId(String id) {    this.m_attachmentContainerId = id;    }
	public String getAttachmentContainerId() {    return this.m_attachmentContainerId;    }
	
//	public Map<String, HAPEmbededExecutableWithId> getNormalComplexAttributes(){    return this.m_complexAttributesNormal;    }
//	public void setNormalComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExeId) {	this.m_complexAttributesNormal.put(attrName, new HAPEmbededExecutableWithId(complexEntityExeId));	}
//	
//	public void setContainerComplexAttribute(String attribute, HAPContainerEntityExecutable entityContainer) {		this.m_complexAttributeContainer.put(attribute, entityContainer);	}
//	public Map<String, HAPContainerEntityExecutable> getContainerComplexAttributes(){   return this.m_complexAttributeContainer;   }
	
	public String toExpandedJsonString(HAPDomainEntityExecutableResourceComplex entityDomainExe) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		jsonMap.put(ENTITYTYPE, this.getEntityType());
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		jsonMap.put(VALUESTRUCTURECOMPLEX, this.m_valueStructureComplex.toExpandedString(entityDomainExe.getValueStructureDomain()));

		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> attrTypeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.buildExpandedAttributeJsonMap(attrJsonMap, attrTypeJsonMap, entityDomainExe);
		jsonMap.put(ATTRIBUTE, HAPJsonUtility.buildMapJson(attrJsonMap, attrTypeJsonMap));

		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedAttributeJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityExecutableResourceComplex entityDomainExe){
		for(String attrName : this.m_complexAttributesNormal.keySet()) {
			jsonMap.put(attrName, this.m_complexAttributesNormal.get(attrName).toExpandedJsonString(entityDomainExe));
		}
		
		for(String attrName : this.m_complexAttributeContainer.keySet()) {
			jsonMap.put(attrName, this.m_complexAttributeContainer.get(attrName).toExpandedJsonString(entityDomainExe));
		}
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(VALUESTRUCTURECOMPLEX, this.m_valueStructureComplex.toResourceData(runtimeInfo).toString());
		
		Map<String, String> childrenJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> childrenTypeJsonMap = new LinkedHashMap<String, Class<?>>(); 
		this.buildChildrenResourceJsonMap(childrenJsonMap, childrenTypeJsonMap, runtimeInfo);
		jsonMap.put(CHILDREN, HAPJsonUtility.buildMapJson(childrenJsonMap, childrenTypeJsonMap));

	}

	protected void buildChildrenResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		for(String attrName : this.m_complexAttributesNormal.keySet()) {
			jsonMap.put(attrName, this.m_complexAttributesNormal.get(attrName).toStringValue(HAPSerializationFormat.JSON));
		}
		
		for(String attrName : this.m_complexAttributeContainer.keySet()) {
			jsonMap.put(attrName, this.m_complexAttributeContainer.get(attrName).toStringValue(HAPSerializationFormat.JSON));
		}
	}

}
