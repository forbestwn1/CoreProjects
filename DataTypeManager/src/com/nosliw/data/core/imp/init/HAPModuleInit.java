package com.nosliw.data.core.imp.init;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;

public class HAPModuleInit {

	public static void init(){

		//init db connection
		HAPDBAccess.getInstance();
		
		//import all the value infos
		
		
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderPath(HAPResourceDiscoveryJSImp.class), false);
		HAPValueInfoManager.getInstance().importFromFolder(HAPFileUtility.getClassFolderPath(HAPDataTypeManagerImp.class), false);

		new HAPRuntimeImpJSRhino();
		
	}
	
}
