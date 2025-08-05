package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockComplexUIPage extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockComplexUIPage(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.UIPAGE_100, HAPManualDefinitionBlockComplexUIPage.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentHtml(HAPManualDefinitionBrick brickManualDef, Object obj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUIPage uiPage = (HAPManualDefinitionBlockComplexUIPage)brickManualDef;
		
		String content = (String)obj;
		try{ 
			Document doc = Jsoup.parse(content, "UTF-8");

			HAPManualDefinitionBlockComplexUIContent uiContentDef = (HAPManualDefinitionBlockComplexUIContent)this.getManualDivisionBrickManager().parseBrickDefinition(doc.body(), HAPEnumBrickType.UICONTENT_100, HAPSerializationFormat.HTML, parseContext);
			uiPage.setUIContent(uiContentDef);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
