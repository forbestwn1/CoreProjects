/**
 * 
 */
//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentQuestionStep = function(){

	var loc_vueComponent = {
		data : function(){
			return {};
		},
		props : ['question', 'story', 'stages', 'currentStage', 'errorMessages'],
		components : {
		},
		computed: {
			stageIndex : {
				get : function(){
					var that = this;
					var index;
					_.each(this.stages, function(stage, i){
						if(stage.name==that.currentStage)  index=i;
					});
					return index;
				},
			}
		},
		methods : {
			onPrevious : function(event) {
				this.$emit("previousStep");
			},
			onNext : function(event) {
				this.$emit("nextStep");
			},
			onFinish : function(event) {
				this.$emit("finishStep");
			},
			onAnswerChange : function(event) {
				this.$emit("answerChange");
			},
		},
		template : `
			<div>
		    	<div>
				  <span v-for="errorMessage in errorMessages" style="color:red;"><br>{{errorMessage}}
				  </span>
		    	</div>

		    	<div>
		    	  ---&nbsp;&nbsp;
				  <span v-for="stage in stages">
    					<span v-if="stage.name==currentStage" style="color:red;">{{stage.name}}</span>
    					<span v-if="stage.name!=currentStage">{{stage.name}}</span>
    					&nbsp;&nbsp;---&nbsp;&nbsp;
				  </span>
		    	</div>
				    
				<br>
				<question-group v-bind:question="question" v-if="question!=undefined" v-bind:story="story"  v-on:answerChange="onAnswerChange">
				</question-group>

				<br>
				<span v-if="stageIndex>=1"><a v-on:click.prevent="onPrevious">Previous</a>&nbsp;&nbsp;</span>
				<span v-if="stageIndex<stages.length-2"><a v-on:click.prevent="onNext">Next</a>&nbsp;&nbsp;</span>
				<span v-if="stageIndex>=stages.length-2"><a v-on:click.prevent="onFinish">Finish</a>&nbsp;&nbsp;</span>
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
packageObj.createChildNode("createComponentQuestionStep", node_createComponentQuestionStep); 

})(packageObj);
