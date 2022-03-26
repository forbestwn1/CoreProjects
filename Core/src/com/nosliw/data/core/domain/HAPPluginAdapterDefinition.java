package com.nosliw.data.core.domain;

public interface HAPPluginAdapterDefinition {

	String getAdapterType();

	Object parseAdapter(Object obj);

}
