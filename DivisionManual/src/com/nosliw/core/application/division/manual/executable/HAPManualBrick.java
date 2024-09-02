package com.nosliw.core.application.division.manual.executable;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.HAPWrapperValueOfValue;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityValueContextProcessor;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualValueContext;
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

	private HAPBundle m_bundle; 

	private HAPContainerVariableInfo m_varInfoContainer;
	
	private HAPContainerValuePorts m_otherInternalValuePortsContainer;
	
	private HAPContainerValuePorts m_otherExternalValuePortsContainer;
	
	public HAPManualBrick() {
		this.m_valueContext = new HAPManualValueContext(); 
		this.m_otherInternalValuePortsContainer = new HAPContainerValuePorts();
		this.m_otherExternalValuePortsContainer = new HAPContainerValuePorts();
	}
	
	public void init() {
		this.m_varInfoContainer = new HAPContainerVariableInfo(this, this.m_bundle.getValueStructureDomain()); 
	}

	public void setBundle(HAPBundle bundle) {    this.m_bundle = bundle;      }
	
	public HAPManualValueContext getManualValueContext() {    return this.m_valueContext;    }
	
	public void setRuntimeEnvironment(HAPRuntimeEnvironment runtimeEnv) {     this.m_runtimeEnv = runtimeEnv;      }
	
	public HAPContainerVariableInfo getVariableInfoContainer() {    return this.m_varInfoContainer;      }
	public void setVariableInfoContainer(HAPContainerVariableInfo varInfoContainer) {     this.m_varInfoContainer = varInfoContainer;      }
	
	protected HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	public void setManualBrickManager(HAPManualManagerBrick manualBrickMan) {    this.m_manualBrickMan = manualBrickMan;       }
	
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   this.m_tempTreeNodeInfo = treeNodeInfo;     }

	public HAPInfoBrickType getBrickTypeInfo() {    return this.m_brickTypeInfo;     }
	public void setBrickTypeInfo(HAPInfoBrickType brickTypeInfo) {    this.m_brickTypeInfo = brickTypeInfo;     }
	
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
		List<HAPManualPartInValueContext> out = new ArrayList<HAPManualPartInValueContext>();
		for(HAPManualPartInValueContext part : this.getManualValueContext().getParts()) {
			out.add(HAPManualUtilityValueContextProcessor.inheritFromParent(part, HAPManualUtilityValueContext.getInheritableCategaries()));
		}
		return out;
	}

	public HAPContainerValuePorts getOtherInternalValuePortContainer() {   return this.m_otherInternalValuePortsContainer;    }
	public HAPContainerValuePorts getOtherExternalValuePortContainer() {   return this.m_otherExternalValuePortsContainer;    }
	
	public HAPGroupValuePorts addOtherInternalValuePortGroup(HAPGroupValuePorts valuePortGroup) {     return this.getInternalValuePorts().addValuePortGroup(valuePortGroup);      }
	public HAPGroupValuePorts addOtherExternalValuePortGroup(HAPGroupValuePorts valuePortGroup) {     return this.getExternalValuePorts().addValuePortGroup(valuePortGroup);      }
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valueContextValuePortGroup = this.getValueContextValuePortGroup();
		if(valueContextValuePortGroup!=null) {
			out.addValuePortGroup(valueContextValuePortGroup);
		}
		
		for(HAPGroupValuePorts group : this.getOtherInternalValuePortContainer().getValuePortGroups()) {
			out.addValuePortGroup(group);
		}
		return out;
	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts(){
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		
		HAPGroupValuePorts valueContextValuePortGroup = this.getValueContextValuePortGroup();
		if(valueContextValuePortGroup!=null) {
			out.addValuePortGroup(valueContextValuePortGroup);
		}
		
		for(HAPGroupValuePorts group : this.getOtherExternalValuePortContainer().getValuePortGroups()) {
			out.addValuePortGroup(group);
		}
		return out;
	}
	
	private HAPGroupValuePorts getValueContextValuePortGroup() {
		HAPGroupValuePorts out = null; 
		if(!this.getManualValueContext().isEmpty(this.m_bundle.getValueStructureDomain())) {
			out = new HAPGroupValuePorts(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
			out.setName(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT);
			
			HAPValuePort valuePort = new HAPValuePort(HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.IO_DIRECTION_BOTH);
			valuePort.getValueStructureIds().addAll(this.getManualValueContext().getValueStructureIds());

			out.addValuePort(valuePort, true);
		}
		return out;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;     }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.getRuntimeEnvironment().getBrickManager();     }
	protected HAPManagerResource getResourceManager() {    return this.getRuntimeEnvironment().getResourceManager();     }
	
	abstract public boolean buildBrick(Object value, HAPSerializationFormat format, HAPManagerApplicationBrick brickMan);

}
