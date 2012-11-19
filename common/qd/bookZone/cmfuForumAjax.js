function CmfuForumAjax() {
	this.xml = false;

	this.GetXmlHttp = function() {
		/* @cc_on@ */
		/*
		 * @if (@_jscript_version >= 5) try { this.xml = new
		 * ActiveXObject("Msxml2.XMLHTTP"); } catch (e) { try { this.xml = new
		 * ActiveXObject("Microsoft.XMLHTTP"); } catch (e2) { this.xml = false; } }
		 * @end@
		 */

		if (!this.xml && typeof XMLHttpRequest != 'undefined') {
			this.xml = new XMLHttpRequest();
		}
	}
	this.GetXmlHttp();
	var xmlHttp = this.xml;
	var ajax = this;

	var callBack = null;

	this.updatePage = function() {

		if (xmlHttp.readyState == 4) {
			var response = eval('(' + xmlHttp.responseText + ')');

			if (callBack != null && typeof callBack == "function") {
				callBack(response);
			}
		}
	}

	this.toQueryString = function(json) {
		var query = "";
		if (json != null) {
			for (var param in json) {
				query += param + "=" + escape(json[param]) + "&"
			}
		}

		return query;
	}

	this.invoke = function(opName, params, pageCallBack, method) {
		if (xmlHttp) {
			var query = "";
			query += this.toQueryString(params);
			query = query.substring(0, query.length - 1);
			callBack = pageCallBack;

			if (method != null && method.toUpperCase() == "GET") {
				var url = "/ForumAjax.aspx?opName=" + opName + "&" + query;

				xmlHttp.onreadystatechange = ajax.updatePage;
				xmlHttp.open("GET", url, true);
				xmlHttp.setRequestHeader("CMFUFORUMAJAX-Ver", "ver1.0");
				xmlHttp.send(null);
			} else {

				var url = "";
				if (opName.toLowerCase() == "getuserip") {
					url = "/userip.aspx";
				} else {
					url = "/ForumAjax.aspx?opName=" + opName;
				}

				xmlHttp.onreadystatechange = ajax.updatePage;
				xmlHttp.open("POST", url, true);
				xmlHttp.setRequestHeader("Content-type",
						"application/x-www-form-urlencoded");
				xmlHttp.setRequestHeader("CMFUFORUMAJAX-Ver", "ver1.0");
				xmlHttp.send(query);
			}
		}
	}
}

var ForumAjax = {
	ActivityVote : function(threadId, score) {
		new CmfuForumAjax().invoke("ActivityVote", {
					"threadId" : threadId,
					"score" : score
				}, arguments[2]);
	},
	AddForumReview : function(bookId, title, content, postType, validCode,
			isAnonymous) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddForumReview", {
					"bookId" : bookId,
					"title" : title,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous
				}, arguments[6]);
	},

	AddForumReviewWithId : function(bookId, title, content, postType,
			validCode, isAnonymous) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddForumReviewWithId", {
					"bookId" : bookId,
					"title" : title,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous
				}, arguments[6]);
	},

	AddForumReviewLogin : function(bookId, title, content, postType, validCode,
			ptId, password) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddForumReviewLogin", {
					"bookId" : bookId,
					"title" : title,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"ptId" : ptId,
					"password" : password,
					"validateString" : validateString
				}, arguments[7]);
	},

	AddForumReviewLoginWithID : function(bookId, title, content, postType,
			validCode, ptId, password) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddForumReviewLoginWithID", {
					"bookId" : bookId,
					"title" : title,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"ptId" : ptId,
					"password" : password,
					"validateString" : validateString
				}, arguments[7]);
	},

	AddPost : function(forumId, threadId, title, content, validCode,
			isAnonymous) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddPost", {
					"forumId" : forumId,
					"threadId" : threadId,
					"title" : title,
					"content" : content,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous
				}, arguments[6]);
	},
	AddPost2 : function(forumId, threadId, title, content, validCode,
			isAnonymous, refPostIds) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddPost", {
					"forumId" : forumId,
					"threadId" : threadId,
					"title" : title,
					"content" : content,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous,
					"refPostIds" : refPostIds
				}, arguments[7]);
	},
	AddPost3 : function(forumId, threadId, title, content, validCode,
			isAnonymous, refPostIds, iswb) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddPost", {
					"forumId" : forumId,
					"threadId" : threadId,
					"title" : title,
					"content" : content,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous,
					"refPostIds" : refPostIds,
					"iswb" : iswb
				}, arguments[8]);
	},
	AddPostFromShort : function(threadId, title, content, validCode,
			isAnonymous, refPostIds, iswb) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddPostFromShort", {
					"threadId" : threadId,
					"title" : title,
					"content" : content,
					"validCode" : validCode,
					"validateString" : validateString,
					"isAnonymous" : isAnonymous,
					"refPostIds" : refPostIds,
					"iswb" : iswb
				}, arguments[7]);
	},
	GetTicketAvailable : function(UserID, BookID) {
		new CmfuForumAjax().invoke("GetTicketAvailable", {
					"UserID" : UserID,
					"BookID" : BookID
				}, arguments[2]);
	},
	GetUserDiamondInfo : function(diamondUserId) {
		if (arguments.length > 2)
			new CmfuForumAjax().invoke("GetUserDiamondInfo", {
						"diamondUserId" : diamondUserId,
						'diamondtype' : arguments[1]
					}, arguments[2]);
		else
			new CmfuForumAjax().invoke("GetUserDiamondInfo", {
						"diamondUserId" : diamondUserId,
						'diamondtype' : 1
					}, arguments[1]);
	},
	GetUserInfoByID : function(UserID) {
		new CmfuForumAjax().invoke("GetUserInfoByID", {
					"UserID" : UserID
				}, arguments[1]);
	},
	AddPostLogin2 : function(forumId, threadId, title, content, validCode,
			ptId, password, refPostIds) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddPostLogin", {
					"forumId" : forumId,
					"threadId" : threadId,
					"title" : title,
					"content" : content,
					"validCode" : validCode,
					"ptId" : ptId,
					"password" : password,
					"validateString" : validateString,
					"refPostIds" : refPostIds
				}, arguments[8]);
	},
	AddReview2 : function(bookId, content, postType, validCode, reviewtype,
			chapterId, threadId) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddReview2", {
					"bookId" : bookId,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"validateString" : validateString,
					"reviewtype" : reviewtype,
					"chapterId" : chapterId,
					"threadId" : threadId
				}, arguments[7]);
	},
	AddReview : function(bookId, content, postType, validCode, reviewtype,
			chapterId) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddReview", {
					"bookId" : bookId,
					"content" : content,
					"postType" : postType,
					"validCode" : validCode,
					"validateString" : validateString,
					"reviewtype" : reviewtype,
					"chapterId" : chapterId
				}, arguments[6]);
	},
	AddSNSReview : function(forumId, content, validCode, refReviewIds,
			isAnonymous) {
		var validateHdd = document.getElementById("hddValidateCode");
		var validateString = "";
		if (validateHdd != null)
			validateString = validateHdd.value;
		new CmfuForumAjax().invoke("AddSNSReview", {
					"forumId" : forumId,
					"content" : content,
					"validCode" : validCode,
					"refReviewIds" : refReviewIds,
					"isAnonymous" : isAnonymous,
					"validateString" : validateString
				}, arguments[5]);
	},
	ReportComment : function(reviewId) {
		new CmfuForumAjax().invoke("ReportComment", {
					"reviewId" : reviewId
				}, arguments[1]);
	},
	SendRewardAccountInfo : function(toId, toUserName, threadId, amountType,
			cTitle, des) {
		new CmfuForumAjax().invoke("SendRewardAccountInfo", {
					'toId' : toId,
					'toUserName' : toUserName,
					'threadId' : threadId,
					'amountType' : amountType,
					'title' : cTitle,
					'des' : des
				}, arguments[6]);
	},
	AddForumNominationVote : function(bookId, threadId, userId, type) {
		new CmfuForumAjax().invoke("AddForumNominationVote", {
					'bookId' : bookId,
					'threadId' : threadId,
					'userId' : userId,
					'type' : type
				}, arguments[4]);
	}
}
