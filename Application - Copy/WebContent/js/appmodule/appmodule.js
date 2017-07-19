/*
 * 
 */
var nosliwCreateAppModule = function(name, parms, parent, extendObj){

	var id = Nosliw.generateId();
	var loc_name = name;
	var loc_data = {};
	var loc_subAppModules = {};
	
	var loc_parms = {};
	if(loc_parms!=undefined)    loc_parms = parms;
	
	
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
		var out = loc_out.ovr_destroy();
		_.each(loc_subAppModules, function(key, module){
			if(module.destroy()==false)  out = false;
		});
		return out;
	};
	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
		return loc_out.ovr_init();
	};

	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){
		return loc_out.ovr_suspend();
	};

	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){
		return loc_out.ovr_resume();
	};

	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){
		return loc_out.ovr_deactive();
	};
	
	var loc_out = {
			pri_finishInit : function(){loc_out.finishInit();},
			pri_finishDestroy : function(){loc_out.finishDestroy();},
			pri_finishSuspend : function(){loc_out.finishSuspend();},
			pri_finishResume : function(){loc_out.finishResume();},
			pri_finishDeactive : function(){loc_out.finishDeactive();},

			pri_addAppModule : function(subModule){
				loc_subAppModules[loc_subAppModules.getName()] = subModule;
			},
			
			pri_removeAppModule : function(name){
				delete this.subAppMoudules[name];
			},
			
			
			ovr_doCommand : function(name, data){},
			
			ovr_init : function(){},
			ovr_destroy : function(){},
			ovr_resume : function(){},
			ovr_suspend : function(){},
			ovr_deactive : function(){},

			
			ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

			getName : function(){  return loc_name;  },
			
			getParm : function(name) { return  loc_parms[name];},
			
			getData : function(name){
				if(loc_data==undefined)	loc_data = {};
				return loc_data[name];
			},

			setData : function(name, data){
				if(loc_data==undefined)	loc_data = {};
				loc_data[name] = data;
			},
			
			clearData : function(name){	delete loc_data[name];	},
	
			command : function(name, data){	this.ovr_doCommand(name, data);	},

			trigueEvent : function(eventName, data, requestInfo){  nosliwEventUtility.triggerEvent(this, eventName, data, requestInfo);	},
			
			registerEvent : function(handler, thisContext){	return	loc_eventSource.registerEventHandler(handler, thisContext);		},
			
	};

	_.extend(loc_out, extendObj);

	//append resource and object life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
	loc_out = nosliwTypedObjectUtility.makeTypedObject(loc_out, NOSLIWCONSTANT.TYPEDOBJECT_TYPE_APPMODULE);

	return loc_out;
};
