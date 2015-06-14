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
		alert(vin);
	});
	$('label').click(function(eventObject) {
		$('body').animate({scrollTop: $('.letter[mark='+ $(this).attr('for') +']').offset().top}, 500);
	});
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}