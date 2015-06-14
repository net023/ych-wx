$(function() {
	var typeId;
    $('.panel-collapse').collapse({
        toggle: false
    })
    $('.type').click(function(eventObject) {
    	typeId = eventObject.target.id;
    	$('#carModal').modal();
    });
    $('#carModal').on('show.bs.modal', function() {
    	$('.modal-body').text('正在加载数据请稍候......');
    	$.get('../car/list?type=' + typeId, function(data) {
    		var html = "";
    		var query = sessionStorage.getItem('query');
    		$.each(data, function(index, value) {
    			html += "<a class='car plain-link' id='c" + value.id +"' href='bcqc?car=" + value.id + (query ? '&query=1' : '') + "'>" + value.name + "</a>";
    		});
    		$('.modal-body').html(html);
    		/*$('.car').click(function(eventObject) {
    			var carId = eventObject.target.id;
    			var order = getOrder() || {};
    			order.car = order.car || {};
    			order.car.id = carId;
    			sessionStorage.setItem('order', JSON.stringify(order));
    			$('#carModal').modal('hide');
    			location = 'bcqc?car=' + carId.substring(1);
    		});*/
    	});
    });
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}