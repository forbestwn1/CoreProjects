{
	"info": {
		"name" : "reference",
		"status": "disabled1"
	},
	"entity":{
		"scriptName": "complexscript_test_value",
		"parm" : {
			"variable1" : ["reference_link_1", "reference_definition_1"],
			"variable" : ["reference_link_1", "reference_definition_1", "reference_link_child"]
		},
		"valueContext" :{
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"reference_link_1": {
							"status": "disabled",
							"definition": {
								"definition" : {
									"elementPath": "parent_public"
								}
							}
						},
						"reference_definition_1": {
							"status": "disabled",
							"definition": {
								"link" : {
									"elementPath": "parent_public"
								}
							}
						},
						"reference_link_child": {
							"definition": {
								"link" : {
									"elementPath": "parent_public_withchildren.a.aa"
								}
							}
						}
					}
				}
			]
		} 			
	}
}
