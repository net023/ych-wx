$(function() {
    $(".form_datetime").datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii',
        autoclose: true,
        todayHighlight: true,
        startDate: new Date,
        todayBtn: true,
        pickerPosition: "bottom-right",
        minuteStep: 10
    });
    $('#sms').click(function(eventObject) {
    	var phone = $('#phone').val();
    	if (!/^13\d|14[57]|15[1-35-9]|17\d|18\d\d{8}$/.test(phone)) {
    		alert('请输入正确的手机号码');
    		return;
    	}
    	$.get('http://120.24.93.43/ych-jk/jk/smc?p=' + phone, function(data) {
    		if (!data) {
    			alert('验证码发送失败');
    			return;
    		}
    		switch(data.r) {
    		case 0:
    			alert('验证码发送失败');
    			break;
    		case 1:
    			disabledTimer();
    			break;
    		case 2:
    			alert('手机号码不正确');
    			break;
    		}
    	});
    });
    $('#finish').click(function(eventObject) {
    	eventObject.preventDefault();
    	var phone = $('#phone').val();
    	var code = $('#code').val();
    	var date = $('#date').val();
    	if (!/^13\d|14[57]|15[1-35-9]|17\d|18\d\d{8}$/.test(phone)) {
    		alert('请输入正确的手机号码');
    		return;
    	}
    	if (!code || code.trim().length == 0) {
    		alert('验证码不能为空');
    		return;
    	}
    	alert(phone);
    	alert(code);
    	$.get('http://120.24.93.43/ych-jk/jk/vc?p=' + phone + '&c=' + code, function(data) {
    		if (!data) {
    			alert('验证码校验失败');
    			return;
    		}
    		alert(data.r);
    		switch(data.r) {
    		case 0:
    			alert('验证码校验错误');
    			break;
    		case 1:
    			var order = getOrder();
    			alert(order);
    			if (!order.car || !order.mtser || !order.commodities || !order.store) {
    				alert('订单数据不完整，请不要直接打开本页面');
    				location = 'yyby';
    				return;
    			}
    			var commodities = [];
    			$(order.commodities).each(function(index, data) {
    				commodities.push(data.id + '-' + data.number);
    			});
    			$.post('wcyy', {date: date, mtser: order.mtser, store: order.store.id, commodities: commodities}, function(data) {
    				if (data.r) {
    					location = 'order_form_success.html';
    				}
    	    	});
    			break;
    		case 2:
    			alert('手机号码不正确');
    			break;
			default:
				alert('未知结果');
				break;
    		}
    	});
    	
    });
});
function disabledTimer(timestamp) {
	var btn = $('#sms');
	btn.addClass('disabled');
	var i = timestamp ? 60 - Math.floor((+new Date - timestamp) / 1000) : 60;
	console.log(timestamp);
	console.log(+new Date);
	btn.text(i--);
	var handler = setInterval(function() {
		if (!i) {
			btn.text('获取验证码')
			btn.removeClass('disabled');
			clearInterval(handler);
			return;
		}
		btn.text(i--)
	}, 1000);
}
function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}
