//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createAppDecoration;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createTypicalConfigure = function(mainModuleRoot, settingModuleRoot, dataService, storeService, framework7App){
	var configure = {
		global : {
			appDecoration : [
				{
					coreFun: node_createAppDecoration,
					id : "application"
				}
				],
			__appDataService : dataService,
			__storeService : storeService,
			app : framework7App,
		},
		parts : {
			application : {
				"parts" : {
					"application" : {
						global : {
							"root" : mainModuleRoot,
							"decoration" : {
								global : ["Decoration_application_framework7"]
							},
							"moduleDecoration" : ["base", "uidecoration", "application_framework7_mobile"]
//							"moduleDecoration" : ["base", "uidecoration", "application_framework7_mobile", "debug"]
						}
					},
					"setting" : {
						global : {
							"root" : settingModuleRoot,
							"decoration" : {
							},
							"moduleDecoration" : ["base", "uidecoration", "setting_framework7_mobile"]
//							"moduleDecoration" : ["base", "uidecoration", "setting_framework7_mobile", "debug"]
						},
						"parts" : {
							"setting_framework7_mobile" : {
								uiResource : "Decoration_setting_framework7"
							}
						}
					}
				}
			}
		}
	};
	return configure;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDecoration", function(){node_createAppDecoration = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTypicalConfigure", node_createTypicalConfigure); 

})(packageObj);
