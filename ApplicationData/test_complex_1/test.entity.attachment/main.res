{
	"info": {
		"name": "test.attachment",
		"description": "test attachment entity"
	},
	"entity": [
		{
			"info": {
				"name" : "constant-none-test_complex_script", 
				"status": "disabled1"
			},
			"parent": {
				"attachment":{
					"mode" : "parent"
				}
			},			
			"entity":{
				"scriptName": "complexscript_test_attachment",
				"parm" : {
					"attachment" : ["data|constant"]
				}
			}
		},
		{
			"info": {
				"name" : "attachment", 
				"status": "disabled1"
			},
			"entity": {
				"data" : [
					{
						"info": {
							"name" : "constant", 
							"status": "disabled1"
						},
						"entity": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 5
						}
					}
				]
			}
		}
	]
}
