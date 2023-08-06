package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecutableSegmentExpressionDataScript extends HAPExecutableSegmentExpression{

	@HAPAttribute
	public static String SEGMENT = "segment";
	
	private List<HAPExecutableSegmentExpression> m_segments;
	
	public HAPExecutableSegmentExpressionDataScript(String id) {
		super(id);
		this.m_segments = new ArrayList<HAPExecutableSegmentExpression>();
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT;  }
	
	public List<HAPExecutableSegmentExpression> getSegments(){     return this.m_segments;      }
	
	public void addSegmentScript(HAPExecutableSegmentExpressionScript scriptSegment) {	this.m_segments.add(scriptSegment);	}
	
	public void addSegmentData(HAPExecutableSegmentExpressionData dataSegment) {	this.m_segments.add(dataSegment);	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		List<String> segmentJsonArray = new ArrayList<String>();
		for(HAPExecutableSegmentExpression segment : this.m_segments) {
			segmentJsonArray.add(segment.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentJsonArray.toArray(new String[0])));
	}

}
