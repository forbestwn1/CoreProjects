package com.nosliw.uiresource.page.tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUITagManager {

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private Map<String, HAPUITagDefinitionData> m_dataTagDefs;
	
	public HAPUITagManager(HAPDataTypeHelper dataTypeHelper) {
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_dataTagDefs = new LinkedHashMap<String, HAPUITagDefinitionData>();
		this.readAllTags();
	}
	
	private void readAllTags() {
		Set<File> files = HAPFileUtility.getAllFiles(HAPSystemFolderUtility.getTagDefinitionFolder());
		for(File file : files) {
			HAPUITagDefinition uiTagDef = HAPUITagDefinitionParser.parseFromFile(file);
			uiTagDef.setSourceFile(file);
			String type = uiTagDef.getType();
			if(HAPConstant.UITAG_TYPE_DATA.equals(type)) {
				this.m_dataTagDefs.put(uiTagDef.getName(), (HAPUITagDefinitionData)uiTagDef);
			}
		}
	}
	
	public HAPUITagQueryResult getDefaultUITag(HAPUITageQueryData query) {
		HAPUITagQueryResult result = null;
		HAPUITagQueryResultSet resultSet = this.queryUITagData(query);
		List<HAPUITagQueryResultInfo> items = resultSet.getItems();
		if(items!=null && items.size()>=1) {
			result = items.get(0).getResult();
		}
		return result;
	}
	
	public HAPUITagQueryResultSet queryUITagData(HAPUITageQueryData query) {
		List<HAPUITagCandidate> candidates = new ArrayList<HAPUITagCandidate>();
		HAPDataTypeCriteria queryDataTypeCriteria = query.getDataTypeCriterai();
		for(String name : this.m_dataTagDefs.keySet()) {
			HAPUITagDefinitionData uiTagDef = this.m_dataTagDefs.get(name);
			HAPDataTypeCriteria tagDataTypeCriteria = uiTagDef.getDataTypeCriteria();
			HAPMatchers matchers = this.m_dataTypeHelper.convertable(queryDataTypeCriteria, tagDataTypeCriteria);
			if(matchers!=null) {
				double score = matchers.getScore();
				if(score>0) candidates.add(new HAPUITagCandidate(uiTagDef, score));
			}
		}
		HAPUITagCandidate[] candiateArray = candidates.toArray(new HAPUITagCandidate[0]);
		Arrays.sort(candiateArray, new Comparator<HAPUITagCandidate>() {
			@Override
			public int compare(HAPUITagCandidate arg0, HAPUITagCandidate arg1) {
				if(arg0.getScore()>arg1.getScore()) return 1;
				if(arg0.getScore()<arg1.getScore()) return -1;
				return 0;
			}
		});

		HAPUITagQueryResultSet out = new HAPUITagQueryResultSet();
		for(HAPUITagCandidate candidate : candiateArray) {
			HAPUITagQueryResult result = new HAPUITagQueryResult(candidate.getUITagDef().getName());
			HAPUITagQueryResultInfo resultInfo = new HAPUITagQueryResultInfo(result, candidate.getScore());
			out.addItem(resultInfo);
		}
		
		return out;
	}
	
	public HAPUITagDefinition getUITagDefinition(HAPUITagId id){
		HAPUITagDefinition out = this.m_dataTagDefs.get(id.getId());
//		out.setSourceFile(file);
//		
//		String fileName = HAPSystemFolderUtility.getTagDefinitionFolder() + id.getId() + ".js";
//		File file = new File(fileName);
//		
//		HAPUITagDefinition out = HAPUITagDefinitionParser.parseFromFile(file);
//		out.setSourceFile(file);
		
		return out;
	}	
	
	class HAPUITagCandidate{
		private HAPUITagDefinition m_uiTagDef;
		
		private double m_score;
		
		public HAPUITagCandidate(HAPUITagDefinition uiTagDef, double score) {
			this.m_uiTagDef = uiTagDef;
			this.m_score = score;
		}
		
		public HAPUITagDefinition getUITagDef() {		return this.m_uiTagDef;		}
		
		public double getScore() {   return this.m_score;    }
	}
}
