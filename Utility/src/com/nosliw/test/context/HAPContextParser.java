package com.nosliw.test.context;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPContextParser {

	public static void main(String[] args) {
		
		
		String contextContent = HAPFileUtility.readFile(HAPContextParser.class.getResourceAsStream("context.json"));
		HAPContextGroup contextGroup = HAPParserContext.parseContextGroup(new JSONObject(contextContent));
		System.out.println(contextGroup);
	}

}
