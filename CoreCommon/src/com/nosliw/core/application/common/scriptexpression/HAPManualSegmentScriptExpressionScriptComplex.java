package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualSegmentScriptExpressionScriptComplex extends HAPManualSegmentScriptExpressionScript{

	@HAPAttribute
	public static String SEGMENT = "segment";
	
	private List<HAPManualSegmentScriptExpressionScript> m_children;
	
	public HAPManualSegmentScriptExpressionScriptComplex(String id) {
		super(id);
		this.m_children = new ArrayList<HAPManualSegmentScriptExpressionScript>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX;  }
	 
	@Override
	public List<HAPManualSegmentScriptExpression> getChildren(){     return (List)this.m_children;      }
	
	public void addSegmentScriptSimple(HAPManualSegmentScriptExpressionScriptSimple scriptSegment) {	this.m_children.add(scriptSegment);	}
	
	public void addSegmentDataExpression(HAPManualSegmentScriptExpressionScriptDataExpression dataSegment) {	this.m_children.add(dataSegment);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> segmentJsonArray = new ArrayList<String>();
		for(HAPManualSegmentScriptExpression segment : this.m_children) {
			segmentJsonArray.add(segment.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentJsonArray.toArray(new String[0])));
	}

}
