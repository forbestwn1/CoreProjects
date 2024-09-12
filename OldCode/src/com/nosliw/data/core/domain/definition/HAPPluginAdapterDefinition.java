package com.nosliw.data.core.domain.definition;

public interface HAPPluginAdapterDefinition {

	String getAdapterType();

	Object parseAdapter(Object obj);

}
