function HashMap(){ 
/**Map��С**/ 
var size=0; 
/**����**/ 
var entry=new Object(); 
/**Map�Ĵ�put����**/ 
this.put=function(key,value){ 
if(!this.containsKey(key)){ 
size++; 
entry[key]=value; 
} 
} 
/**Mapȡget����**/ 
this.get=function(key){ 
return this.containsKey(key) ? entry[key] : null; 
} 
/**Mapɾ��remove����**/ 
this.remove=function(key){ 
if(this.containsKey(key) && ( delete entry[key] )){ 
size--; 
} 
} 
/**�Ƿ����Key**/ 
this.containsKey= function (key){ 
return (key in entry); 
} 
/**�Ƿ����Value**/ 
this.containsValue=function(value){ 
for(var prop in entry) 
{ 
if(entry[prop]==value){ 
return true; 
} 
} 
return false; 
} 
/**���е�Value**/ 
this.values=function(){ 
var values=new Array(); 
for(var prop in entry) 
{ 
values.push(entry[prop]); 
} 
return values; 
} 
/**���е� Key**/ 
this.keys=function(){ 
var keys=new Array(); 
for(var prop in entry) 
{ 
keys.push(prop); 
} 
return keys; 
} 
/**Map size**/ 
this.size=function(){ 
return size; 
} 
/**���Map**/ 
this.clear=function(){ 
size=0; 
entry=new Object(); 
} 
} 
