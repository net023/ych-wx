$(function(){
    $("#kwd").bind("taphold", function(){
        var doc = document, 
            text = doc.getElementById("kwd"),
            range, 
            selection;
        if (doc.body.createTextRange) { //IE
            range = document.body.createTextRange();
            range.moveToElementText(text);
            range.select();
        } else if (window.getSelection) {   //FF CH SF
            selection = window.getSelection();        
            range = document.createRange();
            range.selectNodeContents(text);
            selection.removeAllRanges();
            selection.addRange(range);
            //注意IE9-不支持textContent
            makeSelection(0, text.firstChild.textContent.length, 0, text.firstChild);
        }else{
            alert("浏览器不支持长按复制功能");
        }
    });
    function makeSelection(start, end, child, parent) {
        var range = document.createRange();
        range.setStart(parent.childNodes[child], start);
        range.setEnd(parent.childNodes[child], end);
        var sel = window.getSelection();
        sel.removeAllRanges();
        sel.addRange(range); 
    }
});
