package com.nosliw.core.application.common.scriptexpressio;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPSegmentScriptExpressionScriptDataExpression extends HAPSegmentScriptExpressionScript{

	@HAPAttribute
	public static String DATAEXPRESSIONID = "dataExpressionId";

	private String m_dataExpressionId;
	
	public HAPSegmentScriptExpressionScriptDataExpression(String id, String dataExpressionId) {
		super(id);
		this.m_dataExpressionId = dataExpressionId;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION;  }

	public String getDataExpressionId() {    return this.m_dataExpressionId;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATAEXPRESSIONID, this.getDataExpressionId());
	}

}
