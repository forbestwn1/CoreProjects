package com.nosliw.core.application.common.datadefinition;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.data.HAPData;

public class HAPDataDefinitionWritableWithInit extends HAPDataDefinitionWritable{

	@HAPAttribute
	public static String INITDATA = "initData";

	private HAPData m_initData;

	
	public HAPData getInitData() {    return this.m_initData;     }
	public void setInitData(HAPData initData) {    this.m_initData = initData;      }


}
