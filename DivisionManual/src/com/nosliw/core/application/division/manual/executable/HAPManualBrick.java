package com.nosliw.core.application.division.manual.executable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.valuecontext.HAPValueContext;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPManualBrick extends HAPSerializableImp implements HAPBrick{

	public final static String ISCOMPLEX = "isComplex"; 

	//all attributes
	private List<HAPManualAttributeInBrick> m_attributes;
	
	private HAPValueContext m_valueContext;

	
	private HAPInfoTreeNode m_tempTreeNodeInfo;

	private HAPInfoBrickType m_brickTypeInfo;
	
	private HAPIdBrickType m_brickTypeId;
	
	private HAPRuntimeEnvironment m_runtimeEnv;

	public HAPManualBrick() {
		this.m_valueContext = new HAPValueContext(); 
		this.m_attributes = new ArrayList<HAPManualAttributeInBrick>();
	}
	
	@Override
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.BRICK;   }

	public void setRuntimeEnvironment(HAPRuntimeEnvironment runtimeEnv) {     this.m_runtimeEnv = runtimeEnv;      }
	
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   this.m_tempTreeNodeInfo = treeNodeInfo;     }

	public HAPInfoBrickType getBrickTypeInfo() {    return this.m_brickTypeInfo;     }
	public void setBrickTypeInfo(HAPInfoBrickType brickTypeInfo) {    this.m_brickTypeInfo = brickTypeInfo;     }
	
	public void setBrickType(HAPIdBrickType brickTypeId) {   this.m_brickTypeId = brickTypeId;     }
	
	@Override
	public HAPIdBrickType getBrickType() {   return this.m_brickTypeId;     }
	
	@Override
	public List<HAPAttributeInBrick> getAttributes(){     return (List)this.m_attributes;	}
	public HAPManualAttributeInBrick getAttribute(String attrName) {
		for(HAPManualAttributeInBrick attr : this.m_attributes) {
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
	public HAPManualBrick getAttributeValueOfBrick(String attributeName) {
		HAPManualBrick out = null;
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
	
	public void setAttribute(HAPManualAttributeInBrick attribute) {
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
		HAPManualAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			out = attr.getValueWrapper();
		}
		return out;
	}
	
	public void setAttributeValueWithValue(String attributeName, Object attrValue) {	this.setAttribute(new HAPManualAttributeInBrick(attributeName, new HAPWrapperValueOfValue(attrValue)));	}
	public void setAttributeValueWithBrick(String attributeName, HAPEntityOrReference brickOrRef) {
		if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.BRICK)) {
			this.setAttribute(new HAPManualAttributeInBrick(attributeName, new HAPManualWrapperValueOfBrick((HAPManualBrick)brickOrRef)));
		}
		else if(brickOrRef.getEntityOrReferenceType().equals(HAPConstantShared.RESOURCEID)) {
			this.setAttribute(new HAPManualAttributeInBrick(attributeName, new HAPWrapperValueOfReferenceResource((HAPResourceId)brickOrRef)));
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
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		HAPBrick.buildJSJsonMap(this, jsonMap, typeJsonMap);
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		for(HAPManualAttributeInBrick attr : this.m_attributes) {
			attr.buildResourceDependency(dependency, runtimeInfo);
		}
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.getRuntimeEnvironment().getBrickManager();     }
	protected HAPManagerResource getResourceManager() {    return this.getRuntimeEnvironment().getResourceManager();     }
	
	abstract public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan);

	abstract public void init();
	
}
