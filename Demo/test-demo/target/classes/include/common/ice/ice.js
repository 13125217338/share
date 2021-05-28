/*Ice对象，需要jquery支持*/
var Ice = {
	map: {},
	task: {method: [], state: 0},
	/*按id下的元素初始化，id没有默认初始化body下的元素*/
	init: function(id){
		if (id == null) {id = "body";}
		else{id = "#"+id;}
		/*去除input红色波浪线*/
		$(id+" input,textarea").attr("spellcheck", false);
		/*验证*/
		$(id+" input,textarea").each(function () {
			$(this).attr("placeholder", $(this).attr("label"));
			var win = window;
            var verify = $(this).attr("verify");
            var winStr = $(this).attr("window");
            if (winStr) {winStr = eval(winStr);if (winStr) {win = winStr;}}
            if (verify) {verify = verify.split(" ");}
            var verifyMethod = function(verifyObj){
            	if (verify) {
            		var verifyMTS = [];
            		for (index in verify) {
            			var mt = eval(verify[index]);
            			if (mt) {verifyMTS.push(mt);}
            		}
        			if (verifyObj) {
        				/*聚焦与取消聚焦验证*/
        				$(verifyObj).off("focus").on("focus", function(){
        					$(verifyObj).css("border","");
        				});
        				$(verifyObj).off("blur").on("blur", function(){
        					for (index in verifyMTS) {
        						/*返回结果信息代表验证失败*/
        						var msg = verifyMTS[index]($(verifyObj));
        						if (msg != null) {
        							$(verifyObj).css("border","1px solid red");
        							win.Ice.warn(msg);break;
        						}
							}
        				});
        			}
				}
            }
            /*如果是文件类型*/
            if (this.type == "file") {
            	$(this).css({"border":"none","background":"transparent"});
            	var onchangeMT = eval($(this).attr("onchange"));
            	var readFile = function(file){
            		var fileRead = new FileReader();
            		fileRead.onload = function(e){if (onchangeMT) {onchangeMT(file, e.target.result);}}
            		fileRead.readAsDataURL(file);
            	}
            	/*移除原先的验证*/
            	$(this).removeAttr("verify");
            	/*移除原先的改变事件*/
            	$(this).removeAttr("onchange");
            	var x = $(this).offset().left;var y = $(this).offset().top;
            	var style = "background: #fff;position: absolute;width: "+($(this).outerWidth() - 70)+";height: "+$(this).outerHeight()+";left: "+(x + 65)+";top: "+y+";";
				$(this).after("<input verify='"+verify+"' placeholder='未选择任何文件' class='text' type='text' style='"+style+"'/>");
				/*文件方式验证*/
				verifyMethod($(this).next());
				var fl = $(this);
				var clear = function(){
					$(fl)[0].file = null;
					$(fl).val("");
					$(fl).removeAttr("sel");
					$(fl).next().val("");
				}
				/*事件*/
				$(this).next().off("keydown").on("keydown", function(e){
					var copy = (e.ctrlKey || e.metaKey) && e.keyCode == 86;
					if ($(this).val() && !copy) {clear();if (onchangeMT) {onchangeMT();}}
				});
				$(this).next().off("keyup").on("keyup", function(){
					if ($(this).val() && !$(fl).attr("sel")) {clear();if (onchangeMT) {onchangeMT();}}
				});
				$(this).next().off("paste").on("paste", function(e){
					var data = e.originalEvent.clipboardData;
					if (data && data.items[0]) {
						if (data.items[0].kind == "file") {
							var file = data.items[0].getAsFile();
							$(this).val(file.name);
							$(fl)[0].file = [file];
							$(fl).attr("sel", true);
							readFile(file);
						}
					}
				});
				$(this).off("change").on("change", function(){
					/*去除验证边框*/
					$(this).next().css("border","");
					var files = [];
					var files_ = $(this)[0].files;
					for (var i = 0,j = files_.length; i < j; i++) {
						files.push(files_[i]);
						readFile(files_[i]);
					}
					if (files_.length > 0) {$(this)[0].file = files;$(this).attr("sel", true);}
					if (files_.length > 1) {$(this).next().val(files_.length + "个文件");}
					else if(files_.length == 1){$(this).next().val(files_[0].name);}
					/*不需要files值*/
					$(this).val("");
				});
				$(this)[0].clear = function(){
					if ($(this).next().val()) {clear();if (onchangeMT) {onchangeMT();}}
				}
			/*正常验证*/
			}else{verifyMethod($(this));}
		});
		/*提示*/
		$(id+" [tip]").each(function () {
			$(this).off("mouseenter mouseleave").on("mouseenter mouseleave", function(e){
				var text = $(this).prop("firstChild") ? $(this).prop("firstChild").nodeValue : "";
				if ($(this).attr("tip")) {text = $(this).attr("tip");}
				var x = $(this).offset().left;var y = $(this).offset().top;
				if (e.type == "mouseenter") {
					$(this).append("<div class='ice_tip'></div>");
					$(this).find(".ice_tip").off("click").on("click", function(){Ice.select($(this));return false;});
					$(this).find(".ice_tip").text(text);
					var left = x - $(this).find(".ice_tip").outerWidth() + ($(this).outerWidth() / 10 + 8);
					var top = y - $(this).find(".ice_tip").outerHeight() + ($(this).outerHeight() / 10 + 5);
					if (left < 20) {left = x + $(this).outerWidth() - ($(this).outerWidth() / 10 + 8);}
					if (top < 20) {top = y + $(this).outerHeight() - ($(this).outerHeight() / 10 + 5);}
					$(this).find(".ice_tip").css("left", left);
					$(this).find(".ice_tip").css("top", top);
				}else if(e.type == "mouseleave"){
					$(".ice_tip").remove();
				}
			});
		});
		/*收缩其他组件*/
		$("html").off("click").on("click", function(){
			$(".ice_pullDown").hide();
			$(".ice_dateTime").remove();
			$(".ice_more").text("收起");
			$(".ice_more").click();
		});
	/*初始化列表，传入列表配置对象*/
	},initGrid : function(gridObj){
		var attrs = [];
		var ths = $("#" + gridObj.id + " tbody tr th");
		var width = "100%";
		var height = "100%";
		$("#" + gridObj.id).parent().css("overflow","auto");
		var style = $("#" + gridObj.id).parent().attr("style");
		if (style.indexOf("width:") == -1) {$("#" + gridObj.id).parent().css("width", "100%");}
		if (style.indexOf("height:") == -1) {$("#" + gridObj.id).parent().css("height", "100%");}
		if (gridObj.width) {width = gridObj.width;}
		if (gridObj.height) {height = gridObj.height;}
		$("#" + gridObj.id).css({'border-collapse': 'collapse','width': ''+width+'','height': ''+height+'', "table-layout": "fixed"});
		$(ths).addClass("th");
		for(var i = 0,j = ths.length; i < j; i++){
			var renderStyle = $(ths[i]).attr("renderStyle");
			if (renderStyle == null) {renderStyle = "";}
			/*属性对象*/
			var attr = {
				bindName: $(ths[i]).attr("bindName"),
				renderStyle : renderStyle
			};
			/*如果是多选框自动添加th下多选框*/
			if (attr.bindName == "ck") {
				$(ths[i]).html("<input type='checkbox' class='ck' id='ck'/></td>");
				$(ths[i]).find("#ck").off("click").on("click", function(){
					var isCheck = $(this).prop("checked");
					$("#" + gridObj.id + " .ck").each(function(){
						$(this).prop("checked", isCheck);
					});
					if (isCheck) {$('#'+gridObj.id+' tr').css("background", "#d0d0d6");}
					else{$('#'+gridObj.id+' tr').css("background", "");}
				});
			}
			/*排序*/
			if ($(ths[i]).attr("sort")) {
				$(ths[i]).off("click").on("click", function(){
					var isDescLength = $(this).find(".ice_sort_desc").length;
					$("#"+gridObj.id+" .ice_sort_asc,#"+gridObj.id+" .ice_sort_desc").remove();
					IceGrid.query.sortName = $(this).attr("bindName");
					if (isDescLength > 0) {
						IceGrid.query.sortType = "asc";
						$(this).append("<div class='ice_sort_asc'></div>");
					}else{
						IceGrid.query.sortType = "desc";
						$(this).append("<div class='ice_sort_desc'></div>");
					}
					IceGrid.sort();
				});
			}
			/*放入属性对象*/
			attrs.push(attr);
		}
		if (!gridObj.page) {gridObj.page = {callBack : function(){}};}
		if (gridObj.removeHeader == true) {$(ths).parent().remove();}
		/*默认先追加一条tr用于撑高*/
		$('#'+gridObj.id+' tbody').append("<tr class='other_tr'></tr>");
		/*返回Grid对象*/
		var IceGrid = {
			id: gridObj.id,
			render: gridObj.render,
			data: [],
			sort: gridObj.sort ? gridObj.sort : function(obj){},
			query: {},
			curSum: 0,
			dbClick: gridObj.dbClick,
			removeHeader: gridObj.removeHeader,
			/*重新初始化查询条件*/
			init : function(pageNo, pageSize, sortName, sortType){
				this.query = {};
				this.query.pageNo = pageNo ? Number(pageNo) : 1;
				this.query.pageSize = pageSize ? Number(pageSize) : 0;
				if (this.query.pageNo < 1) {this.query.pageNo = 1;}
				if (this.query.pageSize < 0) {this.query.pageSize = 0;}
				$("#"+this.id+" .ice_sort_asc,#"+this.id+" .ice_sort_desc").remove();
				if (sortName != null && sortType != null) {
					this.query.sortName = sortName;
					this.query.sortType = sortType;
					var sortObj = $("#"+this.id+" [bindName='"+sortName+"']");
					if (sortObj.length != 0) {
						if (sortType == "asc") {sortObj.append("<div class='ice_sort_asc'></div>");}
						else{sortObj.append("<div class='ice_sort_desc'></div>");}
					}
				}
			},page : {
				id: null,
				pageNo: 1,
				pageSize: 0,
				totalPage: 0,
				totalSize: 0,
				callBack: null,
				switchPage : function(isNext){
					isNext ? this.pageNo++ : this.pageNo--;
					$("#"+this.id+" #ice_total").val(this.pageNo);
					$("#"+this.id+" #ice_page").text(this.pageNo);
					this.vail();
					IceGrid.query.pageNo = this.pageNo;
					IceGrid.query.pageSize = this.pageSize;
					if (this.callBack) {this.callBack();}
				},vail : function(){
					this.pageNo <= 1 ? $("#"+this.id+" #ice_prep").attr("disabled", true) : $("#"+this.id+" #ice_prep").attr("disabled", false);
					this.pageNo >= this.totalPage ? $("#"+this.id+" #ice_next").attr("disabled", true) : $("#"+this.id+" #ice_next").attr("disabled", false);
					this.totalPage == 0 ? $("#"+this.id+" #ice_go").attr("disabled", true) : $("#"+this.id+" #ice_go").attr("disabled", false);
					$("#"+this.id+" .ice_PageRight .bt").css("color","");
					$("#"+this.id+" .ice_PageRight [disabled='disabled']").css("color","#999");
				},go : function(){
					var total = Number($("#"+this.id+" #ice_total").val());
					total = total < 1 ? 1 : (total > this.totalPage ? this.totalPage : total);
					$("#"+this.id+" #ice_total").val(total);
					$("#"+this.id+" #ice_page").text(total);
					this.pageNo = total;
					this.vail();
					IceGrid.query.pageNo = this.pageNo;
					IceGrid.query.pageSize = this.pageSize;
					if (this.callBack) {this.callBack();}
				},update : function(totalSize){
					this.pageSize = IceGrid.query.pageSize;
					if (totalSize || totalSize == 0) {
						this.totalSize = Number(totalSize);
						var sum = this.totalSize % this.pageSize;
						var total = Math.floor(this.totalSize / this.pageSize);
						this.totalPage = sum == 0 ? total : (total + 1)
					}
					this.pageNo = IceGrid.query.pageNo;
					this.pageNo = this.pageNo < 1 ? 1 : (this.pageNo > this.totalPage ? this.totalPage : this.pageNo);
					$("#"+this.id+" .ice_PageLeft span").text(this.totalSize);
					$("#"+this.id+" #ice_page").text(this.pageNo);
					$("#"+this.id+" #ice_totalPage").text(this.totalPage);
					this.vail();
				},init :function(){
					this.id = gridObj.page.id;
					this.callBack = gridObj.page.callBack;
					var ht = $("#"+this.id).height();
					if (ht < 30) {$("#"+this.id).css("height","30px");}
					else{$("#"+this.id).css("line-height", ht+"px");}
					$("#"+this.id).css("font-size", "16px");
					$("#"+this.id).css("width", "100%");
					var divLeft = "<div class='ice_PageLeft'>共 <span>0</span> 条</div>";
					var divRight = "<div class='ice_PageRight'>" +
										"<input id='ice_prep' type='button' class='bt' value='上一页'/>" +
										"<span id='ice_page' style='margin-left: 10px;'>0</span> / <span id='ice_totalPage' style='margin-right: 10px;'>0</span>" +
										"<input id='ice_next' type='button' class='bt' value='下一页'/>" +
										"<input style='width: 50px;text-align: center;bottom: 1px;' id='ice_total' class='text' type='number'/>" +
										"<input type='button' value='跳转' class='bt' id='ice_go'>" +
									"</div>";
					$("#"+this.id).html(divLeft + divRight);
					$("#"+this.id+" #ice_prep").off("click").on("click", function(){IceGrid.page.switchPage(false);});
					$("#"+this.id+" #ice_next").off("click").on("click", function(){IceGrid.page.switchPage(true);});
					$("#"+this.id+" #ice_go").off("click").on("click", function(){IceGrid.page.go();});
					this.vail();
				}
			/*设置公共默认信息*/
			},setDefault : function(){
				/*额外追加tr用于控制高度*/
				var tbody = $('#'+this.id+' tbody');
				$(tbody).find(".other_tr").remove();
				/*无数据展示无数据*/
				if (this.curSum == 0) {$(tbody).append("<tr class='other_tr'><td class='noData' colspan='"+attrs.length+"'><暂无数据></td></tr>");}
				else{$(tbody).append("<tr class='other_tr'></tr>");}
				/*渲染事件，移除头双击单个，不移除头双击一行*/
				if (this.removeHeader == true) {
					$("#"+this.id+" td").off("dblclick").on("dblclick", function(){
						if (IceGrid.dbClick) {
							var trIndex = $(this).parent().attr("index");
							IceGrid.dbClick(IceGrid.data[trIndex][$(this).attr("index")]);
						}
					});
				}else{
					/*单击一行选中多选框*/
					 $('#'+this.id+' tr').off("click").on("click", function(e){
						if (e.target.tagName == "TD") {
							var cks = $(this).find(".ck");
							if (cks.length > 0) {
								if($(cks[0]).prop("checked")){
									$(cks[0]).prop("checked", false);
									$(this).css("background", "");
								}else{
									$(cks[0]).prop("checked", true);
									$(this).css("background", "#d0d0d6");
								}
							}
						}
					});
					/*双击一行事件*/
					 $('#'+this.id+' tr').off("dblclick").on("dblclick", function(){
						if (IceGrid.dbClick) {
							IceGrid.dbClick(IceGrid.data[$(this).attr("index")]);
						}
					});
					/*渲染颜色*/
					if (gridObj.oddEvenColor) {
						$('#'+this.id+' tbody tr:odd td').css('background', gridObj.oddEvenColor[0]);
						$('#'+this.id+' tbody tr:even td').css('background', gridObj.oddEvenColor[1]);
					}
				}
				Ice.init(this.id);
			/*设置列表数据，isAppend为true时表示将arrayObj数据追加到列表后，maxSum表示追加时限制总个数，maxSum只会在isAppend为true时生效*/
			},setData : function(arrayObj, isAppend, maxSum){
				if (isAppend != true) {
					$("#"+this.id+" .tr").remove();
					this.curSum = 0;
					this.data = [];
				}
				var appendData = [],recordIndex = 1;
				for(index in arrayObj){
					if (arrayObj[index]) {
						if (isAppend == true && typeof maxSum == "number") {if (this.curSum > maxSum) {break;}}
						this.data.push({element: {}, data: arrayObj[index]});
						appendData.push(arrayObj[index]);
						/*如果是移除头*/
						if (this.removeHeader == true) {
							if (++recordIndex > attrs.length) {
								this.setTrData(appendData);
								/*追加元素对象*/
								var tds = $('#'+this.id+' tr[index="'+this.curSum+'"] td');
								for (var i = this.curSum * attrs.length,k = 0,j = i + appendData.length; i < j; i++,k++) {
									this.data[i].element = $(tds[k]);
								}
								this.curSum++;recordIndex = 1;appendData = [];
							}
						}else{
							this.setTrData(arrayObj[index]);
							/*追加元素对象*/
							var tds = $('#'+this.id+' tr[index="'+this.curSum+'"] td');
							for(bindIndex in attrs){
								if (attrs[bindIndex].bindName != null) {
									this.data[this.curSum].element[attrs[bindIndex].bindName] = $(tds[bindIndex]);
								}
							}
							this.curSum++;
						}
					}
				}
				/*追加*/
				if (this.removeHeader == true && appendData.length > 0) {
					this.setTrData(appendData);
					/*追加元素对象*/
					var tds = $('#'+this.id+' tr[index="'+this.curSum+'"] td');
					for (var i = this.curSum * attrs.length,k = 0,j = i + appendData.length; i < j; i++,k++) {
						this.data[i].element = $(tds[k]);
					}
				}
				this.setDefault();
			},setTrData : function(obj){
				var datas = "<tr index='"+this.curSum+"' class='tr'>";
				if (this.removeHeader == true) {
					for(bindIndex in attrs){
						if (obj[bindIndex]) {
							if (this.render) {
								datas += "<td index='"+bindIndex+"' class='td' style='"+attrs[bindIndex].renderStyle+"'>" + this.render(obj[bindIndex]) + "</td>";
							}else{
								datas += "<td index='"+bindIndex+"' class='td' style='"+attrs[bindIndex].renderStyle+"'>" + obj[bindIndex] + "</td>";
							}
						}else{datas += "<td class='td' style='visibility: hidden;"+attrs[bindIndex].renderStyle+"'></td>";}
					}
				}else{
					for(bindIndex in attrs){
						if (attrs[bindIndex].bindName == 'xh') {
							datas += "<td class='td xh' style='"+attrs[bindIndex].renderStyle+"'>" + (this.curSum + 1) + "</td>";
						}else if (attrs[bindIndex].bindName == "ck") {
							datas += "<td class='td' style='text-align: center;"+attrs[bindIndex].renderStyle+"'><input type='checkbox' class='ck' val='"+this.curSum+"'/></td>";
						}else{
							if (this.render) {
								datas += "<td class='td' style='"+attrs[bindIndex].renderStyle+"'>" + this.render(attrs[bindIndex].bindName, obj) + "</td>";
							}else{
								datas += "<td class='td' style='"+attrs[bindIndex].renderStyle+"'>" + obj[attrs[bindIndex].bindName] + "</td>";
							}
						}
					}
				}
				$("#"+this.id+" tbody").append(datas + "</tr>");
			/*获取选择行数据，需页面有选择框*/
			},getCheck : function(){
				var check = [];
				var trs = $("#"+this.id+" tr");
				for(var i = 1,j = trs.length; i < j; i++){
					var cks = $(trs[i]).find(".ck");
					if (cks.length > 0) {if ($(cks[0]).prop("checked")) {check.push(this.data[$(cks[0]).attr("val")]);}}
				}
				return check;
			/*根据条件获取数据列表*/
			},getData : function(name, value){
				var datas = [];
				if (name != null) {for(i in this.data){if (value == this.data[i].data[name]) {datas.push(this.data[i]);}}}
				return datas;
			}
		};
		if (gridObj.page) {IceGrid.page.init();}
		return IceGrid;
	/*获取数据绑定，返回绑定对象（对象的字段名称为ID的值）：绑定ID下的所有ID框架参数与input参数，isFile代表是否返回文件类型，datas代表赋初始值*/
	},getBind : function(id, isFile, datas){
		var values = {};
		if (datas) {for (key in datas) {if (datas[key]) {values[key] = datas[key];}}}
		var eles = $("#"+id).find("[id]");
		$(eles).each(function(){
			var data = null;
			if (this.nodeName == "INPUT" || this.nodeName == "TEXTAREA") {data = Ice.getVal($(this));if (!(Array.isArray(data))) {data = data.trim();}}
			else if(this.nodeName == "DIV"){
				if ($(this).find(".ice_select").length > 0) {data = Ice.getPD($(this)).trim();}
				else if($(this).find("[type='dateTime']").length > 0){data = Ice.getDT($(this)).trim();}
			}
			if (data != null) {values[$(this).attr("id")] = data;}
		});
		/*如果是文件类型*/
		if (isFile == true) {
			var formData = new FormData();
			for(key in values){
				if (Array.isArray(values[key])) {for (index in values[key]) {formData.append(key, values[key][index]);}}
				else{formData.append(key, values[key]);}
			} 
			return formData;
		}else{return values;}
	/*WebSocket，传入地址与打开回调和信息回调函数与附加参数，附加参数open会传递回去，需手动link才能连接*/
	},initWS : function(url, onOpen, onMsg, other){
		if("WebSocket" in window){
			var ws = {
				url: url,
				other: other,
				ws: null,
				rec: 0,
				open : onOpen,
				msg : onMsg,
				link : function(msg){
					try{
						/*如果已经连接就关闭在连接*/
						if (this.ws) {if(this.ws.readyState === this.ws.OPEN){this.close();}}
						this.ws = new WebSocket(url);
						this.ws.onopen = function(){
							console.log("WebSocket已连接！");
							ws.rec = 0;
							if (ws.open) {ws.open(other);}
							if (msg != null) {ws.ws.send(msg);}
						}
						this.ws.onmessage = function(msg){if (ws.msg) {ws.msg(msg.data);}}
						this.ws.onerror = function(e){
							console.log("WebSocket出现错误！");
							console.error(e);
						}
						this.ws.onclose = function(){console.log("WebSocket已关闭！");}
					}catch(error){
						console.error(error);
						this.close();
						if (ws.rec > 2) {console.log("WebSocket重连超过上限，不在重连！");}
						else{
							console.log("WebSocket出现错误，正在重连...");
							setTimeout(function(){ws.rec++;ws.link(msg);}, 500);
						}
					}
				},send : function(msg){
					if (!this.ws || this.ws.readyState === this.ws.CLOSED) {
						this.close();
						console.log("WebSocket发送信息失败，正在重连...");
						this.link(msg);
					}else{this.ws.send(msg);}
				},close : function(){if (this.ws) {this.ws.close();}}
			}
			return ws;
		}else{throw "浏览器暂不支持WebSocket，请下载最新版谷歌浏览器！";}
	/*提示框，传入信息、背景与停留时间*/
	},msg : function(msg, time, bg){
		if (time == null) {time = 1000;}
		var curTime = new Date().getTime();
		$("body").append("<div id='"+curTime+"' class='ice_msg' style='background: "+bg+";'>" +"<div class='ice_msg_content'><div><div>");
		$("#"+curTime+" div").text(msg);
		$("#"+curTime).show();
		$("#"+curTime).animate({height: '80px'}, 300, function(){
			setTimeout(function(){$("#"+curTime).remove()}, time);
		});
	},tip : function(msg, time){
		this.msg(msg, time, "#6495ED");
	},success : function(msg, time){
		this.msg(msg, time, "#3CB371");
	},error : function(msg, time){
		this.msg(msg, time, "#CD5C5C");
	},warn : function(msg, time){
		this.msg(msg, time, "#F4B460");
	},verify : function(id, win){
		var isIn = true;
		var ipts = $("input,textarea");
		if (id != null) {ipts = $("#"+id+" input,textarea");}
		/*输入框自定义验证*/
		$(ipts).each(function () {
            var verify = $(this).attr("verify");
            if (verify) {
            	verify = verify.split(" ");
            	for (index in verify) {
            		var mt = eval(verify[index]);
            		/*返回结果信息代表验证失败*/
            		if (mt && mt($(this)) != null) {$(this).css("border","1px solid red");isIn = false;break;}
            	}
            }
		});
		/*下拉框只能非空验证*/
		var pdts = $(".ice_select");
		if (id != null) {pdts = ("#"+id+" .ice_select");}
		$(pdts).each(function () {
			var verify = $(this).attr("verify");
			if (verify && !$(this).parent().attr("value")) {$(this).css("border","1px solid red");isIn = false;}
		});
		if (isIn == false) {if (win) {win.Ice.warn("验证不通过！");}else{Ice.warn("验证不通过！");}}
		return isIn;
	/*初始化话下拉框组件，传入配置对象*/
	},initPD : function(pdObj){
		var label = pdObj.label ? pdObj.label : "";
		var isTip = pdObj.tip == false ? false : true;
		$("#"+pdObj.id).attr("label", label);
		$("#"+pdObj.id).addClass("ice_pd");
		var width = $("#"+pdObj.id).outerWidth();
		if (!width || width == 0) {width = 200;$("#"+pdObj.id).css('width', width);}
		var desc = "<div class='ice_sort_desc' style='margin-left: 0px;margin-top: 8px;float: right;'></div>";
		/*设置初始下拉*/
		$("#"+pdObj.id).html("<div class='ice_select'><span "+(isTip ? "tip" : "")+"><请选择"+label+"></span>"+desc+"</div><div class='ice_pullDown'></div>");
		/*非空验证条件*/
		if (pdObj.verify) {$("#"+pdObj.id+" .ice_select").attr("verify", true);}
		/*解析字段*/
		var name = "name";
		var value = "value";
		if (pdObj.name != null) {name = pdObj.name;}
		if (pdObj.value != null) {value = pdObj.value;}
		var select = "";
		for (index in pdObj.data) {
			select += ("<div "+(isTip ? "tip" : "")+" value='"+pdObj.data[index][value]+"'>"+pdObj.data[index][name]+"</div>");
		}
		$("#"+pdObj.id+" .ice_pullDown").html(select);
		/*赋值事件*/
		$("#"+pdObj.id).off("click").on("click", function(){
			var dis = $(this).find(".ice_pullDown").css("display");
			if (dis && dis != "none") {
				$(this).find(".ice_pullDown").hide();
				if (pdObj.change) {pdObj.change(Ice.getPD($("#"+pdObj.id)));}
			}else{
				$(this).find(".ice_pullDown").show();
			}
			return false;
		});
		$("#"+pdObj.id+" .ice_pullDown > div").off("click").on("click", function(e){
			var text = $(this).prop("firstChild") ? $(this).prop("firstChild").nodeValue : "";
			$("#"+pdObj.id+" .ice_select > span").text(text);
			$("#"+pdObj.id).attr("value", $(this).attr("value"));
			$("#"+pdObj.id+" .ice_select").css("border","");
		});
		/*聚焦加回退清空数值*/
		$("#"+pdObj.id).attr("tabindex","0");
		$("#"+pdObj.id).addClass("ice_focus");
		$("#"+pdObj.id).off("keydown").on("keydown", function(e){if (e.keyCode == 8) {$(this).find(".ice_select > span").text("<请选择"+label+">");$(this).removeAttr("value");}});
		$("#"+pdObj.id).off("mouseenter").on("mouseenter", function(){$(this).focus();});
		Ice.init(pdObj.id);
	/*下拉组件获取值，value有值则返回name名称*/
	},getPD : function(ele, value){
		if (value != null) {
			value = value.toString();
			var sels = $(ele).find(".ice_pullDown > div");
			for (var i = 0; i < sels.length; i++) {
				if ($(sels[i]).attr("value") == value) {
					return $(sels[i]).prop("firstChild") ? $(sels[i]).prop("firstChild").nodeValue : "";
				}
			}
		}else{if($(ele).attr("value")){return $(ele).attr("value");}return "";}
	/*设置下拉赋值*/
	},setPD : function(ele, value){
		var label = $(ele).attr("label") ? $(ele).attr("label") : "";
		var select = $(ele).find(".ice_select > span");
		if (value != null) {
			value += "";
			var selectValue = $(ele).find(".ice_pullDown > div");
			for (var i = 0, j = selectValue.length; i < j; i++) {
				if (value == $(selectValue[i]).attr("value")) {
					var text = $(selectValue[i]).prop("firstChild") ? $(selectValue[i]).prop("firstChild").nodeValue : "";
					$(select).text(text);
					$(ele).attr("value", value);
					return;
				}
			}
		}
		$(select).text("<请选择"+label+">");
		$(ele).removeAttr("value");
	/*获取下拉框所有值*/
	},getPDS : function(ele, name_, value_){
		var objs = [];
		var name = "name";
		var value = "value";
		if (name_ != null) {name = name_;}
		if (value_ != null) {value = value_;}
		var sels = $(ele).find(".ice_pullDown > div");
		for (var i = 0; i < sels.length; i++) {
			var obj = {};
			obj[name] = $(sels[i]).prop("firstChild") ? $(sels[i]).prop("firstChild").nodeValue : "";
			obj[value] = $(sels[i]).attr("value");
			objs.push(obj);
		}
		return objs;
	/*jquery方式val方法获取值，为空返回空字符*/
	},getVal : function(ele){
		ele = ele[0];
		if (ele) {
			if (ele.nodeName == "INPUT" || ele.nodeName == "TEXTAREA") {
				if (ele.type == "file") {if(ele.file){return ele.file;}else{if($(ele).next().val()){return $(ele).next().val();}}}
				else if(ele.type == "radio" || ele.type == "checkbox"){if ($(ele).prop("checked")) {return $(ele).val();}}
				else{return $(ele).val();}
			}
		}
		return "";
	/*jquery方式设置val，为空设置空字符（file类型只设置展示文本，不设置实际数据）*/
	},setVal : function(ele, val){
		ele = ele[0];
		if (ele) {
			if (ele.nodeName == "INPUT" || ele.nodeName == "TEXTAREA") {
				if (ele.type == "file") {if (val != null) {$(ele).next().val(val);}else{ele.clear();}}
				else if(ele.type == "radio" || ele.type == "checkbox"){if (val != null) {$(ele).prop("checked", true);}else{$(ele).prop("checked", false);}}
				else{if (val != null){$(ele).val(val);}else{$(ele).val("");}}
			}
		}
	/*清空ID下的所有组件值*/
	},clear : function(id){
		var eles = $("#"+id).find("[id]");
		$(eles).each(function(){
			if (this.nodeName == "INPUT" || this.nodeName == "TEXTAREA") {Ice.setVal($(this));}
			else if(this.nodeName == "DIV"){
				if ($(this).find(".ice_select").length > 0) {Ice.setPD($(this));}
				else if($(this).find("[type='dateTime']").length > 0){Ice.setDT($(this));}
			}
		});
	/*设置数据绑定，绑定ID下所有组件值（对象的字段名称为ID的值）*/
	},setBind : function(id, obj){
		if (obj) {
			var eles = $("#"+id).find("[id]");
			$(eles).each(function(){
				if (this.nodeName == "INPUT" || this.nodeName == "TEXTAREA") {Ice.setVal($(this), obj[$(this).attr("id")]);}
				else if(this.nodeName == "DIV"){
					if ($(this).find(".ice_select").length > 0) {Ice.setPD($(this), obj[$(this).attr("id")]);}
					else if($(this).find("[type='dateTime']").length > 0){Ice.setDT($(this), obj[$(this).attr("id")]);}
				}
			});
		}
	/*设置map值*/
	},set : function(key, value){
		this.map[key] = value;
	},get : function(key){
		return this.map[key];
	},remove : function(key){
		delete this.map[key];
	/*初始化时间组件*/
	},initDT : function(dtObj){
		var curDate = new Date();
		var temp = null;
		var label = dtObj.label;
		$("#"+dtObj.id).attr("val", curDate.getTime());
		$("#"+dtObj.id).off("click").on("click", function(){return false;});
		$("#"+dtObj.id).addClass("ice_dt");
		var width = $("#"+dtObj.id).outerWidth();
		if (!width || width == 0) {width = 200;$("#"+dtObj.id).css("width", width);}
		$("#"+dtObj.id).html("<input type='dateTime' readonly='true' placeHolder='"+(label ? label : '请选择日期时间')+"' class='text' style='width: "+width+"px;'>");
		$("#"+dtObj.id+" [type='dateTime']").off("click").on("click", function(){
			if ($("#"+dtObj.id+" table").length > 0) {return;}
			var curTime = Number($("#"+dtObj.id).attr("val"));
			if (!$(this).val()) {curTime = new Date().getTime();}
			if (temp) {curTime = temp;temp = null;}
			curDate = new Date(curTime);
			var curYMD = curDate.getFullYear()+"年"+(curDate.getMonth()+1)+"月"+curDate.getDate()+"日";
			var flag = "";
			curDate.setDate(1);
			curDate.setDate(1 - curDate.getDay());
			var curDay = curDate.getDate();
			var table = "<table class='ice_dateTime'>" +
					"<tr height='26px'><td id='prepMonth' style='border-bottom: 1px gray solid;'>&lt;</td>" +
					"<td colspan='5' val='"+curTime+"' id='timeVal' style='border-bottom: 1px gray solid;'>"+curYMD+"</td>" +
					"<td id='nextMonth' style='border-bottom: 1px gray solid;'>&gt;</td></tr>" +
					"<tr height='30px'><td>日</td><td>一</td><td>二</td><td>三</td><td>四</td><td>五</td><td>六</td></tr>";
			for (var i = 0; i < 5; i++) {
				var tr = "<tr height='26px'>";
				for (var j = 0; j < 7; j++) {
					if (curTime == curDate.getTime()) {flag = "flag='1' style='background: #ddd;'";}
					tr += ("<td "+flag+" class='timeClick' style='width: 30px;' val='"+curDate.getTime()+"'>" + curDay + "</td>");
					curDate.setDate(curDay + 1);
					curDay = curDate.getDate();
					flag = "";
				}
				table += (tr + "</tr>");
			}
			var tr = "<tr height='26px'>" +
					"<td colspan='5' style='text-align: left;'>" +
					"<input type='time' step='3' class='text' disabled='true' style='border-radius: 0px;margin: 0px;width: 100%;height: 26px;'></td>" +
					"<td colspan='2'><div id='ok' style='background: #fff;border: 1px solid #999;line-height: 26px;'>确认</div></td></tr>";
			table += (tr + "</table>");
			$("#"+dtObj.id).append(table);
			$("#"+dtObj.id+" table td").off("mouseenter mouseleave").on("mouseenter mouseleave", function(e){
				if (e.type == "mouseenter") {$(this).css("background","#ddd");}
				else if(e.type == "mouseleave"){if (!$(this).attr("flag")) {$(this).css("background","#fff");}}
			});
			if (dtObj.dateTime) {
				$("#"+dtObj.id+" [type='time']").removeAttr("disabled");
				$("#"+dtObj.id+" [type='time']").val(Ice.getTime(new Date(curTime), true));
			}
			$("#"+dtObj.id+" .timeClick").off("click").on("click", function(){
				$("#"+dtObj.id+" .timeClick").css("background", "#fff");
				$("#"+dtObj.id+" .timeClick").removeAttr("flag");
				$(this).attr("flag", "1");
				$(this).css("background","#ddd");
				var dt = new Date(Number($(this).attr("val")));
				$("#"+dtObj.id+" #timeVal").text(dt.getFullYear()+"年"+(dt.getMonth()+1)+"月"+dt.getDate()+"日");
				if (dtObj.dateTime) {dt = new Date(dt.getFullYear()+"-"+(dt.getMonth()+1)+"-"+dt.getDate()+" "+$("#"+dtObj.id+" [type='time']").val());}
				$("#"+dtObj.id+" #timeVal").attr("val", dt.getTime());
			});
			$("#"+dtObj.id+" #ok").off("click").on("click", function(){
				curDate = new Date(Number($("#"+dtObj.id+" #timeVal").attr("val")));
				var dateTimeVal = curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+curDate.getDate();
				if (dtObj.dateTime) {dateTimeVal += (" " + $("#"+dtObj.id+" [type='time']").val());curDate = new Date(dateTimeVal);}
				$("#"+dtObj.id).attr("val", curDate.getTime());
				$("#"+dtObj.id+" table").remove();
				$("#"+dtObj.id+" [type='dateTime']").val(dateTimeVal);
			});
			$("#"+dtObj.id+" #prepMonth").off("click").on("click", function(){
				curDate = new Date(Number($("#"+dtObj.id+" #timeVal").attr("val")));
				curDate = new Date(curDate.getFullYear(), curDate.getMonth() - 1, 1, curDate.getHours(), curDate.getMinutes(), curDate.getSeconds());
				temp = curDate.getTime();
				$("#"+dtObj.id+" table").remove();
				$("#"+dtObj.id+" [type='dateTime']").click();
			});
			$("#"+dtObj.id+" #nextMonth").off("click").on("click", function(){
				curDate = new Date(Number($("#"+dtObj.id+" #timeVal").attr("val")));
				curDate = new Date(curDate.getFullYear(), curDate.getMonth() + 1, 1, curDate.getHours(), curDate.getMinutes(), curDate.getSeconds());
				temp = curDate.getTime();
				$("#"+dtObj.id+" table").remove();
				$("#"+dtObj.id+" [type='dateTime']").click();
			});
		});
		/*聚焦加回退清空数值*/
		$("#"+dtObj.id).attr("tabindex","0");
		$("#"+dtObj.id).addClass("ice_focus");
		$("#"+dtObj.id).off("keydown").on("keydown", function(e){if (e.keyCode == 8) {$(this).find("[type='dateTime']").val("");}});
		$("#"+dtObj.id).off("mouseenter").on("mouseenter", function(){$(this).focus();});
	},getDT : function(ele){
		if ($(ele).find("[type='dateTime']").val()) {return $(ele).find("[type='dateTime']").val();}
		else{return ""}
	},setDT : function(ele, value){
		if (value == null) {value = "";}
		$(ele).find("[type='dateTime']").val(value);
	},getDate : function(date, isDuo){
		var year = date.getFullYear();
		var month = (date.getMonth() + 1) + "";
		var day = date.getDate() + "";
		if (isDuo == true) {
			if (month.length == 1) {month = "0"+month;}
			if (day.length == 1) {day = "0"+day;}
		}
		return year + "-" + month + "-" + day;
	},getTime : function(date, isDuo){
		var hour = date.getHours() + "";
		var min = date.getMinutes() + "";
		var sec = date.getSeconds() + "";
		if (isDuo == true) {
			if (hour.length == 1) {hour = "0"+hour;}
			if (min.length == 1) {min = "0"+min;}
			if (sec.length == 1) {sec = "0"+sec;}
		}
		return hour + ":" + min + ":" + sec;
	},getDateTime : function(date, dateDuo, timeDuo){
		return this.getDate(date, dateDuo) + " " + this.getTime(date, timeDuo);
	/*提示确认框，有yes和no回调方法，一个dom只能有一个diget*/
	},diget : function(digObj){
		if (!digObj) {return;}
		var title = digObj.title;
		if (title == null) {title = "";}
		var mask = "<div id='ice_mask'><div>" +
				"<span tip>"+title+"</span>" +
				"<div><input type='button' class='bt' value='确认'/><input type='button' class='bt_gray' value='取消'/></div>" +
				"</div></div>";
		$("body").append(mask);
		var unDiget = function(){$("[id='ice_mask']").remove();}
		$("#ice_mask .bt").off("click").on("click", function(){if (digObj.yes) {digObj.yes();}unDiget();});
		$("#ice_mask .bt_gray").off("click").on("click", function(){if (digObj.no) {digObj.no();}unDiget();});
		Ice.init("ice_mask");
	/*遮罩，定时会消除，一个dom只能有一个mask*/
	},mask : function(timeOut, method){
		var time = new Date().getTime();
		var mask = "<div id='ice_mask_load' val='"+time+"'><div class='ice_mask'></div></div>";
		$("body").append(mask);
		if (timeOut) {setTimeout(function(){
			if(Ice.unMask(time)){if (method) {method();}}
		}, Number(timeOut));}
		return time;
	},unMask : function(time){
		if (time) {
			var isRemove = false;
			$("[id='ice_mask_load']").each(function(){if ($(this).attr("val") == time) {$(this).remove();isRemove = true;}});
			return isRemove;
		}else{$("[id='ice_mask_load']").remove();return true;}
	/*将对象转换为key=val&方式，对象下仅支持数组，不支持对象下的对象*/
	},parseUrl : function(obj){
		var pamera = "";
		if (obj) {
			for (key in obj) {
				if (typeof(obj[key]) == "object") {
					for (index in obj[key]) {
						pamera += (key + "=" + obj[key][index] + "&");
					}
				}else{pamera += (key + "=" + obj[key] + "&");}
			}
			if (pamera) {pamera = pamera.substring(0, (pamera.length - 1));}
		}
		return pamera;
	/*全选元素内容*/
	},select : function(element){
		if (document.body.createTextRange) {
            var range = document.body.createTextRange();
            range.moveToElementText($(element)[0]);
            range.select();
        } else if (window.getSelection) {
            var selection = window.getSelection();
            var range = document.createRange();
            range.selectNodeContents($(element)[0]);
            selection.removeAllRanges();
            selection.addRange(range);
        }
	/*自动滚动Div，timeout越大越慢，毫秒计算，step步进，越大越快*/
	},autoDiv : function(id, timeout, step){
		if (timeout) {
			if (!step) {step = 1;}
			if($("#"+id).attr("val")){return;}
			else{$("#"+id).attr("val", "flag");}
			if($("#"+id)[0].scrollTop < ($("#"+id)[0].scrollHeight - $("#"+id).outerHeight())){
				$("#"+id)[0].scrollTop += step;
				setTimeout(function(){$("#"+id).removeAttr("val");Ice.autoDiv(id, timeout, step);}, timeout);
			}else{$("#"+id).removeAttr("val");}
		}else{$("#"+id)[0].scrollTop = $("#"+id)[0].scrollHeight;}
	/*载入新页面，loadObj.close为true会自动生成遮罩，点击遮罩会关闭页面*/
	},load : function(loadObj){
		$("#"+loadObj.id).html('<iframe onclick="return false;" class="ice_iframe" src="'+loadObj.src+'" class="'+loadObj.className+'" style="'+loadObj.style+'"></iframe>');
		if ($("#"+loadObj.id+" > iframe")[0]) {
			if (loadObj.parentStyle) {$("#"+loadObj.id).attr("style", loadObj.parentStyle);}
			if (loadObj.parentClassName) {$("#"+loadObj.id).attr("class", loadObj.parentClassName);}
			$("#"+loadObj.id+" > iframe")[0].contentWindow.name = loadObj.id;
			$("#"+loadObj.id+" > iframe")[0].contentWindow.data = loadObj.data;
			$("#"+loadObj.id+" > iframe").css({'top':loadObj.top,'left':loadObj.left});
			/*如果设置了close布尔值则一定有遮罩*/
			if (loadObj.close == true || loadObj.close == false){
				/*取最大定位，使得当前定位为最上层*/
				var maxIndex = $("#"+loadObj.id+" > iframe.ice_iframe").css("z-index");
				$("iframe.ice_iframe").each(function(){
					var curIndex = $(this).css("z-index");
					if (curIndex >= maxIndex) {maxIndex += 2;}
				});
				/*追加遮罩*/
				$("#"+loadObj.id+" > iframe.ice_iframe").css("z-index", maxIndex);
				$("#"+loadObj.id).append("<div class='ice_iframe_mask' style='z-index: "+(maxIndex - 1)+";'></div>");
				/*如果为关闭*/
				if (loadObj.close == true) {$("#"+loadObj.id+" > div").off("click").on("click", function(){Ice.close(loadObj.id);});}
			}
		}
	/*关闭载入的页面*/
	},close : function(id){		
		var obj = name ? $(parent.document).find("#"+name) : null;
		if (id != null) {obj = $("#"+id);}
		$(obj).removeClass();
		$(obj).attr("style","");
		$(obj).html("");
	/*初始化table表格，根据div数量初始化多个表格，div一定要指定宽和index，index代表td数量*/
	},initTable : function(id){
		var isShowMore = false;
		var htmls = [];
		var moreTitle = "更多";
		var curHeight = $("#"+id).outerHeight();
		var getTable = function(elements, index){
			if (isShowMore == false) {isShowMore = elements.length > index;}
			var table = "<table class='ice_table' index='"+index+"'>";
			var tr = "";
			var curWidths = [];
			var html = [];
			var sum = 0;
			/*设置table每一行tr数据（自适应百分比）*/
			var setTable = function(){
				var lastPercent = 100;
				var lastIndex = curWidths.length - 1;
				for (var j = 0; j < lastIndex; j++) {
					var curPercent = curWidths[j] / sum * 100;
					lastPercent -= curPercent;
					tr += ("<td style='overflow: hidden;height: "+(curHeight - 4)+"px;width: "+curPercent+"%;'></td>");
				}
				tr += ("<td style='overflow: hidden;height: "+(curHeight - 4)+"px;width: "+lastPercent+"%;'></td>");
				table += ("<tr>"+tr+"</tr>");
				tr = "";curWidths = [];sum = 0;
			};
			/*开始赋值*/
			for (var i = 0; i < elements.length; i++) {
				var curWidth = $(elements[i]).outerWidth();
				sum += curWidth;
				curWidths.push(curWidth);
				html.push($(elements[i]).clone(true));
				/*设置并清空*/
				if ((i + 1) % index == 0) {setTable();}
			}
			/*如果还有在设置最后一次*/
			if (curWidths.length > 0) {setTable();}
			htmls.push(html);
			return table + "</table>";
		};
		var tables = $("#"+id+" > div");
		$(tables).css("overflow","hidden");
		for (var i = 0; i < tables.length; i++) {
			$(tables[i]).css({'height':'100%','display':'inline-block'});
			$(tables[i]).html(getTable($(tables[i]).find("> *"), Number($(tables[i]).attr("index"))));
		}
		/*重新追加更多事件*/
		var head = "<div class='ice_more_content' style='"+(isShowMore ? "width: calc(100% - 50px)" : "width: 100%")+";height: "+curHeight+"px;'>"+$("#"+id).html()+"</div>" +
				"<div class='ice_more' style='"+(isShowMore ? "display: inline-block" : "display: none")+";line-height: "+(curHeight - 2)+"px;'>"+moreTitle+"</div>";
		$("#"+id).html(head);
		/*追加所有table元素与事件*/
		$("#"+id+" > div table").each(function(i){
			$(this).find("td").each(function(j){
				htmls[i][j].appendTo($(this));
			});
			/*补充最后不存在的td样式*/
			var tds = $(this).find("tr:last td");
			for (var i = 0, j = Number($(this).attr("index")) - tds.length; i < j; i++) {
				$(this).find("tr:last").append("<td></td>");
			}
		});
		$("#"+id+" .ice_more").off("click").on("click", function(){
			var eles = $("#"+id+" > div > *");
			var curTitle = $(this).prop("firstChild") ? $(this).prop("firstChild").nodeValue : "";
			if (curTitle == moreTitle) {
				$(this).text("收起");
				$(eles).css("overflow","");
				$("#"+id).addClass("ice_index");
				$("#"+id+" table").css("background","linear-gradient(0deg, #fff 0, rgba(160,160,160,0.7))");
				$("#"+id+" table").css("box-shadow","2px 2px 4px 2px #666");
				$("#"+id+" table td").css("border-bottom","2px #333 dotted");
			}else{
				$(this).text(moreTitle);
				$(eles).css("overflow","hidden");
				$("#"+id).removeClass("ice_index");
				$("#"+id+" table").css("background","");
				$("#"+id+" table").css("box-shadow","");
				$("#"+id+" table td").css("border-bottom","");
			}
			return false;
		});
		/*禁止冒泡*/
		$("#"+id+" .ice_more_content").off("click").on("click", function(){return false;});
	/*异步队列任务*/
	},putTask : function(method){
		if (method) {
			this.task.method.push(method);
			/*不加锁的判断，有小几率导致当次添加的方法闲置不立即执行*/
			if (this.task.state == 0) {
				this.task.state = 1;
				setTimeout(function(){
					while(Ice.task.method.length > 0){Ice.task.method.shift()();}
					Ice.task.state = 0;
				}, 0);
			}
		}
	/*键值翻转*/
	},keyToVal : function(obj){
		if (obj) {
			var value = {};
			for (key in obj) {value[obj[key]] = key;}
			return value;
		}
	/*获取cookie值*/
	},getCookie : function(name) {
	    var strCookie = document.cookie;
	    var arrCookie = strCookie.split(";");
	    for(var i = 0; i < arrCookie.length; i++){
	        var arr = arrCookie[i].trim().split("=");
	        if(name == arr[0]){return arr[1].trim();}
	    }
	    return "";
	}
}
