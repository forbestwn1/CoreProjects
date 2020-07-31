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
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItemConstant = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_vueComponent = {
		data : function(){
			return {
			};
		},
		props : ['data', 'story'],
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
			var element = node_storyUtility.getQuestionTargetElement(this.story, this.data);
			request.addRequest(loc_storyService.getDefaultUITagRequest(element[node_COMMONATRIBUTECONSTANT.STORYNODECONSTANT_DATATYPE], {
				success : function(request, tagResult){
					var tagId = tagResult[node_COMMONATRIBUTECONSTANT.UITAGQUERYRESULT_TAG];
					var uiNode = node_storyUIUtility.buildUINodeFromUITag(tagId);
					return node_uiNodeViewFactory.getCreateUINodeViewRequest([uiNode], "", undefined, {
						success : function(request, uiNodeViewGroup){
							uiNodeViewGroup.appendTo(that.$refs.uiTag);
						}
					});

				}
			}));
			node_requestServiceProcessor.processRequest(request);
			
//			console.log("Mounted");
//			$(this.$refs.uiTag).append(this.data.targetId);
//			  this.$nextTick(function () {
//			    // Code that will run only after the
//			    // entire view has been rendered
//			  })
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

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItemConstant", node_createComponentQuestionItemConstant); 

})(packageObj);
