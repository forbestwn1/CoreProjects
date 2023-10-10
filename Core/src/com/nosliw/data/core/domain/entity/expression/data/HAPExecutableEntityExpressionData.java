package com.nosliw.data.core.domain.entity.expression.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.container.HAPExecutableEntityContainerComplex;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityExpressionData extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static final String ATTRIBUTESREFERENCE = "referenceAttribute";

	@HAPAttribute
	public static final String REFERENCES = "references";

	public abstract List<HAPExecutableExpressionData> getAllExpressionItems();

	public HAPExecutableEntityExpressionData() {
		this.setAttributeValueObject(ATTRIBUTESREFERENCE, new HashSet<String>());
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableCriteriaInfo());
	}

	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getAttributeValue(VARIABLEINFOS);    }
	
	public HAPExecutableEntityContainerComplex getReferences(HAPContextProcessor processContext) {   return(HAPExecutableEntityContainerComplex) processContext.getCurrentExecutableDomain().getEntityInfoExecutable(this.getComplexEntityAttributeValue(REFERENCES)).getEntity();    }
	
	public Set<String> getReferenceAttributes(){    return (Set<String>)this.getAttributeValue(ATTRIBUTESREFERENCE);     }
	
}
