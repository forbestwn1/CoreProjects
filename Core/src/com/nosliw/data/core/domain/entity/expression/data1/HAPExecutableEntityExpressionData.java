package com.nosliw.data.core.domain.entity.expression.data1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.container.HAPExecutableEntityContainerComplex;

@HAPEntityWithAttribute
public abstract class HAPExecutableEntityExpressionData extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static final String ATTRIBUTESREFERENCE = "referenceAttribute";

	@HAPAttribute
	public static final String REFERENCES = "references";

	public abstract List<HAPExecutableExpressionData1> getAllExpressionItems();

	public HAPExecutableEntityExpressionData() {
		this.setAttributeValueObject(ATTRIBUTESREFERENCE, new HashSet<String>());
		this.setAttributeValueObject(VARIABLEINFOS, new HAPContainerVariableInfo());
	}

	public void setVariablesInfo(HAPContainerVariableInfo varInfo) {  this.setAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableInfo getVariablesInfo() {   return (HAPContainerVariableInfo)this.getAttributeValue(VARIABLEINFOS);    }
	
	public HAPExecutableEntityContainerComplex getReferences() {   return(HAPExecutableEntityContainerComplex)this.getComplexEntityAttributeValue(REFERENCES);    }
	
	public Set<String> getReferenceAttributes(){    return (Set<String>)this.getAttributeValue(ATTRIBUTESREFERENCE);     }
	
}
