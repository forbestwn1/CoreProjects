package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePort;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPBrick extends HAPSerializableImp implements HAPEntityOrReference, HAPWithValuePort, HAPWithResourceDependency{

	@HAPAttribute
	public final static String ATTRIBUTE = "attribute"; 

	@HAPAttribute
	public final static String BRICKTYPE = "brickType"; 

	@HAPAttribute
	public final static String ISCOMPLEX = "isComplex"; 

	//all attributes
	private List<HAPAttributeInBrick> m_attributes;
	
	private HAPInfoTreeNode m_tempTreeNodeInfo;

	private HAPInfoBrickType m_brickTypeInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPBrick() {
		this.m_attributes = new ArrayList<HAPAttributeInBrick>();
	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.BRICK;   }

	public void setRuntimeEnvironment(HAPRuntimeEnvironment runtimeEnv) {     this.m_runtimeEnv = runtimeEnv;      }
	
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   this.m_tempTreeNodeInfo = treeNodeInfo;     }

	public HAPInfoBrickType getBrickTypeInfo() {    return this.m_brickTypeInfo;     }
	public void setBrickTypeInfo(HAPInfoBrickType brickTypeInfo) {    this.m_brickTypeInfo = brickTypeInfo;     }
	public HAPIdBrickType getBrickType() {   return this.getBrickTypeInfo().getBrickTypeId();     }
	
	public List<HAPAttributeInBrick> getAttributes(){     return this.m_attributes;      }
	public HAPAttributeInBrick getAttribute(String attrName) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}
	public Object getAttributeValueOfValue(String attributeName) {
		Object out = null;
		HAPWrapperValueOfValue valueWrapper = (HAPWrapperValueOfValue)this.getAttributeValueWrapper(attributeName);
		if(valueWrapper!=null) {
			out = valueWrapper.getValue();
		}
		return out;
	}
	public HAPBrick getAttributeValueOfBrick(String attributeName) {
		HAPBrick out = null;
		HAPWrapperValue valueWrapper = this.getAttributeValueWrapper(attributeName);
		if(valueWrapper!=null) {
			String valueType = valueWrapper.getValueType();
			if(valueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)) {
				out = ((HAPWrapperValueOfBrick)valueWrapper).getBrick();
			}
			else if(valueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				HAPWrapperValueOfReferenceResource valueWrapperResourceId = (HAPWrapperValueOfReferenceResource)valueWrapper;
				out = HAPUtilityBrick.getBrickByResource(valueWrapperResourceId.getNormalizedResourceId(), this.getBrickManager());			
			}
		}
		return out;	
	}
	
	public void setAttribute(HAPAttributeInBrick attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);
		
		HAPInfoTreeNode treeNodeInfo = new HAPInfoTreeNode(this.getTreeNodeInfo().getPathFromRoot().appendSegment(attribute.getName()), this);
		attribute.setTreeNodeInfo(treeNodeInfo);
	}
	
	private HAPWrapperValue getAttributeValueWrapper(String attributeName) {
		HAPWrapperValue out = null; 
		HAPAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			out = attr.getValueWrapper();
		}
		return out;
	}
	
	public void setAttributeValueWithValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPAttributeInBrick(attributeName, new HAPWrapperValueOfValue(attrValue)));	}
	public void setAttributeValueWithBrick(String attributeName, HAPEntityOrReference brickOrRef) {
		if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.BRICK)) {
			this.setAttribute(new HAPAttributeInBrick(attributeName, new HAPWrapperValueOfBrick((HAPBrick)brickOrRef)));
		}
		else if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.RESOURCEID)) {
			this.setAttribute(new HAPAttributeInBrick(attributeName, new HAPWrapperValueOfReferenceResource((HAPResourceId)brickOrRef)));
		}
	}
	public void setAttributeValueWithBrickNew(String attributeName, HAPIdBrickType brickTypeId) {
		this.setAttributeValueWithBrick(attributeName, this.getBrickManager().newBrickInstance(brickTypeId));
	}
	
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {		return null;	}

	@Override
	public HAPContainerValuePorts getInternalValuePorts() {		return null;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildJson(this.m_attributes, HAPSerializationFormat.JSON));
		if(m_brickTypeInfo!=null) {
			jsonMap.put(BRICKTYPE, this.m_brickTypeInfo.getBrickTypeId().toStringValue(HAPSerializationFormat.JSON));
			jsonMap.put(ISCOMPLEX, this.m_brickTypeInfo.getIsComplex()+"");
			typeJsonMap.put(ISCOMPLEX, Boolean.class);
		}
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);

		List<String> attrJsonList = new ArrayList<String>();
		for(HAPAttributeInBrick attr : this.m_attributes) {
			attrJsonList.add(attr.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildArrayJson(attrJsonList.toArray(new String[0])));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			attr.buildResourceDependency(dependency, runtimeInfo);
		}
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.getRuntimeEnvironment().getBrickManager();     }
	protected HAPResourceManager getResourceManager() {    return this.getRuntimeEnvironment().getResourceManager();     }
	
	abstract public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan);

	abstract public void init();
	
}
