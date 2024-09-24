package com.nosliw.core.application.division.manual.executable;

import java.util.List;

import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualPartInValueContext;

public class HAPManualBrickImpSimple extends HAPManualBrickImp{

	@Override
	public List<HAPManualPartInValueContext> getValueContextInhertanceDownstream(){
		HAPManualBrick parent = this.getTreeNodeInfo().getParent();
		if(parent!=null) {
			return parent.getValueContextInhertanceDownstream();
		}
		return null;
	}
}
