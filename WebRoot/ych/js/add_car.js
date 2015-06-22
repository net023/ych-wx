$(function() {
	$('.brand').click(function(eventObject) {
		var order = getOrder() || {};
		order.car = order.car || {};
		var brandId = eventObject.target.id;
		order.car.brand = brandId;
		sessionStorage.setItem('order', JSON.stringify(order));
		location = 'xzqc?brand=' + brandId;
	});
	$('#search').click(function(eventObject) {
		var vin = $('#vin').val();
		if (vin.length != 17) {
			layer.msg('VIN码不是17位');
			return;
		}
		layer.load(2);
		$.ajax('http://120.24.93.43/ych-jk/jk/sv?vin=' + vin, {
			async: false,
			success: function(data) {
				alert(JSON.stringify(data));
				if (!data) {
					layer.msg('无法取得数据');
					return;
				}
				if (data.r == 1) {
					$('#result').removeClass('hidden');
					$('#list').addClass('hidden');
					var query = sessionStorage.getItem('query');
					$('#result .part-body').html('<div class="col-xs-12">品牌：' + 1 + '</div><div class="col-xs-12">厂商：' + 2 + '</div><div class="col-xs-12">系列：' + 3 + '</div><div class="col-xs-12">型号：' + 4 + '</div><div class="col-xs-12"><a class="btn btn-green max-width" href="bcqc?car=' + data.d.id + (query ? '&query=1' : '') + '">确定</a></div>');
				} else {
					layer.msg('查无结果');
				}
			}
		});
		layer.closeAll('loading');
	});
	$('label').click(function(eventObject) {
		$('body').animate({scrollTop: $('.letter[mark='+ $(this).attr('for') +']').offset().top}, 500);
	});
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}