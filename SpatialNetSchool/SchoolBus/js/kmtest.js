/**
author :Warren
http://www.wglong.com
**/
var userImei = '4109115497';
var LATITUDE,LONGITUDE;
function goTo(page) {
    showLoader();
	$.mobile.changePage(page, {
		  transition: "slide"
    });
}
function goBack() {
	$.mobile.back();
}


function showLoader() {
    //显示加载器.for jQuery Mobile 1.2.0
    $.mobile.loading('show', {
        text: '加载中...', //加载器中显示的文字
        textVisible: true, //是否显示文字
        theme: 'b',        //加载器主题样式a-e
        textonly: false,   //是否只显示文字
        html: ""           //要显示的html内容，如图片等
    });
}

//隐藏加载器.for jQuery Mobile 1.2.0
function hideLoader(){
    //隐藏加载器
    $.mobile.loading('hide');
}

var api = {

    pageNum : 0,

    pageSize : 10,

    total : 0,
    
    myScroll : null,

    getkmGPS : function(imei){
        var url = "";
        if(imei){
             url = webPath+"/api/WATCH_SERVICE/getkmCurrentDate/{'IMEI':'"+imei+"'}"
        }else{
             url = webPath+"/api/WATCH_SERVICE/getkmCurrentDate/{'IMEI':'"+userImei+"'}"
        }
        $.getJSON(url, null, function (data, textStatus){
        	if(data['result']){              
        		/*
        		if(data['result']['LBS']=="phone"){
                	$("#timeDiv").html(data['result']['DATE_GPS']+" C ");
                }else{
                	$("#timeDiv").html(data['result']['DATE_GPS']+" G : "+data['result']['SPEED']);
                }
                $("#htime").html(data['result']['DATE_HEART']);                
        		 */
        		if(data['result']['LBS']=="phone"){
        			$("#htime").html(data['result']['DATE']+" C ");
        		}
        		else{
        			$("#htime").html(data['result']['DATE']+" G : "+data['result']['SPEED']);
        		}
        		$("#heartNumber").html(data['result']['SPEED']);
        		
                LATITUDE =data['result']['LATITUDE'];
                LONGITUDE =data['result']['LONGITUDE'];
        		
        	}
        });
    },
    getkmGPSHistry : function(func,imei) {
       
        if(api.pageNum<0){
        	api.pageNum = 0;
        }
            var url = "";
            if (imei) {
                    url = webPath + "/api/WATCH_SERVICE/getkmHistryDate/{'IMEI':'" + imei + "','pageNum':" + api.pageNum + ",'pageSize':" + api.pageSize + "}"
                } else {
                    url = webPath + "/api/WATCH_SERVICE/getkmHistryDate/{'IMEI':'" + userImei + "','pageNum':" + api.pageNum + ",'pageSize':" + api.pageSize + "}"
                }
            //console.log(url);
                $.getJSON(url, null, function (data, textStatus) {
                	//console.log(api);
                    if(data['total']!=api.total){
                        api.total += data.length;
                        api.pageNum += api.pageSize;
                        func(data['items']);
                    }else{
                        api.myScroll.refresh();
                    }
                });
    },
    getPageNum : function(){
        if(this.pageNum>0){
             var n1 = this.pageNum*this.pageSize;
            if(n1>=this.total){
                return -1;
            }else{
                return this.pageNum;
            }
        }else{
            return this.pageNum
        }
    },
    showHistory : function(){
        api.getkmGPSHistry( function (list) {	// <-- Simulate network congestion, remove setTimeout from production!
            var content = ""
            for (var i=0;i<list.length;i++){
                content = content + "<li>";
				content = content + "<div class=\"listtitle\">"+ "<a href=\"#\" onclick=\"api.showMapkm("+list[i]['LATITUDE']+","+list[i]['LONGITUDE']+")\">" +list[i]['DATE'];
				if(list[i]['LBS']=="phone"){
					content = content +"	C"+"</a></div>";
				}else{
					content = content +"	G : "+list[i]['SPEED']+"</a></div>"; 
				}				
				
                content = content + "</li>";
            }
            $("#contentList1").append(content).listview('refresh');
            //console.log(api);
            //console.log(api.myScroll);
            if(api.myScroll){
            	 api.myScroll.refresh();		//数据加载完成后，调用界面更新方法   Remember to refresh when contents are loaded (ie: on ajax completion)
            }
           
        })
    },
    showMapkm:function(temp1,temp2){
		if (temp1) {
			console.log("http://maps.google.com/maps?f=q&q="+temp1+","+temp2+"&z=16");
			window.location.href = "http://maps.google.com/maps?f=q&q="+temp1+","+temp2+"&z=16";
		}
		else {
			console.log("http://maps.google.com/maps?f=q&q="+LATITUDE+","+LONGITUDE+"&z=16");
			window.location.href = "http://maps.google.com/maps?f=q&q="+LATITUDE+","+LONGITUDE+"&z=16";
		}
    	
    }
   

}


