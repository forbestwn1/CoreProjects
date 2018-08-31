package com.nosliw.data.core.script.context;

//store result for resolve reference path
public class HAPInfoRelativeContextResolve{
	//parent reference pat
	public HAPContextPath contextPath;
	//original refered node
	public HAPContextNode referedNode;
	//unmatched path part
	public String remainPath;
	//apply unmatched path, find the resolvedNode
	public HAPContextNode resolvedNode;
}
