package com.nosliw.core.application.common.scriptexpressio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPSegmentScriptExpressionScriptComplex extends HAPSegmentScriptExpressionScript{

	@HAPAttribute
	public static String SEGMENT = "segment";
	
	private List<HAPSegmentScriptExpressionScript> m_children;
	
	public HAPSegmentScriptExpressionScriptComplex(String id) {
		super(id);
		this.m_children = new ArrayList<HAPSegmentScriptExpressionScript>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX;  }
	 
	@Override
	public List<HAPSegmentScriptExpression> getChildren(){     return (List)this.m_children;      }
	
	public void addSegmentScriptSimple(HAPSegmentScriptExpressionScriptSimple scriptSegment) {	this.m_children.add(scriptSegment);	}
	
	public void addSegmentDataExpression(HAPSegmentScriptExpressionScriptDataExpression dataSegment) {	this.m_children.add(dataSegment);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> segmentJsonArray = new ArrayList<String>();
		for(HAPSegmentScriptExpression segment : this.m_children) {
			segmentJsonArray.add(segment.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentJsonArray.toArray(new String[0])));
	}

}
