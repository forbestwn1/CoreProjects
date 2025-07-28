package com.nosliw.entity.sort;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.number.HAPIntegerData;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPWraper;
import com.nosliw.entity.query.HAPQueryEntityWraper;

/*
 * this class is sorting by attribute : entity attribute + order (ascent/descent) 
 */
public class HAPSortingQueryAttribute extends HAPSortingInfo{

	//entity name of the attribute used for ordering
	private String m_entityName;
	//the attribute used for ordering
	private String m_attribute;
	
	//
	private HAPDataType m_dataType;
	
	//ascent or descent during sorting
	private int m_order;
	
	public HAPSortingQueryAttribute(String entityName, String attribute, int order, HAPDataTypeManager dataTypeMan){
		super(HAPConstant.SORTING_TYPE_ATTRIBUTE, dataTypeMan);
		this.m_entityName = entityName;
		this.m_attribute = attribute;
		this.m_order = order;
	}
	
	public String getEntityName(){ return this.m_entityName; }
	public String getAttribute(){return this.m_attribute;}
	public int getOrder(){return this.m_order;}

	public void setDataType(HAPDataType dataType){this.m_dataType=dataType;}
	
	@Override
	public int compare(HAPWraper data1, HAPWraper data2) {
		HAPQueryEntityWraper qData1 = (HAPQueryEntityWraper)data1;
		HAPQueryEntityWraper qData2 = (HAPQueryEntityWraper)data2;
		
		HAPWraper attrWraper1 = qData1.getAttributeWraper(this.m_entityName, this.m_attribute);
		HAPWraper attrWraper2 = qData2.getAttributeWraper(this.m_entityName, this.m_attribute);
		
		if(this.m_dataType==null){
			this.m_dataType = attrWraper1.getData().getDataType(); 
		}
		
		HAPIntegerData compareOut = (HAPIntegerData)this.m_dataType.operate(HAPConstant.DATAOPERATION_COMPARE, new HAPData[]{attrWraper1.getData(), attrWraper2.getData()}).getData();
		int out = compareOut.getValue();
		
		switch(this.m_order){
		case HAPConstant.SORTING_ORDER_ASCEND:
			break;
		case HAPConstant.SORTING_ORDER_DESCEND:
			out = out * -1;
			break;
		}
		return out;
	}
}
