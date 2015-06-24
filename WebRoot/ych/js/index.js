/*function getSelectedMaintains() {
    return $(':checked');
}
function haveSelectedMaintains() {
    return getSelectedMaintains().length;
}
*/
function getCar() {
	return $('#car').val();
}
function haveCar() {
	return getCar();
}
/*
function next() {
	if (!haveSelectedMaintains()) {
	//	alert('请选择一个保养类型');
	//	return;
	}
	if (!haveCar()) {
	//	alert('请添加一款汽车');
	//	return;
	}
//	selectMaintainType();
}*/
/*function selectMaintainType() {
	$.post('${request.contextPath}/order/select_maintain_type', function(data) {
		// TODO 返回成功后跳转
		window.location = 'submit_orders.html';
	});
}*/
$(function() {
    /*$(':checkbox').click(function(eventObject) {
        $(eventObject.target).parent().parent().toggleClass('checked');
    });
    $(':radio').click(function(eventObject) {
        $(':radio').each(function(index, element) {
            if (element.checked) {
                $(element).parent().parent().addClass('checked');
            } else {
                $(element).parent().parent().removeClass('checked');
            }
        });
    });*/
    if(!/MicroMessenger/i.test(navigator.userAgent)) {
        alert('请通过微信打开');
        location = 'about:blank';
        return;
    }
	var url = '../api/getJsConfig?url=' + encodeURIComponent(location.href.split('#')[0]);
	$.get(url, function(data) {
		wx.config({
		    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		  //  appId: 'wxff69838532feef00', // 必填，公众号的唯一标识
//		    appId: 'wx9d17e381728dfb0f', // 必填，公众号的唯一标识
		    appId: data.appId, // 必填，公众号的唯一标识
		    timestamp: data.timestamp, // 必填，生成签名的时间戳
		    nonceStr: data.nonceStr, // 必填，生成签名的随机串
		    signature: data.signature,// 必填，签名，见附录1
		    jsApiList: ['getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
	});
    $("#myCarousel").swipe({
    	swipeLeft: function() { $(this).carousel('next'); },
    	swipeRight: function() { $(this).carousel('prev'); },
	});
    $('.add-car').click(function() {
    	sessionStorage.removeItem('query');
    	window.location = 'tjqc';
    });
    $('#big-maintain').click(function() {
    	if (!haveCar()) {
    		alert('请添加一辆爱车');
    		return;
    	}
    	var carId = getCar();
    	var carlyid = $('#car-lyid').val();
//    	sessionStorage.setItem('order', JSON.stringify({car: {id: carId, lyid: carlyid}, mtser: 1}));
    	wx.getLocation({
    	    success: function (res) {
    	        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
    	        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
    	        var speed = res.speed; // 速度，以米/每秒计
    	        var accuracy = res.accuracy; // 位置精度
    	        location = 'byxq?mtser=1&carlyid=' + carlyid + '&lon=' + longitude + '&lat=' + latitude;
    	    }
    	});
    });
    $('#small-maintain').click(function() {
    	if (!haveCar()) {
    		alert('请添加一辆爱车');
    		return;
    	}
    	var carId = getCar();
    	var carlyid = $('#car-lyid').val();
    	sessionStorage.setItem('order', JSON.stringify({car: {id: carId, lyid: carlyid}, mtser: 2}));
    	wx.getLocation({
    	    success: function (res) {
    	        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
    	        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
    	        var speed = res.speed; // 速度，以米/每秒计
    	        var accuracy = res.accuracy; // 位置精度
    	        location = 'byxq?mtser=2&carlyid=' + carlyid + '&lon=' + longitude + '&lat=' + latitude;
    	    }
    	});
    });
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}