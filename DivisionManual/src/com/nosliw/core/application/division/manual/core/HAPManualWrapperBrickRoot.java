package com.nosliw.core.application.division.manual.core;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;

public class HAPManualWrapperBrickRoot extends HAPWrapperBrickRoot implements HAPTreeNodeBrick{

	private HAPManualDefinitionWrapperBrickRoot m_definition;
	
	public HAPManualWrapperBrickRoot(HAPManualBrick brick) {
		super(brick);
	}

	public void setDefinition(HAPManualDefinitionWrapperBrickRoot definition) {    this.m_definition = definition;     }
	public HAPManualDefinitionWrapperBrickRoot getDefinition() {     return this.m_definition;     }
	
	@Override
	public HAPInfoTreeNode getTreeNodeInfo() {    return new HAPInfoTreeNode(new HAPPath(HAPUtilityBundle.buildBranchPathSegment(this.getName())), null);  }

	@Override
	public Object getNodeValue() {   return this.getBrick();   }

}
