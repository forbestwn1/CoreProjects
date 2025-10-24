package com.nosliw.core.application.division.manual.core;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.HAPWrapperValueOfBrick;
import com.nosliw.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public class HAPManualAttributeInBrick extends HAPAttributeInBrick implements HAPTreeNodeBrick, HAPWithResourceDependency{

	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private HAPInfoTreeNode m_tempTreeNodeInfo;
	
	public HAPManualAttributeInBrick() {
	}
	
	public HAPManualAttributeInBrick(String attrName, HAPWrapperValue valueWrapper) {
		super(attrName, valueWrapper);
	}
	
	@Override
	public void setValueWrapper(HAPWrapperValue valueInfo) {
		super.setValueWrapper(valueInfo);
		this.synTreeNodeInfoInBrick();
	}

	public List<HAPManualAdapter> getManualAdapters(){    return (List)this.getAdapters();    }
	
	public void setValueOfBrick(HAPManualBrick brick) {		this.setValueWrapper(new HAPWrapperValueOfBrick(brick));	}

	@Override
	public HAPInfoTreeNode getTreeNodeInfo() {  return this.m_tempTreeNodeInfo;  }
	public void setTreeNodeInfo(HAPInfoTreeNode treeNodeInfo) {   
		this.m_tempTreeNodeInfo = treeNodeInfo;
		this.synTreeNodeInfoInBrick();
	}

	@Override
	public Object getNodeValue() {  return this.getValueWrapper();   }

	private void synTreeNodeInfoInBrick() {
		if(this.m_tempTreeNodeInfo!=null&&this.getValueWrapper()!=null&&this.getValueWrapper().getValueType().equals(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_BRICK)&&this.getValueWrapper().getValue() instanceof HAPManualBrick) {
			((HAPManualBrick)((HAPWrapperValueOfBrick)this.getValueWrapper()).getBrick()).setTreeNodeInfo(m_tempTreeNodeInfo);
		}
	}
}
