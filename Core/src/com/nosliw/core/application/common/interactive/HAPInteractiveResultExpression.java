package com.nosliw.core.application.common.interactive;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort11111;

@HAPEntityWithAttribute
public class HAPInteractiveResultExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String RESULT = "result";
	
	private HAPResultElementInInteractiveExpression m_result;
	
//	private HAPValuePort1111 m_internalValuePort;
//	private HAPValuePort1111 m_externalValuePort;
	
	public HAPInteractiveResultExpression(HAPResultElementInInteractiveExpression result) {
		this.m_result = result;
//		this.m_internalValuePort = new HAPValuePortInteractiveExpressionResult(this.m_result, HAPConstantShared.IO_DIRECTION_IN);
//		this.m_externalValuePort = new HAPValuePortInteractiveExpressionResult(this.m_result, HAPConstantShared.IO_DIRECTION_OUT);
	}
	
	public HAPResultElementInInteractiveExpression getResult() {   return this.m_result;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(RESULT, HAPManagerSerialize.getInstance().toStringValue(this.getResult(), HAPSerializationFormat.JSON));
	}
}


class HAPValuePortInteractiveExpressionResult extends HAPValuePortImp{

	private HAPResultElementInInteractiveExpression m_expressionResult;
	
	public HAPValuePortInteractiveExpressionResult(HAPResultElementInInteractiveExpression expressionResult, String ioDirection) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, ioDirection));
		this.m_expressionResult = expressionResult;
	}

	@Override
	public HAPValueStructureInValuePort11111 getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort11111 out = new HAPValueStructureInValuePort11111();
		
		HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(this.m_expressionResult.getDataCriteria()));
		root.setName(HAPConstantShared.NAME_ROOT_RESULT);
		out.addRoot(root);
		
		return out;
	}

	@Override
	protected List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria,
			HAPConfigureResolveElementReference configure) {
		return Lists.asList(HAPConstantShared.NAME_DEFAULT, new String[0]);
	}

	@Override
	public void updateElement(HAPIdElement elementId, HAPElementStructure structureElement) {
		this.m_expressionResult.setDataCriteria(((HAPElementStructureLeafData)structureElement).getCriteria());
	}

	@Override
	protected HAPResultReferenceResolve extendValueStructure(String valueStructureInValuePort, String elementPath,
			HAPElementStructure structureEle, HAPConfigureResolveElementReference configure) {
		// TODO Auto-generated method stub
		return null;
	}
}


