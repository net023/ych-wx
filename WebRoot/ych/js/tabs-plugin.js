
var Tabs = function(config){
	this.options = {
		idArray:config.idArray || [],
		className:config.className || "",
		click:config.click || undefined
	};
	this.initialization();
}

Tabs.prototype = {
	initialization:function(){
		var $this = this;
		var clickFn = this.options.click && this.options.click instanceof Function;
		var idArray = this.options.idArray;
		if(this.options.idArray.length > 0 && clickFn){
			for(var i in idArray){
				if($("#"+idArray[i]+" input[type='radio']").length > 0){
					$("#"+idArray[i]+" input[type='radio']").bind("click",{index:i},function(e){
						var index = e.data.index;
						$this.options.click($(this).parent(),index);
					});
				}else{
					$("#"+idArray[i]).unbind("click").bind("click",{index:i},function(e){
						var index = e.data.index;
						$this.options.click($(this),index);
					});
				}
			}
		}else{
			var flage = this.options.className && this.options.className != "";
			if(flage && clickFn){
				if($("."+this.options.className+" input[type='radio']").length > 0){
					$("."+this.options.className).each(function(e){
						$(this).find("input[type='radio']").bind("click",function(){
							$this.options.click($(this).parent(),e);
						});
					});
				}else{
					$("."+this.options.className).each(function(e){
						$(this).unbind("click").bind("click",function(){
							$this.options.click($(this),e);
						});
					});
				}
			}
		}
	}
};