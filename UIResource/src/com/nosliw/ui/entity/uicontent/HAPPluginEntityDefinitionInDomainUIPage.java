package com.nosliw.ui.entity.uicontent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainUIPage extends HAPPluginEntityDefinitionInDomainWithUIContent{

	public HAPPluginEntityDefinitionInDomainUIPage(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, HAPDefinitionEntityComplexUIPage.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		String content = (String)obj;
		try{
			Document doc = Jsoup.parse(content, "UTF-8");
			parseUIContent(doc.body(), entityId, parserContext);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
