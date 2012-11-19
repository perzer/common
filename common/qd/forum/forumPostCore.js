var urlRewriteEnable = true;

function validateCode(kind) {
    var validatecode = $("validatecode").value;

    if (validatecode == "") {
        TipsDialog('提示', '请填写验证码！', '');
        return false;
    }

    var params = "validatecode=" + validatecode;

    if (kind == 0)
    {
        new Ajax(
            "/Ajax/CommonHandler.ashx",
            params,
            validateCode_callBack,
            "post",
            "validateCode"
        );
    }
    if (kind == 1)
    {
        new Ajax(
            "/Ajax/CommonHandler.ashx",
            params,
            validateCode1_callBack,
            "post",
            "validateCode"
        );
    }
}

function validateCode_callBack() {
    var obj = eval(arguments[0]);
    if (obj.result) {
        addPost(0);
    }
    else {
        TipsDialog('提示', '输入的验证码错误！', '');
        refreshValiadCode();
        return false;
    }
}

function validateCode1_callBack()
{
    var obj = eval(arguments[0]);
    if (obj.result)
    {
        editPost();
    }
    else
    {
        TipsDialog('提示', '输入的验证码错误！', '');
        refreshValiadCode();
        return false;
    }
}

//发帖子
function addPost(kind, obj) {
    obj.disabled = true;

    var title = encodeURIComponent($("txtTitle").value);
    var moduleID = $("hidModuleID").value;
    var reviewID = $("hidreviewID").value;
    var content = encodeURIComponent(record_editor.getHTML());
    var refID = $("hidRefID").value;
    var refKind = $("hidRefKind").value;
    var snsRecordID = $("hidSNSRecordID").value;
    var hidTotalCount =  $("hidTotalCount").value;
    var div_login = $("divLogin");
    if (div_login != null && div_login.style.display != "none" && kind == 0) {
        obj.disabled = false;
        QuickLogin();
        return;
    }

    if (content == '') {
        obj.disabled = false;
        TipsDialog('提示', '内容不能为空，请填写具体内容！', '');
        return false;
    }

    var params = "moduleID=" + moduleID + "&reviewID=" + reviewID +
        "&title=" + title + "&content=" + content + "&refID=" + refID +
        "&refKind=" + refKind + "&SNSRecordID=" + snsRecordID+"&TotalCount="+hidTotalCount;

    new Ajax(
            "/Ajax/PostHandler.ashx",
            params,
            addPost_callBack,
            "post",
            "addPost"
        );
}

function addPost_callBack() {
    var obj = eval(arguments[0]);
    if (obj.result)
    {
       //增加免费投票券
        if (obj.returnString=="1")
        {
           AddBallot(); 
        }
         $("btnSummit").disabled = false;
        switch(obj.returnString)
        {
              case 3:TipsDialog('提示', '帖子回复成功！并恭喜您获得：特制双黄月饼', 'location.reload();');break;
              case 4:TipsDialog('提示', '帖子回复成功！并恭喜您获得：鲜肉月饼', 'location.reload();');break;
              case 5:TipsDialog('提示', '帖子回复成功！并恭喜您获得：豆沙月饼', 'location.reload();');break;
              default:TipsDialog('提示', '帖子回复成功！', 'location.reload();');break;
        }
       
        /*TipsDialog('提示', '帖子回复成功！', 'location.reload();');*/
        //尝试去除客户端缓存
//        var str = window.location.href.replace('#updata_box', '');
//        str = str + "&t=" + String(Math.ceil(Math.random() * 1000));
//        setTimeout("location.reload();", 2000);
    }
    else
    {
        $("btnSummit").disabled = false;
        
           if(obj.returnString.indexOf("审核")>0)
                {
                  TipsDialog('提示', obj.returnString, "location.href=location.href;");
                     
                }
                else
                {
                    TipsDialog('提示', obj.returnString, '');
                }
    }
}

function setRefKind(refID, refKind, refFloor) {
    $("hidRefID").value = refID;
    $("hidRefKind").value = refKind;
    $("txtTitle").value = refFloor + "楼";
}

function QuickLogin(){
  //登录名
  var loginName = encodeURIComponent($("txtLoginName1").value.trim());
  //密码
  var loginPass = encodeURIComponent($("txtLoginPass1").value.trim());

  if (loginName != "" && loginPass != "") {
      var param = "loginName=" + loginName + "&loginPass=" + loginPass;
      new Ajax("/Ajax/LoginHandler.ashx", param, QuickLogin_callBack, "post", "QuickLogin");
  }
  else {
      TipsDialog('提示', '你还没有登陆，请登陆后使用该功能！', '');
  }
}

function QuickLogin_callBack() {
    var obj = eval(arguments[0]);
    if (obj.ReturnCode == 1) {
        addPost(1, $("btnSummit"));
    }
    else {
        TipsDialog('提示', obj.ReturnString, '');
    }
}

function delPost(moduleId, reviewId, postID)
{
    Dialog('删除回复', 'delPost_sure(' + moduleId + ',' + reviewId + ',' + postID + ')', '', true, '<div class=\'confirmAjaxBox\'>确定要删除该回复？</div>');
}

function delPost_sure(moduleId, reviewId, postID)
{
    var params = "&moduleId=" + moduleId + "&reviewId=" + reviewId + "&postID=" + postID;

    new Ajax(
        "/Ajax/PostHandler.ashx",
        params,
        delPost_callBack,
        "post",
        "delPost"
    );
}

function delPost_callBack()
{
    var obj = eval(arguments[0]);
    if (obj.result == 1)
    {
        //尝试去除客户端缓存
        var str = location + "&t=" + String(Math.ceil(Math.random() * 1000));
        setTimeout("location.reload(true);", 2000);
        //location.reload(true);
    }
    else
    {
        TipsDialog('提示', obj.returnString, '');
    }
}

function adminEditPost(moduleId, reviewId, postId) {
    if (urlRewriteEnable) {
        location.href = "/editpost/" + moduleId + "/" + reviewId + "/" + postId;
    }
    else {
        location.href = "/EditPost.aspx?mid=" + moduleId + "&rid=" + reviewId + "&pid=" + postId;
    }
}

function editPost()
{
    var title = encodeURIComponent($("hidTitle").value);
    var moduleID = $("hidModuleId").value;
    var reviewID = $("hidReviewId").value;
    var postId = $("hidPostId").value;
    var content = encodeURIComponent(record_editor.getHTML());

    if (content == '')
    {
        TipsDialog('提示', '内容不能为空，请填写具体内容！', '');
        refreshValiadCode();
        return false;
    }

    var params = "moduleID=" + moduleID + "&reviewID=" + reviewID + " &postID=" + postId + "&title=" + title + "&content=" + content;

    new Ajax(
        "/Ajax/PostHandler.ashx",
        params,
        editPost_callBack,
        "post",
        "editPost"
    );
}

function editPost_callBack()
{
    var obj = eval(arguments[0]);
    if (obj.result == 1)
    {
        TipsDialog('提示', '操作成功！', '');
        //尝试去除客户端缓存
        var str = "&t=" + String(Math.ceil(Math.random() * 1000));
        if (true) {
            setTimeout("location = '/show" + obj.returnString + "?t=" + String(Math.ceil(Math.random() * 1000)) + "'", 2000);
        }
        else {
            setTimeout("location = '/show.aspx" + obj.returnString + str + "'", 2000);
        }
    }
    else
    {
        if(obj.returnString.indexOf("审核")>0)
        {
             TipsDialog('提示', obj.returnString, '');
             setTimeout("location = '/show" + obj.rid + "?t=" + String(Math.ceil(Math.random() * 1000)) + "'", 2000);
        }
        else
        {
            TipsDialog('提示', obj.returnString, '');
        }
        //refreshValiadCode();
    }
}

function updateStatus(moduleId, reviewId, postId, status)
{
//    if (status == 5)
//    {
//        Dialog('删除回复', 'updateStatus_Sure(' + moduleId + ',' + reviewId + ',' + postId + ',' + status + ')', '', true, '<div class=\'confirmAjaxBox\'>确定要删除该回复？</div>');
//    }
//    else
//    {
//        updateStatus_Sure(moduleId, reviewId, postId, status);
    //    }
    Dialog('提示', 'updateStatus_Sure(' + moduleId + ',' + reviewId + ',' + postId + ',' + status + ')', '', true, '是否确认执行该操作？');
}

function updateStatus_Sure(moduleId, reviewId, postId, status)
{
    var params = "&moduleID=" + moduleId + "&reviewID=" + reviewId + "&postID=" + postId + "&status=" + status;

    new Ajax(
            "/Ajax/PostHandler.ashx",
            params,
            updateStatus_callBack,
            "post",
            "updateStatus"
        );
}

function updateStatus_callBack()
{
    var obj = eval(arguments[0]);
    if (obj.result == 1)
    {
        //TipsDialog('提示', '操作成功！', 'location.reload();');
        //setTimeout("location.reload();", 3000);
        location.reload();
    }
    else
    {
        TipsDialog('提示', obj.returnString, '');
    }
}

function AddBallot() 
{ 
     var params="";
        new Ajax(
                "/Ajax/ReviewVoteForCEO30Handler.ashx",
                params,
                AddBallot_callBack,
                "post",
                "AddBallot"
            );

} 
function AddBallot_callBack()
{
    var obj = eval(arguments[0]);
   if (obj)
   {
        if (obj.result == 1)
        {
           //AlertDialog(obj.returnString);
        }
        else
        {
            TipsDialog('提示', obj.returnCode + "|" + obj.returnString, '');
        }
    }

}