package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContext;

@HAPEntityWithAttribute
public class HAPExecutableProcess extends HAPSerializableImp{

	//process definition
	private HAPDefinitionProcess m_processDefinition;
	
	//unique in system
	private String m_id;
	
	//all activity
	private Map<String, HAPExecutableActivity> m_activities;
	
	//activity to start with in process
	private String m_startActivityId;
	
	//input variables
	private HAPContext m_input;  
	
	//all possible result
	private Map<String, HAPDefinitionDataAssociationGroup> m_results;
	
	
	public HAPExecutableProcess(HAPDefinitionProcess definition, String id) {
		this.m_activities = new LinkedHashMap<String, HAPExecutableActivity>();
		this.m_results = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
		this.m_processDefinition = definition;
		this.m_id = id;
	}

	public void addActivity(String activityId, HAPExecutableActivity activity) {  
		this.m_activities.put(activityId, activity);
	}
	
	public void setStartActivityId(String id) {   this.m_startActivityId = id;   }
	
	
}
