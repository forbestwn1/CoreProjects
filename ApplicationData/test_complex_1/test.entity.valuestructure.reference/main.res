{
	"info": {
		"name": "test.entity.valuestructure",
		"description": "test value structure feature"
	},
	"entity": [
		{
			"info": {
				"name" : "reference_localresource-none-test_complex_script",
				"status": "disabled1"
			},
			"parent": {
				"valuestructure":{
					"inherit":{
						"mode" : "none"
					}
				}
			},
			"resourceId": "test_complex_script|#reference"
		},
		{
			"info": {
				"name": "valueContext",
				"status": "disabled1"
			},
			"entity": [
				{
					"groupType" : "public",
					"valueStructure" : {
						"parent_public": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parent_public"
							}
						},
						"parent_matchers_public": {
							"definition":{
								"criteria": "test.url;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.url;1.0.0",
								"value": "default value of parent_matchers_public"
							}
						},
						"parent_public_withchildren": {
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
								"a": {
									"aa" : {
										"dataTypeId": "test.string;1.0.0",
										"value": "default value of parent_public_withchildren.a.aa"
									}
								}
							}
						}
					}
				},
				{
					"groupType" : "protected",
					"valueStructure" : {
						"parent_protected": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parent_protected"
							}
						}
					}
				},
				{
					"groupType" : "private",
					"valueStructure" : {
						"parent_private": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parent_private"
							}
						}
					}
				},
				{
					"groupType" : "internal",
					"valueStructure" : {
						"parent_public": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of parent_internal"
							}
						}
					}
				}
			]
		}
	]
}