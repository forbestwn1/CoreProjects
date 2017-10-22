package com.nosliw.data.core.runtime.js.gateway;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaWithSubCriteria;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

public class HAPGatewayCriteriaOperation extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA = "getChildCriteria";
	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA_CRITERIA = "criteria";
	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA_CHILDNAME = "childName";
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETCHILDCRITERIA:
			String childName = parms.getString(COMMAND_GETCHILDCRITERIA_CHILDNAME);
			String criteriaStr = parms.getString(COMMAND_GETCHILDCRITERIA_CRITERIA);
			HAPDataTypeCriteria criteria = HAPCriteriaParser.getInstance().parseCriteria(criteriaStr);
			HAPDataTypeCriteria childCriteria = null;
			if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
				childCriteria = ((HAPDataTypeCriteriaWithSubCriteria)criteria).getSubCriteria().getSubCriteria(childName);
			}
			out = this.createSuccessWithObject(childCriteria.toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}
		
		return out;
	}

}
