package com.nosliw.core.application.common.dataexpression;

import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.data.HAPDataTypeConverter;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute(baseName="OPERAND")
public interface HAPOperand extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type"; 

	@HAPAttribute
	public static final String STATUS = "status"; 
	
	@HAPAttribute
	public static final String CHILDREN = "children"; 

	@HAPAttribute
	public static final String DATATYPEINFO = "dataTypeInfo"; 

	@HAPAttribute
	public static final String CONVERTERS = "converters"; 

	@HAPAttribute
	public static final String OUTPUTCRITERIA = "outputCriteria"; 

	//type of operand
	String getType();

	//children operand
	List<HAPWrapperOperand> getChildren();

	/**
	 * Try best to process operand in order to discovery
	 * 		Variables:	narrowest variable criteria
	 * 		Converter for the gap between output criteria and expect criteria
	 * 		Output criteria
	 * @param variablesInfo  all the variables info in context
	 * @param expectCriteria expected output criteria for this operand
	 * @return  matchers from output criteria to expect criteria
	 */
	HAPMatchers discover(HAPContainerVariableCriteriaInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria,
			HAPProcessTracker processTracker, 
			HAPDataTypeHelper dataTypeHelper);


	//operand output data type criteria
	HAPDataTypeCriteria getOutputCriteria();
	
	//status of operand: new, processed, failed
	String getStatus();
	
	List<HAPResourceIdSimple> getResources(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager);
	
	//get all the convertor required by this operand
	Set<HAPDataTypeConverter> getConverters();  
	
	HAPOperand cloneOperand();
}
