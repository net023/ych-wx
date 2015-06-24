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
	$('form').submit(function() {
		if ($('textarea').val().trim().length > 80) {
			layer.msg('评论内容不得超过80个字');
			return false;
		}
	});
});