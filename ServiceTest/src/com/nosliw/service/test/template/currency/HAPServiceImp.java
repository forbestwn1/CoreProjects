package com.nosliw.service.test.template.currency;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.codetable.HAPCodeTable;
import com.nosliw.data.core.codetable.HAPCodeTableId;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPProviderService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms) {
		//read content
		String file = HAPSystemFolderUtility.getCodeTableFolder()+"currencytype.res";
		//parse content
		HAPCodeTable codeTable = parseCodeTable(new JSONObject(HAPFileUtility.readFile(HAPSystemFolderUtility.getCodeTableFolder()+"currencytype.res")));
		
		HAPData fromMoneyData = parms.get("fromMoney");
		HAPData toCurrencyData = parms.get("toCurrency");
		
		String fromCurrency = ((JSONObject)fromMoneyData.getValue()).getJSONObject("currency").getString("value");
		double fromAmount = ((JSONObject)fromMoneyData.getValue()).getDouble("amount");

		String toCurrency = (String)toCurrencyData.getValue();
		
		double toAmount = fromAmount * (this.getCurrencyIndex(codeTable, fromCurrency)+0.1*this.getCurrencyIndex(codeTable, toCurrency));
		
		JSONObject toMoneyObj = new JSONObject();
		toMoneyObj.put("amount", toAmount);
		toMoneyObj.put("currency", new JSONObject(toCurrencyData.toStringValue(HAPSerializationFormat.JSON)));
		HAPData outData = new HAPDataWrapper(new HAPDataTypeId("test.money;1.0.0"), toMoneyObj);
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("toMoney", outData);
		return HAPUtilityService.generateSuccessResult(output);
	}

	private int getCurrencyIndex(HAPCodeTable codeTable, String currency) {
		int i = 0;
		for(HAPData data : codeTable.getDataSet()) {
			if(data.getValue().equals(currency))   return i;
			i++;
		}
		return i;
	}
	
	public HAPCodeTable getCodeTable(HAPCodeTableId codeId){
		//read content
		String file = HAPSystemFolderUtility.getCodeTableFolder()+codeId.getId()+".res";
		//parse content
		return parseCodeTable(new JSONObject(HAPFileUtility.readFile(file)));
	}
	
	private HAPCodeTable parseCodeTable(JSONObject codeTableJson){
		HAPCodeTable out = new HAPCodeTable();
		out.buildObject(codeTableJson, HAPSerializationFormat.JSON);
		return out;
	}

}
