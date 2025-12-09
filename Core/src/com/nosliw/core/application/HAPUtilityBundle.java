package com.nosliw.core.application;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityBundle {

	public static HAPBundle getBrickBundle(HAPResourceIdSimple resourceId, HAPManagerApplicationBrick brickMan, HAPRuntimeInfo runtimeInfo) {
		HAPBundle bundle = brickMan.getBrickBundle(HAPUtilityBrickId.fromResourceId2BrickId(resourceId), runtimeInfo);
		HAPUtilityExport.exportBundle(resourceId, bundle);
		return bundle;
	}

	public static String buildBranchPathSegment(String branchName) {
		return HAPConstantShared.SYMBOL_KEYWORD + branchName;
	}
	
	public static String normalizePathWithBranch(String path, String defaultBranch) {
		String out = path;
		if(!path.startsWith(HAPConstantShared.SYMBOL_KEYWORD)) {
			out = HAPUtilityNamingConversion.cascadePath(buildBranchPathSegment(defaultBranch), path);
		}
		return out;
	}
	
	public static HAPComplexPath getBrickFullPathInfo(String path, String defaultBranch) {
		HAPPath pathNorm = new HAPPath(normalizePathWithBranch(path, defaultBranch));
		Pair<String, HAPPath> pathPair = pathNorm.trimFirst();
		return new HAPComplexPath(getBranchName(pathPair.getLeft()), pathPair.getRight());
	}

	public static HAPComplexPath getBrickFullPathInfo(String path) {
		return getBrickFullPathInfo(new HAPPath(path));
	}
	
	public static HAPComplexPath getBrickFullPathInfo(HAPPath path) {
		Pair<String, HAPPath> pathPair = path.trimFirst();
		return new HAPComplexPath(getBranchName(pathPair.getLeft()), pathPair.getRight());
	}

	public static String getBranchName(String pathSegment) {
		String out = pathSegment;
		if(pathSegment.startsWith(HAPConstantShared.SYMBOL_KEYWORD)) {
			out = pathSegment.substring(HAPConstantShared.SYMBOL_KEYWORD.length());
		}
		return out;
	}
}
