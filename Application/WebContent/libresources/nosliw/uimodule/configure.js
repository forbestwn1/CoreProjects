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
			uiDecoration : [
				{
					id: 'Decoration_setting_framework7'
				}
			],
			moduleDecoration : 
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
		setting : {
			uiDecoration : {},
			moduleDecoration : 
			{
				parts : [
					{
						id: 'base',
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
