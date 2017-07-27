package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

public class HAPRuntimeImpJSBroswerImp extends HAPRuntimeImpJSBroswer{

	public HAPRuntimeImpJSBroswerImp(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan) {
		super(resourceDiscovery, resourceMan);
	}

	@Override
	public void start() {
		//init db connection
		HAPDBAccess.getInstance();
		
		//import all the value infos
		
		
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderPath(HAPResourceDiscoveryJSImp.class), false);
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderPath(HAPDataTypeManagerImp.class), false);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
