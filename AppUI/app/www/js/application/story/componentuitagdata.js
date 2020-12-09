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

var node_createComponentUITagData = function(){

	var loc_storyService = node_createStoryService();
	
	var loc_updateTagUI = function(that){
		var uiTagInfo = that.uitaginfo;
		var tagId = uiTagInfo[node_COMMONATRIBUTECONSTANT.UITAGINFO_TAG];
		var uiNode = node_storyUIUtility.buildUINodeFromUITag(tagId);
		
		var data = node_createData(that.data, node_CONSTANT.WRAPPER_TYPE_APPDATA);
		var dataVarEleInfo = node_createContextElementInfo("data", data);
		var elementInfosArray = [dataVarEleInfo];
		that.context = node_createContext("id", elementInfosArray, request);
		
		that.context.getContextElement("data").registerDataOperationEventListener(undefined, function(event, eventData, request){
			that.data = eventData.value;
			loc_dataUpdated(that);
//			that.$emit("dataChange", eventData.value);
		}, this);

		var request = node_uiNodeViewFactory.getCreateUINodeViewRequest([uiNode], "", that.context, uiTagInfo[node_COMMONATRIBUTECONSTANT.UITAGINFO_MATCHERS], {
			success : function(request, uiNodeViewGroup){
				that.uiNodeView = uiNodeViewGroup;
				uiNodeViewGroup.appendTo(that.$refs.uiTag);
			}
		});

		node_requestServiceProcessor.processRequest(request);
	};
	
	var loc_dataUpdated = function(that){
		var request = that.context.getUpdateContextRequest({
			"data":that.data
		}, handlers, requestInfo);
		node_requestServiceProcessor.processRequest(request);
	};
	
	var loc_vueComponent = {
		data : function(){
			return {
				uiNodeView : {},
				context : {}
			};
		},
		props : ['uitaginfo', 'data'],
		components : {
		},
		methods : {
		},
		watch : {
			uitaginfo : function(){
				loc_updateTagUI();
			}
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
nosliw.registerSetNodeDataEvent("uidata.data.entity.createData", function(){node_createData = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentUITagData", node_createComponentUITagData); 

})(packageObj);
