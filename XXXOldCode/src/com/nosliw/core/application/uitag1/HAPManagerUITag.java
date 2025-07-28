package com.nosliw.core.application.uitag1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPResourceDefinition;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.system.HAPSystemFolderUtility;
import com.nosliw.uiresource.page.tag.HAPParserUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;
import com.nosliw.uiresource.page.tag.HAPUITagDefinitionData;
import com.nosliw.uiresource.page.tag.HAPUITagInfo;
import com.nosliw.uiresource.page.tag.HAPUITagQueryResult;
import com.nosliw.uiresource.page.tag.HAPUITagQueryResultSet;
import com.nosliw.uiresource.page.tag.HAPUITageQueryData;

public class HAPManagerUITag {

	private HAPRuntimeEnvironment m_runtimeEnv;
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private Map<String, HAPUITagDefinitionData> m_dataTagDefs;
	private Map<String, HAPUITagDefinition> m_otherTagDefs;
	
	public HAPManagerUITag(HAPRuntimeEnvironment runtimeEnv, HAPDataTypeHelper dataTypeHelper) {
		this.m_runtimeEnv = runtimeEnv;
		this.m_dataTypeHelper = dataTypeHelper;
	}
	
	public HAPIdEntityInDomain getUITagDefinition(String tagId, HAPDomainEntityDefinitionGlobal globalDomain){
		HAPResourceDefinition tagDefResourceDef = this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGDEFINITION, tagId), globalDomain, null);
		return tagDefResourceDef.getEntityId();
	}	
	
	private void readAllTags() {
		this.m_dataTagDefs = new LinkedHashMap<String, HAPUITagDefinitionData>();
		this.m_otherTagDefs = new LinkedHashMap<String, HAPUITagDefinition>();
		Set<File> files = HAPUtilityFile.getAllFiles(HAPSystemFolderUtility.getTagDefinitionFolder());
		for(File file : files) {
			HAPUITagDefinition uiTagDef = HAPParserUITagDefinition.parseFromFile(file);
			uiTagDef.setSourceFile(file);
			String type = uiTagDef.getType();
			if(HAPConstantShared.UITAG_TYPE_DATA.equals(type)) {
				this.m_dataTagDefs.put(uiTagDef.getName(), (HAPUITagDefinitionData)uiTagDef);
			}
			else {
				this.m_otherTagDefs.put(uiTagDef.getName(), uiTagDef);
			}
		}
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
	
	public HAPUITagQueryResultSet queryUITagData(HAPUITageQueryData query) {
		if(this.m_dataTagDefs==null)   this.readAllTags();
		
		List<HAPUITagCandidate> candidates = new ArrayList<HAPUITagCandidate>();
		HAPDataTypeCriteria queryDataTypeCriteria = query.getDataTypeCriterai();
		for(String name : this.m_dataTagDefs.keySet()) {
			HAPUITagDefinitionData uiTagDef = this.m_dataTagDefs.get(name);
			HAPDataTypeCriteria tagDataTypeCriteria = uiTagDef.getDataTypeCriteria();
			HAPMatchers matchers = this.m_dataTypeHelper.convertable(queryDataTypeCriteria, tagDataTypeCriteria);
			if(matchers!=null) {
				double score = matchers.getScore();
				if(score>0) candidates.add(new HAPUITagCandidate(uiTagDef, score, matchers));
			}
		}
		HAPUITagCandidate[] candiateArray = candidates.toArray(new HAPUITagCandidate[0]);
		Arrays.sort(candiateArray, new Comparator<HAPUITagCandidate>() {
			@Override
			public int compare(HAPUITagCandidate arg0, HAPUITagCandidate arg1) {
				if(arg0.getScore()>arg1.getScore()) return -1;
				if(arg0.getScore()<arg1.getScore()) return 1;
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
