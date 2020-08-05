/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_createComponentQuestionItemService;
	var node_createComponentQuestionItemSwitch;
	var node_createComponentQuestionItemConstant;
	var node_createComponentQuestionItemUIData;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionItem = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data', 'story'],
		components : {
			'question-item-service': node_createComponentQuestionItemService(),
			'question-item-switch': node_createComponentQuestionItemSwitch(),
			'question-item-constant': node_createComponentQuestionItemConstant(),
			'question-item-uidata': node_createComponentQuestionItemUIData(),
		},
		methods : {
		},
		template : `
			<div v-if="data.element.enable==true">
				<br>
				Item Start {{data.targetId}} : {{data.question}}
				<br>
				<question-item-service v-if="data.element.type=='service'" v-bind:data="data" v-bind:story="story"/>
				<question-item-switch v-if="data.element.type=='switch'" v-bind:data="data" v-bind:story="story"/>
				<question-item-constant v-if="data.element.type=='constant'" v-bind:data="data" v-bind:story="story"/>
				<question-item-uidata v-if="data.element.type=='UI_data'" v-bind:data="data" v-bind:story="story"/>
				<br>
				Item End {{data.targetId}} : {{data.question}}
				<br>
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemService", function(){node_createComponentQuestionItemService = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemSwitch", function(){node_createComponentQuestionItemSwitch = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemConstant", function(){node_createComponentQuestionItemConstant = this.getData();});
nosliw.registerSetNodeDataEvent("application.story.module.builder.createComponentQuestionItemUIData", function(){node_createComponentQuestionItemUIData = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionItem", node_createComponentQuestionItem); 

})(packageObj);
