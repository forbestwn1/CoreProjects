package com.nosliw.servlet.utils.browseresource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.servlet.HAPBaseServlet;

public class HAPBrowseResourceServlet extends HAPBaseServlet{

	private String[] resourceTypes = {
			HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE,
			HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE,
			HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY,

			
	}
	
	public static final String RUNTIME_RESOURCE_TYPE_UITAG = "uiTag";
	public static final String RUNTIME_RESOURCE_TYPE_UIMODULE = "uiModule";

	
	@Override
	protected void doGet (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		
	
	}

}
