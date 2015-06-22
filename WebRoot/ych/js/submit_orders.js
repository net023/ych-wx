$(function() {
    $(':radio[name=sort]').click(function(eventObject) {
        $(':radio[name=sort]').each(function(index, element) {
            if (element.checked) {
                $(element).parent().addClass('checked');
            } else {
                $(element).parent().removeClass('checked');
            }
        });
    });
    $(':radio[name=brand]').click(function(){
        var oil1 = $('#oil1').val();
        var oil2 = $('#oil2').val();
        if (oil1) {
            var oil1Arr = oil1.split('-');
            oil1 = oil1Arr.length == 3 ? oil1Arr[1] : '';
        }
        if (oil2) {
            var oil2Arr = oil2.split('-');
            oil2 = oil2Arr.length == 3 ? oil2Arr[1] : '';
        }
        var brand = $(this).val();
        var url = 'qhjypp?' + (oil1 ? 'oil1=' + oil1 : '') + (oil2 ? (oil1 ? '&oil2=' + oil2 : 'oil2=' + oil2) : '') + '&brand=' + brand;
        /*alert(url);*/
        layer.load(2);
        var result = false;
        $.ajax(url, {
            async: false,
            success: function(data) {
                /*alert(JSON.stringify(data));*/
                if (!data) {
                    layer.msg('未更换成功');
                    return;
                }
                if (!data.r) {
                    var arr = data.d;
                    var totalPrice = $('#totalPrice');
                    var totalPriceValue = totalPrice.val();
                    var totalPriceText = $('#totalPriceText');
                    for(var i = 0; i < arr.length; i++) {
                        var element = arr[i];
                        var litre = element.litre;
                        var newId = element.id;
                        var newName = element.name;
                        var newPic = element.p_id;
                        var newPrice = element.price;

                        var oil = $('#oil' + litre);
                        var oldValue = oil.val();
                        var oldArr = oldValue.split('-');
                        var newValue;
                        var oilName = $('#oil' + litre + '_name');
                        oilName.text(newName);
                        var oilPrice = $('#oil' + litre + '_price');
                        var oldPriceValue = oilPrice.text();
                        /*alert(totalPriceValue + ', ' + oldPriceValue + ', ' + newPrice);*/
                        if (oldPriceValue != newPrice) {
                            if (totalPriceValue >= 0) {
                                totalPriceValue = totalPriceValue - oldPriceValue + newPrice;
                            }
                            oilPrice.text(newPrice);
                        }
                        if (oldArr.length == 3) {
                            newValue = oldArr[0] + '-' + newId + '-' + oldArr[2];
                            oil.val(newValue);
                        } else {
                            layer.msg('品牌更换失败');
                        }
                    }
                    totalPrice.val(totalPriceValue);
                    totalPriceText.text(totalPriceValue >= 0 ? '￥' + totalPriceValue.toFixed(2) + '元' : '请以门店价格为准');
                    result = true;
                } else {
                    layer.msg(data.e);
                }
            }
        });
        layer.closeAll('loading');
        if (!result) {
            return false;
        }
        $(':radio[name=brand]').each(function(index, element) {
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
    	/*eventObject.preventDefault();
    	var commodities = [];
    	$('.commodity').each(function(index, data){
//    		console.log(index, $(data).val().split('-'));
    		commodities.push($(data).val());
    	});
    	alert('commodities');
    	alert(commodities);*/
    });
});

function getOrder() {
	return JSON.parse(sessionStorage.getItem('order'));
}