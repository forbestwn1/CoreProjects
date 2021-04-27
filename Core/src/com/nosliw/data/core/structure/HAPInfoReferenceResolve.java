package com.nosliw.data.core.structure;

//store result for resolve reference path
public class HAPInfoReferenceResolve{
	//parent reference path
	public HAPPathStructure path;
	//parent root node
	public HAPRoot rootNode;
	//original refered node
	public HAPElement referedNode;
	//refered solid node
	public HAPElement referedSolidNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path,
	public HAPElement resolvedNode;
	//after apply remain path, data criteria
//	HAPDataTypeCriteria resolvedCriteria;
}
