var oHead = document.getElementsByTagName('head')[0];
var oScript = document.createElement('script');
oScript.setAttribute('src', "http://res.wx.qq.com/open/js/jweixin-1.0.0.js");
oScript.setAttribute('type', 'text/javascript');

var sFunc = function(){
	var param = window.wxparam;
	wx.config({
		debug : param.debug,
		appId : param.appId,
		timestamp : param.timestamp,
		nonceStr : param.nonceStr,
		signature : param.signature,
		jsApiList : param.jsApiList
	});
	/*wx.ready(function() {

	});*/
	/*wx.error(function(res) {
		console.log("微信js-sdk错误:",res);
		alert("微信错误,console查看错误"+JSON.stringify(res));
	});	*/
}

oScript.onload = sFunc;

oHead.appendChild(oScript);
