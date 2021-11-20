package com.nosliw.uiresource.page.processor;

import com.nosliw.uiresource.page.definition.HAPDefinitionStyle;
import com.nosliw.uiresource.page.execute.HAPExecutableStyle;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit1;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUITag;

public class HAPProcessorStyle {

	public static void process(HAPExecutableUIUnitPage pageUnit) {
		HAPExecutableStyle style = createStyle(pageUnit);
		processChild(pageUnit, style);
		pageUnit.setStyle(style);
	}
	
	private static void processChild(HAPExecutableUIUnit1 uiUnit, HAPExecutableStyle parentStyle) {
		for(HAPExecutableUITag tag: uiUnit.getBody().getUITags()) {
			HAPExecutableStyle style = createStyle(tag);
			parentStyle.addChild(style);
		}
	}
	
	private static HAPExecutableStyle createStyle(HAPExecutableUIUnit1 uiUnit) {
		HAPExecutableStyle style = null;
		HAPDefinitionStyle styleDef = uiUnit.getUIUnitDefinition().getStyle();
		style = new HAPExecutableStyle(uiUnit.getId());
		if(styleDef!=null) {
			style.setDefinition(styleDef.getDefinition());
		}
		return style;
	}
}
