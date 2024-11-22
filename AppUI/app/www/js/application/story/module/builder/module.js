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
	var node_createComponentUITagData;
	var node_builderUtility;
	var node_storyUtility;
	var node_storyChangeUtility;
	var node_errorUtility;
	var node_designUtility;
	var node_DesignStep;
	var node_requestServiceProcessor;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "storyBuilder";

var node_createModuleStoryBuilder = function(parm){
	var loc_vue;


	var loc_root = parm;

	var loc_storyService = node_createStoryService();
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_componentData = {
		designId : "",
		resourceId : undefined,
		story : {},

		steps : [],
		stepCursor : -1,

		stageInfos : [],

		errorMessages : []
	};

	
	
	//process design step from backend
	var loc_processNewStep = function(step, processChange){
		
		var changes = step[node_COMMONATRIBUTECONSTANT.STORYDESIGNSTEP_CHANGES];
		var questionair = step[node_COMMONATRIBUTECONSTANT.STORYDESIGNSTEP_QUESTIONAIRE];

		//apply step changes
		if(processChange!=false){
			var i = 0;
			_.each(changes, function(changeItem){
				loc_processChangeItem(changeItem);
				i++;
			});
		}

		var question = questionair[node_COMMONATRIBUTECONSTANT.STORYQUESTIONNAIRE_QUESTIONS];
		var answers = questionair[node_COMMONATRIBUTECONSTANT.STORYQUESTIONNAIRE_ANSWERS];
		loc_processQuestion(question, answers);
		
		var out = new node_DesignStep(changes, question, step[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO]);
		loc_componentData.steps.push(out);
		return out;

	};
	
	var loc_processQuestion = function(question, answers){
		var type = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_processQuestion(child, answers);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			//find target element
			var targetRef = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TARGETREF];
			var element = node_storyUtility.getStoryElementByRef(loc_componentData.story, targetRef);
			question.element = element;
			
			//assign answer to question
			var answerInfo = answers[question[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]];
			if(answerInfo!=undefined)  question.answer = answerInfo[node_COMMONATRIBUTECONSTANT.STORYANSWER_CHANGES];
			if(question.answer==undefined || question.answer.length()==0){
				question.answer = [];
				question.answered = false;
			}
			else{
				question.answered = true;
			}
			
			//question label
			node_builderUtility.processQuestionLabel(question);
		}
	};
	
	//update answers in question
	var loc_updateQuestion = function(question, answers){
		var type = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TYPE];
		if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_GROUP){
			var children = question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_CHILDREN];
			_.each(children, function(child, i){
				loc_updateQuestion(child, answers);
			});
		}
		else if(type==node_COMMONCONSTANT.STORYDESIGN_QUESTIONTYPE_ITEM){
			var answer = answers[question[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]];
			if(answer!=undefined){
				var existingAnswersById = {};
				_.each(question.answer, function(change, i){
					existingAnswersById[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID] = change;
				});
				
				var newChanges = answer[node_COMMONATRIBUTECONSTANT.STORYANSWER_CHANGES];
				//find extend change and apply
				_.each(newChanges, function(change, i){
					if(existingAnswersById[change[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]]==undefined){
						loc_processChangeItem(change);
					}
				});
				question.answer = newChanges;
			}
		}
	};
	
	var loc_onPrevious = function(){
		loc_componentData.stepCursor--;
	};

	var loc_onNext = function(){
		if(loc_isAtLastStep()){
			var step = loc_getCurrentStep();
			
			var errors = node_designUtility.validateQuestionAnswers(step.question);
			if(errors!=undefined){
				//failed validation
				loc_componentData.errorMessages = errors;
			}
			else{
				var answers = node_designUtility.discoverAllQuestionAnswers(step.question);
				var extraChanges = [node_storyChangeUtility.createChangeItemStoryIdIndex(loc_componentData.story)];
				
				var nextRequest = node_createServiceRequestInfoSequence();
				nextRequest.addRequest(loc_storyService.getDoDesignRequest(undefined, loc_componentData.designId, answers, extraChanges, loc_componentData.stepCursor, {
					success : function(request, serviceData){
						if(node_errorUtility.isSuccess(serviceData)){
							var response = serviceData[node_COMMONATRIBUTECONSTANT.STORYSERVICEDATA_DATA];
							var stepRes = response[node_COMMONATRIBUTECONSTANT.STORYRESPONSEDESIGN_STEP];
							var answersRes = response[node_COMMONATRIBUTECONSTANT.STORYRESPONSEDESIGN_ANSWER];
							loc_updateQuestion(loc_getCurrentStep().question, answersRes);
							var step = loc_processNewStep(stepRes);
							loc_componentData.errorMessages = [];
							loc_componentData.stepCursor++;
							
							var stageName = node_designUtility.getStepStage(step)
							if(stageName==node_COMMONCONSTANT.DESIGNSTAGE_NAME_END){
								//end stage, convert to show
								return loc_storyService.getConvertDesignRequest(loc_componentData.designId, {
									success : function(request, resourceId){
										loc_componentData.resourceId = resourceId;
									}
								});
							}
						}
						else{
							loc_componentData.errorMessages = serviceData[node_COMMONATRIBUTECONSTANT.STORYSERVICEDATA_DATA];
						}
					}
				}));
				node_requestServiceProcessor.processRequest(nextRequest);
			}
		}
		else{
			loc_componentData.stepCursor++;
		}
	};
	
	var loc_processChangeItem = function(changeItem){		node_storyChangeUtility.applyChange(loc_componentData.story, changeItem);	};
	
	var loc_getCurrentStep = function(){	return loc_componentData.steps[loc_componentData.stepCursor];	};
	
	var loc_isAtLastStep = function(){  return loc_componentData.steps.length-1== loc_componentData.stepCursor;  };
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, requestInfo){
		
		Vue.component('question-step', node_createComponentQuestionStep());
		Vue.component('question-group', node_createComponentQuestionGroup());
		Vue.component('question-item', node_createComponentQuestionItem());
		Vue.component('uitag_data', node_createComponentUITagData());
		Vue.component('uitag_select', node_createComponentUITagSelect());
		
		loc_vue = new Vue({
			el: loc_root,
			data: loc_componentData,
			components : {
			},
			computed : {
				isProduct : {
					get : function(){
						return nosliwApplication.info.envName.startsWith("product");
					}
				},
				question : {
					get : function(){
						if(this.stepCursor!=-1){
							var step = this.steps[this.stepCursor];
							return step.question;
						}
					}
				},
				currentStage : {
					get : function(){
						if(this.stepCursor!=-1){
							var step = this.steps[this.stepCursor];
							return node_designUtility.getStepStage(step);
						}
					}
				},
				overviewUrl : {
					get : function(){
						return "story.html?env="+nosliwApplication.info.envName+"&configure=overviewtest&app=story&version=2&designId="+this.designId;
					}
				},
				pageViewUrl : {
					get : function(){
						return "story.html?env="+nosliwApplication.info.envName+"&configure=ui&app=story&version=2&designId="+this.designId;
					}
				},
				resourceUrl : {
					get : function(){
						if(this.resourceId.id!=undefined){
							var appInfo = nosliwApplication.info;
							return appInfo.baseServer + "page_"+appInfo.envName+".html?name="+this.resourceId.id.id;
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
					loc_storyService.executeGetConvertDesignRequest(this.designId, {
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
					loc_onPrevious();
				},
				onNextStage : function(event) {
					loc_onNext();
				},
				onFinishStage : function(event) {
					loc_onNext();
				},
				onAnswerChange : function(event) {
					if(!loc_isAtLastStep()){
						//reverse steps behind
						for(var i=this.steps.length-1; i>this.stepCursor; i--){
							node_designUtility.reverseStep(this.story, this.steps[i]);
							this.steps.pop();
						}
					}	
				},
			},
			template :
				`
				    <div class="block">
						<br>
						
						<span v-if="!isProduct">
						{{designId}}
						<br>
						<a v-on:click.prevent="onStory">story</a>
						<br>
						<a v-on:click.prevent="onShowDefinition">showDefinition</a>
						<br>
						<a v-on:click.prevent="onConvertToShow">convertToShow</a>
						<br>
						Page Id : {{resourceId}}
						<br>
						</span>
			
						<a :href="overviewUrl"  target="_blank">Diagram</a>
						<br>
						
						<a :href="pageViewUrl"  target="_blank">UI Preview</a>
						<br>

						<span v-if="resourceId!=undefined">
						<a :href="resourceUrl"  target="_blank">Application</a>
						<br>
						</span>
						
						<question-step 
							v-bind:question="question"
							v-bind:story="story"
							v-bind:stages="stageInfos"
							v-bind:errorMessages="errorMessages"
							v-bind:currentStage="currentStage"
					  		v-on:previousStep="onPreviousStage"
					  		v-on:nextStep="onNextStage"
					  		v-on:finishStep="onFinishStage"
					  		v-on:answerChange="onAnswerChange"
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
					loc_componentData.story = design[node_COMMONATRIBUTECONSTANT.STORYDESIGNSTORY_STORY];
					loc_componentData.stageInfos = node_designUtility.getDesignStages(design);
					var changeHistory = design[node_COMMONATRIBUTECONSTANT.STORYDESIGNSTORY_CHANGEHISTORY];
					_.each(changeHistory, function(changeStep, i){
						loc_processNewStep(changeStep, false);
					});
					loc_componentData.stepCursor = loc_componentData.stepCursor + changeHistory.length;
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
nosliw.registerSetNodeDataEvent("application.story.module.builder.builderUtility", function(){node_builderUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.createComponentUITagData", function(){node_createComponentUITagData = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.createComponentUITagSelect", function(){node_createComponentUITagSelect = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("error.utility", function(){node_errorUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.DesignStep", function(){node_DesignStep = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleStoryBuilder", node_createModuleStoryBuilder); 

})(packageObj);
