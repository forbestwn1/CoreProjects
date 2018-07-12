//get/create package
var packageObj = library.getChildPackage("lifecycle");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_eventUtility;
	var node_getObjectName;
	var node_getObjectType;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;

//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "lifecycle";
	
/*
 * utility functions to build lifecycle object
 */
var node_makeObjectWithLifecycle = function(baseObject, lifecycleCallback, thisContext){
	return node_buildInterface(baseObject, INTERFACENAME, loc_createResourceLifecycle(thisContext==undefined?baseObject:thisContext, lifecycleCallback));
};
	
var node_getLifecycleInterface = function(baseObject){
	return node_getInterface(baseObject, INTERFACENAME);
};


/**
 * create resource lifecycle object which provide basic lifecycle method and status
 * all the thisContext for life cycle method is either loc_thisContext or this
 * the transition of status can be monitored by register event listener, so that when status transition is done, the listener will be informed
 */
var loc_createResourceLifecycle = function(thisContext, lifecycleCallback){
	//this context for lifycycle callback method
	var loc_thisContext = thisContext;
	
	//name for this lifecycle object, it can be used in logging
//	var loc_name = name;
	//status: start with START status
	var loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
	//wether lifecycle object is under transit
	var loc_inTransit = false;	

	//life cycle call back including all call back method
	var loc_lifecycleCallback = lifecycleCallback==undefined? {}:lifecycleCallback;
	
	/*
	 * get this context
	 */
	var loc_getThisContext = function(){
		return loc_thisContext;
	};

	/*
	 * method called when transition is finished
	 */
	var loc_finishStatusTransition = function(oldStatus, status){
		loc_inTransit = false;
		node_eventUtility.triggerEvent(loc_out.getBaseObject(), node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION, {
			oldStatus : oldStatus,
			newStatus : status,
		});
	};
	
	//method for init event
	var loc_onResourceInit = function(args, handlers, requestInfo){
		//if resource has been inited, then just do nothing
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT];
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, args);
		
		return loc_processStatuesResult(initResult, loc_out.finishInit, handlers, requestInfo);
		
		//if return false, then waiting until finishInit is called
//		if(initResult!=false)		loc_out.finishInit();
	};

	
	var loc_processStatuesResult = function(result, switchFun, handlers, requestInfo){
		if(result==true || result==undefined){
			//success finish
			switchFun.call(loc_out);
			return true;
		}
		if(result==false)   return  false;           //not finish, wait for finish method get called
		
		var entityType = node_getObjectType(result);
		if(node_CONSTANT.TYPEDOBJECT_TYPE_REQUEST==entityType){
			//if return request, then build wrapper request
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ProcessLifecycleResult", {}), handlers, requestInfo);
			out.addRequest(result);
			out.addRequest(node_createServiceRequestInfoSimple(new node_ServiceInfo("LifecycleStateTransit", {}), 
				function(requestInfo){
					switchFun.call(loc_out);
				}));
			return out;
		}
		//fallback: not switch status
		return false;
	};
	
	//method for init event
	var loc_onResourceDeactive = function(){
		//if resource is START OR DEAD, then just do nothing
		if(loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START || loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		//if in transit, do nothing
		if(loc_inTransit==true)  return;
		
		loc_inTransit = true;
		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDeactive is called
		if(initResult!=false)		loc_out.finishDeactive();
	};
	
	//method for destroy method
	var loc_onResourceDestroy = function(){
		if(loc_status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY];
		
		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishDestroy is called
		if(initResult!=false)		loc_out.finishDestroy();
	};

	//method for suspend method
	var loc_onResourceSuspend = function(){
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishSuspend is called
		if(initResult!=false)		loc_out.finishSuspend();
	};

	//method for resume method
	var loc_onResourceResume = function(){
		if(loc_status!=node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED)   return;
		if(loc_inTransit==true)  return;

		var that = loc_getThisContext();
		var fun = loc_lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME];

		var initResult = true;      
		if(fun!=undefined)	initResult = fun.apply(that, arguments);
		//if return false, then waiting until finishResume is called
		if(initResult!=false)		loc_out.finishResume();
	};
	
	var loc_out = {
		init : function(){	loc_onResourceInit.apply(this, arguments);	},

		//must have handlers and requestInfo as last two parms
		initRequest : function(){	
			return loc_onResourceInit.apply(this, [arguments, 
				node_requestUtility.getHandlersFromFunctionArguments(arguments), 
				node_requestUtility.getRequestInfoFromFunctionArguments(arguments)]);	
		},
		
		finishInit : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		destroy : function(){
			loc_onResourceDestroy.apply(this, arguments);
		},
		
		finishDestroy : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_DEAD;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		refresh : function(){
			var oldStatus = this.getResourceStatus();
			
			var listener = {};
			var that = this;
			this.registerEventListener(listener, function(event, status){
				if(status==oldStatus){
					that.unregisterEventListener(listener);
				}
				else{
					if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START){
						that.init();
					}
					else if(status==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE){
						if(oldStatus==node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED){
							that.suspend();
						}
					}
				}
			});
			
			loc_onResourceDeactive.apply(this, arguments);
		},
		
		finishDeactive : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_START;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		suspend : function(){
			loc_onResourceSuspend.apply(this, arguments);
		},
		
		finishSuspend : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_SUSPENDED;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		resume : function(){
			loc_onResourceResume.apply(this, arguments);
		},
		
		finishResume : function(){
			var oldStatus = loc_status;
			loc_status = node_CONSTANT.LIFECYCLE_RESOURCE_STATUS_ACTIVE;
			loc_finishStatusTransition(oldStatus, loc_status);
		},
		
		getResourceStatus : function(){return loc_status;},

		registerEventListener : function(listener, handler){
			node_eventUtility.registerListener(listener, loc_out.getBaseObject(), node_CONSTANT.EVENT_EVENTNAME_ALL, handler);
		},

		unregisterEventListener : function(listener){
			node_eventUtility.unregister(listener);
		},
		
		//callback method when this interface is connect to baseObject
		bindBaseObject : function(baseObject){
			//listener to status transit event by itself
			loc_out.registerEventListener(baseObject, function(event, data){
				//logging ths status transit
				//only for module with name

				var name = node_getObjectName(baseObject); 
				if(name!=null){
					nosliw.logging.info(name, "status transit from " + data.oldStatus + " to " + data.newStatus);
				}
			});
		}
	};

	loc_constructor = function(){
	};
	
	loc_constructor();
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.getObjectName", function(){node_getObjectName = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});

//Register Node by Name
packageObj.createChildNode("getLifecycleInterface", node_getLifecycleInterface); 
packageObj.createChildNode("makeObjectWithLifecycle", node_makeObjectWithLifecycle); 

})(packageObj);
