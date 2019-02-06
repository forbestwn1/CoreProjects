package com.nosliw.data.core.service.use;

import java.util.Map;

import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;

public class HAPUtilityServiceUse {

	public static HAPContext buildContextFromServiceParms(HAPServiceInterface serviceInterface) {
		return buildContextFromServiceParms(serviceInterface.getParms());
	}
	
	public static HAPContext buildContextFromServiceParms(Map<String, HAPServiceParm> parms) {
		HAPContext out = new HAPContext();
		for(String parm : parms.keySet()) {
			out.addElement(parm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(parms.get(parm).getCriteria())));
		}
		return out;
	}

	public static HAPContext buildContextFromResultServiceOutputs(HAPServiceInterface serviceInterface, String result) {
		return buildContextFromServiceOutputs(serviceInterface.getResultOutput(result));
	}
	
	public static HAPContext buildContextFromServiceOutputs(Map<String, HAPServiceOutput> serviceOutput) {
		HAPContext out = new HAPContext();
		for(String outParm : serviceOutput.keySet()) {
			out.addElement(outParm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(serviceOutput.get(outParm).getCriteria())));
		}
		return out;
	}
}
