package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIPage;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
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

			HAPManualDefinitionBrick uiContentDef = this.getManualDivisionEntityManager().parseBrickDefinition(doc.body(), HAPEnumBrickType.UICONTENT_100, HAPSerializationFormat.JAVASCRIPT, parseContext);
			uiPage.setAttributeWithValueBrick(HAPBlockComplexUIPage.UICONTENT, uiContentDef);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
