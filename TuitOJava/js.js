var
remoteJSON = {"toto": {"follows": ["titi","tutu"],"messages": {"2": {"content": "Message numero2","date": "2024-01-20T09:00:00Z","likes": [],"statut": 1}}},"tutu": {"follows": ["toto"],"messages": {"1": {"content": "Hello World !","date": "2024-01-20T09:00:00Z","likes": ["titi","livreur"],"statut": 1}}},"titi": {"follows": ["tutu"],"messages": {"3": {"content": "Message 3","date": "2024-01-20T09:00:00Z","likes": ["tutu"],"statut": 1}}},"livreur": {"follows": ["toto"],"messages": {"4": {"content": "Test","date": "2024-01-20T09:00:00Z","likes": ["titi","tutu"],"statut": 1}}}},
localJSON = {"toto" : {"follows" : [ "tutu", "titi" ],"messages" : {"2" : {"date" : "2024-01-20T09:00:00Z","content" : "Message numero2","statut" : 1,"likes" : []}}},"tutu" : {"follows" : [ "toto" ],"messages" : {"1" : {"date" : "2024-01-20T09:00:00Z","content" : "Hello World !","statut" : 1,"likes" : [ "titi", "livreur" ]}}},"livreur" : {"follows" : [ "toto" ],"messages" : {"4" : {"date" : "2024-01-20T09:00:00Z","content" : "Test","statut" : 1,"likes" : [ "titi", "tutu" ]}}},"titi" : {"follows" : [ "tutu" ],"messages" : {"3" : {"date" : "2024-01-20T09:00:00Z","content" : "Message 3","statut" : 1,"likes" : [ "tutu" ]}}}};
    
console.log( _.isEqual(remoteJSON, localJSON) );