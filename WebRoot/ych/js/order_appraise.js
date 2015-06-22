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
});