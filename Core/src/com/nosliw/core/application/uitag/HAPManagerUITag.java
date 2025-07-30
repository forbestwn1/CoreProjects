package com.nosliw.core.application.uitag;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerUITag{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Map<String, HAPUITagDefinitionData> m_dataTagDefs;
	private Map<String, HAPUITagDefinition> m_otherTagDefs;
	
	public HAPManagerUITag(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPUITagDefinition getUITagDefinition(String tagId, String version) {
		if(version==null) {
			version = "1.0.0";
		}
		String fileName = getUITagFolder(tagId, version) + "definition.json";
		JSONObject jsonObj = new JSONObject(HAPUtilityFile.readFile(new File(fileName)));
		return HAPUtilityUITagDefinitionParser.parseUITagDefinition(jsonObj, tagId, version);
	}
	
	public HAPUITagInfo getDefaultUITagData(HAPUITageQueryData query) {
		HAPUITagInfo result = null;
		HAPUITagQueryResultSet resultSet = this.queryUITagData(query);
		List<HAPUITagQueryResult> items = resultSet.getItems();
		if(items!=null && items.size()>=1) {
			result = items.get(0).getUITagInfo();
		}
		return result;
	}
	
	
	private String getUITagFolder(String tagId, String version) {
		return HAPSystemFolderUtility.getTagDefinitionFolder() + version + "/" + tagId +"/";
	}

	private void readAllTags() {
		this.m_dataTagDefs = new LinkedHashMap<String, HAPUITagDefinitionData>();
		this.m_otherTagDefs = new LinkedHashMap<String, HAPUITagDefinition>();
		
		for(File versionFolder : HAPUtilityFile.getChildrenFolder(HAPSystemFolderUtility.getTagDefinitionFolder())) {
			String version = versionFolder.getName();
			for(File tagFolder : HAPUtilityFile.getChildrenFolder(versionFolder)) {
				HAPUITagDefinition uiTagDef = getUITagDefinition(tagFolder.getName(), version);
//				uiTagDef.setSourceFile(file);
				String type = uiTagDef.getType();
				if(HAPConstantShared.UITAG_TYPE_DATA.equals(type)) {
					this.m_dataTagDefs.put(uiTagDef.getName(), (HAPUITagDefinitionData)uiTagDef);
				}
				else {
					this.m_otherTagDefs.put(uiTagDef.getName(), uiTagDef);
				}
			}
		}
	}
	
	public HAPUITagQueryResultSet queryUITagData(HAPUITageQueryData query) {
		if(this.m_dataTagDefs==null) {
			this.readAllTags();
		}
		
		List<HAPUITagCandidate> candidates = new ArrayList<HAPUITagCandidate>();
		HAPDataTypeCriteria queryDataTypeCriteria = query.getDataTypeCriterai();
		for(String name : this.m_dataTagDefs.keySet()) {
			HAPUITagDefinitionData uiTagDef = this.m_dataTagDefs.get(name);
			HAPDataTypeCriteria tagDataTypeCriteria = uiTagDef.getDataTypeCriteria();
			HAPMatchers matchers = this.m_runtimeEnv.getDataTypeHelper().convertable(queryDataTypeCriteria, tagDataTypeCriteria);
			if(matchers!=null) {
				double score = matchers.getScore();
				if(score>0) {
					candidates.add(new HAPUITagCandidate(uiTagDef, score, matchers));
				}
			}
		}
		HAPUITagCandidate[] candiateArray = candidates.toArray(new HAPUITagCandidate[0]);
		Arrays.sort(candiateArray, new Comparator<HAPUITagCandidate>() {
			@Override
			public int compare(HAPUITagCandidate arg0, HAPUITagCandidate arg1) {
				if(arg0.getScore()>arg1.getScore()) {
					return -1;
				}
				if(arg0.getScore()<arg1.getScore()) {
					return 1;
				}
				return 0;
			}
		});

		HAPUITagQueryResultSet out = new HAPUITagQueryResultSet();
		for(HAPUITagCandidate candidate : candiateArray) {
			HAPUITagInfo result = new HAPUITagInfo(candidate.getUITagDef());
			result.addMatchers("internal_data", candidate.getMatchers());
			HAPUITagQueryResult resultInfo = new HAPUITagQueryResult(result, candidate.getScore());
			out.addItem(resultInfo);
		}
		
		return out;
	}

	public String getUITagDefinitionContent(String tagId) {
		String fileName = HAPSystemFolderUtility.getTagDefinitionFolder() + tagId + "/definition.json";
		File file = new File(fileName);
		return HAPUtilityFile.readFile(file);
	}
	
	class HAPUITagCandidate{
		private HAPUITagDefinition m_uiTagDef;
		
		private double m_score;
		
		private HAPMatchers m_matchers;
		
		public HAPUITagCandidate(HAPUITagDefinition uiTagDef, double score, HAPMatchers matchers) {
			this.m_uiTagDef = uiTagDef;
			this.m_score = score;
			this.m_matchers = matchers;
		}
		
		public HAPUITagDefinition getUITagDef() {		return this.m_uiTagDef;		}
		public double getScore() {   return this.m_score;    }
		public HAPMatchers getMatchers() {    return this.m_matchers;    }
	}
}
