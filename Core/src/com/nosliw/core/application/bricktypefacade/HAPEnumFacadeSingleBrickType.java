package com.nosliw.core.application.bricktypefacade;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPEnumFacadeSingleBrickType {

	private static final Map<String, HAPFacadeBrickTypeSingle> m_facades = new LinkedHashMap<String, HAPFacadeBrickTypeSingle>();
	
	public static final String FACADENAME_TASK = "task";
	
	private HAPEnumFacadeSingleBrickType() {}
	
	static {
		registerFacade(new HAPFacadeBrickTypeSingle(FACADENAME_TASK, ""));
	}
	
	public static HAPFacadeBrickTypeSingle getFacadeByName(String name) {
		return m_facades.get(name);
	}
	
	private static void registerFacade(HAPFacadeBrickTypeSingle facade) {
		m_facades.put(facade.getName(), facade);
	}
}
