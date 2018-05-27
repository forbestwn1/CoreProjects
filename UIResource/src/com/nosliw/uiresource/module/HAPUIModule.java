package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;

public class HAPUIModule {

	private String m_entryPage;
	
	private Map<String, HAPUIDefinitionUnitResource> m_pages;
	
	public HAPUIModule(String entryPage) {
		this.m_entryPage = entryPage;
	}

	public String getEntryPage() {   return this.m_entryPage;   }
	
	public void addPage(String pageName, HAPUIDefinitionUnitResource page) {
		this.m_pages.put(pageName, page);
	}
}
