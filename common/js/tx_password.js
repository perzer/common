$("#pwd").focus(function(){
	if($(this).attr("class") == "gray"){										
		$(this).val("");
		$(this).removeClass("gray");
		$(this).addClass("greencolor");
	}
})

$('#search').click(function(){
	var	score = 0;
	var pwd = $('#pwd').val().toLowerCase();
	var pwd_ori = $('#pwd').val();
	var digit = /^\d+$/;
	var pattern = /^([\u4E00-\u9FA5]|[\uFE30-\uFFA0])*$/gi; 
	var str = /^[a-zA-Z\s]+$/;
	var character = /^\w|\s+$/;
	var specialchar = /[-\da-zA-Z\s`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*((\d+[a-zA-Z\s]+[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+)|(\d+[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+[a-zA-Z\s]+)|([a-zA-Z\s]+\d+[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+)|([a-zA-Z\s]+[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+\d+)|([-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+\d+[a-zA-Z\s]+)|([-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+[a-zA-Z\s]+\d+))[-\da-zA-Z\s`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*/;
	var temp = /^[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+$/;
	var siglechar = /^[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+$/;
	var digit_special = /[-\d`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*\d*[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+[-\d`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*/;
	var char_special =/[-a-zA-Z\s`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*[a-zA-Z\s]*[-`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]+[-a-zA-Z\s`=\\\[\];',.\/~!@#$%^&*()_+|{}:"<>?]*/;
	if(pattern.test(pwd_ori)){
		$(".pwd_box").css("display","block");
		$(".result_box").css("display","none");
		$(".tips").css("display","none");
		$(".result_tips").css("display","block");
		$("#content_tips").html('密码不支持中文输写。');
		$("#title_tips").html('');
		$('#pwd').val("");
	}
	else if(pwd.length <6 && pwd.length >0){
		$(".pwd_box").css("display","none");
		$(".result_box").css("display","block");
		$(".tips").css("display","none");
		$(".result_tips").css("display","block");
		$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/weak.png");
		$("#result_title").html('您的密码强度得分为<span id="score">0</span>分，非常不安全！');	
		$("#result_content").html('您的上网密码太简单了，黑客一秒钟就可以破解，强烈建议更换');
		$("#content_tips").html('您的密码长度太短了，绝大多数网站都已经不支持，最小密码长度为 6 位。');
		$("#title_tips").html('');
	}
	else if(pwd =="输入您要检测的密码" || pwd.length ==0){
		$(".pwd_box").css("display","block");
		$(".result_box").css("display","none");
		$(".tips").css("display","none");
		$(".result_tips").css("display","block");
		$("#content_tips").html('输入您要检测的密码。');
		$("#title_tips").html('');
	}
	else
	{
		if(digit.test(pwd)){
			if(pwd.length%6 > 2)
			{
				score = 1 + parseInt((pwd.length)/6)*2-2 + 1;
			}
			else
			{
				score = 1 + parseInt((pwd.length)/6)*2-2;
			}
			$(".pwd_box").css("display","none");
			$(".result_box").css("display","block");
			$(".tips").css("display","none");
			$(".result_tips").css("display","block");
			$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/weak.png");
			if(score > 50){
				score =50;
			}
			$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常不安全！');	
			$("#result_content").html('您的上网密码太简单了，黑客一秒钟就可以破解，强烈建议更换');
			$("#content_tips").html('安全的密码长度最好8个字符以上，应该包含数字，大小写字母，特殊符号（如@），并且尽量没有规律。');			
			$("#title_tips").html('如何设置安全密码');
		}
		else if(str.test(pwd)){
			if(pwd.length%6 > 1)
			{
				score = 15 + parseInt((pwd.length)/6)*3*5-15 + parseInt(pwd.length%6/2)*5;
			}
			else
			{
				score = 15 + parseInt((pwd.length)/6)*3*5-15;
			}
			$(".pwd_box").css("display","none");
			$(".result_box").css("display","block");
			$(".tips").css("display","none");
			$(".result_tips").css("display","block");
			$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/weak.png");
			if(score > 50){
				score =50;
			}
			$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常不安全！');	
			$("#result_content").html('您的上网密码太简单了，黑客一秒钟就可以破解，强烈建议更换');
			$("#content_tips").html('安全的密码长度最好8个字符以上，应该包含数字，大小写字母，特殊符号（如@），并且尽量没有规律。');
			$("#title_tips").html('如何设置安全密码');
		}
		else if(siglechar.test(pwd)){
			if(pwd.length%6 > 0)
			{
				score = 30 + parseInt((pwd.length)/6)*6*5-30 + pwd.length%6*5;
			}
			else
			{
				score = 30 + parseInt((pwd.length)/6)*6*5-30;
			}
			$(".pwd_box").css("display","none");
			$(".result_box").css("display","block");
			$(".tips").css("display","none");
			$(".result_tips").css("display","block");
			$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/weak.png");
			if(score > 50){
				score =50;
			}
			$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常不安全！');	
			$("#result_content").html('您的上网密码太简单了，黑客一秒钟就可以破解，强烈建议更换');
			$("#content_tips").html('安全的密码长度最好8个字符以上，应该包含数字，大小写字母，特殊符号（如@），并且尽量没有规律。');
			$("#title_tips").html('如何设置安全密码');
		}
		else if(specialchar.test(pwd)){
			var count=0;
			for(var i =0;i<pwd_ori.length;i++)
			{
				if((pwd_ori.charCodeAt(i)>=65)&&(pwd_ori.charCodeAt(i)<=90))
				{
					count++;
				}

			}
			if(pwd.length >7&&count>0)
			{
				score = 80;
				var addscore = 0;
				var end_pwd = pwd.substring(8);					
				if(end_pwd){					
					for(var i =0; i<end_pwd.length; i++){
						var lastchar = end_pwd.charAt(i);
						if(siglechar.test(lastchar))
						{
							addscore += 5;
						}
						else if(str.test(lastchar))
						{
							addscore += 2;
						}
						else if(digit.test(lastchar))
						{
							addscore += 1;
						}
					}						
					score = 80 + addscore;	
				}
				else{
					score = 80;	
				}	
				$(".pwd_box").css("display","none");
				$(".result_box").css("display","block");
				$(".tips").css("display","none");
				$(".result_tips").css("display","block");
					
				if(score > 95){
					score =95;
				}
				$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/strong.png");
				$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常安全！');	
				$("#result_content").html('您的上网密码非常强大，很难被黑客破解！建议定期更换密码进一步保证帐号安全。');
				$("#content_tips").html('<a href="http://www.tianya.cn/" target="_blank">天涯4000万用户密码泄露，马上用强密码加固天涯帐号</a><br/><a href="http://www.csdn.net/" target="_blank">CSDN600万用户密码泄露，马上用强密码加固您的CSDN帐号</a><br/>重要的网站应该每个网站都有自己单独的密码，避免一号丢失多号遭殃。');
				$("#title_tips").html('安全提示');
				
			}
			else
			{
				score = 60;
				var addscore = 0;
				var end_pwd = pwd.substring(6);	
				if(end_pwd){					
						for(var i =0; i<end_pwd.length; i++){
							var lastchar = end_pwd.charAt(i);
							if(siglechar.test(lastchar))
							{
								addscore += 5;
							}
							else if(str.test(lastchar))
							{
								addscore += 2;
							}
							else if(digit.test(lastchar))
							{
								addscore += 1;
							}
						}						
					score = 60 + addscore;	
				}
				else{
					score = 60;	
				}	
				$(".pwd_box").css("display","none");
				$(".result_box").css("display","block");
				$(".tips").css("display","none");
				$(".result_tips").css("display","block");
				if(score >= 80){
					score =80;
					$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/strong.png");
					$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常安全！');	
					$("#result_content").html('您的上网密码非常强大，很难被黑客破解！建议定期更换密码进一步保证帐号安全。');
					$("#content_tips").html('<a href="http://www.tianya.cn/" target="_blank">天涯4000万用户密码泄露，马上用强密码加固天涯帐号</a><br/><a href="http://www.csdn.net/" target="_blank">CSDN600万用户密码泄露，马上用强密码加固您的CSDN帐号</a><br/>重要的网站应该每个网站都有自己单独的密码，避免一号丢失多号遭殃。');
					$("#title_tips").html('安全提示');
				}
				else{
					$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/middle.png");
					$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，安全刚刚及格！');	
					$("#result_content").html('您的上网密码可以被黑客破解，建议修改');	
					$("#content_tips").html('您当前的上网密码仅适合于不重要的网站使用，建议网购，聊天，游戏，微博不使用这个密码。');
					$("#title_tips").html('');
				}

			}
		}
		
		else if(character.test(pwd) || digit_special.test(pwd) || char_special.test(pwd)){
			if(pwd.length%6 > 1)
			{
				score = 40 + parseInt((pwd.length)/6)*3*5-15 + parseInt(pwd.length%6/2)*5;
			}
			else
			{
				score = 40 + parseInt((pwd.length)/6)*3*5-15;
			}
			$(".pwd_box").css("display","none");
			$(".result_box").css("display","block");
			$(".tips").css("display","none");
			$(".result_tips").css("display","block");
			if(score < 50){
				$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/weak.png");
				$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，非常不安全！');
				$("#result_content").html('您的上网密码太简单了，黑客一秒钟就可以破解，强烈建议更换');	
				$("#content_tips").html('安全的密码长度最好8个字符以上，应该包含数字，大小写字母，特殊符号（如@），并且尽量没有规律。');
				$("#title_tips").html('如何设置安全密码');
			}
			else{
				if(score > 60){
					score =60;
				}
				$("#getpic").attr("src","http://pc2.gtimg.com/pcmgr/images/middle.png");
				$("#result_title").html('您的密码强度得分为<span id="score">'+ score +'</span>分，安全刚刚及格！');	
				$("#result_content").html('您的上网密码可以被黑客破解，建议修改');	
				$("#content_tips").html('您当前的上网密码仅适合于不重要的网站使用，建议网购，聊天，游戏，微博不使用这个密码。');
				$("#title_tips").html('');
			}
		}
	}
	
});
