//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	var node_createConfigure;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleConfigure = function(settingName, parms){
	
	var moduleConfigure = node_createConfigure(loc_moduleSetting, loc_globalConfig, parms).getChildConfigure(undefined, settingName);
	return moduleConfigure;
	
	
//	var moduleSetting = loc_moduleSetting[settingName];
//	var modulesDecorationInfoArray = loc_createTypicalModuleDecorationInfoArray(moduleSetting.moduleDecoration, parms);
//	return loc_createTypicalModuleConfigure(rootView, storeService, moduleSetting.uiDecorations, modulesDecorationInfoArray);
};


var loc_globalConfig = {
	__storeService : function(parms){
		return parms.storeService;
	}
};
	
//predefined module setting by name
//each setting include ui decoration and module decoration 
var loc_moduleSetting = {
	
	share : {
		root : function(parms){
			return parms.rootView;
		}
	},
	
	parts : {
		test : {
			uiDecoration : {},
			moduleDecoration : 
			{
				parts : [
					{
						id: 'base',
					},
				]
			},
		},
		setting : {
			uiDecoration : {},
			moduleDecoration : 
			{
				parts : [
					{
						id: 'base',
					},
					{
						id: 'uidecoration',
					},
					{
						id: 'setting_framework7_mobile',
						uiResource : 'Decoration_setting_framework7',
					},
				]
			},
		},
		
		application : {
			uiDecoration : {
				parts : [
					{
						id: 'Decoration_application_framework7'
					}
				]
			},
			moduleDecoration : 
			{
				parts : [
					{
						id: 'base',
					},
					{
						id: 'uidecoration',
					},
					{
						id: 'application_framework7_mobile',
						app : function(parms){
							return parms.framework7App;
						}
					}
				]
			},
		}
	}
	

};	

//generate module configure
var loc_createTypicalModuleConfigure = function(rootView, storeService, uiDecorations, modulesDecorationInfo){

	//build module decoration 
	var moduleDecorations = [];
	moduleDecorations.push("base");
	moduleDecorations.push("uidecoration");
	_.each(modulesDecorationInfo, function(moduleDecorationInfo, i){
		moduleDecorations.push(moduleDecorationInfo.name);
		partsConfigure[moduleDecorationInfo.name] = moduleDecorationInfo.configure;
	});
	
	var partsConfigure = {};
	_.each(modulesDecorationInfo, function(moduleDecorationInfo, i){
		moduleDecorations.push(moduleDecorationInfo.name);
		partsConfigure[moduleDecorationInfo.name] = moduleDecorationInfo.configure;
	});
	
	var configure = {
		share : {
			root : rootView,
			decoration : uiDecorations,
			moduleDecoration : moduleDecorations,
			__storeService : storeService
		},
		parts : partsConfigure 
	};
	
	var globalConfig = {
		root : rootView,
		__storeService : storeService
	};
	
	return configure;
};

//create module decoration info
var loc_createTypicalModuleDecorationInfoArray = function(decorationSettingArray, parms){
	var out = [];
	_.each(decorationSettingArray, function(decorationSetting, i){
		if(decorationSetting.configure!=undefined){
			//build configure for decoration
			var configure = {};
			_.each(decorationSetting.configure, function(configureItem, name){
				if(typeof configureItem === "function"){
					configure[name] = configureItem(parms);
				}
				else  configure[name] = configureItem;
			});
		}
		out.push(new node_DecorationInfo(decorationSetting.id, decorationSetting.name, configure));
	});
	return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.DecorationInfo", function(){node_DecorationInfo = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});


//Register Node by Name
packageObj.createChildNode("createModuleConfigure", node_createModuleConfigure); 

})(packageObj);
