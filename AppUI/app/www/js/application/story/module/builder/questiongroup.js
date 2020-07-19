/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionGroup = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['data'],
		methods : {
		},
		template : `
			<div>
				{{data.question}}
				<div 
					v-for="question in data.children"
					v-bind:key="children.id"
					v-bind:data="question"
				>
					<question-group v-if="question.type=='group'" v-bind:data="question"/>
					<question-item v-if="question.type=='item'" v-bind:data="question"/>
				</div>
			</div>
		`
	};
	return loc_vueComponent;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentQuestionGroup", node_createComponentQuestionGroup); 

})(packageObj);
