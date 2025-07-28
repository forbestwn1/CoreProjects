package com.nosliw.service.test.template.currency;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.service.HAPExecutableService;
import com.nosliw.core.application.service.HAPProviderService;
import com.nosliw.core.application.service.HAPResultInteractive;
import com.nosliw.core.application.service.HAPUtilityService;
import com.nosliw.data.core.codetable.HAPCodeTable;
import com.nosliw.data.core.codetable.HAPCodeTableId;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPServiceImp implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms) {
		//read content
		String file = HAPSystemFolderUtility.getCodeTableFolder()+"currencytype.res";
		//parse content
		HAPCodeTable codeTable = parseCodeTable(new JSONObject(HAPUtilityFile.readFile(HAPSystemFolderUtility.getCodeTableFolder()+"currencytype.res")));
		
		HAPData fromMoneyData = parms.get("fromMoney");
		HAPData toCurrencyData = parms.get("toCurrency");
		
		String fromCurrency = ((JSONObject)fromMoneyData.getValue()).getJSONObject("currency").getString("value");
		double fromAmount = ((JSONObject)fromMoneyData.getValue()).getDouble("amount");

		String toCurrency = (String)toCurrencyData.getValue();
		
		double fromCurrencyIndex = this.getCurrencyIndex(codeTable, fromCurrency);
		double toCurrencyIndex = this.getCurrencyIndex(codeTable, toCurrency);
		double toAmount = fromAmount * (fromCurrencyIndex+0.1*toCurrencyIndex);
		
		JSONObject toMoneyObj = new JSONObject();
		toMoneyObj.put("amount", toAmount);
		toMoneyObj.put("currency", new JSONObject(toCurrencyData.toStringValue(HAPSerializationFormat.JSON)));
		HAPData outData = new HAPDataWrapper(new HAPDataTypeId("test.money;1.0.0"), toMoneyObj);
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("toMoney", outData);
		return HAPUtilityService.generateSuccessResult(output);
	}

	private double getCurrencyIndex(HAPCodeTable codeTable, String currency) {
		double i = 1;
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
		return parseCodeTable(new JSONObject(HAPUtilityFile.readFile(file)));
	}
	
	private HAPCodeTable parseCodeTable(JSONObject codeTableJson){
		HAPCodeTable out = new HAPCodeTable();
		out.buildObject(codeTableJson, HAPSerializationFormat.JSON);
		return out;
	}

}
