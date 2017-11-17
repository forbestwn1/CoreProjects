package com.nosliw.uiresource.tag;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.interpolate.HAPStringTemplate;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPUITagManager1 {

	private Map<String, HAPUITagDefinition> m_uiTags;
	
	private HAPConfigure m_configure;
	
	private HAPStringTemplate m_strTemplate;
	
	public HAPUITagManager1(HAPConfigure configure){
		this.m_configure = configure;
		this.m_uiTags = new LinkedHashMap<String, HAPUITagDefinition>();

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUITagManager1.class, "UITagScript.txt");
		m_strTemplate = new HAPStringTemplate(templateStream);
	}
	
	public void registerUITag(HAPUITagDefinition tagInfo){
//		this.m_uiTags.put(tagInfo.getName(), tagInfo);
	}
	
	public HAPUITagDefinition getUITag(String name){
		return this.m_uiTags.get(name);
	}

	protected String getTagScript(File tagScriptFile){
		String script = HAPFileUtility.readFile(tagScriptFile);
		String name = HAPFileUtility.getFileName(tagScriptFile);
		
		Map<String, String> parms = new LinkedHashMap<String, String>();
		parms.put("name", name);
		parms.put("tagFactoryFunScript", script);
		String tagScript = m_strTemplate.getContent(parms);
		return tagScript;
	}
	
	protected HAPConfigure getConfigure(){
		return this.m_configure;
	}
}
