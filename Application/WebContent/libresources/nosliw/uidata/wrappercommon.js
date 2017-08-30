//get/create package
var packageObj = library.getChildPackage("wrapper");    

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

	
//*******************************************   Start Node Definition  ************************************** 	
/**
 * 
 */
var node_createWraperCommon = function(data, path, request){
	//sync task name for remote call 
	var loc_moduleName = "wrapper";

	/*
	 * mark data as invalid so that it would be recalculated
	 * also trigue event to inform that data need to be updated
	 */
	var loc_invalidateData = function(requestInfo){
		loc_out.pri_validValue = false;
		loc_out.pri_value = undefined;
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(obj, path, request){
		
		//if true, this wrapper is based on root data, otherwise, this wrapper is based on parent wrapper, 
		loc_out.pri_dataBased = true;
		//root data used when data based
		loc_out.pri_rootData = undefined;
		//parent wrapper used when wraper based
		loc_out.pri_parent = undefined;
		//the path from basis (data or wraper) to current data
		loc_out.pri_path = node_basicUtility.emptyStringIfUndefined(path);
		
		//event and listener for data operation event
		loc_out.pri_dataOperationEventObject = node_createEventObject();

		//calculated: the value this wrapper represented
		loc_out.pri_validValue = false;
		loc_out.pri_value = undefined;
		
		//whether data based or wrapper based
		if(node_getObjectType(obj)==node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER){
			//wrapper based
			loc_out.pri_parent = obj;
			loc_out.pri_dataBased = false;
		}
		else{
			//data based
			loc_out.pri_rootData = node_dataUtility.createDataByObject(obj);
			loc_out.pri_dataBased = true;
		}
		
		if(loc_out.pri_dataBased==false){
			//if parent based, then listen to parent's event
			loc_out.pri_parent.registerDataOperationListener(this.pri_dataOperationEventObject, function(event, path, opValue, requestInfo){
				
				if(event==node_CONSTANT.WRAPPER_EVENT_FORWARD){
					//for forward event, expand it
					event = opValue.event;
					path = opValue.path;
					opValue = opValue.operationValue;
				}
				
				if(event==node_CONSTANT.WRAPPER_EVENT_CHANGE){
					//for change event from parent, just make data invalid & forward the event, 
					loc_invalidateData(requestInfo);
					loc_out.pri_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, "", {}, requestInfo);
				}
				else{
					var k = -1;
					if(loc_out.pri_path==path){
						//event happens on this wrapper, trigue the same
						//inform the change of wrapper
						if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
							loc_out.destroy();
						}
						else{
							loc_invalidateData(requestInfo);
							loc_out.pri_trigueDataOperationEvent(event, "", opValue, requestInfo);
						}
					}
					else if(loc_out.pri_path.startsWith(path+".") || node_basicUtility.isStringEmpty(path)){
						//something happens in the middle between parent and this
						if(event==node_CONSTANT.WRAPPER_EVENT_SET){
							loc_invalidateData(requestInfo);
							loc_out.pri_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_CHANGE, "", {}, requestInfo);
						}
						else if(event==node_CONSTANT.WRAPPER_EVENT_DELETE){
							loc_out.destroy();
						}
					}
					else if(path.startsWith(loc_out.pri_path+".")){
						//something happens beyond this, just forward the event with rest path, only set event
						loc_out.pri_triggerForwardEvent(event, path.substring(loc_out.pri_path.length+1), opValue, requestInfo);
					}
					else if(node_basicUtility.isStringEmpty(loc_out.pri_path)){
						//something happens beyond this, just forward the event with rest path, only set event
						loc_out.pri_triggerForwardEvent(event, path, opValue, requestInfo);
					}
					else{
						//not on right path, do nothing
					}
				}
			}, this);
		}
	};

	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		//for delete event, it means itself and all children should be destroy
		loc_invalidateData();
		//forward the event
		loc_out.pri_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_DELETE, "", {}, requestInfo);
		//clear up event source
		loc_out.pri_dataOperationEventObject.clearup();
		
		loc_out.pri_parent = undefined;
		loc_out.pri_rootData = undefined;
		loc_out.pri_path = undefined;
	};
	
	
	var loc_out = {
			pri_getPath : function(){  return this.pri_path;  },
			pri_getParent : function(){  return this.pri_parent; },
			pri_getRootData : function(){  return this.pri_rootData; },
			pri_isDataBased : function(){  return this.pri_dataBased; },
			pri_setValue : function(value){ this.pri_value = value;},
			pri_triggerForwardEvent : function(event, path, opValue, requestInfo){
				var eData = {
						event : event, 
						path : path, 
						operationValue : opValue,
				};
				this.pri_trigueDataOperationEvent(node_CONSTANT.WRAPPER_EVENT_FORWARD, "", eData, requestInfo);
			},
			
			pri_trigueDataOperationEvent : function(event, path, operationValue, requestInfo){this.pri_dataOperationEventObject.triggerEvent(event, path, operationValue, requestInfo);},

			ovr_calValue : function(){},
			
			isDataBased : function(){  return this.pri_dataBased; },
			
			getPath : function(){ return this.pri_path;  },
			
			getData : function(){
				var value = this.getValue();
				return node_dataUtility.createDataByValue(object, value);
			},
			
			getValue : function(){
				if(this.pri_validValue==false){
					this.ovr_calValue();
				}
				return this.pri_value;
			},
			
			getDataType : function(){ return this.getRootData().dataTypeInfo; },

			getFullPath : function(){
				if(loc_out.pri_dataBased==true)   return loc_out.pri_path;
				return node_namingConvensionUtility.cascadePath(loc_out.pri_parent.getFullPath(), loc_out.pri_path);
			},
			
			getRootWrapper : function(){
				if(loc_out.pri_dataBased==true)   return this;
				return loc_out.pri_parent.getRootWrapper();
			},
			
			getRootData : function(){
				return this.getRootWrapper().pri_getRootData();
			},

			/*
			 * handler : function (event, path, operationValue, requestInfo)
			 */
			registerDataOperationListener : function(listenerEventObj, handler, thisContext){
				this.pri_dataOperationEventObject.registerListener(undefined, listenerEventObj, handler, thisContext);
			},

			unregisterDataOperationListener : function(listenerEventObj){
				this.pri_dataOperationEventObject.unregister(listenerEventObj);
			},
			
			createChildWrapper : function(path, request){		return wrapperFactory.createWrapper(this, path, request);		},

			getWrapperType : function(){},
			getChildData : function(path){},
			requestDataOperation : function(service, request){},
			handleEachElement : function(handler, thatContext){	},
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_WRAPPER);
	
	loc_out = node_makeObjectWithId(loc_out, nosliw.generateId());
	
	node_getLifecycleInterface(loc_out).init(data, path, request);
	
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
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createWraperCommon", node_createWraperCommon); 

})(packageObj);
