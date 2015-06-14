$(function() {
	var byxmDict = {
			'001': '更换机油',
			'002': '更换机油滤清器',
			'003': '更换空气滤清器',
			'004': '更换燃油滤清器',
			'005': '更换全部火花塞',
			'006': '检查助力转向油',
			'007': '更换整车制动液',
			'008': '检查自动变速箱油',
			'009': '检查或者更换手动变速箱油',
			'010': '检查发动机正时皮带',
			'011': '更换空调滤清器',
			'012': '定期添加冷媒',
			'013': '检查空调管路',
			'014': '更换防冻冷却液',
			'015': '检查厚度、调整，必要时更换前刹车片',
			'016': '检查厚度、调整，必要时更换前刹车盘',
			'017': '检查厚度、调整，必要时更换后刹车片（蹄）',
			'018': '检查厚度、调整，必要时更换后刹车盘（鼓）',
			'019': '调整雨刮（雨刷）',
			'020': '检查电瓶',
			'021': '检查减震器'
	};
    $('.add-car').click(function() {
    	sessionStorage.setItem('query', 1);
        window.location = 'tjqc';
    });
    $('#query').click(function() {
        var car = getCar(); // 得到力洋id
        var km = $('#km').val(); // 得到已行驶公里数
        var lastkm = $('#last-km').val();
        if (!haveCar()) {
            alert('请添加一辆爱车');
            return;
        }
        if (!/^((\d+\.?\d+)|(\d+))$/.test(km) || !/^((\d+\.?\d+)|(\d+))$/.test(lastkm)) {
            alert('请输入正确的行驶公里数和上次保养公里');
            return;
        }
        if (km < lastkm) {
        	alert('行驶公里不小于上次保养公里');
        	return;
        }
        console.log(car, km, lastkm);
        $.get('http://120.24.93.43/ych-jk/jk/smi?lyid=' + car + '&xgls=' + km + '&scgls=' + lastkm, function(data) {
        	console.log(data);
            if (!data) {
                alert('无返回数据');
                return;
            }
            if (data.r) {
            	var bys = data.d != '没识别到该车型相关信息！' ? JSON.parse(data.d) : null;
                if ($.isArray(bys)) {
                    console.log(bys);
                    $('.part-body').empty();
                    $.each(bys, function(index, obj) {
                        $('.part-body').append('<div class="col-xs-12">保养项目：' + byxmDict[obj.byxmdm] + '</div><div class="col-xs-12">建议说明：' + obj.remake + '</div><div class="col-xs-12">保养类型：' + obj.bylx + '</div><div class="last-item col-xs-12">保养间隔：' + obj.byjg + '</div>')
                    })
                } else {
                	alert(data.d);
                }
            } else {
                alert(data.d);
            }
        });
    });
});

function getCar() {
    return $('#car').val();
}

function haveCar() {
    return getCar();
}
