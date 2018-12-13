package com.nosliw.data.core.script.context;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

//store result for resolve reference path
public class HAPInfoRelativeContextResolve{
	//parent reference path
	public HAPContextPath path;
	//parent root node
	public HAPContextDefinitionRoot rootNode;
	//original refered node
	public HAPContextDefinitionElement referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path,
	public HAPContextDefinitionElement resolvedNode;
	//after apply remain path, data criteria
//	HAPDataTypeCriteria resolvedCriteria;
}
