{
	"extra": {
		"name": "test1",
		"description": "test1"
	},
	"entity": {
		"value":{
			"test1_string": {
				"definition": {
					"criteria": "test.string;1.0.0"
				},
				"defaultValue": {
					"dataTypeId": "test.string;1.0.0",
					"value": "Default value for forlistservice_1_ex_parm1"
				}
			},
			"test2_float": {
				"definition": {
					"criteria": "test.float;1.0.0"
				}
			},
			"test3_array": {
				"definition": {
					"criteria": "test.array;1.0.0%%||element:test.map;1.0.0%%||attribute1:test.string;1.0.0,attribute2:test.float;1.0.0,attribute3:test.float;1.0.0||%%||%%"
				}
			}
		}
	}
}
