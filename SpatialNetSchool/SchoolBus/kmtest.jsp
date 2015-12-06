<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.io.*" errorPage=""%>
<%
	String realPath1 = "http://" + request.getServerName() + ":"
			+ request.getServerPort() + request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title></title>
<link rel="stylesheet" href="jqm/jquery.mobile-1.4.3.min.css">
<link rel="stylesheet" href="./css/scrollbar/scrollbar.css"
	rel="stylesheet" />
<link rel="stylesheet" href="css/global.css">
<link rel="stylesheet" href="css/watch.css">
<script type="text/javascript">
      var webPath = '<%=realPath1%>';
</script>
<script src="jqm/jquery.js"></script>
<script src="jqm/jquery.mobile-1.4.3.min.js"></script>
<script type="text/javascript" charset="utf-8" src="./js/iscroll.js"></script>
<script src="js/kmtest.js"></script>


</head>
<body>
	<div data-role="page" data-theme="a" data-dom-cache="true"
		id="contentPage1">
		<script type="text/javascript">
			var  pullDownEl, pullDownOffset, pullUpEl, pullUpOffset, generatedCount = 0;

			/**
			 * Pull down to refresh
			 * api.myScroll.refresh();		// 数据加载完成后，调用界面更新方法
			 */
			function pullDownAction() {
				api.showHistory();
			}

			/**
			 * 上拉刷新
			 * api.myScroll.refresh();		// 数据加载完成后，调用界面更新方法
			 */
			function pullUpAction() {
				api.showHistory();
			}

			/**
			 * 初始化iScroll控件
			 */
			function loaded2() {
				if (api.myScroll != null) {
					api.myScroll.destroy();
				}
				pullDownEl = document.getElementById('pullDown');
				pullDownOffset = pullDownEl.offsetHeight;
				pullUpEl = document.getElementById('pullUp');
				pullUpOffset = pullUpEl.offsetHeight;

				api.myScroll = new iScroll(
						'wrapperContent1',
						{
							scrollbarClass : 'myScrollbar', /* 重要样式 */
							useTransition : false, /* 此属性不知用意，本人从true改为false */
							topOffset : pullDownOffset,
							onRefresh : function() {
								if (pullDownEl.className.match('loading')) {
									pullDownEl.className = '';
									pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Pull down to refresh...';
								} else if (pullUpEl.className.match('loading')) {
									pullUpEl.className = '';
									pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull load more...';
								}
							},
							onScrollMove : function() {
								if (this.y > 5
										&& !pullDownEl.className.match('flip')) {
									pullDownEl.className = 'flip';
									pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Go to start the update...';
									this.minScrollY = 0;
								} else if (this.y < 5
										&& pullDownEl.className.match('flip')) {
									pullDownEl.className = '';
									pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Pull down to refresh...';
									this.minScrollY = -pullDownOffset;
								} else if (this.y < (this.maxScrollY - 5)
										&& !pullUpEl.className.match('flip')) {
									pullUpEl.className = 'flip';
									pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Go to start the update...';
									this.maxScrollY = this.maxScrollY;
								} else if (this.y > (this.maxScrollY + 5)
										&& pullUpEl.className.match('flip')) {
									pullUpEl.className = '';
									pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull load more...';
									this.maxScrollY = pullUpOffset;
								}
							},
							onScrollEnd : function() {
								if (pullDownEl.className.match('flip')) {
									pullDownEl.className = 'loading';
									pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
									pullDownAction(); // Execute custom function (ajax call?)
								} else if (pullUpEl.className.match('flip')) {
									pullUpEl.className = 'loading';
									pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';
									pullUpAction(); // Execute custom function (ajax call?)
								}
							}
						});

				setTimeout(
						function() {
							document.getElementById('wrapperContent1').style.left = '0';
						}, 800);
			}

			//初始化绑定iScroll控件
			//document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
			//document.addEventListener('DOMContentLoaded', loaded2, false);

			$("#contentPage1").on('pageinit', function() {

				$(function() {
					api.getkmGPS();
					window.setInterval(function() {
						api.getkmGPS();
					}, 1000 * 10);
					api.showHistory();
				}); 

			});
			$("#contentPage1").on("pagebeforeshow", function() {
				setTimeout(loaded2, 200);
			});
			$("#contentPage1").on("pageshow", function() {
				$("#heartNav1").addClass("ui-btn-active");
				$("#locationNav1").removeClass("ui-btn-active");
			});
		</script>
		<style type="text/css" media="all">
/**
         *
         * 下拉样式 Pull down styles
         *
         */
#pullDown,#pullUp {
	background: #fff;
	height: 50px;
	line-height: 40px;
	padding: 5px 10px;
	border-bottom: 1px solid #ccc;
	font-weight: bold;
	font-size: 14px;
	color: #888;
}

#pullUp {
	padding: 15px 10px 0px 0px;
}

#pullDown .pullDownIcon,#pullUp .pullUpIcon {
	display: block;
	float: left;
	width: 40px;
	height: 40px;
	background: url(./css/scrollbar/pull-icon@2x.png) 0 0 no-repeat;
	-webkit-background-size: 40px 80px;
	background-size: 40px 80px;
	-webkit-transition-property: -webkit-transform;
	-webkit-transition-duration: 250ms;
}

#pullDown .pullDownIcon {
	-webkit-transform: rotate(0deg) translateZ(0);
}

#pullUp .pullUpIcon {
	-webkit-transform: rotate(-180deg) translateZ(0);
}

#pullDown.flip .pullDownIcon {
	-webkit-transform: rotate(-180deg) translateZ(0);
}

#pullUp.flip .pullUpIcon {
	-webkit-transform: rotate(0deg) translateZ(0);
}

#pullDown.loading .pullDownIcon,#pullUp.loading .pullUpIcon {
	background-position: 0 100%;
	-webkit-transform: rotate(0deg) translateZ(0);
	-webkit-transition-duration: 0ms;
	-webkit-animation-name: loading;
	-webkit-animation-duration: 2s;
	-webkit-animation-iteration-count: infinite;
	-webkit-animation-timing-function: linear;
}

@
-webkit-keyframes loading {from { -webkit-transform:rotate(0deg)translateZ(0);
	
}

to {
	-webkit-transform: rotate(360deg) translateZ(0);
}
}
</style>
		<div data-role="header">
			<a href="#popupLogin" data-rel="popup" data-position-to="window"
				class="ui-btn-left ui-btn ui-btn-inline ui-mini ui-corner-all ui-btn-icon-left ui-icon-comment"
				data-transition="pop">K_MOUSE</a>
			<div data-role="popup" id="popupLogin" data-theme="a"
				class="ui-corner-all">
					<div style="padding:10px 20px;">
						<label for="un" class="ui-hidden-accessible">Please enter the information</label> <input
							type="text" name="message" id="message2" value="" placeholder="Please enter the information"
							data-theme="a">
						<button class="ui-btn ui-corner-all ui-shadow ui-btn-b ui-btn-icon-left ui-icon-check" onclick="">submit</button>
					</div>
			</div>
			<h1>Heartbeat</h1>
			<a href="#"
				class="ui-btn-right ui-btn ui-btn-inline ui-mini ui-corner-all ui-btn-icon-right ui-icon-recycle"
				onclick="api.getkmGPS()">Refresh</a>
		</div>

		<div data-role="content">
			<div class="details-content">
				<div class="user-head">
					<img src="img/default.jpg" alt="John"
						class="image-wrap">
					<div class="user-infor">
						<div id="nameDiv" class="name">KMTEST</div>
						<div id="telDiv" class="tel">2565295686</div>
    <a href="#" onclick=""><div id="timeDiv" class="time"></div></a>
					</div>
				</div>


				<div class="user-heart">
					<div class="dividing-line"><a href="#" onclick="api.showMapkm()"><div id="htime" class="htime"></div></a></div>
					<div class="heart-number" id="heartNumber"></div>
				</div>


				<div id="wrapperContent1" class="wrapper">
					<div id="scrollerContent1" class="scroller">
						<div id="pullDown">
							<span class="pullDownIcon"></span><span class="pullDownLabel">Pull
								down to refresh...</span>
						</div>

						<ul data-role="listview" id="contentList1" class="contentList">

						</ul>

						<div id="pullUp">
							<span class="pullUpIcon"></span><span class="pullUpLabel">Pull
								load more...</span>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div data-role="footer" data-position="fixed" data-id="footernav">
		</div>

	</div>








</body>
</html>
