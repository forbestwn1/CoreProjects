package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPSegmentScriptExpressionDataScript extends HAPSegmentScriptExpression{

	@HAPAttribute
	public static String SEGMENT = "segment";
	
	private List<HAPSegmentScriptExpression> m_segments;
	
	public HAPSegmentScriptExpressionDataScript(String id) {
		super(id);
		this.m_segments = new ArrayList<HAPSegmentScriptExpression>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT;  }
	
	public List<HAPSegmentScriptExpression> getSegments(){     return this.m_segments;      }
	
	public void addSegmentScript(HAPSegmentScriptExpressionScript scriptSegment) {	this.m_segments.add(scriptSegment);	}
	
	public void addSegmentData(HAPSegmentScriptExpressionData dataSegment) {	this.m_segments.add(dataSegment);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> segmentJsonArray = new ArrayList<String>();
		for(HAPSegmentScriptExpression segment : this.m_segments) {
			segmentJsonArray.add(segment.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentJsonArray.toArray(new String[0])));
	}

}
