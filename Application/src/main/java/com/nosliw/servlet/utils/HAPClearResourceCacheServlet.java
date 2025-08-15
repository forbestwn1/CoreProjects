package com.nosliw.servlet.utils;

import java.io.IOException;

import com.nosliw.servlet.HAPBaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HAPClearResourceCacheServlet extends HAPBaseServlet {

	@Override
	protected void doGet (HttpServletRequest request,	HttpServletResponse response) throws ServletException, IOException {
		this.getResourceManager().clearCache();
	}
}
