package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.division.manual.executable.HAPInfoTreeNode;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPTreeNodeBrick;

public class HAPManualWrapperBrickRoot extends HAPWrapperBrickRoot implements HAPTreeNodeBrick{

	public HAPManualWrapperBrickRoot(HAPManualBrick brick) {
		super(brick);
	}

	@Override
	public HAPInfoTreeNode getTreeNodeInfo() {    return new HAPInfoTreeNode(new HAPPath(), null);  }

	@Override
	public Object getNodeValue() {   return this.getBrick();   }

}
