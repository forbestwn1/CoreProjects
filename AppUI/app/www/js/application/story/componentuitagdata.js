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
	var node_createValueInVar;
	var node_createContextElementInfo;
	var node_createContext;
	var node_createValueInVarOperationRequest;
	var node_ValueInVarOperation;
	var node_valueInVarOperationServiceUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentUITagData = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_updateTagUI = function(that){
		var uiTagInfo = that.uitaginfo;
		var tagId = uiTagInfo==undefined?undefined:uiTagInfo[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
		if(tagId==undefined){
			$(that.$refs.uiTag).empty();
			that.uiNodeView = {};
		}
		else{
			var uiNode = node_storyUIUtility.buildUINodeFromUITag(uiTagInfo, undefined, undefined, {internal_data:that.datainfo});

			that.context = undefined;
			if(that.dynamicdata=='true'){
				var data = node_createValueInVar(that.tagData, node_CONSTANT.WRAPPER_TYPE_APPDATA);
				var dataVarEleInfo = node_createContextElementInfo("data", data);
				var elementInfosArray = [dataVarEleInfo];
				that.context = node_createContext("id", elementInfosArray, request);
				
				that.context.getContextElement("data").registerDataChangeEventListener(undefined, function(event, eventData, request){
					if(that.requestFromDataUpdate[request.getId()]==undefined){
						//data change from tag ui
						var getDataRequest = node_createValueInVarOperationRequest(that.context, new node_ValueInVarOperation("data", node_valueInVarOperationServiceUtility.createGetOperationService("")), {
							success : function(request, uiData){
								that.tagData = uiData==undefined?undefined:uiData.value;
								that.$emit("dataChange", {data:that.tagData,request:request});
							}
						}, request);
						node_requestServiceProcessor.processRequest(getDataRequest);
					}
				}, this);
			}
			
			var request = node_uiNodeViewFactory.getCreateUINodeViewRequest([uiNode], "", that.context, {
				success : function(request, uiNodeViewGroup){
					$(that.$refs.uiTag).empty();
					that.uiNodeView = uiNodeViewGroup;
					that.uiNodeView.appendTo(that.$refs.uiTag);
				}
			});

			node_requestServiceProcessor.processRequest(request);
		}
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				tagData : {},
				uiNodeView : {},
				context : {},
				requestFromDataUpdate : {}
			};
		},
		props : ['uitaginfo', 'datainfo', 'dynamicdata', 'initdata'],
		components : {
		},
		methods : {
		},
		created : function(){
			this.tagData = this.initdata;
		},
		watch : {
			uitaginfo : function(){
				loc_updateTagUI(this);
			},
		},
		template : `
			<div ref="uiTag">
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
nosliw.registerSetNodeDataEvent("variable.valueinvar.entity..createValueInVar", function(){node_createValueInVar = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("variable.context.createContext", function(){node_createContext = this.getData();});

nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.createValueInVarOperationRequest", function(){node_createValueInVarOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.ValueInVarOperation", function(){node_ValueInVarOperation = this.getData();});
nosliw.registerSetNodeDataEvent("variable.valueinvar.operation.valueInVarOperationServiceUtility", function(){node_valueInVarOperationServiceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUITagData", node_createComponentUITagData); 

})(packageObj);
