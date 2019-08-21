//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleConfigure = function(rootView, storeService, settingName, parms){
	var moduleSetting = loc_moduleSetting[settingName];
	var modulesDecorationInfoArray = loc_createTypicalModuleDecorationInfoArray(moduleSetting.moduleDecoration, parms);
	return loc_createTypicalModuleConfigure(rootView, storeService, moduleSetting.uiDecorations, modulesDecorationInfoArray);
};
	
//predefined module setting by name
//each setting include ui decoration and module decoration 
var loc_moduleSetting = {
	setting : {
		uiDecorations : ['Decoration_setting_framework7'],
		moduleDecoration : ['setting_framework7_mobile'],
	},
	
	application : {
		uiDecorations : ['Decoration_application_framework7'],
		moduleDecoration : ['application_framework7_mobile'],
	}

};	

//generate module configure
var loc_createTypicalModuleConfigure = function(rootView, storeService, uiDecorations, modulesDecorationInfo){

	var moduleDecorations = [];
	moduleDecorations.push("base");
	moduleDecorations.push("uidecoration");
	
	var partsConfigure = {};
	
	_.each(modulesDecorationInfo, function(moduleDecorationInfo, i){
		moduleDecorations.push(moduleDecorationInfo.name);
		partsConfigure[moduleDecorationInfo.name] = moduleDecorationInfo.configure;
	});
	
	var configure = {
		global : {
			root : rootView,
			decoration : uiDecorations,
			moduleDecoration : moduleDecorations,
			__storeService : storeService
		},
		parts : partsConfigure 
	};
	return configure;
};

//create module decoration info according to decoration name
var loc_createTypicalModuleDecorationInfo = function(decorationName, parms){
	var configure;
	if(decorationName=="setting_framework7_mobile"){
		configure = {
				app : parms.framework7App
		};
	}
	else if(decorationName=="application_framework7_mobile"){
		configure = {
			app : parms.framework7App
		};
	}
	return new node_DecorationInfo(decorationName, configure);
};

var loc_createTypicalModuleDecorationInfoArray = function(decorationNameArray, parms){
	var out = [];
	_.each(decorationNameArray, function(decorationName, i){
		out.push(loc_createTypicalModuleDecorationInfo(decorationName, parms));
	});
	return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.DecorationInfo", function(){node_DecorationInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleConfigure", node_createModuleConfigure); 

})(packageObj);
