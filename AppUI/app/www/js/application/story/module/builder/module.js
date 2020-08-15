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
	var node_basicUtility;
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
	var node_storyChangeUtility;
	var node_errorUtility;
	var node_designUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "storyBuilder";

var node_createModuleStoryBuilder = function(parm){

	var loc_root = parm;

	var loc_storyService = node_createStoryService();
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData1 = {
		question : {},
		story : {},
		designId : "",
		resourceId : "",
		stages : [],
		currentStage : "",
		errorMessages : []
	};
	
	var loc_componentData = {
		designId : "",
		resourceId : "",
		story : {},

		steps : [],
		stepCursor : -1,

		stageInfos : [],

		errorMessages : []
	};

	
	var loc_vue;

	
	//process design step from backend
	var loc_processNewStep = function(step, processChange){
		
		var changes = step[node_COMMONATRIBUTECONSTANT.DESIGNSTEP_CHANGES];
		var questionair = step[node_COMMONATRIBUTECONSTANT.DESIGNSTEP_QUESTIONAIRE];

		var question = questionair[node_COMMONATRIBUTECONSTANT.QUESTIONNAIRE_QUESTIONS];
		var answers = questionair[node_COMMONATRIBUTECONSTANT.QUESTIONNAIRE_ANSWERS];
		loc_processQuestion(question, answers);
		
		loc_componentData.push(node_DesignStep(changes, question, [node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO]));

		//apply step changes
		if(processChange!=false){
			_.each(changes, function(changeItem){
				loc_processChangeItem(changeItem);
			});
		}
	};
	
	var loc_processQuestion = function(question, answers){
		var type = question[node_COMMONATRIBUTECONSTANT.QUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.QUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_processQuestion(child, loc_componentData.story);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			var targetCategary = question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETCATEGARY];
			var targetId = question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETID];
			var element = node_storyUtility.getStoryElement(loc_componentData.story, targetCategary, targetId);
			question.element = element;
			question.changes = answers[question[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]];
			if(question.changes==undefined)  question.changes = [];
		}
	};
	

	
	
	
	
	var loc_processPrevious = function(){
		
	};
	
	
	
	
	
	
	
	var loc_processNext = function(){
		var step = loc_getCurrentStep();
		var changes = node_storyChangeUtility.discoverAllChanges(step[node_COMMONATRIBUTECONSTANT.DESIGNSTEP_QUESTION]);
		loc_storyService.executeDoDesignRequest(undefined, loc_designId, changes, {
			success : function(request, serviceData){
				if(node_errorUtility.isSuccess(serviceData)){
					var step = serviceData[node_COMMONATRIBUTECONSTANT.SERVICEDATA_DATA];
					loc_processNewStep(step);
					loc_componentData.errorMessages = [];
				}
				else{
					loc_componentData.errorMessages = serviceData[node_COMMONATRIBUTECONSTANT.SERVICEDATA_DATA];
				}
			}
		});
	};
	
	var loc_processChangeItem = function(changeItem){
		node_storyChangeUtility.applyChange(loc_componentData.story, changeItem);
	};
	
	
	var loc_getCurrentStep = function(){
		return loc_stepHistory[loc_stepHistory.length-1];
	};

	
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
				overviewUrl : {
					get : function(){
						return "http://localhost:8080/AppUI/story.html?env=local&configure=overviewtest&app=story&version=2&designId="+loc_designId;
					}
				},
				resourceUrl : {
					get : function(){
						if(this.resourceId.id!=undefined){
							return "http://localhost:8082/nosliw/page.html?name="+this.resourceId.id.id;
						}
						else return undefined;
					}
				},
			},
			methods : {
				onStory : function(event) {
					console.log(node_basicUtility.stringify(this.story));
				},
				onConvertToShow : function(){
					var that = this;
					loc_storyService.executeGetConvertDesignRequest(loc_designId, {
						success : function(request, resourceId){
							that.resourceId = resourceId;
						}
					});
				},
				onShowDefinition : function(){
					
				},
				onShow : function(){
					
				},
				onPreviousStage : function(event) {
					
				},
				onNextStage : function(event) {
					loc_processNext();
				},
				onFinishStage : function(event) {
					loc_processNext();
				},
			},
			template :
				`
				    <div class="block">
						<br>
						<a :href="overviewUrl"  target="_blank">overview</a>
						<br>
						<a v-on:click.prevent="onStory">story</a>
						<br>
						<a v-on:click.prevent="onConvertToShow">convertToShow</a>
						<br>
						<a v-on:click.prevent="onShowDefinition">showDefinition</a>
						<br>
						<a :href="resourceUrl"  target="_blank">show</a>
						<br>
						<br>
				    	QuestionModule
						<br>
						<question-step 
							v-bind:data="question"
							v-bind:story="story"
							v-bind:stages="stages"
							v-bind:errorMessages="errorMessages"
							v-bind:currentStage="currentStage"
					  		v-on:previousStage="onPreviousStage"
					  		v-on:nextStage="onNextStage"
					  		v-on:finishStage="onFinishStage"
						></question-step>
				    </div>
				`
		});
	};

	var loc_out = {
		
		refreshRequest : function(undefined, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("RefreshStoryBuilderModule", {}), handlers, requestInfo);
			
			out.addRequest(loc_storyService.getNewDesignRequest(undefined, "pageSimple", {
				success : function(request, design){
					loc_componentData.designId = design[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
					loc_componentData.story = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_STORY];
					loc_componentData.stageInfos = node_designUtility.getDesignStages(design);
					var changeHistory = design[node_COMMONATRIBUTECONSTANT.DESIGNSTORY_CHANGEHISTORY];
					loc_processNewStep(changeHistory[changeHistory.length-1], false);
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
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();	});
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
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleStoryBuilder", node_createModuleStoryBuilder); 

})(packageObj);
