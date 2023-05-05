{
	"info": {
		"name": "test.common.configure.root",
		"description": "test.common.configure.root"
	},
	"entity": [
		{
			"info": {
				"name" : "a-none-test_complex_script", 
				"status": "disabled"
			},
			"entity":{
				"scriptName": "complexscript_test_printconfigure",
				"parm" : {
					"configure" : ["a_configure"]
				}
			}
		},
		{
			"info": {
				"name" : "b-none-test_complex_1", 
				"status": "disabled1"
			},
			"entity":[
				{
					"info": {
						"name" : "ba_none_test_complex_script", 
						"status": "disabled1"
					},
					"entity":{
						"scriptName": "complexscript_test_printconfigure",
						"parm" : {
							"configure" : []
						}
					}
				},
					
			]
		}
	]
}
