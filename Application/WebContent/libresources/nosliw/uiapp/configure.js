//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	
//*******************************************   Start Node Definition  ************************************** 	

	
var node_createModuleConfigure = function(settingName, parms){
	
	var appConfigure = node_createConfigure(loc_appSetting, loc_globalConfig, parms).getChildConfigure(undefined, settingName);
	return appConfigure;
	
};

var loc_globalConfig = {
	__appDataService : function(parms){
		return parms.dataService;
	},
	__storeService : function(parms){
		return parms.storeService;
	}
};


var loc_appSetting = {
	global : {
		appDecoration : [
			{
				coreFun: node_createAppDecoration,
				id : "application"
			}
			],
		__appDataService : function(parms){
			return parms.dataService;
		},
		__storeService : function(parms){
			return parms.storeService;
		},
		app : function(parms){
			return parms.framework7App;
		},
	},
	parts : {
		application : {
			"parts" : {
				application : {
					"root" : function(parms){
						return parms.mainModuleRoot;
					},
					uiDecoration : [
						{
							id: 'Decoration_application_header_framework7'
						}
					],
					moduleDecoration : 
					{
						parts : [
							{
								id: 'base',
							},
							{
								id: 'application_framework7_mobile',
								app : function(parms){
									return parms.framework7App;
								},
								uiResource : {
									container : {
										id : "Decoration_application_container_framework7"
									}
								}
							}
						],
					},
				},
				"setting" : {
					"root" : function(parms){
						return parms.settingModuleRoot;
					},
					"uiDecoration" : [
						{
							id: 'Decoration_setting_framework7'
						}
					],
					"moduleDecoration" : 
					{
						parts : [
							{
								id: 'base',
							},
							{
								id: 'setting_framework7_mobile',
							},
						]
					},
				},
			}
		}
	}
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleConfigure", node_createModuleConfigure); 

})(packageObj);
