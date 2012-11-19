function check() {
	$("btnCreateThread").disabled = "disabled";
	$('ctl00_MainZonePart_ucCreateThread1_hdCheckCode').value = $("hddValidateCode").value;
	if (enableAnonymousThread != null && enableAnonymousThread == "False"
			&& !IsLogin && bookType != "0" && enableReviewChange == "True") {
		forumNew2.DialogReturnMSG('请先登录！', '发表新回复', '0');
		$("btnCreateThread").disabled = "";
		return false;
	}
	var times = 5;
	if (IsLogin) {
		times = 1;
	}
	var body = $('txtBody');
	if (!CheckPostFromCookie("threadid", times, body.value.substring(0, 20))) {
		$("btnCreateThread").disabled = "";
		return false;
	}
	var forumId = "1908768";
	var subject = "\u56de\u590d\uff1a[\u8bc4\u8bba]\u30109\u670810\u65e5\uff0c\u65e5\u62a5\u51fa\u54c1\u3011\u8d76\u65f6\u95f4";
	var inputCode = $('txtValidationCode').value;
	var emptyRegex = /^\s*$/gi;
	var isAnonymous = "0"; // $("chkIsAnonymous").checked ? "1" : "0";
	var refPostIds = $("hddRefPostIds").value; // 评论引用
	if (subject == "" || subject.match(emptyRegex)) {
		forumNew2.DialogReturnMSG('标题不能为空', '发表新回复', '0');
		$("btnCreateThread").disabled = "";
		return false;
	}
	if (body.value == null) {
		forumNew2.DialogReturnMSG('请填写回复内容', '发表新回复', '0');
		body.focus();
		$("btnCreateThread").disabled = "";
		return false;
	} else if (body.value == "无评论，不读书！您的评论是对作者最大的支持，请在此输入您的评论内容") {
		forumNew2.DialogReturnMSG('请填写回复内容', '发表新回复', '0');
		body.focus();
		$("btnCreateThread").disabled = "";
		return false;
	}
	inputCode = inputCode.replace('忽略大小写', '');
	inputCode = inputCode.replace('忽略大小', '');
	if (inputCode == "" && !checkLoginByCookie()) {
		forumNew2.DialogReturnMSG('请填写验证码!', '发表新回复', '0');
		$('valicodeimg').click();
		$('txtValidationCode').focus();
		$("btnCreateThread").disabled = "";
		return false;
	}
	if (subject.length > 45) {
		forumNew2.DialogReturnMSG('标题不能超出45个字数', '发表新回复', '0');
		$("btnCreateThread").disabled = "";
		return false;
	}
	body.value = ReplaceUnsafeKeyword(body.value);
	if (body.value.length > 2000) {
		forumNew2.DialogReturnMSG('评论内容不能超出2000字', '发表新回复', '0');
		$("btnCreateThread").disabled = "";
		return false;
	}
	if (ie) {
		AutoCopy();
	}
	subject = subject.replace(/</g, "&lt;");
	subject = subject.replace(/>/g, "&gt;");
	subject = subject.replace(/\+/g, "＋");
	body.value = body.value.replace(/</g, "&lt;");
	body.value = body.value.replace(/>/g, "&gt;");
	body.value = body.value.replace(/\+/g, "＋");

	SetPostCookie("threadid", body.value.substring(0, 20));
	var iswb = $('chbwt').checked;
	ForumAjax.AddPost3(forumId, "152772484", subject, body.value, inputCode,
			isAnonymous, refPostIds, iswb, ShowCallBackNew);
	return false;
}

ForumAjax.AddPost3("1908768", "152772484", "回复", "[fn=2][fn=2][fn=2][fn=2]", "",
			0, "", false, function (data) {console.log(data)});
