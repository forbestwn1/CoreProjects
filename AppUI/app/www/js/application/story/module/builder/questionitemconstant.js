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
	var node_CONSTANT;
	var node_createData;
	var node_createContextElementInfo;
	var node_createContext;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemConstant = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_vueComponent = {
		data : function(){
			return {
				uiNodeView : {},
				context : {}
			};
		},
		props : ['question', 'story'],
		components : {
		},
		methods : {
		},
		mounted: function () {
			var that = this;
			var request = node_createServiceRequestInfoSequence(undefined, {
				success : function(request, uiTag){
					
				}
			});
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.question);
			request.addRequest(loc_storyService.getDefaultUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE], {
				success : function(request, tagResult){
					var tagId = tagResult[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULT_TAG];
					var uiNode = node_storyUIUtility.buildUINodeFromUITag(tagId);
					
					var data = node_createData(element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATA], node_CONSTANT.WRAPPER_TYPE_APPDATA);
					var dataVarEleInfo = node_createContextElementInfo("data", data);
					var elementInfosArray = [dataVarEleInfo];
					that.context = node_createContext("id", elementInfosArray, request);
					
					that.context.getContextElement("data").registerDataOperationEventListener(undefined, function(event, eventData, request){
						node_designUtility.applyPatchFromQuestion(that.story, that.question, node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATA, eventData.value, that.question.answer);
						that.$emit("answerChange", eventData.value);
					}, this);

					return node_uiNodeViewFactory.getCreateUINodeViewRequest([uiNode], "", that.context, {
						success : function(request, uiNodeViewGroup){
							that.uiNodeView = uiNodeViewGroup;
							uiNodeViewGroup.appendTo(that.$refs.uiTag);
						}
					});

				}
			}));
			node_requestServiceProcessor.processRequest(request);
		},	
		template : `
			<div ref="uiTag">
				Constant Question: 
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

nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemConstant", node_createComponentQuestionItemConstant); 

})(packageObj);
