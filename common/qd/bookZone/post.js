var id = ShowBook.BookInfo.BookId;
var title = "支持一下";
var content = "RT [fn=2][fn=2][fn=2]";
for (var i = 0; i < 5; i++) {
	content += content;
}
var millisec = 60000;

ForumAjax.AddForumReviewWithId(id, title, content, "[评论]", "", 0,
					function(result) {
						console.log(result);
						console.log(new Date());
					});
					
var post = setInterval(function() {
			ForumAjax.AddForumReviewWithId(id, title, content, "[评论]", "", 0,
					function(result) {
						console.log(result);
						console.log(new Date());
					})
		}, millisec);

//停止
//window.clearInterval(post);