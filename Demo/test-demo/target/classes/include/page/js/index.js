/*初始化*/
var ws = Ice.initWS("ws://127.0.0.1:9999?chengshi", null, onMsg);
$(function(){
	Ice.init();
})

/*测试一下*/
function test(){
	var data = Ice.getBind("test");
	Ice.tip(data.info);
}

/*后端传递的消息*/
function onMsg(msg){
	Ice.success(msg);
}

/*交互内容*/
function send(){
	var data = Ice.getBind("ws");
	ws.send(data.info);
}