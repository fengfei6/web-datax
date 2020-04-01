//嵌入页获取cron 表达式
function getcron(){
   return getvalues();
}
var radio_type=new HashMap(); 
radio_type.put("1","2"); 
radio_type.put("2","1");
radio_type.put("3","1");
radio_type.put("4","1");
radio_type.put("5","1");
radio_type.put("6","1");
radio_type.put("7","1");

//修改radio_type值
function editradio(key,value){
  if(radio_type.containsKey(key)){
    radio_type.remove(key);
	radio_type.put(key,value);
  }else{
    radio_type.put(key,value);
  }
}


//分钟tab事件
function fz_radio_click(obj){ 
var str=obj.value;

if(str=="fz1"){
 editradio("1","1");
 document.getElementById("fks").disabled=true; 
 document.getElementById("fzx").disabled=true; 
 document.getElementById("mks").disabled=false; 
 document.getElementById("mzx").disabled=false; 
 chk_disabled(1);
}
if(str=="fz2"){
 editradio("1","2");
 document.getElementById("fks").disabled=false; 
 document.getElementById("fzx").disabled=false; 
 document.getElementById("mks").disabled=true; 
 document.getElementById("mzx").disabled=true; 
 chk_disabled(1);
}
if(str=="fz3"){
 editradio("1","3");
 document.getElementById("fks").disabled=true; 
 document.getElementById("fzx").disabled=true; 
 document.getElementById("mks").disabled=true; 
 document.getElementById("mzx").disabled=true; 
 chk_disabled(2);
}

}
//0-59 分钟disabled控制
function chk_disabled(str){
   if(str=='1'){
     for(var i=0;i<60;i++){
	   document.getElementById("chk"+i).disabled=true; 
	   document.getElementById("chk"+i).checked=false;
	 }
   }
   if(str=='2'){
     for(var i=0;i<60;i++){
	   document.getElementById("chk"+i).disabled=false; 
	   //document.getElementById("chk"+i).checked=true;
	 }
   }

}
//小时tab事件

function xs_radio_click(obj){
  var str=obj.value;
  if(str=="xs1"){
   editradio("2","1");
   for(var i=0;i<24;i++){
       document.getElementById("sj"+i).disabled=true; 
	   document.getElementById("sj"+i).checked=false;
   }
  }
  if(str=="xs2"){
     editradio("2","2");
    for(var i=0;i<24;i++){
       document.getElementById("sj"+i).disabled=false; 
	   //document.getElementById("sj"+i).checked=false;
   } 
  }
}
//天tab事件

function mt_radio_click(obj){
  var str=obj.value;
  if(str=="mt1"){
    editradio("3","1");
   for(var i=1;i<32;i++){
       document.getElementById("t"+i).disabled=true; 
	   document.getElementById("t"+i).checked=false;
   }
  }
  if(str=="mt2"){
     editradio("3","2");
    for(var i=1;i<32;i++){
       document.getElementById("t"+i).disabled=false; 
	   //document.getElementById("sj"+i).checked=false;
   } 
  }
}

//月tab事件

function my_radio_click(obj){
  var str=obj.value;
  if(str=="my1"){
   editradio("4","1");
   for(var i=1;i<13;i++){
       document.getElementById("y"+i).disabled=true; 
	   document.getElementById("y"+i).checked=false;
   }
  }
  if(str=="my2"){
    editradio("4","2");
    for(var i=1;i<13;i++){
       document.getElementById("y"+i).disabled=false; 
	   //document.getElementById("sj"+i).checked=false;
   } 
  }
}

//周tab事件
function zhou_checkbox_click(){
 if(document.getElementById("mzhou").checked){ 
   editradio("5","2");
   document.getElementsByName("mzh")[0].disabled = false 
   document.getElementsByName("mzh")[1].disabled = false 
 }else{
   editradio("5","1");
   editradio("6","0");
   document.getElementsByName("mzh")[0].disabled = true 
   document.getElementsByName("mzh")[0].checked = true 
   document.getElementsByName("mzh")[1].disabled = true 
    for(var i=1;i<8;i++){
         document.getElementById("zhou"+i).disabled=true;
	     document.getElementById("zhou"+i).checked=false;
     }
 }
 }

function  zhou_radio_click(obj){
    var str=obj.value;
	 
	if(str=="mzh1"){
	  editradio("6","1");
	   for(var i=1;i<8;i++){
         document.getElementById("zhou"+i).disabled=true;
	     document.getElementById("zhou"+i).checked=false;
     }
	}
	if(str=="mzh2"){
	  editradio("6","2");
	  for(var i=1;i<8;i++){
        document.getElementById("zhou"+i).disabled=false;
	    //document.getElementById("y"+i).checked=false;
     }
	}
 }

//获取值
function  getvalues(){  
  //var mks,mzx,fks,fzx,chk1,sj0,t1,y1,zhou1,d4311,d4312="";
  var values=new HashMap(); 

  //循环周期 分钟处理
   if(radio_type.get("1")=="1"){
      var mks=document.getElementById("mks").value;
	  var mzx=document.getElementById("mzx").value;
	  var str=mks+"/"+mzx;
	  values.put("1",str);
   }
   if(radio_type.get("1")=="2"){
      var fks=document.getElementById("fks").value;
	  var fzx=document.getElementById("fzx").value;
	  var str=fks+"/"+fzx;
	  values.put("2",str);
   }
   if(radio_type.get("1")=="3"){
    var fen="";
    for(var i=0;i<60;i++){ 
	   if(document.getElementById("chk"+i).checked)
	   fen+=document.getElementById("chk"+i).value+",";
	}
     values.put("2",fen.substring(0,fen.length-1));
   }
//小时处理
    if(radio_type.get("2")=="1"){
        values.put("5","*");
	}
    if(radio_type.get("2")=="2"){
		var xiaoshi="";
        for(var i=0;i<24;i++){
	      if(document.getElementById("sj"+i).checked)
             xiaoshi+=document.getElementById("sj"+i).value+",";   
        }
         values.put("5",xiaoshi.substring(0,xiaoshi.length-1));
	}
//天处理
  if(radio_type.get("3")=="1"){
     values.put("6","*");
  }
  if(radio_type.get("3")=="2"){
    var tian="";
    for(var i=1;i<32;i++){
	     if(document.getElementById("t"+i).checked)
             tian+=document.getElementById("t"+i).value+",";   
    }
         values.put("6",tian.substring(0,tian.length-1));
  }

//月处理
  if(radio_type.get("4")=="1"){
     values.put("7","*");
  }
  if(radio_type.get("4")=="2"){
    var yue="";
    for(var i=1;i<13;i++){
	     if(document.getElementById("y"+i).checked)
             yue+=document.getElementById("y"+i).value+",";   
    }
         values.put("7",yue.substring(0,yue.length-1));
  }

//周处理

  if(radio_type.get("5")=="1"){
     values.put("8","?");
  }
  if(radio_type.get("5")=="2"){
     if(radio_type.get("6")=="1"){
	   values.put("8","*");
	 }
	 if(radio_type.get("6")=="2"){
	    var zhou="";
        for(var i=1;i<8;i++){
	        if(document.getElementById("zhou"+i).checked)
             zhou+=document.getElementById("zhou"+i).value+",";   
         }
         values.put("8",zhou.substring(0,zhou.length-1));
      }
  }
   
//勾选周循环天循环失效
if(radio_type.get("5")=="2"&&values.containsKey("6")){
    values.remove("6");
	values.put("6","?");
}
//秒循环设0
if(values.get("1")==null||values.get("1")=="0/0"){
   if(values.containsKey("1")){
      values.remove("1");
	  values.put("1","0");
   }else{
      values.put("1","0");
   } 
}
//分循环设0
if(values.get("2")==null||values.get("2")=="0/0"){
   if(values.containsKey("2")){
      values.remove("2");
	  values.put("2","0");
   }else{
      values.put("2","0");
   } 
}
var kssj=document.getElementById("d4311").value;
var jssj=document.getElementById("d4312").value;
var cronstr="秒:"+values.get("1")+"  分:"+values.get("2")+"  小时:"+values.get("5")+"  天:"+values.get("6")+" 月"+values.get("7")+"  周:"+values.get("8")+"\n  开始日期:"+kssj+"  结束日期:"+jssj;
var cron=values.get("1")+" "+values.get("2")+" "+values.get("5")+" "+values.get("6")+" "+values.get("7")+"  "+values.get("8")
document.getElementById("mspan1").innerText = cronstr;
document.getElementById("mspan2").innerText ="表达式:"+cron;

if(kssj!=""){
  cron+=";"+kssj;
}
if(jssj!=""){
  cron+=";"+jssj;
}
return cron;
}
