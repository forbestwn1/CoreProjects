package com.nosliw.data.core.domain.entity.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPPluginAdapterProcessor;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPPluginAdapterProcessorDataAssociation implements HAPPluginAdapterProcessor{

	@Override
	public String getAdapterType() {  return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION;  }

	@Override
	public Object process(Object adapter, HAPExecutableImp parentEntityExecutable, HAPExecutableImp entityExecutable, HAPContextProcessor processContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
