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

	var loc_discoverAllQuestionChanges = function(question, changes){
		var type = question[node_COMMONATRIBUTECONSTANT.QUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.QUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_discoverAllQuestionChanges(child, changes);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			_.each(question.answer, function(change, i){
				changes.push(change);
			});
		}
	};

	var loc_discoverAllQuestionAnswers = function(question, answers){
		var type = question[node_COMMONATRIBUTECONSTANT.QUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.QUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_discoverAllQuestionAnswers(child, answers);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			var answer = {};
			var changes = [];
			_.each(question.answer, function(change, i){
				changes.push(change);
			});
			answer[node_COMMONATRIBUTECONSTANT.ANSWER_CHANGES] = changes;
			answer[node_COMMONATRIBUTECONSTANT.ANSWER_QUESTIONID] = question[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			answers.push(answer);
		}
	};

	var loc_createChangeItemDelete = function(targetCategary, targetId){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_DELETE;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF] = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF][node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY] = targetCategary;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF][node_COMMONATRIBUTECONSTANT.IDELEMENT_ID] = targetId;
		return out;
	};
	
	var loc_createChangeItemNew = function(element, aliasObj){           
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ELEMENT] = element;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ALIAS] = aliasObj;
		return out;
	};
	
	var loc_createChangeItemPatch = function(targetRef, path, value){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_PATH] = path;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_VALUE] = value;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF] = targetRef;
		return out;
	};
	
	var loc_createChangeItemAlias = function(aliasName, elementId){
		var out = {};
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE] = node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_ALIAS;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ALIAS] = aliasName;
		out[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ELEMENTID] = elementId;
		return out;
	};
	
	var loc_createChangeItemDeleteForElement = function(element){  return loc_createChangeItemDelete(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);   };
	var loc_createChangeItemPatchForElement = function(element, path, value){	return loc_createChangeItemPatch(new node_ElementId(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]), path, value);	};
	var loc_createChangeItemNewForElement = function(element, aliasObj){  return loc_createChangeItemNew(element, aliasObj);   };

	var loc_applyChangeNew = function(story, changeItem, prepareRevert){
		var aliasObj = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ALIAS];
		if(aliasObj!=undefined){
			var oldAliasEleId = node_storyUtility.getElementIdByReference(story, aliasObj);
		}
		var element = node_storyUtility.addStoryElement(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ELEMENT], aliasObj);
		if(prepareRevert!=false){
			var revertChanges = [];
			revertChanges.push(loc_createChangeItemDelete(element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY], element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]));
			if(aliasObj!=undefined)  revertChanges.push(loc_createChangeItemAlias(aliasObj[node_COMMONATRIBUTECONSTANT.ALIASELEMENT_NAME], oldAliasEleId));
			changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_REVERTCHANGES] = revertChanges;
		}
	};
	
	var loc_applyChangePatch = function(story, changeItem, extend, allChanges, prepareRevert){
		if(allChanges!=undefined)	allChanges.push(changeItem);
		var changes = loc_applyChangePatchSingle(story, changeItem, extend, prepareRevert);
		_.each(changes, function(change, i){
			loc_applyChangePatch(story, change, extend, allChanges, prepareRevert);
		});
	};
	
	var loc_applyChangeDelete = function(story, changeItem, prepareRevert){
		var element = node_storyUtility.deleteStoryElementByRef(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF]);
		if(prepareRevert!=false)  changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_REVERTCHANGES] = [loc_createChangeItemNew(element)];
	};
	
	//output: a array of new change item
	var loc_applyChangePatchSingle = function(story, changeItem, extend, prepareRevert){
		var path = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_PATH];
		var value = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_VALUE];
		var element = node_storyUtility.getStoryElementByRef(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETELEMENTREF]);
		var eleCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
		var eleType = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];

		var out = [];
		if(extend==true){
			if(eleCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){ 
				var children = element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUP_ELEMENTS];
				if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_SWITCH){
					if(path==node_COMMONATRIBUTECONSTANT.ELEMENTGROUPSWITCH_CHOICE){
						var currentChoice = element[node_COMMONATRIBUTECONSTANT.ELEMENTGROUPSWITCH_CHOICE];
						if(currentChoice!=value){
							_.each(children, function(child, i){
								var childId = child[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
								var childEleId = node_storyUtility.getElementIdByReference(story, child[node_COMMONATRIBUTECONSTANT.INFOELEMENT_ELEMENTREF]);
								var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID])
								if(childId==value){
									if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==false){
										out.push(loc_createChangeItemPatchForElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, true));
									}
								}
								else{
									if(childEle[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE]==true){
										out.push(loc_createChangeItemPatchForElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, false));
									}
								}
							});
						}
					}
				}
				else if(eleType==node_COMMONCONSTANT.STORYGROUP_TYPE_BATCH){
					if(path==node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE){
						_.each(children, function(child, i){
							var childEleId = node_storyUtility.getElementIdByReference(story, child[node_COMMONATRIBUTECONSTANT.INFOELEMENT_ELEMENTREF]);
							var childEle = node_storyUtility.getStoryElement(story, childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], childEleId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID])
							out.push(loc_createChangeItemPatchForElement(childEle, node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENABLE, value));
						});
					}
				}
			}
		}
		
		if(prepareRevert!=false){
			var oldValue = node_objectOperationUtility.operateObject(element, path, node_CONSTANT.WRAPPER_OPERATION_SET, value);
			changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_REVERTCHANGES] = [loc_createChangeItemPatchForElement(element, path, oldValue)];
		}
		
		return out;
	};
	
	var loc_out = {
		
		applyChange : function(story, changeItem, prepareRevert){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW){
				loc_applyChangeNew(story, changeItem, prepareRevert);
			}
			else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
				loc_applyChangePatch(story, changeItem, false, undefined, prepareRevert);
			}
			else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_DELETE){
				loc_applyChangeDelete(story, changeItem, prepareRevert);
			}
		},
		
		//return an array of all changes
		applyChangeAll : function(story, changeItem, allChanges){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_PATCH){
				loc_applyChangePatch(story, changeItem, true, allChanges);
			}
			else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW){
				loc_applyChangeNew(story, changeItem);
			}
			else if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_DELETE){
				loc_applyChangeDelete(story, changeItem);
			}
		},
		
		applyPatchFromQuestion : function(story, question, path, value, changesResult){
			var changeItem = this.createChangeItemPatch(question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETREF], path, value);
			loc_applyChangePatch(story, changeItem, true, changesResult);
		},

		createChangeItemPatch : function(targetRef, path, value){  return loc_createChangeItemPatch(targetRef, path, value);		},
		
		discoverAllQuestionAnswers : function(question){
			var out = [];
			loc_discoverAllQuestionAnswers(question, out);
			return out;
		},
		
		reverseStep : function(story, step){
			var that = this;
			var changes = step.changes;
			for(var i=changes.length-1; i>=0; i--){
				this.reverseChange(story, changes[i]);
			}
			
			var answerChanges = [];
			loc_discoverAllQuestionChanges(step.question, answerChanges);
			for(var i=answerChanges.length-1; i>=0; i--){
				this.reverseChange(story, answerChanges[i]);
			}
		},
		
		reverseChange : function(story, changeItem){
			var that  = this;
			var reverseChanges = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_REVERTCHANGES];
			if(reverseChanges!=undefined){
				_.each(reverseChanges, function(reverseChange, i){
					that.applyChange(story, reverseChange, false);
				});
			}
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
