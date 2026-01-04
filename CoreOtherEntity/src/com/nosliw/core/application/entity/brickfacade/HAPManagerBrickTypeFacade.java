package com.nosliw.core.application.entity.brickfacade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HAPManagerBrickTypeFacade {

	private static final Map<String, HAPFacadeBrickType> m_facades = new LinkedHashMap<String, HAPFacadeBrickType>();
	
	public static final String FACADENAME_INTERACTIVE = "interactive";
	
	public static final String FACADENAME_TASK = "task";
	public static final String FACADENAME_EXPRESSION = "expression";
	
	private HAPManagerBrickTypeFacade() {}
	
	static {
		registerFacade(new HAPFacadeBrickTypeSimple(FACADENAME_INTERACTIVE, ""));
		registerFacade(buildComplexFacade(FACADENAME_TASK, List.of(FACADENAME_INTERACTIVE)));
		registerFacade(buildComplexFacade(FACADENAME_EXPRESSION, List.of(FACADENAME_INTERACTIVE)));
	}
	
	public static HAPFacadeBrickType getFacadeByName(String name) {
		return m_facades.get(name);
	}
	
	private static void registerFacade(HAPFacadeBrickType facade) {
		m_facades.put(facade.getName(), facade);
	}
	
	private static HAPFacadeBrickTypeComplex buildComplexFacade(String name, List<String> childFacadeNames) {
		List<HAPFacadeBrickType> childFacades = new ArrayList<HAPFacadeBrickType>();
		for(String facadeName : childFacadeNames) {
			childFacades.add(getFacadeByName(facadeName));
		}
		return new HAPFacadeBrickTypeComplex(name, childFacades);
	}
}
