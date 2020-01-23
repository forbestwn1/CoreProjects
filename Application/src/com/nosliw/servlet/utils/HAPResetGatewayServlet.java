package com.nosliw.servlet.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.servlet.HAPBaseServlet;
import com.nosliw.servlet.core.HAPGatewayServlet;

public class HAPResetGatewayServlet extends HAPBaseServlet{

	private static final long serialVersionUID = -1256294161988262894L;

	@Override
	public void doGet (HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		
		HAPGatewayServlet.index = 0;
		HAPServiceData serviceData = HAPServiceData.createSuccessData();
		this.printContent(serviceData, response);
	}
}
