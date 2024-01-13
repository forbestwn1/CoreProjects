package com.nosliw.data.core.domain.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.data.core.domain.HAPDomainEntity;
import com.nosliw.data.core.domain.HAPExpandable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.valueport.HAPContainerValuePorts;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntity extends HAPExecutableImp implements HAPExpandable, HAPEntity{

	@HAPAttribute
	public static final String ENTITYTYPE = "entityType";
	@HAPAttribute
	public static final String ATTRIBUTE = "attribute";
	@HAPAttribute
	public static final String PARENT = "parent";
	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private String m_entityType;
	
	private List<HAPAttributeEntityExecutable> m_attributes;

	private HAPExecutableEntity m_parent;
	private HAPPath m_pathFromRoot;
	
	private HAPIdEntityInDomain m_definitionEntityId;
	
	public HAPExecutableEntity() {
		this.m_attributes = new ArrayList<HAPAttributeEntityExecutable>();
	} 

	public HAPExecutableEntity(String entityType) {
		this();
		this.m_entityType = entityType;
	}
	
	public abstract HAPContainerValuePorts getValuePorts();
	
	public void setDefinitionEntityId(HAPIdEntityInDomain defEntityId) {    this.m_definitionEntityId = defEntityId;      }
	public HAPIdEntityInDomain getDefinitionEntityId() {    return this.m_definitionEntityId;     }
	
	public String getId() {   return this.m_definitionEntityId.toStringValue(HAPSerializationFormat.LITERATE);    }
	
	public String getEntityType() {    return this.m_entityType;   }
	public void setEntityType(String entityType) {    this.m_entityType = entityType;     }

	public HAPExecutableEntity getParent() {    return this.m_parent;     }
	public void setParent(String attrName, HAPExecutableEntity parent) {
		this.m_parent = parent;
		this.m_pathFromRoot = new HAPPath(this.m_parent.getPathFromRoot()).appendSegment(attrName);
	}
	
	public HAPPath getPathFromRoot() {    return this.m_pathFromRoot;     }
	public void setPathFromRoot(HAPPath path) {   this.m_pathFromRoot = path;     }
	
	public HAPAttributeEntityExecutable getDescendantAttribute(HAPPath path) {
		HAPAttributeEntityExecutable out = null;
		for(int i=0; i<path.getLength(); i++) {
			String attribute = path.getPathSegments()[i];
			if(i==0)  out = this.getAttribute(attribute);
			else out = ((HAPExecutableEntity)out.getValue().getValue()).getAttribute(attribute);
		}
		return out;
	}

	public HAPExecutableEntity getDescendantEntity(HAPPath path) {
		if(path==null||path.isEmpty())  return this;
		else return (HAPExecutableEntity)this.getDescendantAttribute(path).getValue().getValue();
	}
	
	public List<HAPAttributeEntityExecutable> getAttributes(){    return this.m_attributes;     }
	
	public List<HAPAttributeEntityExecutable> getNormalAttributes(){
		List<HAPAttributeEntityExecutable> out = new ArrayList<HAPAttributeEntityExecutable>();
		for(HAPAttributeEntityExecutable attr : this.getAttributes()) {
			if(HAPUtilityNosliw.getNosliwCoreName(attr.getName())==null) {
				out.add(attr);
			}
		}
		return out;
	}
	
	
	public HAPAttributeEntityExecutable getAttribute(String attrName) {
		for(HAPAttributeEntityExecutable attr : this.m_attributes) {
			if(attrName.equals(attr.getName()))  return attr;
		}
		return null;
	}

	public HAPEmbededExecutable getAttributeEmbeded(String attrName) {
		HAPAttributeEntityExecutable attr = this.getAttribute(attrName);
		return attr==null?null:attr.getValue(); 
	}

	public String getAttributeValueType(String attrName) {    return this.getAttributeEmbeded(attrName).getValueType();     }
	
	public Object getAttributeValue(String attrName) {
		Object out = null;
		HAPEmbededExecutable embeded = this.getAttributeEmbeded(attrName);
		if(embeded!=null) {
			out = embeded.getValue();
		}
		return out;
	}

	public HAPExecutableEntity getAttributeValueEntity(String attrName) {   return (HAPExecutableEntity)this.getAttributeValue(attrName);      }
	public HAPReferenceExternal getAttributeReferenceExternal(String attrName) {   return (HAPReferenceExternal)this.getAttributeValue(attrName);     }
	
	public void setAttribute(HAPAttributeEntityExecutable attrObj) {
		attrObj.setParentEntity(attrObj.getName(), this);
		this.m_attributes.add(attrObj);
	}
	
	public void setAttributeValueObject(String attributeName, Object value) {    setAttribute(attributeName, new HAPEmbededExecutable(value, HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE), new HAPInfoValueType());   }
	
	public void setAttribute(String attributeName, HAPEmbededExecutable embededEntity, HAPInfoValueType valueTypeInfo) {	this.setAttribute(new HAPAttributeEntityExecutable(attributeName, embededEntity, valueTypeInfo));	}
	public void setAttributeSimple(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, false)); }
	public void setAttributeComplex(String attributeName, HAPEmbededExecutable embededEntity, String valueType) {  setAttribute(attributeName, embededEntity, new HAPInfoValueType(valueType, true)); }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		
		this.buildExpandedJsonMap(jsonMap, typeJsonMap, entityDomain);
		return HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap);
	}

	protected void buildExpandedJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPDomainEntity entityDomain) {
		buildCommonJsonMap(jsonMap, typeJsonMap);

		List<String> attrArray = new ArrayList<String>();
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			attrArray.add(attribute.toExpandedJsonString(entityDomain));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrArray.toArray(new String[0])));
	}

	protected void buildCommonJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {	
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		if(this.m_pathFromRoot!=null) jsonMap.put(PATHFROMROOT, this.m_pathFromRoot.toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		for(HAPAttributeEntityExecutable attribute : this.m_attributes) {
			HAPEmbededExecutable embeded = attribute.getValue();
			Object valueObj = embeded.getValue();
			if(valueObj instanceof HAPExecutable) {
				dependency.addAll(((HAPExecutable)valueObj).getResourceDependency(runtimeInfo, resourceManager));
			}
		
			for(HAPInfoAdapter adapterInfo : embeded.getAdapters()) {
				Object adapterObj = adapterInfo.getValue();
				if(adapterObj instanceof HAPExecutable) {
					dependency.addAll(((HAPExecutable)adapterObj).getResourceDependency(runtimeInfo, resourceManager));
				}
			}
		}
	}
}
