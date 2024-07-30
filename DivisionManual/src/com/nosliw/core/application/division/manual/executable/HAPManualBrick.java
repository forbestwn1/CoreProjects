package com.nosliw.core.application.division.manual.executable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPValueContext;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWrapperValuePort;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValuePortValueContext;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualBrick extends HAPBrickImp{

	public final static String ISCOMPLEX = "isComplex"; 

	private HAPManualValueContext m_valueContext;
	
	private HAPInfoTreeNode m_tempTreeNodeInfo;

	private HAPInfoBrickType m_brickTypeInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManualManagerBrick m_manualBrickMan;

	private HAPDomainValueStructure m_valueStructureDomain; 

	private HAPContainerVariableInfo m_varInfoContainer;
	
	public HAPManualBrick() {
		this.m_varInfoContainer = new HAPContainerVariableInfo(this); 
		this.m_valueContext = new HAPManualValueContext(); 
	}
	
	@Override
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	public HAPManualValueContext getManualValueContext() {    return this.m_valueContext;    }
	
	public void setRuntimeEnvironment(HAPRuntimeEnvironment runtimeEnv) {     this.m_runtimeEnv = runtimeEnv;      }
	
	public void setValueStructureDomain(HAPDomainValueStructure valueStructureDomain) {   this.m_valueStructureDomain = valueStructureDomain;     }

	public HAPContainerVariableInfo getVariableInfoContainer() {    return this.m_varInfoContainer;      }
	public void setVariableInfoContainer(HAPContainerVariableInfo varInfoContainer) {     this.m_varInfoContainer = varInfoContainer;      }
	
	protected HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	public void setManualBrickManager(HAPManualManagerBrick manualBrickMan) {    this.m_manualBrickMan = manualBrickMan;       }
	
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   this.m_tempTreeNodeInfo = treeNodeInfo;     }

	public HAPInfoBrickType getBrickTypeInfo() {    return this.m_brickTypeInfo;     }
	public void setBrickTypeInfo(HAPInfoBrickType brickTypeInfo) {    this.m_brickTypeInfo = brickTypeInfo;     }
	
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
				out = ((HAPManualWrapperValueOfBrick)valueWrapper).getBrick();
			}
			else if(valueType.equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID)) {
				HAPWrapperValueOfReferenceResource valueWrapperResourceId = (HAPWrapperValueOfReferenceResource)valueWrapper;
				out = HAPUtilityBrick.getBrickByResource(valueWrapperResourceId.getNormalizedResourceId(), this.getBrickManager());			
			}
		}
		return out;	
	}
	
	public void setAttribute(HAPManualAttributeInBrick attribute) {
		super.setAttribute(attribute);
		
		HAPInfoTreeNode treeNodeInfo = new HAPInfoTreeNode(this.getTreeNodeInfo().getPathFromRoot().appendSegment(attribute.getName()), this);
		attribute.setTreeNodeInfo(treeNodeInfo);
	}
	
	private HAPWrapperValue getAttributeValueWrapper(String attributeName) {
		HAPWrapperValue out = null; 
		HAPManualAttributeInBrick attr = (HAPManualAttributeInBrick)this.getAttribute(attributeName);
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
		this.setAttributeValueWithBrick(attributeName, this.getManualBrickManager().newBrick(brickTypeId));
	}
	
	public List<HAPManualPartInValueContext> getValueContextInhertanceDownstream(){
		
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		if(!this.getValueContext().isEmpty()) {
			HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
			valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
			valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPManualValuePortValueContext(this, this.m_valueStructureDomain)), true);
			out.addValuePortGroup(valePortGroup, true);
		}
			
		for(HAPGroupValuePorts group : this.getInternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		if(!this.getValueContext().isEmpty()) {
			HAPGroupValuePorts valePortGroup = new HAPGroupValuePorts();
			valePortGroup.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
			valePortGroup.addValuePort(new HAPWrapperValuePort(new HAPManualValuePortValueContext(this, this.m_valueStructureDomain)), true);
			out.addValuePortGroup(valePortGroup, true);
		}
		
		for(HAPGroupValuePorts group : this.getExternalOtherValuePortGroups()) {
			out.addValuePortGroup(group, false);
		}
		return out;
	}

	protected Set<HAPGroupValuePorts> getInternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	protected Set<HAPGroupValuePorts> getExternalOtherValuePortGroups() {   return new HashSet<HAPGroupValuePorts>();   }
	
//	@Override
//	protected boolean buildBrickFormatJson(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
//		super.buildBrickFormatJson(jsonObj, brickMan);
//		JSONObject valueContextJsonObj = jsonObj.optJSONObject(VALUECONTEXT);
//		if(valueContextJsonObj!=null) {
//			this.m_valueContext = new HAPValueContext();
//			this.m_valueContext.buildObject(valueContextJsonObj, HAPSerializationFormat.JSON);
//			
//		}
//		return true;
//	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.getRuntimeEnvironment().getBrickManager();     }
	protected HAPManagerResource getResourceManager() {    return this.getRuntimeEnvironment().getResourceManager();     }
	
	abstract public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan);

	abstract public void init();
	
}
