/**
 * 
 */
var nosliwCreateUITagManager = function(){
	//sync task name for remote call 
	var loc_moduleName = "uiTagManager";
	
	//registered factory function for tag
	var loc_tagFactoryFuns = {};
	
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		registerUITag : function(name, tagFactory){
			loc_tagFactoryFuns[name] = tagFactory;
		},
		
		getUITagFactoryFunction : function(name){
			return loc_tagFactoryFuns[name];
		},
		
		createUITagObject : function(id, uiTagInfo, uiResourceView, requestInfo){
			var tagName = uiTagInfo.tagName;
			var tagFactoryFunction = loc_tagFactoryFuns[tagName];
			
			var tagCommon = nosliwCreateUITagCommonObject(id, uiTagInfo, uiResourceView, requestInfo);
			var tagObj = tagFactoryFunction.call(this);
			var out = _.extend(tagCommon, tagObj);
			
			out.init(id, uiTagInfo, uiResourceView, requestInfo);
			return out;
		},
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	
	return loc_out;
};
	








/*
createUITag : function(id, uiTag, uiResourceView){
	var tag = {};
	
	if(uiTag.tagName=='container'){
		tag = loc_createContainerUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='switch'){
		tag = loc_createSwitchUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='case'){
		tag = loc_createCaseUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='default'){
		tag = loc_createDefaultUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='error'){
		tag = loc_createErrorUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='input'){
		tag = loc_createInputUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='checkbox'){
		tag = loc_createCheckboxUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='select'){
		tag = loc_createSelectUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='include'){
		tag = loc_createIncludeUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='border'){
		tag = loc_createBorderUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='uicomponent'){
		tag = loc_createComponentUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='body'){
		tag = loc_createBodyUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='face'){
		tag = loc_createFaceUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='uiunit'){
		tag = loc_createUIUnitUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='storyboard'){
		tag = loc_createStoryboardUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='datatype'){
		tag = loc_createDatatypeUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='uiface'){
		tag = loc_createUIFaceUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='navigation'){
		tag = loc_createNavigationTag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='unitstructure'){
		tag = loc_createUnitStructureUITag(id, uiTag, uiResourceView);
	}
	else if(uiTag.tagName=='unitdata'){
		tag = loc_createUnitDataTag(id, uiTag, uiResourceView);
	}
	else{
		alert('Custom Tag  ' + uiTag.tagName + '  Doese not exists!!!!!');
		return;
	}
	
	return tag;
},
*/
