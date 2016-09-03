package com.nosliw.entity.options;

import java.util.List;

import com.nosliw.common.exception.HAPServiceDataException;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPWraper;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.expression.HAPAttributeExpression;
import com.nosliw.entity.expression.HAPAttributeExpressionUtility;
import com.nosliw.entity.sort.HAPSortingInfo;
import com.nosliw.entity.sort.HAPSortingUtility;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;
import com.nosliw.expression.utils.HAPExpressionUtility;

/*
 * this is option attribute object that attached to attribute definition
 * this options wraper another options which provide source options data
 * after getting options data from source options, we may can process it by :
 * 		filter out some data according to filter expression
 * 		sorting the data ???? not implemented yet
 */
public class HAPOptionsComplex extends HAPOptionsDefinition{
	//base options that provide source options data
 	private HAPOptionsDefinition m_sourceOptions;
 	//fiter expression used to fiter out data from base option
	private HAPExpression m_filterExpression;
	//sorting configures
	private List<HAPSortingInfo> m_sortingInfos;
	
	public HAPOptionsComplex(HAPOptionsDefinition sourceOptions, HAPAttributeDefinition attrDef, HAPExpressionInfo filterExpression, HAPDataTypeManager dataTypeMan){
		super("", attrDef.getDataTypeDefinitionInfo(), "", dataTypeMan);
		this.m_sourceOptions = sourceOptions;
		if(filterExpression!=null){
			this.m_filterExpression = new HAPAttributeExpression(filterExpression, attrDef, dataTypeMan);
		}
	}
	
	public void addSortingInfo(HAPSortingInfo sortingInfo){this.m_sortingInfos.add(sortingInfo);}
	
	public HAPContainerOptionsWraper getOptions(HAPDataWraper attrDataWraper) {
		List<HAPWraper> optionsResult = null;
		String optionsType = this.m_sourceOptions.getType();
		switch(optionsType){
		case HAPConstant.OPTIONS_TYPE_STATIC:
			optionsResult = ((HAPOptionsDefinitionStatic)this.m_sourceOptions).getOptions();
			break;
		case HAPConstant.OPTIONS_TYPE_DYNAMIC:
//			optionsResult = ((HAPOptionsDefinitionDynamicAttribute)this.m_sourceOptions).getOptions(attrDataWraper);
			break;
		case HAPConstant.OPTIONS_TYPE_QUERY:
//			optionsResult = ((HAPOptionsDefinitionQuery)this.m_sourceOptions).getOptions(attrDataWraper);
			break;
		}
		
		HAPContainerOptionsWraper out = new HAPContainerOptionsWraper(this.getDataTypeManager());
		
		if(this.m_filterExpression==null)  out.getContainerData().addOptionsDatas(optionsResult);
		else{
			//all result go through filter expression
			for(HAPWraper result : optionsResult){
				boolean valid = false;
				try {
					valid = HAPExpressionUtility.executeValidationExpression(this.m_filterExpression,  null, HAPAttributeExpressionUtility.prepareAttributeExpressionWraperParms(attrDataWraper, null));
				} catch (HAPServiceDataException e) {
					e.printStackTrace();
				}
				if(valid)  out.getContainerData().addOptionsData(result);
			}
		}
		
		//sorting
		HAPSortingUtility.sort(out.getContainerData().getDatas(), this.m_sortingInfos);
		
		return out;
	}

	@Override
	public String toStringValue(String format) {
		return null;
	}

	@Override
	public String getType() {	return HAPConstant.OPTIONS_TYPE_COMPLEX;	}

	public String getSourceType() {return this.m_sourceOptions.getType();	}
	public HAPOptionsDefinition getSourceOptions(){return this.m_sourceOptions;}
	
	@Override
	public void init() {
	}
}
