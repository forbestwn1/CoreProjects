package com.nosliw.entity.options;

import java.util.List;
import java.util.Map;

import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.sort.HAPSortingInfo;
import com.nosliw.expression.HAPExpression;

public class HAPOptionsComponent {

	private String m_id;
	
	//base options that provide source options data
	private HAPOptionsDefinition m_optionsDef;
	
	
	//fiter expression used to fiter out data from base option
	private HAPExpression m_filterExpression;
	//sorting configures
	private List<HAPSortingInfo> m_sortingInfos;

	
	public void getOptionsValue(Map<String, HAPData> parms, Map<String, HAPWraper> wapperParms){
		
	}

}
