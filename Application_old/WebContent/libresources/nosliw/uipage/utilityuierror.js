//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_createServiceRequestInfoSequence;
	var node_resourceUtility;
	var node_buildServiceProvider;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createContextElementInfo;
	var node_dataUtility;
	var node_createContext;
	var node_createContextVariableInfo;
	var node_createUIDataOperationRequest;
	var createServiceRequestInfoSet;
	var node_UIDataOperation;
	var node_uiDataOperationServiceUtility;
	var node_requestUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_utilityUIError = function(){

	var loc_out = {
			
		getClearUIValidationErrorRequest : function(uiView, handlers, request){
			var affectedContexts = [];
			affectedContexts.push(uiView.getContext());
			
			var childTags = uiView.getTags(undefined, true);
			_.each(childTags, function(childTag){
				var uiViews = childTag.getChildUIViews();
				_.each(uiViews, function(uiView){
					affectedContexts.push(uiView.getContext());
				});
			});
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			_.each(affectedContexts, function(context){
				out.addRequest(node_createUIDataOperationRequest(context, new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService("", {}))));
			});
			
			return out;
		},
		
		getUITagValidationRequest : function(uiTag, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(uiTag.getValidateDataRequest({
				success : function(request, message){
					if(message!=undefined&&message.length!=0){
						return node_createUIDataOperationRequest(uiTag.getParentContext(), new node_UIDataOperation(node_COMMONCONSTANT.UIRESOURCE_CONTEXTELEMENT_NAME_UIVALIDATIONERROR, node_uiDataOperationServiceUtility.createSetOperationService(uiTag.getId(), message)), {
							success : function(){
								return message;
							}
						}, request);
					}
					else return node_requestUtility.getEmptyRequest();
				}
			}));
			return out;
		},
		
		getUITagsValidationRequest : function(uiTags, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			var uiTagValidateRequestSet = node_createServiceRequestInfoSet(undefined, {
				success : function(requestInfo, validationsResult){
					var results = validationsResult.getResults();
					var allMessages = {};
					var errorOccur = false;
					_.each(results, function(message, uiTagId){
						if(message!=undefined&&message.length!=0){
							allMessages[uiTagId] = message;
							errorOccur = true;
						}
					});
					
					if(errorOccur)  return allMessages;
				},
			});
			_.each(uiTags, function(uiTag, i){
				uiTagValidateRequestSet.addRequest(uiTag.getId(), loc_out.getUITagValidationRequest(uiTag));
			});
			out.addRequest(uiTagValidateRequestSet);
			return out;
		},
			
	};
	
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextElementInfo", function(){node_createContextElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContext", function(){node_createContext = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.createContextVariableInfo", function(){node_createContextVariableInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.createUIDataOperationRequest", function(){node_createUIDataOperationRequest = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.UIDataOperation", function(){node_UIDataOperation = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.uidataoperation.uiDataOperationServiceUtility", function(){node_uiDataOperationServiceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utilityUIError", node_utilityUIError); 

})(packageObj);
