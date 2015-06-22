$(function() {
    if(!/MicroMessenger/i.test(navigator.userAgent)) {
        alert('请通过微信打开');
        location = 'about:blank';
        return;
    }
    var now = new Date();
    var startDate = new Date();
    startDate.setDate(now.getDate() + 1);
    var endDate = new Date();
    endDate.setDate(now.getDate() + 11);
    $(".form_datetime").datetimepicker({
        language: 'zh-CN',
        format: 'yyyy-mm-dd hh:ii',
        autoclose: true,
        todayHighlight: true,
        startDate: startDate,
        todayBtn: false,
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
    $('#form').submit(function(eventObject) {
    	var phone = $('#phone').val();
        var date = $('#date').val();
        if (!/^13\d|14[57]|15[1-35-9]|17\d|18\d\d{8}$/.test(phone)) {
            alert('请输入正确的手机号码');
            return false;
        }
        var selectDate = date ? new Date(date) : false;
        if (!date || selectDate < startDate || selectDate > endDate || selectDate.getHours() < 9 || selectDate.getHours() > 18) {
            alert('预约日期不在合法的范围内');
            return false;
        }
        if ($('#phonecode').length) {
            var code = $('#phonecode').val();
            if (!code || code.trim().length == 0) {
                alert('验证码不能为空');
                return false;
            }
            var result = false;
            $.ajax('http://120.24.93.43/ych-jk/jk/vc?p=' + phone + '&c=' + code, {
                async: false,
                success: function(data) {
                    if (!data) {
                        alert('验证码校验失败');
                        result = false;
                    }
                    switch(data.r) {
                    case 0:
                        alert('验证码校验错误');
                        result = false;
                        break;
                    case 1:
                        result = true;
                        break;
                    case 2:
                        alert('手机号码不正确');
                        result = false;
                        break;
                    default:
                        alert('未知错误');
                        result = false;
                        break;
                    }
                }
            });
            return result;
        }
        return true;
    	
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
