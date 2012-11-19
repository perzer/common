
var bid; //论坛ID
var title = "回复"; // 回复标题
var postLen = 2; // 帖子表情长度
var millisec = 60000; // 时间间隔
var postId; // 帖子ID
if (curForumId) {
	bid = curForumId;
} else {
	alert("论坛ID为空");
}
if (curThreadId) {
	postId = curThreadId;
} else {
	alert("主题ID为空");
}

// 表情格式[fn=1]到[fn=60]
var NO_USE_FEEL = "10;11;12;13;14;15;16;17;18;19;20;21;22;23;24;25;27;29;30;34;38;39;40;44;54;55;56;"; // 不使用的表情

/**
 * 随机生成回帖内容 
 * @param  count 生成表情个数
 * @return 生成内容
 */
function createContent(count) {
	var num;
	var content = "";
	for (var i = 0; i < count; i++) {
		num = Math.round(Math.random() * 45);
		if (num == 0) num = 32;
		if (num > 9 && NO_USE_FEEL.indexOf(num + "") >= 0) num = 32;
		content += "[fn=" + num + "]";
	}
	return content;
}

ForumAjax.AddPost3(bid, postId, title, createContent(postLen), "", 0, "",
		false, function(data) {
			console.log(data);
			console.log(new Date());
		});
		
var reply = setInterval(function() {
			ForumAjax.AddPost3(bid, postId, title, createContent(postLen), "",
					0, "", false, function(data) {
						console.log(data);
						console.log(new Date());
					});
		}, millisec);
// window.clearInterval(reply);
