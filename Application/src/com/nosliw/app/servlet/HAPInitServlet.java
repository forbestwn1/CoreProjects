package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.application.runtimerhino.HAPRuntimeRhinoMain;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.expression.HAPExpressionManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPRuntimeEnvironmentImpJS;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public void init() throws ServletException
	   {
			//module init
			HAPModuleRuntimeJS runtimeJSModule = new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance());;

			//create runtime
			HAPRuntimeEnvironmentImpJS runtimeEnvironment = new HAPRuntimeEnvironmentImpJS(runtimeJSModule);

			HAPExpressionManagerImp expressionMan = (HAPExpressionManagerImp)runtimeEnvironment.getExpressionManager();
			expressionMan.importFromClassFolder(HAPRuntimeRhinoMain.class);

			//set runtime object to context
			this.getServletContext().setAttribute("runtime", runtimeEnvironment);
	   }
}
