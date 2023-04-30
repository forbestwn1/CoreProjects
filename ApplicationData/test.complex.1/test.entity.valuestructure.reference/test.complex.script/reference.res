{
	"info": {
		"name" : "reference",
		"status": "disabled1"
	},
	"entity":{
		"scriptName": "complexscript_test_value",
		"parm" : {
			"variable" : []
		},
		"valueContext" :{
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"reference_definition": {
							"status": "disabled1",
							"definition": {
								"definition" : {
									"elementPath": "parent_public"
								}
							}
						},
						"reference_link": {
							"status": "disabled1",
							"definition": {
								"link" : {
									"elementPath": "parent_public"
								}
							}
						},
						"reference_link_child": {
							"status": "disabled1",
							"definition": {
								"link" : {
									"elementPath": "parent_public_withchildren.a.aa"
								}
							}
						},
						"reference_link_matchers": {
							"status": "disabled1",
							"definition": {
								"link" : {
									"elementPath": "parent_public",
									definition : {
										criteria : "test.url;1.0.0"
									}
								}
							}
						}
					}
				}
			]
		} 			
	}
}
