package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUITagManager {

	private HAPDataTypeHelper m_dataTypeHelper;
	
	public HAPUITagManager(HAPDataTypeHelper dataTypeHelper) {
		this.m_dataTypeHelper = dataTypeHelper;
	}
	
	public HAPUITagQueryResult getDefaultUITagDefnition(HAPUITageQuery query) {
		HAPUITagQueryResult result = null;
		Set<HAPDataTypeId> dataTypeIds = query.getDataTypeCriterai().getValidDataTypeId(m_dataTypeHelper);
		HAPDataTypeId dataTypeId = dataTypeIds.iterator().next();
		if(dataTypeId.getName().equals("test.string")) {
			result = new HAPUITagQueryResult("textinput");
		}
		return result;
	}
	
	public HAPUITagDefinition getUITagDefinition(HAPUITagId id){
		String fileName = HAPSystemFolderUtility.getTagDefinitionFolder() + id.getId() + ".js";
		File file = new File(fileName);
		
		HAPUITagDefinition out = HAPUITagDefinitionParser.parseFromFile(file);
		out.setSourceFile(file);
		
		return out;
	}
	
}
