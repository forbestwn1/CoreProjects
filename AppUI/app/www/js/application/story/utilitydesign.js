//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_storyUtility;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_validateQuestionAnswer = function(question, errorMsgs){
		var type = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_validateQuestionAnswer(child, errorMsgs);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			if(question.element.enable==true){
				if(question.answered==false){
					if(question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_ISMANDATORY]==true){
						errorMsgs.push("'"+question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_QUESTION]+"' is not answered!!!");
					}
				}
			}
		}
	};

	var loc_discoverAllQuestionChanges = function(question, changes){
		var type = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_CHILDREN];
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
		var type = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_CHILDREN];
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
			answer[node_COMMONATRIBUTECONSTANT.STORYANSWER_CHANGES] = changes;
			answer[node_COMMONATRIBUTECONSTANT.STORYANSWER_QUESTIONID] = question[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			answers.push(answer);
		}
	};

	var loc_out = {
		
		getDesignStages : function(design){
			var info = design[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			return info[node_COMMONCONSTANT.STORYDESIGN_INFO_STAGES];
		},

		getStepStage : function(step){
			var info = step.info;
			return info[node_COMMONCONSTANT.STORYDESIGN_CHANGE_INFO_STAGE];
		},
		
		applyPatchFromQuestion : function(story, question, path, value, changesResult){
			var changeItem = node_storyChangeUtility.createChangeItemPatch(question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TARGETREF], path, value);
			node_storyChangeUtility.applyChangeAll(story, changeItem, changesResult);
		},

		validateQuestionAnswers : function(question){
			var errorMsgs = [];
			loc_validateQuestionAnswer(question, errorMsgs)
			if(errorMsgs.length==0)  return;
			else return errorMsgs;
		},
		
		discoverAllQuestionAnswers : function(question){
			var out = [];
			loc_discoverAllQuestionAnswers(question, out);
			return out;
		},
		
		reverseStep : function(story, step){
			var that = this;
			var changes = step.changes;
			for(var i=changes.length-1; i>=0; i--){
				node_storyChangeUtility.reverseChange(story, changes[i]);
			}
			
			var answerChanges = [];
			loc_discoverAllQuestionChanges(step.question, answerChanges);
			for(var i=answerChanges.length-1; i>=0; i--){
				node_storyChangeUtility.reverseChange(story, answerChanges[i]);
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
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("designUtility", node_utility); 

})(packageObj);
