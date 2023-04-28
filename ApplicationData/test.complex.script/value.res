{
	"info": {
		"name" : "value",
		"status": "disabled1"
	},
	"entity":{
		"scriptName": "complexscript_test_value",
		"parm" : {
			"variable" : ["root_public", "tree_public", "tree_public.a.aa","matchers_public"]
		},
		"valueContext" :{
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"root_public": {
							"definition":{
								"criteria": "test.string"
							},
							"defaultValue": {
								"dataTypeId": "test.string",
								"value": "default value of root_public"
							}
						},
						"matchers_public": {
							"definition":{
								"criteria": "test.url"
							},
							"defaultValue": {
								"dataTypeId": "test.url",
								"value": "default value of url_public"
							}
						},
						
						"tree_public" : {
							"definition": {
								"child" : {
									"a" : {
										"child" : {
											"aa" : {"criteria":"test.string;1.0.0"}
										}
									}
								}
							},
							"defaultValue": {
								"a" : {
									"aa" : {
										"dataTypeId": "test.string;1.0.0",
										"value": "default value of tree_public.a.aa"
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
