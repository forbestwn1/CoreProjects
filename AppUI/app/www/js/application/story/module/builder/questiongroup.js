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
		props : ['question', 'story'],
		methods : {
			onAnswerChange : function(event) {
				this.$emit("answerChange");
			},
		},
		template : `
			<div>
				<br>
				Group Start: {{question.question}}
				<br>
				<div 
					v-for="question in question.children"
				>
					<question-group v-if="question.type=='group'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
					<question-item v-if="question.type=='item'" v-bind:question="question" v-bind:story="story" v-on:answerChange="onAnswerChange"/>
				</div>
				<br>
				Group End: {{question.question}}
				<br>
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
