package com.nosliw.core.application.common.structure22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityStructure {

	public static String[] getAllCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
		};
		return contextTypes;
	}

	public static List<String> getAllCategariesWithResolvePriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return new ArrayList<>(Arrays.asList(contextTypes));
	}

	public static String[] getAllCategariesWithPriority(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PRIVATE,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}

	//context type that can be inherited by child
	public static String[] getInheritableCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
		};
		return contextTypes;
	}

	//visible to child
	public static String[] getVisibleToChildCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_INTERNAL,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED,
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC,
		};
		return contextTypes;
	}
	
	public static String[] getVisibleToExternalCategaries(){
		String[] contextTypes = {
			HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC
		};
		return contextTypes;
	}




}
