//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_storyUtility;
	var node_ElementId;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_createChangeItemDelete = function(targetCategary, targetId){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_DELETE;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF] = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF][node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY] = targetCategary;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF][node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID] = targetId;
		return out;
	};
	
	var loc_createChangeItemNew = function(element, aliasObj, story){           
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW;
		
		var eleId = element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		if(eleId==undefined){
			var eleCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
			var eleType = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];
			eleId = eleCategary+node_COMMONCONSTANT.SEPERATOR_LEVEL1+eleType+node_COMMONCONSTANT.SEPERATOR_LEVEL1+node_storyUtility.getNextIdForStory(story);
			element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID] = eleId;
		}
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ELEMENT] = element;

		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ALIAS] = aliasObj;
		return out;
	};
	
	var loc_createChangeItemPatch = function(targetRef, path, value){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_PATH] = path;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_VALUE] = value;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF] = targetRef;
		return out;
	};
	
	var loc_createChangeItemAlias = function(aliasName, elementId){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_ALIAS;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ALIAS] = aliasName;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ELEMENTID] = elementId;
		return out;
	};

	var loc_createChangeItemStoryInfo = function(infoName, infoValue){           
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_STORYINFO;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_INFONAME] = infoName;
		out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_INFOVALUE] = infoValue;
		return out;
	};
	
	var loc_createChangeItemDeleteForStoryElement = function(element){  return loc_createChangeItemDelete(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);   };
	var loc_createChangeItemPatchForStoryElement = function(element, path, value){	return loc_createChangeItemPatch(new node_ElementId(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]), path, value);	};
	var loc_createChangeItemNewForStoryElement = function(element, aliasObj, story){  return loc_createChangeItemNew(element, aliasObj, story);   };

	var loc_applySingleChangeNew = function(story, changeItem, saveRevert){
		var aliasObj = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ALIAS];
		if(aliasObj!=undefined){
			var oldAliasEleId = node_storyUtility.getElementIdByReference(story, aliasObj);
		}
		var element = node_storyUtility.addStoryElement(story, changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_ELEMENT], aliasObj);
		if(loc_isRevertable(saveRevert, changeItem)!=false){
			var revertChanges = [];
			revertChanges.push(loc_createChangeItemDelete(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]));
			if(aliasObj!=undefined)  revertChanges.push(loc_createChangeItemAlias(aliasObj[node_COMMONATRIBUTECONSTANT.STORYALIASELEMENT_NAME], oldAliasEleId));
			changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = revertChanges;
		}
	};
	
	var loc_applySingleChangeDelete = function(story, changeItem, saveRevert){
		var element = node_storyUtility.deleteStoryElementByRef(story, changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF]);
		if(loc_isRevertable(saveRevert, changeItem))  changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = [loc_createChangeItemNew(element, undefined, story)];
	};
	
	//output: a array of new change item
	var loc_applySingleChangePatch = function(story, changeItem, saveRevert, extend){
		var path = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_PATH];
		var value = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_VALUE];
		var element = node_storyUtility.getStoryElementByRef(story, changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_TARGETELEMENTREF]);
		var eleCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
		var eleType = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];

		var out = [];
		if(extend==true){
			if(eleCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){ 
				var children = element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUP_ELEMENTS];
				if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_SWITCH){
					if(path==node_COMMONATRIBUTECONSTANT.STORYELEMENTGROUPSWITCH_CHOICE){
						var currentChoice = element[node_COMMONATRIBUTECONSTANT.STORYELEMENTGROUPSWITCH_CHOICE];
						if(currentChoice!=value){
							if(children.length>=2){
								_.each(children, function(child, i){
									var childName = child[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
									var childEleId = node_storyUtility.getElementIdByReference(story, child[node_COMMONATRIBUTECONSTANT.STORYINFOELEMENT_ELEMENTREF]);
									var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID])
									if(childName==value){
										if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==false){
											out.push(loc_createChangeItemPatchForStoryElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, true));
										}
									}
									else{
										if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==true){
											out.push(loc_createChangeItemPatchForStoryElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, false));
										}
									}
								});
							}
							else{
								var child = children[0];
								var childEleId = node_storyUtility.getElementIdByReference(story, child[node_COMMONATRIBUTECONSTANT.STORYINFOELEMENT_ELEMENTREF]);
								var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID])
								if(value==true){
									out.push(loc_createChangeItemPatchForStoryElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, true));
								}
								else{
									out.push(loc_createChangeItemPatchForStoryElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, false));
								}
							}
						}
					}
				}
				else if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_BATCH){
					if(path==node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE){
						_.each(children, function(child, i){
							var childEleId = node_storyUtility.getElementIdByReference(story, child[node_COMMONATRIBUTECONSTANT.STORYINFOELEMENT_ELEMENTREF]);
							var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID])
							out.push(loc_createChangeItemPatchForStoryElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, value));
						});
					}
				}
			}
		}
		
		if(eleCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP && path==node_COMMONATRIBUTECONSTANT.STORYELEMENTGROUP_ELEMENT){
			//group element patch
			if(value[node_COMMONATRIBUTECONSTANT.STORYINFOELEMENT_ELEMENTREF]!=undefined){
				//append element
				element[node_COMMONATRIBUTECONSTANT.STORYELEMENTGROUP_ELEMENTS].push(value);
				if(loc_isRevertable(saveRevert, changeItem)){
					changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = [loc_createChangeItemPatchForStoryElement(element, path, value[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID])];
				}
			}
			else if(typeof value === 'string'){
				//delete element
				var children = element[node_COMMONATRIBUTECONSTANT.STORYELEMENTGROUP_ELEMENTS];
				for(var i in children){
					if(children[i][node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]==value){
						var oldValue = children[i];
						children.splice(i, 1);
						if(loc_isRevertable(saveRevert, changeItem)){
							changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = [loc_createChangeItemPatchForStoryElement(element, path, oldValue)];
						}
						break;
					}
				}
			}
		}
		else{
			var oldValue = node_objectOperationUtility.operateObject(element, path, node_CONSTANT.WRAPPER_OPERATION_SET, value);
			if(loc_isRevertable(saveRevert, changeItem)){
				changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = [loc_createChangeItemPatchForStoryElement(element, path, oldValue)];
			}
		}
		
		return out;
	};
	
	var loc_applySingleChangeStoryInfo = function(story, changeItem, saveRevert){
		var info = story[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
		var infoName = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_INFONAME];
		var infoValue = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_INFOVALUE];
		var oldInfoValue = info[infoName];
		info[infoName] = infoValue;
		if(loc_isRevertable(saveRevert, changeItem))  changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES] = [loc_createChangeItemStoryInfo(infoName, oldInfoValue)];
	};
	
	var loc_isRevertable = function(saveRevert, changeItem){
		if(saveRevert==false)  return false;
		return changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTABLE];
	};

	var loc_applySingleChange = function(story, changeItem, saveRevert, extend){
		//set change item id
		var itemId = changeItem[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		if(itemId==undefined){
			itemId = node_storyUtility.getNextIdForStory(story);
			changeItem[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID] = itemId;
		}

		//if a change has extended, then don't extend it again
		if(changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_EXTENDED]==true)  extend = false;
		
		var extendedChanges;
		var changeType = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_CHANGETYPE];
		if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW){
			loc_applySingleChangeNew(story, changeItem, saveRevert);
		}
		else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
			extendedChanges = loc_applySingleChangePatch(story, changeItem, saveRevert, extend);
		}
		else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_DELETE){
			loc_applySingleChangeDelete(story, changeItem, saveRevert);
		}
		else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_STORYINFO){
			loc_applySingleChangeStoryInfo(story, changeItem, saveRevert);
		}
		
		if(extendedChanges!=undefined && extendedChanges.length>0){
			_.each(extendedChanges, function(extendedChange, i){
				//extended from
				extendedChange[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_EXTENDFROM] = changeItem[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			});
			//mark change as extended
			if(changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_EXTENDED]!=true)   changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_EXTENDED] = true;   
		}
		return extendedChanges;
	};
	
	var loc_applyChange = function(story, changeItem, saveRevert, allChanges, extend){
		if(allChanges!=undefined)	allChanges.push(changeItem);
		var extendChanges = loc_applySingleChange(story, changeItem, saveRevert, extend);
		if(extendChanges!=undefined){
			_.each(extendChanges, function(extendChange, i){
				loc_applyChange(story, extendChange, saveRevert, allChanges, extend);
			});
		}
	};
	
	var loc_out = {
		
		applyChange : function(story, changeItem, saveRevert){
			loc_applyChange(story, changeItem, saveRevert, undefined, false);
		},
		
		//return an array of all changes
		applyChangeAll : function(story, changeItem, allChanges){
			loc_applyChange(story, changeItem, true, allChanges, true);
		},
		
		reverseChange : function(story, changeItem){
			var that  = this;
			var reverseChanges = changeItem[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTCHANGES];
			if(reverseChanges!=undefined){
				_.each(reverseChanges, function(reverseChange, i){
					loc_applyChange(story, reverseChange, false, undefined, false);
				});
			}
		},
		
		createChangeItemPatch : function(targetRef, path, value){  return loc_createChangeItemPatch(targetRef, path, value);		},
		
		createChangeItemStoryIdIndex : function(story){
			var out = loc_createChangeItemStoryInfo(node_COMMONCONSTANT.STORY_INFO_IDINDEX, node_storyUtility.getIdIndex(story));
			out[node_COMMONATRIBUTECONSTANT.STORYCHANGEITEM_REVERTABLE] = false;
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
nosliw.registerSetNodeDataEvent("application.instance.story.entity.ElementId", function(){node_ElementId = this.getData();});

//Register Node by Name
packageObj.createChildNode("storyChangeUtility", node_utility); 

})(packageObj);
