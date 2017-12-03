//get/create package
var packageObj = library.getChildPackage("wrapper.object");    

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
var node_objectWrapperUtility;	
//*******************************************   Start Node Definition  ************************************** 	
var node_createWraperObject = function(){
	
	var loc_getOperationObject = function(obj){
		var opObject = obj;
		if(node_getObjectType(obj)==node_CONSTANT.TYPEDOBJECT_TYPE_DATA){
			if(obj.dataTypeInfo==node_CONSTANT.DATA_TYPE_OBJECT){
				//if operation data is object, then use value
				opObject = obj.value;
			}
		}
		return opObject;
	};
	
	var loc_out = {		
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
			
			requestDataOperation : function(service, request){
				var command = service.command;
				var serviceData = service.parms;
				var path = serviceData.path;
				
				var rootValue = this.getRootData().value;
				var fullPath = node_namingConvensionUtility.cascadePath(this.getFullPath(), path);

				var opPath = fullPath;   //node_namingConvensionUtility.cascadePath("value", fullPath);
				if(command==node_CONSTANT.WRAPPER_OPERATION_SET){
					var opValue = loc_getOperationObject(serviceData.data);
					//change value
					if(_.isObject(rootValue)){
						node_objectWrapperUtility.operateObject(rootValue, opPath, node_CONSTANT.WRAPPER_OPERATION_SET, opValue);
					}
					else{
						this.getRootData().value = opValue;
						this.getRootWrapper().pri_invalidateData(request);
					}
					//trigue event
					this.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_SET, path, opValue, request);
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					var opValue = loc_getOperationObject(serviceData.data);
					var index = serviceData.index;
					var operationData = {
						data : opValue,
						index : index,
					};
					node_objectWrapperUtility.operateObject(rootValue, opPath, node_CONSTANT.WRAPPER_OPERATION_ADDELEMENT, operationData);
					//trigue event
					if(path==undefined)  path="";
					this.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_ADDELEMENT, path, operationData, request);
				}
				else if(command==node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					var index = serviceData.index; 
					node_objectWrapperUtility.operateObject(rootValue, opPath, node_CONSTANT.WRAPPER_OPERATION_DELETEELEMENT, index);
					//trigue event
					this.pri_triggerOperationEvent(node_CONSTANT.WRAPPER_EVENT_DESTROY, node_namingConvensionUtility.cascadePath(path, index), {}, request);
				}
			},

			handleEachElement : function(handler, thatContext){	
				var containerData = this.getData();
				_.each(containerData.value, function(data, key, list){
					var child = this.createChildWrapper(key);
			    	handler.call(thatContext, child, key);
				}, this);
			},
			
			getWrapperType : function(){	return NOSLIWCONSTANT.WRAPPER_TYPE_OBJECT;		},
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
nosliw.registerSetNodeDataEvent("uidata.wrapper.object.utility", function(){node_objectWrapperUtility = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType([node_CONSTANT.DATA_TYPE_OBJECT], node_createWraperObject);
});

//Register Node by Name
packageObj.createChildNode("createWraperObject", node_createWraperObject); 

})(packageObj);
