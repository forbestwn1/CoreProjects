//get/create package
var packageObj = library.getChildPackage("wrapper.appdata");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_createEventObject;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithId;
var node_basicUtility;
var node_dataUtility;
var node_wrapperFactory;
var node_namingConvensionUtility;
var node_appDataWrapperUtility;	
//*******************************************   Start Node Definition  ************************************** 	
var node_createWraperData = function(){
	
	var loc_dataOperation = function(dataTypeId, operation, parms){
		
	};
	
	var loc_out = {		
			
			getDataOperationRequest : function(operationService, handlers, requester_parent){
				var command = operationService.command;
				var serviceData = operationService.parms;
				var path = serviceData.path;
				
				var rootValue = this.getRootData().value;
				var fullPath = node_namingConvensionUtility.cascadePath(this.getFullPath(), path);

				var out;
				if(command==node_CONSTANT.WRAPPER_OPERATION_GET){
					out = node_appDataWrapperUtility.getGetChildAppDataRequest(rootValue, fullPath, handlers, requester_parent);
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
					out = node_appDataWrapperUtility.getSetChildAppDataRequest(rootValue, fullPath, serviceData.data, handlers, requester_parent);
					var that  = this;
					out.addPostProcessor({
						success : function(requestInfo, data){
							that.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_SET, path, serviceData.data, requestInfo);
						}
					});
				}
				return out;
			},

			
			/*
			 * 
			 */
			pri_triggerOperationEvent : function(event, path, opValue, request){
				var rootWrapper = this.getRootWrapper();
				var rootPath = rootWrapper.getPath();
				if(rootPath==undefined)  rootPath = "";
				var fullPath = node_namingConvensionUtility.cascadePath(this.getFullPath(), path);
				if(rootPath==fullPath){
					//on root wrapper
					rootWrapper.pri_trigueDataOperationEvent(event, fullPath, opValue, request);
				}
				else{
					//on child
					if(rootPath=="")  rootWrapper.pri_triggerForwardEvent(event, fullPath, opValue, request);
					else rootWrapper.pri_triggerForwardEvent(event, fullPath.substring(rootPath.length+1), opValue, request);
				}
			},

			
			
			getWrapperType : function(){	return node_CONSTANT.DATA_TYPE_APPDATA;		},
			
			

			
			
			
			
			
			
			
			
			
			getChildData : function(path){
				var object = node_objectWrapperUtility.getObjectAttributeByPath(this.getValue(), path);
				var data = node_dataUtility.createDataByObject(object);
				return data;
			},
		
			
			
			/*
			 * calculate current object
			 */
			ovr_calValue : function(){
				var baseValue = undefined;
				if(this.pri_isDataBased()==false){
					baseValue = this.pri_getParent().getValue();
				}
				else{
					baseValue = this.pri_getRootData().value;			
				}
				this.pri_setValue(node_objectWrapperUtility.getObjectAttributeByPath(baseValue, this.pri_getPath()));
				this.pri_validValue = true;
			},
			
			getChildData : function(path){
				var object = node_objectWrapperUtility.getObjectAttributeByPath(this.getValue(), path);
				var data = node_dataUtility.createDataByObject(object);
				return data;
			},
			
			handleEachElement : function(handler, thatContext){	
				var containerData = this.getData();
				_.each(containerData.value, function(data, key, list){
					var child = this.createChildWrapper(key);
			    	handler.call(thatContext, child, key);
				}, this);
			},
			
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.appdata.utility", function(){node_appDataWrapperUtility = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType([node_CONSTANT.DATA_TYPE_APPDATA], node_createWraperData);
});


//Register Node by Name
packageObj.createChildNode("createWraperData", node_createWraperData); 

})(packageObj);
