package com.nosliw.data.core.complex;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LinkedMap;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.domain.HAPContainerEntity;
import com.nosliw.data.core.domain.HAPDomainEntityExecutable;
import com.nosliw.data.core.domain.HAPEmbededEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public abstract class HAPExecutableEntityComplex extends HAPExecutableImp{

	public static final String VALUESTRUCTURECOMPLEXID = "valueStructureComplexId";
	public static final String ATTACHMENTCONTAINERID = "attachmentContainerId";
	public static final String ATTRIBUTE = "attribute";
	
	private String m_valueStructureComplexId;

	private String m_attachmentContainerId;
	
	//simple attribute by name
	private Map<String, HAPEmbededEntity> m_attributesNormal;
	
	//container attribute by name
	private Map<String, HAPContainerEntity> m_attributeContainer;
	
	private String m_entityType;
	
	public HAPExecutableEntityComplex(String entityType) {
		this.m_entityType = entityType;
		this.m_attributesNormal = new LinkedHashMap<String, HAPEmbededEntity>();
		this.m_attributeContainer = new LinkedHashMap<String, HAPContainerEntity>();
	}
	
	public String getEntityType() {    return this.m_entityType;   }
	
	public void setValueStructureComplexId(String id) {     this.m_valueStructureComplexId = id;      }
	public String getValueStructureComplexId() {    return this.m_valueStructureComplexId;    }
	
	public void setAttachmentContainerId(String id) {    this.m_attachmentContainerId = id;    }
	public String getAttachmentContainerId() {    return this.m_attachmentContainerId;    }
	
	public Map<String, HAPEmbededEntity> getNormalAttributes(){    return this.m_attributesNormal;    }
	public void setNormalComplexAttribute(String attrName, HAPIdEntityInDomain complexEntityExeId) {	this.m_attributesNormal.put(attrName, new HAPEmbededEntity(complexEntityExeId));	}
	
	public void setContainerAttributeElementComplex(String attribute, HAPContainerEntity entityContainer) {		this.m_attributeContainer.put(attribute, entityContainer);	}
	public Map<String, HAPContainerEntity> getContainerAttributes(){   return this.m_attributeContainer;   }
	
	public String toExpandedJsonString(HAPDomainEntityExecutable entityDomainExe) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedMap<String, Class<?>>(); 
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomainExe);
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntityExecutable entityDomainExe){
		jsonMap.put(ATTACHMENTCONTAINERID, this.m_attachmentContainerId);
		jsonMap.put(VALUESTRUCTURECOMPLEXID, this.m_valueStructureComplexId);
		
		for(String attrName : this.m_attributesNormal.keySet()) {
			HAPSerializableImp entityId = this.m_attributesNormal.get(attrName);
			jsonMap.put(attrName, this.m_attributesNormal.get(attrName).toExpandedJsonString(entityDomainExe));
		}
		
		for(String attrName : this.m_attributeContainer.keySet()) {
			jsonMap.put(attrName, this.m_attributeContainer.get(attrName).toExpandedJsonString(entityDomainExe));
		}
	}

}
