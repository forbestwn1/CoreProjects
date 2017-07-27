package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSConverter;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSHelper;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSLibrary;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSOperation;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeImpJSBroswerImp;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

public class HAPInitServlet  extends HttpServlet{

	   public void init() throws ServletException
	   {
//			HAPApplicationInstance instance = HAPApplicationInstance.getApplicationInstantce();
//			this.getServletContext().setAttribute("appInstantce", instance);
		   
		   
			HAPResourceManagerJS resourceMan = new HAPResourceManagerJS();
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, new HAPResourceManagerJSOperation());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
			resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper());
			
//			HAPDataTypeManager dataTypeMan = new HAPDataTypeManagerImp();
		   
			HAPRuntimeImpJSBroswer runtime = new HAPRuntimeImpJSBroswerImp(new HAPResourceDiscoveryJSImp(), resourceMan);
			runtime.start();
			this.getServletContext().setAttribute("runtime", runtime);
		   
		   
	   }
}
