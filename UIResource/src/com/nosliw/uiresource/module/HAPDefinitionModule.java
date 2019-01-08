package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.script.context.HAPContext;
/**
Module is a independent entity that is runnable within a container
It is a simple application which contains pages and transition between pages
Also be aware that module can be run in different container (pc browser, mobile browser, ...), try to make it platform independent
for instance, for a module that shows a school information, it contains two pages: list of school, school info
    when in mobile phone, the school info should overlap on top of list page
    when in pc, the list page and the school page should diplay sid by side

Don't need to define service information here, as service information will be gathered from all the mdoule ui definition
 */
@HAPEntityWithAttribute
public class HAPDefinitionModule extends HAPEntityInfoImp{

	@HAPAttribute
	public static String PAGE = "page";

	// all the page required by module (name -- page id)
	private Map<String, HAPDefinitionPage> m_pages;
	
	// what to do when module just start
	private HAPDefinitionProcess m_initProcess;

	// what to do when module need to destroy
	private HAPDefinitionProcess m_clearupProcess;

	// all the module uis (name -- definition)
	private Map<String, HAPDefinitionModuleUI> m_uis;

	// data definition
	// hook up with real data during runtime
	private HAPContext m_context;
}
