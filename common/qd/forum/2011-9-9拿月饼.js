function addPost_callBack() {
    var obj = eval(arguments[0]);
    if (obj.result)
    {
        //AddBallot(); 
        switch(obj.returnString)
        {
              case 3:console.log(new Date() + "特制双黄月饼");break;
              case 4:console.log(new Date() + "鲜肉月饼");break;
              case 5:console.log(new Date() + "豆沙月饼");break;
              default:console.log(new Date() + "nothing");break;
        }
    } else 
	console.log(new Date() + "稍等");
}

new Ajax("/Ajax/PostHandler.ashx","moduleID=100&reviewID=409789&title=goodtip&content=我吃月饼啊&refID=-1&refKind=-1&SNSRecordID=0&TotalCount=1932",addPost_callBack,"post","addPost");

var post = setInterval(function(){new Ajax("/Ajax/PostHandler.ashx","moduleID=100&reviewID=409789&title=goodtip&content=我的啊&refID=-1&refKind=-1&SNSRecordID=0&TotalCount=1932",addPost_callBack,"post","addPost")}, 180000);

//window.clearInterval(post);

//Peryy 3个豆沙
//疯狂 2个鲜肉1个豆沙
//perzer 2个豆沙1个鲜肉
//寒枫 3个鲜肉
//心跳 3个豆沙

//“豆沙月饼”包含：1个起点书架藏书量+9个钻石+99起点经验值

//“鲜肉月饼”包含：9个钻石+99起点币
