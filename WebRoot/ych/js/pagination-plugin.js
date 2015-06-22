(function($) {
	var $this;
	$.fn.pagination = function() {
		var config = arguments[0];
		if (typeof config === "string") {
			var data = $(this).data("data");
			if (data[config] instanceof Function) {
				var params = [];
				for (var i = 1; i < arguments.length; i++) {
					if(typeof arguments[i] === "string"){
						params.push("\""+arguments[i]+"\"");
					}else{
						params.push(arguments[i]);
					}
				}
				eval("(var result = data[\"" + config + "\"](" + params.join(",") + "))");
				return result;
			}
		} else {
			return new Pagination($(this).get(0), config);
		}
	};

	function Pagination(el, config) {
		this.el = $(el);
		this.options = {
			totalPage: config.totalPage || 1,
			data: config.data || [],
			htmlTplDomId: config.htmlTplDomId
		};
		$.extend(this.options,config);
		this.pageIndex = 1;
		this.el.data("data", this);
		$this = this;
		scrollBottom(this.options.scrollBottomCalFn);
		this.initialization(this.options.data);
	}

	Pagination.prototype = {
		initialization: function(data) {//初始化
			$this.el.html("");
			appendChild(data);
		},
		appendChild:function(data){//添加子节点
			appendChild(data);
		},
		reloadData:function(data){//重新加载数据
			this.options.data = data;
			this.initialization(data);
		}
//		scrollBottomCalFn:function(fn){
//			//this.options.scrollBottomCalFn = fn == undefined ? this.options.scrollBottomCalFn : fn;
//			scrollBottom(fn);
//		}
	};
	
	function appendChild(data){
		var theTmpl = $this.options.htmlTplDomId;
		for(var e in data){
			var htmlOutput = $.templates("#"+theTmpl).render(data[e]);
			$this.el.append(htmlOutput);
		}
		$(window).scrollTop($(document).height() - $(window).height()-50);
	}
	
	function scrollBottom(calFn){
		var me = $this;
		//var calFn = $this.options.scrollBottomCalFn;
		if(me.options.totalPage > 1 && calFn && calFn instanceof Function){
//				jQuery获取位置和尺寸相关函数：
//				$(document).height()    获取整个页面的高度
//				$(window).height()    获取当前也就是浏览器所能看到的页面的那部分的高度。这个大小在你缩放浏览器窗口大小时会改变，与document是不一样的
//				scrollTop()    获取匹配元素相对滚动条顶部的偏移。
//				scrollLeft()    获取匹配元素相对滚动条左侧的偏移。
//				scroll([[data],fn])    当滚动条发生变化时触犯scroll事件
			$(window).unbind("scroll").bind("scroll",function(e){
				if (me.pageIndex <= me.options.totalPage && $(document).scrollTop() >= $(document).height() - $(window).height()) {
		            console.log("---加载"+me.pageIndex+"页---"+$(document).scrollTop());
    				me.pageIndex++;
    				calFn(me.pageIndex);
		        }
			});
		}
	}
})(jQuery);
document.write('<script src="js/jsrender.js" type="text/javascript" charset="utf-8"></script>');