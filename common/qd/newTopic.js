var title = "连载佳作推荐" + parseInt(Math.random() * 100);
var content = "读过再退，我为人人。";
jQuery.post("/Ajax/NewTopic.ashx", {
			title : title,
			content : content,
			ajaxMethod : "AddTopic"
		}, function() {
			jQuery.loadingClose();
			var result = arguments[0];
			if (result.ReturnCode == 1) {
				location.href = "/BookList/Create2/" + result.ReturnString;
			} else {
				(new (jQuery.dialog)({
							title : "专题",
							isHTML : true,
							htmlOrUrl : ['<div class="info">'
									+ result.ReturnString + '</div>'].join(""),
							yesBtnStyle : "display:none",
							cancelBtnText : "关闭",
							cancelCallBack : ""
						})).open();
			}
		}, "json");