//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_storyUtility;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_createChangeItemPatch = function(element, path, value){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETCATEGARY] = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETID] = element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_PATH] = path;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_VALUE] = value;
		return out;
	};
	
	var loc_appChangeNew = function(story, changeItem){
		node_storyUtility.addStoryElement(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETCATEGARY], changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ELEMENT]);
	};
	
	var loc_applyChangePatch = function(story, changeItem, extend, allChanges){
		allChanges.push(changeItem);
		
		var changes = loc_appChangePatchToElement(element, changeItem, story, extend);
		_.each(changes, function(change, i){
			loc_applyChangePatch(story, changeItem, extend, allChanges);
		});
	};
	
	var loc_appChangePatch = function(story, changeItem){
		var element = node_storyUtility.getStoryElement(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETCATEGARY], changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETID]);
		loc_appChangePatchToElement(element, changeItem, story);
	};
	
	var loc_appChangePatchToElement = function(element, changeItem, story, extend){
		var path = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_PATH];
		var value = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_VALUE];
		var eleCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
		var eleType = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];

		var out = [];
		if(extend==true){
			if(eleCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
				var children = element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUP_ELEMENTS];
				if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_SWITCH){
					if(path==node_COMMONATRIBUTECONSTANT.ELEMENTGROUPSWITCH_CHOICE){
						var currentChoice = element[ELEMENTGROUPSWITCH_CHOICE];
						if(currentChoice!=value){
							_.each(children, function(child, i){
								var childId = child[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
								var childEleId = child[node_COMMONATRIBUTECONSTANT.INFOELEMENT_ELEMENTID];
								var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID])
								if(childId==value){
									if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==false){
										out.push(loc_createChangeItemPatch(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, true));
									}
								}
								else{
									if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==true){
										out.push(loc_createChangeItemPatch(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, false));
									}
								}
							});
						}
					}
				}
				else if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_BATCH){
					if(path==node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE){
						_.each(children, function(child, i){
							var childEleId = child[node_COMMONATRIBUTECONSTANT.INFOELEMENT_ELEMENTID];
							var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID])
							out.push(loc_createChangeItemPatch(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, value));
						});
					}
				}
			}
		}
		
		node_objectOperationUtility.operateObject(element, path, node_CONSTANT.WRAPPER_OPERATION_SET, value);
		return out;
	};
	
	var loc_appChangePatchToElement = function(element, changeItem, story){
		var path = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_PATH];
		var value = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_VALUE];
		node_objectOperationUtility.operateObject(element, path, node_CONSTANT.WRAPPER_OPERATION_SET, value);
		
		
	};

	
	
	var loc_discoverAllChanges = function(question, changes){
		var type = question[node_COMMONATRIBUTECONSTANT.QUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.QUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_discoverAllChanges(child, changes);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			_.each(question.changes, function(change, i){
				changes.push(change);
			});
		}
	};
	
	var loc_out = {
		
		applyChange : function(story, changeItem){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW){
				loc_appChangeNew(story, changeItem);
			}
			else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
				loc_appChangePatch(story, changeItem);
			}
		},
		
		applyChangeToElement : function(element, changeItem){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
				loc_appChangePatchToElement(element, changeItem);
			}
		},
		
		applyAllChangeToElement : function(element, changeItem, story){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
				loc_appChangePatchToElement(element, changeItem);
			}
		},
		
		createChangeItemPatch : function(element, path, value){  return loc_createChangeItemPatch(element, path, value);		},
		
		discoverAllChanges : function(question){
			var out = [];
			loc_discoverAllChanges(question, out);
			return out;
		},
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("storyChangeUtility", node_utility); 

})(packageObj);
