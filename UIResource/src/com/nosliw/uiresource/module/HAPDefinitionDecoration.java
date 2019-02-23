package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPDefinitionDecoration {

	@HAPAttribute
	public static String GLOBAL = "global";

	@HAPAttribute
	public static String UI = "ui";

	private List<String> m_global;
	
	private Map<String, List<String>> m_byUI;
	
	public HAPDefinitionDecoration() {
		this.m_global = new ArrayList<String>();
		this.m_byUI = new LinkedHashMap<String, List<String>>();
	}
	
	
}
