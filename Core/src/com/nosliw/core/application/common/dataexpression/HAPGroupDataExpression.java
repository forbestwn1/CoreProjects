package com.nosliw.core.application.common.dataexpression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.core.application.common.valueport.HAPWithVariable;

@HAPEntityWithAttribute
public class HAPGroupDataExpression extends HAPSerializableImp implements HAPWithVariable{

	@HAPAttribute
	public static String ITEM = "item";

	private List<HAPElementInGroupDataExpression> m_items;
	
	private HAPContainerVariableInfo m_variableInfo;
	
	public HAPGroupDataExpression() {
		this.m_variableInfo = new HAPContainerVariableInfo();
		this.m_items = new ArrayList<HAPElementInGroupDataExpression>();
	}

	public void addItem(HAPElementInGroupDataExpression item) {    this.m_items.add(item);     }
	public List<HAPElementInGroupDataExpression> getItems(){   return this.m_items;    }
	
	@Override
	public HAPContainerVariableInfo getVariablesInfo() {   return this.m_variableInfo;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		Map<String, String> itemMap = new LinkedHashMap<String, String>();
		for(HAPElementInGroupDataExpression item : this.m_items) {
			itemMap.put(item.getId(), item.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ITEM, HAPUtilityJson.buildMapJson(itemMap));
		jsonMap.put(HAPWithVariable.VARIABLEINFOS, HAPManagerSerialize.getInstance().toStringValue(this.m_variableInfo, HAPSerializationFormat.JSON));
	}

}
