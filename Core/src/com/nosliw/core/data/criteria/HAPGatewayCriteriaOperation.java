package com.nosliw.core.data.criteria;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaWithSubCriteria;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPGatewayCriteriaOperation extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA = "getChildCriteria";
	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA_CRITERIA = "criteria";
	@HAPAttribute
	final public static String COMMAND_GETCHILDCRITERIA_CHILDNAME = "childName";

	@HAPAttribute
	final public static String COMMAND_GETCHILDRENCRITERIA = "getChildrenCriteria";
	@HAPAttribute
	final public static String COMMAND_GETCHILDRENCRITERIA_CRITERIA = "criteria";
	
	@HAPAttribute
	final public static String COMMAND_ADDCHILDCRITERIA = "addChildCriteria";
	@HAPAttribute
	final public static String COMMAND_ADDCHILDCRITERIA_CRITERIA = "criteria";
	@HAPAttribute
	final public static String COMMAND_ADDCHILDCRITERIA_CHILDNAME = "childName";
	@HAPAttribute
	final public static String COMMAND_ADDCHILDCRITERIA_CHILD = "child";
	
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETCHILDCRITERIA:
		{
			String childName = parms.getString(COMMAND_GETCHILDCRITERIA_CHILDNAME);
			String criteriaStr = parms.getString(COMMAND_GETCHILDCRITERIA_CRITERIA);
			HAPDataTypeCriteria criteria = HAPParserCriteria.getInstance().parseCriteria(criteriaStr);
			HAPDataTypeCriteria childCriteria = null;
			if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
				childCriteria = HAPUtilityCriteria.getChildCriteria(criteria, childName); 
				out = this.createSuccessWithObject(childCriteria.toStringValue(HAPSerializationFormat.LITERATE));
			}
			break;
		}
		case COMMAND_ADDCHILDCRITERIA:
		{
			String criteriaStr = parms.getString(COMMAND_ADDCHILDCRITERIA_CRITERIA);
			String childName = parms.getString(COMMAND_ADDCHILDCRITERIA_CHILDNAME);
			String childCriteriaStr = parms.getString(COMMAND_ADDCHILDCRITERIA_CHILD);
			HAPDataTypeCriteria criteria = HAPParserCriteria.getInstance().parseCriteria(criteriaStr);
			HAPDataTypeCriteria childCriteria = HAPParserCriteria.getInstance().parseCriteria(childCriteriaStr);
			if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
				((HAPDataTypeCriteriaWithSubCriteria)criteria).getSubCriteria().addSubCriteria(childName, childCriteria);
				out = this.createSuccessWithObject(criteria.toStringValue(HAPSerializationFormat.LITERATE));
			}
			break;
		}
		case COMMAND_GETCHILDRENCRITERIA:{
			String criteriaStr = parms.getString(COMMAND_GETCHILDRENCRITERIA_CRITERIA);
			HAPDataTypeCriteria criteria = HAPParserCriteria.getInstance().parseCriteria(criteriaStr);
			if(criteria instanceof HAPDataTypeCriteriaWithSubCriteria){
				Map<String, String> subCriterias = new LinkedHashMap<String, String>();
				HAPDataTypeCriteriaWithSubCriteria criteriaWithSub = (HAPDataTypeCriteriaWithSubCriteria)criteria;
				for(String subName : criteriaWithSub.getSubCriteria().getSubCriteriaNames()){
					subCriterias.put(subName, criteriaWithSub.getSubCriteria().getSubCriteria(subName).toStringValue(HAPSerializationFormat.LITERATE));
				}
				out = this.createSuccessWithObject(subCriterias);
			}
			break;
		}
		}
		
		return out;
	}
}
