package com.nosliw.data.core.runtime;

/*
 * This entity represent a runtime env
 * A runtime information is composed of two part:
 * 		language:  the execution language (java, javascription, ...)
 * 		environment: the execution environment (server, ui, mobile, ...)
 */
public interface HAPRuntimeInfo {

	String getLanguage();
	
	String getEnvironment();
	
}
