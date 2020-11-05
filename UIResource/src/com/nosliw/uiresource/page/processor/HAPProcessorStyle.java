package com.nosliw.uiresource.page.processor;

import com.nosliw.uiresource.page.definition.HAPDefinitionStyle;
import com.nosliw.uiresource.page.execute.HAPExecutableStyle;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnit;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitTag;

public class HAPProcessorStyle {

	public static void process(HAPExecutableUIUnitPage pageUnit) {
		HAPExecutableStyle style = createStyle(pageUnit);
		processChild(pageUnit, style);
		pageUnit.setStyle(style);
	}
	
	private static void processChild(HAPExecutableUIUnit uiUnit, HAPExecutableStyle parentStyle) {
		for(HAPExecutableUIUnitTag tag: uiUnit.getBody().getUITags()) {
			HAPExecutableStyle style = createStyle(tag);
			parentStyle.addChild(style);
		}
	}
	
	private static HAPExecutableStyle createStyle(HAPExecutableUIUnit uiUnit) {
		HAPExecutableStyle style = null;
		HAPDefinitionStyle styleDef = uiUnit.getUIUnitDefinition().getStyle();
		style = new HAPExecutableStyle(uiUnit.getId());
		if(styleDef!=null) {
			style.setDefinition(styleDef.getDefinition());
		}
		return style;
	}
}
