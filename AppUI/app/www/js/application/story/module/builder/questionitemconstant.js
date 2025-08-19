/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_storyChangeUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoCommon;
	var node_createStoryService;
	var node_requestServiceProcessor;
	var node_storyUtility;
	var node_storyUIUtility;
	var node_designUtility;
	var node_requestServiceProcessor;
	var node_dataRuleUtility;
	var node_CONSTANT;
	var node_createContextElementInfo;
	var node_createContext;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemConstant = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_vueComponent = {
		data : function(){
			return {
				uiNodeView : {},
				context : {},
				tagInfo : {},
				dataInfo : {},
				data : {},
				tagData : {},
				hasError : false,
				errorMsg : ""
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
			onDataChange : function(eventData){
				var data = eventData.data;
				var request = eventData.request;
				var that = this;
				var out = node_dataRuleUtility.getDataValidationByDataTypeInfoRequest(data, this.dataInfo, {
					success : function(request, errorMsgs){
						if(errorMsgs==undefined){
							node_designUtility.applyPatchFromQuestion(that.story, that.question, node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATA, data, that.question.answer);
							that.$emit("answerChange", data);
							that.data = data;
							that.hasError = false;
							that.errorMsg = "";
						}
						else{
							that.hasError = true;
							that.errorMsg = errorMsgs[0];
						}
					}
				}, request);
				node_requestServiceProcessor.processRequest(out);
			},
		},
		computed: {
		},
		created : function(){
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			this.dataInfo = element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE];
			this.tagData = element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATA];
		},
		mounted: function () {
			var that = this;
			var request = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, uiTag){
					
				}
			});
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			request.addRequest(loc_storyService.getDefaultUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE][node_COMMONATRIBUTECONSTANT.VARIABLEDATAINFO_CRITERIA], {
				success : function(request, tagInfo){
					that.tagInfo = tagInfo;
				}
			}));
			node_requestServiceProcessor.processRequest(request);
		},	
		template : `
			<div>
				{{question.question}}: 
				<div>
					<div>
						<uitag_data v-bind:uitaginfo="tagInfo" dynamicdata="true" v-bind:initdata="tagData"  v-bind:datainfo="dataInfo" v-on:dataChange="onDataChange"/>
					</div>
					<div v-if="hasError" style="color:red">{{errorMsg}}</div>
				</div>
			</div>
		`
	};
		
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyChangeUtility", function(){node_storyChangeUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){	node_createServiceRequestInfoCommon = this.getData();	});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUIUtility", function(){node_storyUIUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.designUtility", function(){node_designUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("data.dataRuleUtility", function(){node_dataRuleUtility = this.getData();	});

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemConstant", node_createComponentQuestionItemConstant); 

})(packageObj);
