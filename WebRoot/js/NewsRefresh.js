jQuery.NewsRefresh = {
    window: [],
    maxUlNum: 2,
    url: "",
    lastId: 0,
    interval: 4000,
    liHeight: 96,
    total: 0,
    current: 0,
    maxUlLiNum: 8,
    init: function(opts) {
        this.window = $(".tasklist");
        this.url = opts.url;
        this.lastId = opts.lastId;
        this.total = opts.total;
        this.current = opts.current;
        return this
    },
    start: function() {
        this.loop(this);
        setInterval(this.loop, this.interval, this)
    },
    loop: function($this) {
        if ($this.total < $this.maxUlLiNum * $this.maxUlNum) {
            $this.fetch();
            return
        }
        if ($this.lastUl().position().top < 0) {
        	
            $this.dropUls();
            return
        }
        if ($this.lastUl().position().top == 0 && $this.uls().length == $this.maxUlNum) {
            $this.removeFirstUl();
            $this.fetch()
        }
    },
    newUl: function() {
        return $('<ul></ul>').css("top", -15 - this.maxUlLiNum * this.liHeight + "px")
    },
    newLi: function(rowData) {
        var itemLi =$('<li><div class="taskcon"><img alt="" src="img/200x200.jpg"><div class="detail"><h4>琼 <span>15 秒前</span></h4><p class="con1">完成：万普任务《ZHFJR》</p><p class="con2">共赚 <em>30,954.12</em> 元</p></div></div></li>');
        itemLi.find("img").attr("src", rowData.pUrl);
        itemLi.find("h4").html(rowData.nick + " &nbsp;<span>" + rowData.str + "秒前</span>");
        itemLi.find(".con1").html("完成："+rowData.jj);
        itemLi.find(".con2").html("共赚：<em>"+rowData.money+"</em>元");
        itemLi.data("time", rowData.cTime);
       
        return itemLi;
    },
    lastUl: function() {
        return this.window.children().last()
    },
    dropUls: function() {
        for (var i = 0; i < this.uls().length; i++) {
            if (i == 0) {
                continue
            } else {
                var curElement = this.uls().eq(i);
                var preElement = this.uls().eq(i - 1);
                var newTop = preElement.position().top - curElement.height();
                curElement.css("top", newTop + "px")
            }
        }
        if (this.current == this.maxUlLiNum * this.maxUlNum) {
            return
        }
        this.current++;
        var indexOfUl = Math.ceil(this.current / this.maxUlLiNum) - 1;
        var curUl = this.uls().eq(indexOfUl);
        var indexOfLi = this.maxUlLiNum - (this.current - indexOfUl * this.maxUlLiNum);
        var curLi = curUl.children().eq(indexOfLi);
        this.uls().velocity({
            top: "+=" + this.liHeight + "px"
        }, {
            duration: 700
        })
    },
    fetch: function() {
        var $this = this;
        $.ajax({
            url: this.url,
            type: "POST",
            data: { id: this.lastId},
            dataType: "json",
            success: function(data) {
                $this.resetTime();
                for (var row in data.indexRanks) {
                    if (row > $this.maxUlLiNum - 1) {
                        break
                    }
                    var itemLi = $this.newLi(data.indexRanks[row]);
                    $this.appendLi(itemLi);
                    if ($this.lastId < data.indexRanks[row].id) {
                        $this.lastId = data.indexRanks[row].id
                    }
                }
            }
        })
    },
    resetTime: function() {
        var now = parseInt((new Date).getTime() / 1000);
        if (this.window.find("li").length > 0) {
            this.window.find("li").each(function() {
                var diff = now - $(this).data("time");
                if (diff > 60) {
                    $(this).find("h4 span").text(parseInt(diff / 60) + " 分钟前")
                } else {
                    $(this).find("h4 span").text(diff + " 秒前")
                }
            })
        }
    },
    uls: function() {
        return this.window.children()
    },
    appendLi: function(li) {
        var success = false;
        for (var i = 0; i < this.uls().length; i++) {
            if (this.uls().eq(i).children().length < this.maxUlLiNum) {
                this.uls().eq(i).prepend(li);
                this.total++;
                success = true;
                break
            }
        }
        if (!success && this.uls().length < this.maxUlNum) {
            var newUl = this.newUl();
            newUl.prepend(li);
            this.total++;
            this.window.append(newUl)
        }
    },
    removeFirstUl: function() {
        var firstUl = this.uls().first();
        this.total -= firstUl.children().length;
        this.current -= firstUl.children().length;
        firstUl.remove()
    }
};