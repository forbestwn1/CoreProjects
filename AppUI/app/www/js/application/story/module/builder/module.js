/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createEventObject;
	var node_createComponentQuestionStep;
	var node_createComponentQuestionGroup;
	var node_createComponentQuestionItem;
	var node_storyUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "storyBuilder";

var node_createModuleStoryBuilder = function(parm){

	var loc_root = parm;

	var loc_storyService = node_createStoryService();
	
	var loc_story;
	var loc_changeHistory;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData = {
		question : {}
	};
	
	var loc_vue;

	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		
		Vue.component('question-step', node_createComponentQuestionStep());
		Vue.component('question-group', node_createComponentQuestionGroup());
		Vue.component('question-item', node_createComponentQuestionItem());
		
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			components : {
			},
			computed : {
			},
			methods : {
				onPreviousStep : function(event) {
				},
				onNextStep : function(event) {
				},
				onFinishStep : function(event) {
				},
			},
			template :
				`
				    <div class="block">
						<br>
				    	QuestionModule
						<br>
						<question-step 
							v-bind:data="question"
					  		v-on:previousStep="onPreviousStep"
					  		v-on:nextStep="onNextStep"
					  		v-on:finishStep="onFinishStep"
						></question-step>
				    </div>
				`
		});
	};

	var loc_processChangeItem = function(changeItem){
		node_storyChangeUtility.applyChange(loc_story, changeItem);
	};
	
	var loc_processQuestion = function(question){
		var type = question[node_COMMONATRIBUTECONSTANT.QUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.QUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_processQuestion(child, loc_story);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			var targetCategary = question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETCATEGARY];
			var targetId = question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETID];
			var element = node_storyUtility.getStoryElement(loc_story, targetCategary, targetId);
			question.element = element;
		}
	};
	
	var loc_out = {
		
		refreshRequest : function(undefined, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshStoryBuilderModule", {}), handlers, requestInfo);
			
			out.addRequest(loc_storyService.getNewDesignRequest(undefined, "pageSimple", {
				success : function(request, design){
					loc_story = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];
					var changeHistory = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_CHANGEHISTORY];
					var changeBatch = changeHistory[changeHistory.length-1];
					_.each(changeBatch[node_COMMONATRIBUTECONSTANT.CHANGEBATCH_CHANGES], function(changeItem){
						loc_processChangeItem(changeItem);
					});
					
					var question = changeBatch[node_COMMONATRIBUTECONSTANT.CHANGEBATCH_QUESTION];
					loc_processQuestion(question);
					
					loc_componentData.question = question;
				}
			}));
			return out;
		},

		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionStep", function(){node_createComponentQuestionStep = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionGroup", function(){node_createComponentQuestionGroup = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItem", function(){node_createComponentQuestionItem = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});



//Register Node by Name
packageObj.createChildNode("createModuleStoryBuilder", node_createModuleStoryBuilder); 

})(packageObj);
