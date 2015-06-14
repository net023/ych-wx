$(function() {
    $(':radio').click(function(eventObject) {
        $(':radio').each(function(index, element) {
            if (element.checked) {
                $(element).parent().addClass('checked');
            } else {
                $(element).parent().removeClass('checked');
            }
        });
    });
    $('input[name="sort"]').click(function() {
    	// TODO 根据value值发送不同的请求获取排序好的数据
    });
    $('#submit').click(function(eventObject) {
    	var commodities = [];
    	$('.commodity').each(function(index, data) {
    		var values = data.value.split('-');
    		commodities.push({id: values[0], number: values[1]});
    	});
    	var order = getOrder() || {};
    	order.commodities = commodities;
    	order.store = {id: $('.store:checked').val()};
    	sessionStorage.setItem('order', order);
//    	eventObject.preventDefault();
    });
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}