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
	
//*******************************************   Start Node Definition  ************************************** 	
var node_createWraperObject = function(){
	
	var loc_getOperationObject = function(obj){
		var opObject = obj;
		if(nosliwTypedObjectUtility.getObjectType(obj)==NOSLIWCONSTANT.TYPEDOBJECT_TYPE_DATA){
			var dataCategary = obj.dataTypeInfo.categary;
			if(dataCategary==NOSLIWCOMMONCONSTANT.CONS_DATATYPE_CATEGARY_OBJECT){
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
			pri_triggerOperationEvent : function(event, path, opData, request){
				var rootWrapper = this.getRootWrapper();
				var rootPath = rootWrapper.getPath();
				if(rootPath==undefined)  rootPath = "";
				var fullPath = nosliwNamingConversionUtility.cascadePath(this.getFullPath(), path);
				if(rootPath==fullPath){
					//on current
					rootWrapper.pri_trigueDataOperationEvent(event, fullPath, opData, request);
				}
				else{
					//on child
					rootWrapper.pri_triggerForwardEvent(event, fullPath, opData, request);
				}
			},

			/*
			 * calculate current object
			 */
			ovr_calObject : function(){
				var baseObject = undefined;
				if(this.pri_isDataBased()==false){
					baseObject = this.pri_getParent().getObject();
				}
				else{
					baseObject = this.pri_getRootData().value;			
				}
				this.pri_setObject(nosliwObjectUtility.getObjectAttributeByPath(baseObject, this.pri_getPath()));
				loc_validData = true;
			},
			
			getChildData : function(path){
				var object = nosliwObjectUtility.getObjectAttributeByPath(this.getObject(), path);
				var data = nosliwDataUtility.createDataByObject(object);
				return data;
			},
			
			requestDataOperation : function(service, request){
				var command = service.command;
				var serviceData = service.parms;
				var path = serviceData.path;
				
				var rootData = this.getRootData();
				var fullPath = nosliwNamingConversionUtility.cascadePath(this.getFullPath(), path);

				var opObject = rootData;
				var opPath = nosliwNamingConversionUtility.cascadePath("value", fullPath);
				if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_SET){
					var opData = loc_getOperationObject(serviceData.data);
					//change value
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_SET, opData);
					//trigue event
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_SET, path, opData, request);
				}
				else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT){
					var opData = loc_getOperationObject(serviceData.data);
					var index = serviceData.index;
					var operationData = {
						data : opData,
						index : index,
					};
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_ADDELEMENT, operationData);
					//trigue event
					if(path==undefined)  path="";
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_ADDELEMENT, path, operationData, request);
				}
				else if(command==NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT){
					var index = serviceData.index; 
					nosliwObjectUtility.operateObject(opObject, opPath, NOSLIWCONSTANT.WRAPPER_OPERATION_DELETEELEMENT, index);
					//trigue event
					this.pri_triggerOperationEvent(NOSLIWCONSTANT.WRAPPER_EVENT_DELETE, nosliwNamingConversionUtility.cascadePath(path, index), {}, request);
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
nosliw.registerSetNodeDataEvent("uidata.data.utility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){node_wrapperFactory = this.getData();});

nosliw.registerSetNodeDataEvent("uidata.wrapper.wrapperFactory", function(){
	//register wrapper faction
	this.getData().registerWrapperFactoryByDataType("", node_createWraperObject);
});


//Register Node by Name
packageObj.createChildNode("node_createWraperObject", node_createWraperObject); 

})(packageObj);
