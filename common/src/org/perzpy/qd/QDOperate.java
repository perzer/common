package org.perzpy.qd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 模拟QD部分动作
 * 
 * @date Sep 17, 2011
 * 
 */
public class QDOperate {
	//一些提交URL、Referer URL
	private static final String LOGIN_URL = "http://cas.sdo.com/dplogin";
	private static final String REPLY_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=AddPost";
	private static final String REPLY_MM_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=AddPost";
	private static final String REPLY_WX_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=AddPost";
	private static final String POST_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=AddForumReviewWithId";
	private static final String POST_REFERER = "http://forum.qidian.com/BookForumNew.aspx?BookId=";
	private static final String VOTEBOOK_URL = "http://www.qidian.com/ajax.aspx?opName=VoteBook";
	private static final String VOTEBOOK_REFERER = "http://www.qidian.com/pop/showbook/Recommend.aspx?BookId={0}&mt=1&ds=1&ut=1&rand=131200212";
	private static final String GIFTS_URL = "http://me.qidian.com/Ajax/TodayGifts.aspx?random={0}";
	private static final String SIGN_URL = "http://bbs.qidian.com/Ajax/SignHandler.ashx";
	private static final String CHECKIN_URL = "http://www.qidian.com/ajaxcom.aspx?opName=BookCheckIn";
	private static final String CHECKIN_REFERER = "http://www.qidian.com/Pop/ShowBook/BookCheckIn.aspx?bookId={0}&mt=0&ds=1&ut=1&rand=1286923359";
	private static final String SANJIANG_URL = "http://sjg.qidian.com/Ajax.aspx?opName=qd.p.up.sjgvote";
	private static final String SANJIANG_REFERER = "http://sjg.qidian.com/Default.aspx";
	private static final String REWARD_URL = "http://forum.qidian.com/NewForum/List.aspx?bookId={0}&postType=41";
	private static final String IMPRESSION_URL = "http://www.qidian.com/AjaxCom.aspx?opName=pub.p.up.submitreaderlabel";
	private static final String IMPRESSION_REFERER = "http://www.qidian.com/pop/showbook/ReaderLabel.aspx?BookId={0}&mt=1&ds=1&ut=1&categoryId=4&rand=2193865587";
	private static final String NOW_GH_URL = "http://me.qidian.com/comment/friendComment.aspx?userId=2466610";
	private static final String NOW_GH_URL_BAK = "http://me.qidian.com/comment/friendComment.aspx?userId=114264777";
	private static final String TWITTER_URL = "http://me.qidian.com/Ajax/Twitter.ashx";
	private static final String TWITTER_REFERER = "http://me.qidian.com/";
	private static final String ADD_CHAPTER_URL = "http://author.qidian.com/BookMan/AddChapter.aspx?authorBookId={0}&authorBookName={1}";
	private static final String RELEASE_REWARD_REFERER_OLD = "http://forum.qidian.com/Manage/PostManage.aspx?fid={0}&tid={1}";
	private static final String RELEASE_REWARD_REFERER = "http://forum.qidian.com/NewForum/Detail.aspx?threadid={0}";
	private static final String RELEASE_REWARD_URL_OLD = "http://forum.qidian.com/ForumAjax.aspx?opName=ShangHandler";
	private static final String RELEASE_REWARD_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=ShangHandlerNew";
	private static final String ADD_TOPIC_URL = "http://me.qidian.com/Ajax/NewTopic.ashx";
	private static final String FORUM_URL = "http://forum.qidian.com/BookForumNew.aspx?bookId={0}";
	private static final String RELEASE_SCORE_REFERER = "http://forum.qidian.com/Manage/ForumManage.aspx?forumId={0}";
	private static final String RELEASE_SCORE_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=AddScoreByAdmin";
	private static final String FORUM_HOME_URL_OLD = "http://forum.qidian.com/Manage/ForumManage.aspx?forumId={0}";
	private static final String FORUM_HOME_URL = "http://forum.qidian.com/NewForum/List.aspx?bookId={0}";
	private static final String FORUM_DEL_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=pub.p.up.threaddelete";
	private static final String FORUM_DEL_REFERER = "http://forum.qidian.com/NewForum/Pop/DeleteThread.aspx?isgood=false&forumid={0}&threadid={1}&reviewtitle=dd";
	private static final String FORUM_RELEASE_URL = "http://forum.qidian.com/ForumAjax.aspx?opName=pub.p.up.addscorebyadminnew";
	private static final String FORUM_RELEASE_REFERER = "http://forum.qidian.com/NewForum/Pop/Jiang.aspx?forumid={0}&threadid={1}&operationobj={2}&userid=3831365&threadname=d";
	private static final String AUTHOR_DEL_CHAPTER_REFERER = "http://author.qidian.com/BookMan/ChapterVolumeMan.aspx?authorBookId={0}&authorBookName={1}";
	private static final String GET_CHAPTERID_URL = "http://author.qidian.com/Ajax/BookMan.ashx";
	private static final String GET_OLD_EXP_URL = "http://me.qidian.com/Ajax/Occupation.ashx?ajaxMethod=getoldexp&random={0}";
	private static final String MY_PET_URL = "http://me.qidian.com/pet/myPet.aspx";
	private static final String ACTIVITY_HOME_URL = "http://me.qidian.com/profile/activity.aspx";
	private static final String ACTIVITY_HOME_EXECUTE = "http://me.qidian.com/Ajax/Missions.ashx?ajaxMethod=getfinishdaymissionnum";
	private static final String FRIEND_HOME_URL = "http://me.qidian.com/friendIndex.aspx?id={0}";
	private static final Logger log = Logger.getLogger(QDOperate.class);
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private List<NameValuePair> formparams;

	/**
	 * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
	 */
	private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
		// 自定义的恢复策略
		@Override
		public boolean retryRequest(IOException exception, int executionCount,
				HttpContext context) {
			// 设置恢复策略，在发生异常时候将自动重试3次
			if (executionCount >= 3) {
				// Do not retry if over max retry count
				return false;
			}
			if (exception instanceof NoHttpResponseException) {
				// Retry if the server dropped connection on us
				return true;
			}
			if (exception instanceof SSLHandshakeException) {
				// Do not retry on SSL handshake exception
				return false;
			}
			HttpRequest request = (HttpRequest) context
					.getAttribute(ExecutionContext.HTTP_REQUEST);
			boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
			if (!idempotent) {
				// Retry if the request is considered idempotent
				return true;
			}
			return false;
		}
	};

	/**
	 * @return the httpclient
	 */
	public DefaultHttpClient getHttpclient() {
		return httpclient;
	}

	/**
	 * @return the formparams
	 */
	@SuppressWarnings("unused")
	private List<NameValuePair> getFormparams() {
		return formparams;
	}

	/**
	 * 设置httpclient
	 */
	private void setHttpclient() {
		if (httpclient == null) {
			httpclient = new DefaultHttpClient();
		}
		httpclient.setHttpRequestRetryHandler(requestRetryHandler);
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
	}

	/**
	 * 模拟登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public void login(String username, String password, boolean isUseProxy)
			throws Exception {
		setHttpclient();
		log.info(username + " 正在登录……");
		//设置代理
		if (isUseProxy) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					getHttpHost());
		}
		StringBuilder loginUrl = new StringBuilder();
		loginUrl.append(LOGIN_URL);
		loginUrl.append("?method=jQuery1609284083869017707_1341207545773&appId=10&areaId=0&code=2");
		loginUrl.append("&password=" + password + "&username=" + username);
		loginUrl.append("&service=http%3A%2F%2Favd.qidian.com%2FOALoginJump.aspx%3FReturnUrl%3Dhttp%253a%252f%252fme.qidian.com%252fIndex.aspx&infoEx=&tag=10&productId=2&productVersion=2.8.3&_=1341207561947");
		HttpGet get = new HttpGet(loginUrl.toString());
		HttpResponse response = httpclient.execute(get);
		String result = EntityUtils.toString(response.getEntity(), "gb2312");
		get.abort();
		if (result.indexOf("CAPTCHA") > -1) {
			String codeUrl = result.substring(result.indexOf("http:"), result.lastIndexOf("\";"));
			log.info("验证码地址：" + codeUrl);
			throw new Exception("登录失败！");
		}
		get = new HttpGet(result.substring(result.indexOf("http"),
				result.indexOf("\";")));
		response = httpclient.execute(get);
		get.abort();
		get = new HttpGet("http://me.qidian.com/Index.aspx");
		response = httpclient.execute(get);
		result = EntityUtils.toString(response.getEntity(), "gb2312");
		if (result != null && result.contains("起点个人中心")) {
			log.info("登录成功！");
		} else {
			throw new Exception("登录失败！");
		}
	}

	/**
	 * 回复主题
	 * 
	 * @param threadUrl 主题URL
	 * @param content 回复内容
	 * @param isLow 是否是低于500分的帐号
	 */
	public double replyThread(String threadUrl, String content, boolean isLow) {
		if (isBlank(threadUrl)) {
			log.info("回帖地址为空，回帖失败");
			return -1;
		}
		if (isBlank(content)) {
			content = "[fn=2][fn=2]";
		}
		HttpGet get = null;
		HttpPost post = null;
		HttpResponse response = null;
		String result = "";

		/**
		 * 处理回帖地址（QD现在有短网址，处理方式需要判断）
		 */
		try {
			if (threadUrl.contains("u.qidian.com")) {
				//为短网址，需要进行POST请求，得到Headers中的Location
				post = new HttpPost(threadUrl.trim());
				response = httpclient.execute(post);
				post.abort();
				threadUrl = getHeaderInfo(response.getAllHeaders(), "Location");
				if (!threadUrl.contains("forum")) {
					log.info(threadUrl + "短网址解析失败");
					return -1;
				}
			}
			if (isLow && threadUrl.contains("forum.qidian.com")) {
				log.info(threadUrl + " 积分不够500，不能回复此帖！");
				return -1;
			}
			if (!threadUrl.contains("forum")) {
				log.info(threadUrl + "回帖地址出错，回帖失败！");
				return -1;
			}
			//如果地址中含有#XXX的，则为锚记，必须去掉后作为请求地址
			if (threadUrl.contains("#newPost")) {
				threadUrl = threadUrl.substring(0,
						threadUrl.lastIndexOf("#newPost"));
			}
			get = new HttpGet(threadUrl.trim());
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity(), "gb2312");
			//从result中读出指定帖子的ID以及书评区ID
			if (!result.contains("postUtil.forumId")) {
				log.error(threadUrl + " 没有找到书评区信息！");
				return -1;
			}
			String curForumId = result.substring(
					result.indexOf("postUtil.forumId = ") + 19,
					result.indexOf("postUtil.threadId") - 4);
			String curThreadId = null;

			/*
			 * if (threadUrl.contains("qidian")) {
			 * curThreadId = result.substring(result.indexOf("curThreadId = \"") + 15,
			 * result.indexOf("var curReviewId;") - 4);
			 * } else {
			 * curThreadId = result.substring(result.indexOf("curThreadId = \"") + 15,
			 * result.indexOf("var curReviewId;") - 6);
			 * }
			 */
			if (threadUrl.contains("?threadid=")) {
				curThreadId = threadUrl.substring(threadUrl
						.indexOf("?threadid=") + 10);
			} else {
				log.info(threadUrl + "回帖地址出错，回帖失败！");
			}
			log.info("书评区ID：" + curForumId + "，帖子ID：" + curThreadId);

			//构造回帖POST参数
			formparams = new ArrayList<NameValuePair>();
			addPostParameter("forumId", curForumId);
			addPostParameter("threadId", curThreadId);
			addPostParameter("title", "回复：");
			addPostParameter("content", content);
			addPostParameter("validCode", "");
			addPostParameter("validateString", "");
			addPostParameter("isAnonymous", "0");
			addPostParameter("refPostIds", "");
			addPostParameter("iswb", "false");

			//设置回帖POST请求
			String replayURL = REPLY_URL;
			//不同的站点，用不同的提交地址（主站、女频、文学）
			if (threadUrl.contains("qdmm")) {
				replayURL = REPLY_MM_URL;
			} else if (threadUrl.contains("qdwenxue")) {
				replayURL = REPLY_WX_URL;
			}
			post = new HttpPost(replayURL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			//设置请求Headers中的Referer，否则无法回复
			post.setHeader("Referer", threadUrl);
			response = httpclient.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		if (result.contains("成功")) {
			log.info(result);
			if (result.contains("获取")) {
				return Double.parseDouble(result.substring(
						result.indexOf("获取") + 2, result.lastIndexOf("个")));
			}
			return 0;
		}
		if (result.contains("速度太快")) {
			log.info("重新回复");
			replyThread(threadUrl, content, isLow);
		}
		if (result.contains("您访问的页面发生错误")) {
			log.error(threadUrl + " 回复失败！ 服务器出错。重新回复");
		}
		return -1;
	}

	/**
	 * 批量回复主题
	 * 
	 * @param content 回复内容
	 * @param isLow 帐号是否低于500积分
	 * @param filepath 文件路径
	 *            其中文件要求如下，一行放一个主题URL
	 * @return 得到评论积分
	 * @throws Exception
	 */
	public double batchReplyByFile(String content, boolean isLow,
			String filepath) throws Exception {
		File file = new File(filepath);
		if (!file.exists()) {
			throw new Exception("批量回复文件不存在，请检查文件路径是否正确！");
		}
		int total = 0; //文件中符合条件的主题数
		int succ = 0; //回复成功主题数
		int score = 0; //所得评论积分

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String threadUrl = null;
		while ((threadUrl = reader.readLine()) != null) {
			if (threadUrl.indexOf("http") != -1) {
				total += 1;
				log.info(total);
				double tmp = replyThread(threadUrl, content, isLow);
				if (tmp != -1) {
					succ += 1;
					score += tmp;
				}
			}
		}
		reader.close();
		log.info("共回复：" + total + "个帖子，失败：" + (total - succ));
		log.info("共得积分：" + score);
		return score;
	}

	/**
	 * 自动得到恭贺帖地址并回复
	 * 
	 * @param content
	 * @param isLow
	 * @return
	 * @throws Exception
	 */
	public double batchReplyGH(String content, boolean isLow) throws Exception {
		HttpResponse response = null;
		int total = 0; //文件中符合条件的主题数
		int succ = 0; //回复成功主题数
		int score = 0; //所得评论积分
		String nowGHUrl = getNowGHURL(NOW_GH_URL);
		if (isBlank(nowGHUrl)) {
			log.info("最新恭贺帖地址没有找到！");
			return -1;
		}
		response = httpclient.execute(new HttpGet(nowGHUrl));
		String[] result = EntityUtils.toString(response.getEntity())
				.split("\n");
		for (String data : result) {
			if (data.contains("http://u.qidian.com")) {
				String[] allUrl = data
						.split("<a\\shref=\"http://u[.]qidian[.]com/.{6}\"\\stitle=\"");
				for (String value : allUrl) {
					if (value.indexOf("\" target") != -1) {
						total += 1;
						log.info(total);
						double tmp = replyThread(
								value.substring(0, value.indexOf("\" target")),
								content, isLow);
						if (tmp != -1) {
							succ += 1;
							score += tmp;
						}
					}
				}
			}
		}

		log.info("共回复：" + total + "个帖子，失败：" + (total - succ));
		log.info("共得积分：" + score);
		return score;
	}

	/**
	 * 发表主题
	 * 
	 * @param bid 书评区ID
	 * @param title 主题标题
	 * @param content 主题内容
	 */
	public void postThread(String bid, String title, String content)
			throws Exception {
		HttpPost post = null;
		HttpResponse response = null;
		log.info("BID: " + bid);

		//构造发帖POST参数
		formparams = new ArrayList<NameValuePair>();
		addPostParameter("bookId", bid);
		addPostParameter("title", title);
		addPostParameter("content", content);
		addPostParameter("postType", "");
		addPostParameter("validCode", "");
		addPostParameter("validateString", "");
		addPostParameter("isAnonymous", "0");
		addPostParameter("isSynchronous", "false");

		try {
			//设置回帖POST请求
			post = new HttpPost(POST_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

			//设置请求Headers中的Referer，否则无法回复
			post.setHeader("Referer", POST_REFERER + bid);
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 投推荐票
	 * 
	 * @param bid 书籍ID
	 * @param num 票数
	 */
	public void voteBook(String bid, int num) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		//构造投推荐票POST参数
		formparams = new ArrayList<NameValuePair>();
		addPostParameter("BookId", bid);
		addPostParameter("voteCount", String.valueOf(num));
		addPostParameter("ticketAvailable", String.valueOf(num));

		try {
			//设置投推荐票POST请求
			post = new HttpPost(VOTEBOOK_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

			//设置请求Headers中的Referer
			post.setHeader("Referer",
					MessageFormat.format(VOTEBOOK_REFERER, bid));
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 领取个人礼包
	 * 
	 * @throws Exception
	 */
	public void getGift() throws Exception {
		HttpResponse response = null;
		HttpGet get = null;
		String result = "";
		try {
			get = new HttpGet(MessageFormat.format(GIFTS_URL, Math.random()));
			get.setHeader("Referer", "http://me.qidian.com/Index.aspx");
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity());
			if (!result.contains("今天你的礼包中有")) {
				log.info("礼包领取失败！");
				return;
			}

			log.info("个人礼包："
					+ result.substring(result.indexOf("经验值 <em>+"),
							result.indexOf("经验值 <em>+") + 11).replace("<em>",
							"")
					+ "，"
					+ result.substring(result.indexOf("钻石 <em>+"),
							result.indexOf("钻石 <em>+") + 9).replace("<em>", ""));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 论坛打卡
	 * 
	 * @throws Exception
	 */
	public void sign() throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		formparams = new ArrayList<NameValuePair>();
		addPostParameter("content", "");
		addPostParameter("type", "3");
		addPostParameter("img", "a");
		addPostParameter("random", String.valueOf(Math.random()));
		addPostParameter("ajaxMethod", "addusersign");

		try {
			post = new HttpPost(SIGN_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 书籍签到
	 * 
	 * @param bids 单个或书籍数组
	 * @throws Exception
	 */
	public void bookCheckIn(String... bids) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		try {
			for (int i = 0; i < bids.length; i++) {
				formparams = new ArrayList<NameValuePair>();
				addPostParameter("pageTitle", "起点中文小说网");
				addPostParameter("pageDescription", "起点中文小说网");
				addPostParameter("comment", "在这里签到一下，分享感想给书友");
				addPostParameter("bookname", bids[i]);
				addPostParameter("bookid", bids[i]);
				addPostParameter("pageUrl", "http://www.qidian.com/Book/"
						+ bids[i] + ".aspx");

				post = new HttpPost(CHECKIN_URL);
				post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
				post.setHeader("Referer",
						MessageFormat.format(CHECKIN_REFERER, bids[0]));
				response = httpclient.execute(post);
				log.info(EntityUtils.toString(response.getEntity()));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 投三江票
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void voteSJ(String bid) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		formparams = new ArrayList<NameValuePair>();
		addPostParameter("bookList", bid);
		addPostParameter("description", "就喜欢这本。就喜欢这本。就喜欢这本。");

		try {
			post = new HttpPost(SANJIANG_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer", SANJIANG_REFERER);
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 抢悬赏
	 * 
	 * @param bid 书号
	 * @param content 内容
	 * @param time 间隔时间
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	public void getReward(String bid, String content, long time,
			String username, String password) throws Exception {
		login(username, password, false);
		HttpResponse response = null;
		String[] result = null;
		String replyNum = "";
		boolean isFindReply = false;
		String threadUrl = "";
		boolean isFindUrl = false;

		try {
			while (true) {
				response = httpclient.execute(new HttpGet(MessageFormat.format(
						REWARD_URL, bid)));
				result = EntityUtils.toString(response.getEntity()).split("\n");
				for (int i = 0; i < result.length; i++) {
					if (isBlank(result[i]))
						continue;
					//找到最新悬赏回复数
					if (result[i]
							.indexOf("<td align=\"left\" style=\"padding-left:15px;\">") != -1
							&& !isFindReply) {
						replyNum = result[i]
								.replace(
										"<td align=\"left\" style=\"padding-left:15px;\">",
										"").replace("</td>", "")
								.replaceAll(".*/", "").trim();
						isFindReply = true;
					}
					//找到最新悬赏地址
					if (result[i].indexOf("<a target=\"_blank\" href='") != -1
							&& !isFindUrl) {
						threadUrl = result[i]
								.replace("<a target=\"_blank\" href='", "")
								.replaceAll("'\\sstyle='font-size:14px;.*", "")
								.trim();
						isFindUrl = true;
					}
					//如果全部找到，则跳出循环
					if (isFindReply && isFindUrl) {
						isFindReply = isFindUrl = false;
						break;
					}
				}
				log.info("回复数：" + replyNum);
				log.info("回复地址：" + threadUrl);
				if ("0".equals(replyNum.trim())) {
					threadUrl = "http://forum.qidian.com/" + threadUrl;
					log.info("最新悬赏地址：" + threadUrl);
					replyThread(threadUrl, content, false);
					replyNum = threadUrl = "";
				}
				if (time > 0)
					Thread.sleep(time);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 添加读者印象
	 * 
	 * @param bid 书号
	 * @param newLabel 印象
	 * @throws Exception
	 */
	public void addImpression(String bid, String newLabel) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		formparams = new ArrayList<NameValuePair>();
		addPostParameter("bookId", bid);
		addPostParameter("newLabel", newLabel);

		try {
			post = new HttpPost(IMPRESSION_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer",
					MessageFormat.format(IMPRESSION_REFERER, bid));
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 得到最新恭贺帖地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getNowGHURL(String ghURL) throws Exception {
		String url = "";
		boolean isFind = false;
		try {
			HttpResponse response = httpclient.execute(new HttpGet(ghURL));
			String[] result = EntityUtils.toString(response.getEntity()).split(
					"\n");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			String nowDate = (new SimpleDateFormat("M.d"))
					.format(cal.getTime());
			for (String data : result) {
				if (data.contains(nowDate)) {
					isFind = true;
					url = data.substring(data.indexOf("href=\"") + 6,
							data.indexOf("\" target"));
					log.info(nowDate + "最新恭贺帖地址：" + url);
					break;
				}
			}

			if (!isFind) {
				if (ghURL.equals(NOW_GH_URL_BAK))
					return null;
				url = getNowGHURL(NOW_GH_URL_BAK);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return url;
	}

	/**
	 * 发送twitter
	 * 
	 * @param content 发送内容
	 * @throws Exception
	 */
	public void sendTwitter(String content) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		formparams = new ArrayList<NameValuePair>();
		addPostParameter("ajaxMethod", "addtwitter");
		addPostParameter("type", "0");
		addPostParameter("content", content);

		try {
			post = new HttpPost(TWITTER_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer", TWITTER_REFERER);
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void addTopic() throws Exception {
		HttpPost post = null;
		HttpResponse response = null;

		formparams = new ArrayList<NameValuePair>();
		addPostParameter("ajaxMethod", "AddTopic");
		addPostParameter("title", "冒牌书友会" + (int) (Math.random() * 100));
		addPostParameter("content", "人人推荐。");

		try {
			post = new HttpPost(ADD_TOPIC_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			response = httpclient.execute(post);
			log.info(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 登录作者专区
	 * 
	 * @throws Exception
	 */
	public void loginAuthorArea() throws Exception {
		HttpGet get = new HttpGet(
				"http://me.qidian.com/author/authorLogin.aspx");
		try {
			httpclient.execute(get);
			get.abort();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 发布新章节
	 * 
	 * @param bid 书号
	 * @param bookName 书名
	 * @param chapterIndex 更新章节数
	 * @throws Exception
	 */
	public void addChapter(String bid, String bookName, String chapterIndex)
			throws Exception {
		HttpPost post = null;
		HttpResponse response = null;
		String viewstate = "";
		String eventvalidation = "";
		String bookVolume = "";

		try {
			response = httpclient.execute(new HttpGet(MessageFormat.format(
					ADD_CHAPTER_URL, bid, bookName)));
			String[] result = EntityUtils.toString(response.getEntity()).split(
					"\n");
			for (String data : result) {
				if (data.contains("__VIEWSTATE")) {
					viewstate = data.substring(data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("__EVENTVALIDATION")) {
					eventvalidation = data.substring(
							data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("正文")) {
					bookVolume = data.substring(data.indexOf("value=\"") + 7,
							data.lastIndexOf("\">"));
				}

				if (!isBlank(viewstate) && !isBlank(eventvalidation)
						&& !isBlank(bookVolume)) {
					break;
				}
			}

			//构造multipart/form-data数据
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("__EVENTTARGET",
					new StringBody("", Charset.forName("UTF-8")));
			entity.addPart("__EVENTARGUMENT",
					new StringBody("", Charset.forName("UTF-8")));
			entity.addPart("__VIEWSTATE",
					new StringBody(viewstate, Charset.forName("UTF-8")));
			entity.addPart("__EVENTVALIDATION", new StringBody(eventvalidation,
					Charset.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$ddlBookVolume",
					new StringBody(bookVolume, Charset.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$hddVolumeId",
					new StringBody(bookVolume, Charset.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$hddVolumeName",
					new StringBody("正文", Charset.forName("UTF-8")));
			entity.addPart(
					"ctl00$ContentPlaceHolder1$txtChapterName",
					new StringBody("第" + chapterIndex + "章", Charset
							.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$h_chapterimgtype",
					new StringBody("2", Charset.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$txtChapterImgUrl",
					new StringBody("", Charset.forName("UTF-8")));
			entity.addPart(
					"ctl00$ContentPlaceHolder1$txtChapterContent",
					new StringBody(new SimpleDateFormat("yyyy.MM.dd 日记")
							.format(new Date()) + chapterIndex, Charset
							.forName("UTF-8")));
			entity.addPart("ctl00$ContentPlaceHolder1$btnSubmit",
					new StringBody("确认无误，上传新章节", Charset.forName("UTF-8")));

			//此处无附件时，参数必须要，所以新建一个文件
			if (!new File("1.jpg").exists()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"1.jpg"));
				writer.append("111");
				writer.flush();
				writer.close();
			}
			entity.addPart("ctl00$ContentPlaceHolder1$imgChapterImg",
					new FileBody(new File("1.jpg")));

			post = new HttpPost(MessageFormat.format(ADD_CHAPTER_URL, bid,
					bookName));
			post.setEntity(entity);
			post.setHeader("Referer", URLEncoder.encode(
					MessageFormat.format(ADD_CHAPTER_URL, bid, bookName),
					"UTF-8"));
			response = httpclient.execute(post);
			String location = getHeaderInfo(response.getAllHeaders(),
					"Location");
			post.abort();
			if (!isBlank(location)
					&& URLDecoder.decode(
							location.substring(location.indexOf("ret=") + 4),
							"UTF-8").contains("成功")) {
				log.info("章节发布成功");
			} else {
				log.info("章节发布失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 回复所有回复为0的悬赏帖
	 * 
	 * @param bid 书号
	 */
	public List<String> replyAllZeroReward(String bid) throws Exception {
		HttpResponse response = null;
		String[] result = null;
		String replyNum = "";
		String threadUrl = "";
		List<String> rewardURLs = new ArrayList<String>();
		List<String> threadIds = new ArrayList<String>();

		try {
			response = httpclient.execute(new HttpGet(MessageFormat.format(
					REWARD_URL, bid)));
			result = EntityUtils.toString(response.getEntity()).split("\n");
			//找到所有回复为0的悬赏帖的地址
			for (int i = 0; i < result.length; i++) {
				if (isBlank(result[i].trim()))
					continue;
				//找到最新悬赏回复数
				//找到最新悬赏地址
				if (result[i].indexOf("回复数") != -1) {
					replyNum = result[i].substring(
							result[i].indexOf("回复数") + 43,
							result[i].lastIndexOf("</a></p>"));
					threadUrl = result[i].substring(
							result[i].indexOf("href=\"") + 6, result[i].lastIndexOf("\">"));
					rewardURLs.add(threadUrl);
				}
				
			}

			//开始回复
			for (int i = 0; i < rewardURLs.size(); i++) {
				if (isBlank(rewardURLs.get(i)))
					continue;
				threadUrl = "http://forum.qidian.com/NewForum/"
						+ rewardURLs.get(i);
				threadIds.add(threadUrl.substring(threadUrl
						.lastIndexOf("threadid=") + 9));
				log.info("悬赏地址：" + threadUrl);
				replyThread(threadUrl, "[fn=2]", false);
			}
			log.info("BID: " + bid + threadIds);
			return threadIds;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 发送悬赏
	 * 若3次全部发送成功，则删除主题
	 * 
	 * @param bid 书号
	 * @param threadIds 主题ID集合
	 * @throws Exception
	 */
	public void releaseRewardOld(String bid, String... threadIds)
			throws Exception {
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		int succ = 0;
		try {
			for (String threadId : threadIds) {
				log.info("书号：" + bid + "， 悬赏主题ID：" + threadId);
				get = new HttpGet(MessageFormat.format(RELEASE_REWARD_REFERER,
						bid, threadId));
				response = httpclient.execute(get);
				result = EntityUtils.toString(response.getEntity()).split("\n");
				get.abort();
				for (String tmp : result) {
					if (isBlank(tmp))
						continue;
					if (tmp.contains("Javascript:ShowIsJiang")
							&& tmp.contains("3831365")) {
						String[] params = tmp.substring(
								tmp.indexOf("ShowIsJiang(") + 12,
								tmp.lastIndexOf(")'>赏")).split(",");
						formparams = new ArrayList<NameValuePair>();
						addPostParameter("bookId", params[0]);
						addPostParameter("chapterId", params[1]);
						addPostParameter("threadId", params[2]);
						addPostParameter("getUserId", params[3]);
						addPostParameter("getUserNickName", params[4]);
						addPostParameter("postId", params[5]);
						addPostParameter("operateType", params[6]);
						addPostParameter("operatorId", params[7]);
						addPostParameter("operatorName", params[8]);
						addPostParameter("threadSubject", params[9]);
						addPostParameter("isVipBook", params[10]);
						addPostParameter("isVipBook", "1");
						addPostParameter("shangCiGetUserId", params[11]);

						post = new HttpPost(RELEASE_REWARD_URL);
						post.setEntity(new UrlEncodedFormEntity(formparams,
								"UTF-8"));
						post.setHeader("Referer", RELEASE_REWARD_REFERER);
						response = httpclient.execute(post);
						System.out.println(EntityUtils.toString(response
								.getEntity()));
						if (EntityUtils.toString(response.getEntity())
								.contains("1")) {
							succ++;
							log.info("发放悬赏成功");
						} else {
							log.info("发放悬赏失败");
						}
					}
				}
			}
			//全部成功，删除悬赏主题
			if (succ == 3) {
				delFirstPageThread(bid);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 发送悬赏
	 * 若3次全部发送成功，则删除主题
	 * 
	 * @param bid 书号
	 * @param threadIds 主题ID集合
	 * @throws Exception
	 */
	public void releaseReward(String bid, String... threadIds) throws Exception {
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		int succ = 0;
		try {
			for (String threadId : threadIds) {
				log.info("书号：" + bid + "， 悬赏主题ID：" + threadId);
				log.info(MessageFormat.format(RELEASE_REWARD_REFERER,
						threadId));
				get = new HttpGet(MessageFormat.format(RELEASE_REWARD_REFERER,
						threadId));
				response = httpclient.execute(get);
				result = EntityUtils.toString(response.getEntity()).split("\n");
				get.abort();
				String chapterId = "", postId = "", threadSubject = "";
				String getUserId = "3831365";
				String getUserNickName = "%u75AF%u72C2%u5C0F%u6CF0%u54E5";
				for (String tmp : result) {
					if (isBlank(tmp))
						continue;
					if (tmp.indexOf("ChapterId : '") > -1) {
						tmp = tmp.trim();
						chapterId = tmp.substring(
								tmp.indexOf("ChapterId : '") + 13, tmp.lastIndexOf("',"));
					}
					if (tmp.indexOf("ThreadSubject : '") > -1) {
						tmp = tmp.trim();
						threadSubject = tmp.substring(
								tmp.indexOf("ThreadSubject : ''") + 18, tmp.lastIndexOf("',"));
					}
					if (tmp.indexOf("divUserBlock") > -1) {
						tmp = tmp.trim();
						postId = tmp.substring(
								tmp.indexOf("divUserBlock") + 13, tmp.lastIndexOf("\">"));
					}
					
				}
				
				formparams = new ArrayList<NameValuePair>();
				addPostParameter("bookId", bid);
				addPostParameter("chapterId", chapterId);
				addPostParameter("threadId", threadId);
				addPostParameter("getUserId", getUserId);
				addPostParameter("getUserNickName", getUserNickName);
				addPostParameter("postId", postId);
				addPostParameter("threadSubject", threadSubject);
				addPostParameter("isVipBook", "1");
				addPostParameter("shangCiGetUserId", "0");

				post = new HttpPost(RELEASE_REWARD_URL);
				post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
				post.setHeader("Referer",
						MessageFormat.format(RELEASE_REWARD_REFERER, threadId));
				response = httpclient.execute(post);
				String tmp = EntityUtils.toString(response.getEntity());
				if (tmp.contains("1")) {
					succ++;
					log.info("发放悬赏成功");
				} else if (tmp.contains("2")) {
					log.info("悬赏满10次");
				} else {
					log.info("发放悬赏失败");
				}
				post.abort();
			}
			delFirstPageThread(bid);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 得到代理集合
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> getProxys() {
		setHttpclient();
		HttpGet get = new HttpGet("http://www.nbdaili.com/");
		Map<String, Integer> proxyList = new HashMap<String, Integer>();
		try {
			HttpResponse response = httpclient.execute(get);
			String[] result = EntityUtils.toString(response.getEntity()).split(
					"\n");
			for (String tmp : result) {
				if (tmp.contains("</tr><tr bgcolor=\"#FFFFFF\" ")) {
					String[] proxys = tmp
							.trim()
							.substring(tmp.trim().indexOf("</tr>") + 5,
									tmp.lastIndexOf("</table></div>"))
							.split("<tr bgcolor=\"#FFFFFF\"");
					for (String proxy : proxys) {
						if (isBlank(proxy))
							continue;
						proxyList
								.put(proxy.substring(proxy.indexOf("<td>") + 4,
										proxy.indexOf("</td>")),
										Integer.valueOf(proxy.substring(
												proxy.indexOf("</td><td>") + 9,
												proxy.indexOf(
														"</td><td>",
														proxy.indexOf("</td><td>") + 1))));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (proxyList.size() == 0) {
			proxyList = getProxys();
		}
		log.info("共找到 " + proxyList.size() + " 个代理");
		log.info(proxyList);
		return proxyList;
	}

	/**
	 * 得到使用的主机代理
	 * 
	 * @return
	 */
	private HttpHost getHttpHost() {
		HttpHost proxy = null;
		Map<String, Integer> proxys = getProxys();
		int use = (int) (Math.random() * 10);
		int i = 0;
		Iterator<Entry<String, Integer>> iter = proxys.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, Integer> entry = iter.next();
			if (use == i) {
				proxy = new HttpHost(entry.getKey().toString(),
						Integer.valueOf(entry.getValue().toString()));
				log.info("使用的代理为：" + entry.getKey() + ":" + entry.getValue());
				break;
			}
			i++;
		}
		return proxy;
	}

	/**
	 * 回复专题区的帖子
	 * 
	 * @param threadUrl
	 */
	public void replyTopic(String threadUrl) {
		if (isBlank(threadUrl)) {
			log.info("回帖地址为空，回帖失败");
			return;
		}
		HttpGet get = null;
		HttpPost post = null;
		HttpResponse response = null;
		String result = "";

		try {
			if (!threadUrl.contains("forum")) {
				log.info(threadUrl + "回帖地址出错，回帖失败！");
				return;
			}
			//如果地址中含有#XXX的，则为锚记，必须去掉后作为请求地址
			if (threadUrl.contains("#newPost")) {
				threadUrl = threadUrl.substring(0,
						threadUrl.lastIndexOf("#newPost"));
			}
			get = new HttpGet(threadUrl.trim());
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity(), "gb2312");
			//从result中读出指定帖子的ID以及书评区ID
			if (!result.contains("var forumId = \"")) {
				log.error(threadUrl + " 没有找到专题区信息！");
				return;
			}
			String curForumId = result.substring(
					result.indexOf("forumId = \"") + 11,
					result.indexOf("forumId = \"") + 19);
			String curThreadId = result.substring(
					result.indexOf("threadId = \"") + 12,
					result.indexOf("threadId = \"") + 21);

			log.info("专题区ID：" + curForumId + "，帖子ID：" + curThreadId);

			//构造回帖POST参数
			formparams = new ArrayList<NameValuePair>();
			addPostParameter("forumId", curForumId);
			addPostParameter("threadId", curThreadId);
			addPostParameter("title", "回复：");
			addPostParameter("content", "[fn=2]");
			addPostParameter("validCode", "");
			addPostParameter("validateString", "");
			addPostParameter("isAnonymous", "0");
			addPostParameter("refPostIds", "");

			//设置回帖POST请求
			post = new HttpPost(REPLY_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			//设置请求Headers中的Referer，否则无法回复
			post.setHeader("Referer", threadUrl);
			response = httpclient.execute(post);
			result = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		if (result.contains("成功")) {
			log.info(result);
			return;
		}
		log.error(threadUrl + " 回复失败！ " + result);
		if (result.contains("速度太快")) {
			log.info("重新回复");
			replyTopic(threadUrl);
		}
	}

	/**
	 * 回复所有专题区回复为0的主题
	 * 
	 * @param bid 专题区ID
	 * @throws Exception
	 */
	public void replyAllZeroTopic(String bid) throws Exception {
		HttpResponse response = null;
		String[] result = null;
		String replyNum = "";
		String threadUrl = "";
		List<String> rewardURLs = new ArrayList<String>();

		try {
			response = httpclient.execute(new HttpGet(MessageFormat.format(
					FORUM_URL, bid)));
			result = EntityUtils.toString(response.getEntity()).split("\n");
			//找到所有回复为0的主题的地址
			for (int i = 0; i < result.length; i++) {
				if (isBlank(result[i]))
					continue;
				//找到最新地址
				if (result[i].indexOf("<a target=\"_blank\" href='") != -1) {
					threadUrl = result[i]
							.replace("<a target=\"_blank\" href='", "")
							.replaceAll("'\\sstyle='font-size:14px;.*", "")
							.trim();
				}

				//找到最新回复数
				if (result[i]
						.indexOf("<td align=\"left\" style=\"padding-left:15px;\">") != -1) {
					replyNum = result[i]
							.replace(
									"<td align=\"left\" style=\"padding-left:15px;\">",
									"").replace("</td>", "")
							.replaceAll(".*/", "").trim();
					if ("0".equals(replyNum)) {
						rewardURLs.add(threadUrl);
					}
				}
			}

			//开始回复
			for (int i = 0; i < rewardURLs.size(); i++) {
				if (isBlank(rewardURLs.get(i)))
					continue;
				threadUrl = "http://forum.qidian.com/" + rewardURLs.get(i);
				log.info("主题地址：" + threadUrl);
				replyTopic(threadUrl);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 发送专题区积分奖励
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void releaseScoreOld(String bid) throws Exception {
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		try {
			log.info("书号：" + bid);
			get = new HttpGet(MessageFormat.format(RELEASE_SCORE_REFERER, bid));
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity()).split("\n");
			get.abort();
			for (String tmp : result) {
				if (isBlank(tmp))
					continue;
				if (tmp.contains("Javascript:ShowDetail")
						&& tmp.contains("3831365")) {
					String[] params = tmp.substring(
							tmp.indexOf("ShowDetail(") + 11,
							tmp.lastIndexOf(")'>奖")).split(",");
					formparams = new ArrayList<NameValuePair>();
					addPostParameter("forumId", params[0]);
					addPostParameter("operationObj", params[1]);
					addPostParameter("userId", params[2]);
					addPostParameter("amount", "15");
					addPostParameter("threadName",
							params[3].replaceAll("\"", ""));
					addPostParameter("operationName",
							params[4].replaceAll("\"", ""));
					addPostParameter("operation", "1");
					addPostParameter("threadID", params[2]);

					post = new HttpPost(RELEASE_SCORE_URL);
					post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
					post.setHeader("Referer", RELEASE_SCORE_REFERER);
					response = httpclient.execute(post);
					log.info(EntityUtils.toString(response.getEntity()));
					post.abort();
				}
			}
			//删除第一页所有帖子
			delFirstPageThread(bid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 发送书评区积分奖励
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void releaseScore(String bid) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;
		List<String> threadIds = getFirstPageThreadIds(bid);
		try {
			for (String threadId : threadIds) {
				formparams = new ArrayList<NameValuePair>();
				addPostParameter("forumid", bid);
				addPostParameter("operationObj", threadId);
				addPostParameter("userId", "3831365");
				addPostParameter("amount", "15");
				addPostParameter("threadName", "");
				addPostParameter("operationName", "奖赏");
				addPostParameter("operation", "1");
				addPostParameter("threadID", threadId);
				
				post = new HttpPost(FORUM_RELEASE_URL);
				post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
				post.setHeader("Referer", MessageFormat.format(FORUM_RELEASE_REFERER, bid, threadId, threadId));
				response = httpclient.execute(post);
				String postResult = EntityUtils.toString(response.getEntity());
				log.info(threadId + postResult);
				post.abort();
			}

			delFirstPageThread(bid);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 删除书评区第一页所有主题
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void delFirstPageThreadOld(String bid) throws Exception {
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		String viewstate = "";
		String eventvalidation = "";
		List<String> delThreadSign = new ArrayList<String>();
		try {
			log.info("书号：" + bid);
			get = new HttpGet(MessageFormat.format(FORUM_HOME_URL, bid));
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity()).split("\n");
			get.abort();
			for (String data : result) {
				if (isBlank(data))
					continue;
				if (data.contains("__VIEWSTATE")) {
					viewstate = data.substring(data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("__EVENTVALIDATION")) {
					eventvalidation = data.substring(
							data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("name=\"ctl00$ctl00$MainZonePart$MainZonePart$rptThreadList$ctl")) {
					delThreadSign
							.add(data.substring(data.indexOf("name=\"") + 6,
									data.lastIndexOf("\"")));
				}
			}
			formparams = new ArrayList<NameValuePair>();
			addPostParameter("__EVENTTARGET", "");
			addPostParameter("__EVENTARGUMENT", "");
			addPostParameter("__VIEWSTATE", viewstate);
			addPostParameter("__EVENTVALIDATION", eventvalidation);
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$txtBody",
					URLEncoder.encode("版主公告，在书评论区会显示", "gbk"));
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$btnDeleteAll",
					URLEncoder.encode("删除", "gbk"));
			for (String str : delThreadSign) {
				addPostParameter(str, "on");
			}
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$inputSearch", "");
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$ddlForbidDay", "7");
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$inputItem",
					"");
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$txtReason",
					"");

			post = new HttpPost(MessageFormat.format(FORUM_HOME_URL, bid));
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer", MessageFormat.format(FORUM_HOME_URL, bid));
			response = httpclient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				post.abort();
				log.info("删除已执行");
				return;
			}
			post.abort();
			log.info("删除未执行");

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 删除书评区第一页所有主题
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void delFirstPageThread(String bid) throws Exception {
		HttpPost post = null;
		HttpResponse response = null;
		List<String> threadIds = getFirstPageThreadIds(bid);
		try {
			for (String threadId : threadIds) {
				formparams = new ArrayList<NameValuePair>();
				addPostParameter("isgood", "false");
				addPostParameter("forumid", bid);
				addPostParameter("threadid", threadId);
				addPostParameter("reviewtitle", "");
				addPostParameter("reason", "");
				post = new HttpPost(FORUM_DEL_URL);
				post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
				post.setHeader("Referer", MessageFormat.format(FORUM_DEL_REFERER, bid, threadId));
				response = httpclient.execute(post);
				String postResult = EntityUtils.toString(response.getEntity());
				if (postResult.indexOf("成功删除") > -1) {
					log.info(threadId + "删除成功");
				} else {
					log.info(threadId + "删除失败");
				}
				post.abort();
			}

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	/**
	 * 得到书评区第一页所有主题ID
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public List<String> getFirstPageThreadIds(String bid) throws Exception {
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		String threadId = "";
		List<String> threadIds = new ArrayList<String>();
		try {
			log.info("书号：" + bid);
			get = new HttpGet(MessageFormat.format(FORUM_HOME_URL, bid));
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity()).split("\n");
			get.abort();
			for (int i = 0; i < result.length; i++) {
				if (isBlank(result[i].trim()))
					continue;
				if (result[i].indexOf("splb_tab_h4") > -1) {
					threadId = result[i].substring(
							result[i].indexOf("threadid=") + 9,
							result[i].lastIndexOf("\" target"));
					threadIds.add(threadId);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
		return threadIds;
	}

	/**
	 * 删除001章以外的所有章节
	 * 
	 * @param bid 书号
	 * @param bookName 书名
	 * @throws Exception
	 */
	public void delChapter(String bid, String bookName) throws Exception {
		HttpResponse response = null;
		HttpGet get = null;
		HttpPost post = null;
		String[] result;
		String bodyId = ""; //正文分卷ID
		String recycleId = ""; //回收站分卷ID
		try {
			get = new HttpGet(MessageFormat.format(AUTHOR_DEL_CHAPTER_REFERER,
					bid, bookName));
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity()).split("\\n");

			if (result == null || result.length == 0) {
				log.info(bid + "：无法取得分卷ID");
				return;
			}
			//取得正文分卷ID用来得到所有章节ID
			for (String data : result) {
				if (isBlank(data))
					continue;
				if (data.contains("SelVolumeStr")) {
					bodyId = data.substring(data.indexOf("正文</option>") - 14)
							.replaceAll(":\\S*\\s*\\S*", "");
					recycleId = data.substring(
							data.indexOf("回收站</option>") - 14,
							data.indexOf("回收站</option>")).replaceAll(
							":\\S*\\s*\\S*", "");
				}

				if (!isBlank(bodyId) && !isBlank(recycleId))
					break;
			}
			addPostParameter("bookId", bid);
			addPostParameter("volumeId", bodyId);
			addPostParameter("ajaxMethod", "chapterdataget");
			post = new HttpPost(GET_CHAPTERID_URL);
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer", URLEncoder.encode(MessageFormat.format(
					AUTHOR_DEL_CHAPTER_REFERER, bid, bookName), "UTF-8"));
			response = httpclient.execute(post);

			if (response.getStatusLine().getStatusCode() != 200) {
				log.info(bid + "：无法取得正文所有章节ID");
				return;
			}
			result = EntityUtils.toString(response.getEntity()).split("},");
			post.abort();
			for (int i = 0; i < result.length; i++) {
				String data = result[i];
				if (isBlank(data))
					continue;
				//删除001章以外的所有章节
				if (i == 0)
					continue;
				if (!data.contains("ci"))
					continue;
				log.info(data.substring(data.indexOf("ci:") + 3,
						data.indexOf(",cn")));
				formparams = new ArrayList<NameValuePair>();
				addPostParameter("bookId", bid);
				addPostParameter(
						"chapterId",
						data.substring(data.indexOf("ci:") + 3,
								data.indexOf(",cn")));
				addPostParameter("volumeId", recycleId);
				addPostParameter("volumeCode", "-10");
				addPostParameter("volumeName",
						URLEncoder.encode("回收站", "utf-8"));
				addPostParameter("ajaxMethod", "changevolume");
				post = new HttpPost(GET_CHAPTERID_URL);
				post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
				post.setHeader("Referer", URLEncoder.encode(MessageFormat
						.format(AUTHOR_DEL_CHAPTER_REFERER, bid, bookName),
						"UTF-8"));
				post.setHeader("Accept",
						"application/json, text/javascript, */*");
				post.setHeader("X-Requested-With", "XMLHttpRequest");
				response = httpclient.execute(post);
				if (EntityUtils.toString(response.getEntity()).contains("成功")) {
					post.abort();
					log.info("调卷成功");
				} else {
					post.abort();
					delChapter(bid, bookName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 每日任务
	 * 
	 * @param checkInBooks 签到书籍数组
	 * @param sjBook 三江投票书号
	 * @throws Exception
	 */
	public void dailyTash(String[] checkInBooks, String sjBook)
			throws Exception {
		getGift();
		sign();
		bookCheckIn(checkInBooks);
		voteSJ(sjBook);
	}

	/**
	 * 对书评区第一页所有主题进行加精
	 * 
	 * @param bid 书号
	 * @throws Exception
	 */
	public void addEssence(String bid) throws Exception {
		HttpPost post = null;
		HttpGet get = null;
		HttpResponse response = null;
		String[] result = null;
		String viewstate = "";
		String eventvalidation = "";
		List<String> addThreadSign = new ArrayList<String>();
		try {
			log.info("书号：" + bid);
			get = new HttpGet(MessageFormat.format(FORUM_HOME_URL, bid));
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity()).split("\n");
			get.abort();
			for (String data : result) {
				if (isBlank(data))
					continue;
				if (data.contains("__VIEWSTATE")) {
					viewstate = data.substring(data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("__EVENTVALIDATION")) {
					eventvalidation = data.substring(
							data.indexOf("value=\"") + 7,
							data.lastIndexOf("\" />"));
				}

				if (data.contains("name=\"ctl00$ctl00$MainZonePart$MainZonePart$rptThreadList$ctl")) {
					addThreadSign
							.add(data.substring(data.indexOf("name=\"") + 6,
									data.lastIndexOf("\"")));
				}
			}

			formparams = new ArrayList<NameValuePair>();
			addPostParameter("__EVENTTARGET", "");
			addPostParameter("__EVENTARGUMENT", "");
			addPostParameter("__VIEWSTATE", viewstate);
			addPostParameter("__EVENTVALIDATION", eventvalidation);
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$txtBody",
					URLEncoder.encode("版主公告，在书评论区会显示", "gbk"));
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$btnGoodAll",
					URLEncoder.encode("加精", "gbk"));
			for (String str : addThreadSign) {
				addPostParameter(str, "on");
			}
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$inputSearch", "");
			addPostParameter(
					"ctl00$ctl00$MainZonePart$MainZonePart$ddlForbidDay", "7");
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$inputItem",
					"");
			addPostParameter("ctl00$ctl00$MainZonePart$MainZonePart$txtReason",
					"");

			post = new HttpPost(MessageFormat.format(FORUM_HOME_URL, bid));
			post.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));
			post.setHeader("Referer", MessageFormat.format(FORUM_HOME_URL, bid));
			response = httpclient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				post.abort();
				log.info("加精已执行");
				return;
			}
			post.abort();
			log.info("加精未执行");

		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 定时发帖
	 * 
	 * @param specialHour 特定的时间
	 * @param specialMinute
	 * @param post 帖子属性
	 * @throws Exception
	 */
	public void timingPost(int specialHour, int specialMinute, String... post)
			throws Exception {
		if (post == null || post.length == 0) {
			throw new Exception("发帖不明确！");
		}
		/**
		 * 分析帖子属性
		 */
		String bid = post[0];
		String title = post.length < 2 ? "支持一下" : post[1];
		String content = post.length < 2 ? "支持一下" : post[2];
		while (true) {
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);

			if (hour != specialHour)
				continue;
			if (minute != specialMinute)
				continue;
			if (second > 10)
				continue;
			postThread(bid, title, content);
			log.info("post time: " + cal.getTime());
			log.info("end");
			break;
		}
	}

	/**
	 * 领取2013.01.17之前的经验
	 */
	public void getOldExp() throws Exception {
		HttpResponse response = null;
		HttpGet get = null;
		String result = "";
		try {
			get = new HttpGet(MessageFormat.format(GET_OLD_EXP_URL,
					Math.random()));
			get.setHeader("Referer", "http://me.qidian.com/Index.aspx");
			response = httpclient.execute(get);
			result = EntityUtils.toString(response.getEntity());
			log.info(result.substring(result.indexOf("Msg\":\"") + 6,
					result.lastIndexOf("\"")));
			get.abort();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 宠物签到
	 */
	public void signMyPet() throws Exception {
		visitURLByGet(MY_PET_URL);
	}

	/**
	 * 获取活跃度
	 * 
	 * @throws Exception
	 */
	public void getActivity() throws Exception {
		visitURLByGet(ACTIVITY_HOME_URL);
		visitURLByGet(ACTIVITY_HOME_EXECUTE);
		String[] friends = new String[] { "3188362", "130116907", "12685579",
				"21456503", "115921869", "610769" };
		for (int i = 0; i < friends.length; i++) {
			visitURLByGet(MessageFormat.format(FRIEND_HOME_URL, friends[i]));
		}
	}

	/**
	 * 通过get方式访问指定网址
	 * 
	 * @param url
	 * @throws Exception
	 */
	private void visitURLByGet(String url) throws Exception {
		try {
			HttpGet get = new HttpGet(url);
			get.setHeader("Referer", "http://me.qidian.com/Index.aspx");
			httpclient.execute(get);
			get.abort();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 释放HttpClient连接
	 */
	public void abortConnection() {
		if (httpclient != null) {
			httpclient.getConnectionManager().shutdown();
			httpclient = null;
		}
	}

	/**
	 * 添加POST提价参数
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public void addPostParameter(String name, String value) {
		this.formparams.add(new BasicNameValuePair(name, value));
	}

	/**
	 * 根据key得到Header的值
	 * 
	 * @param header
	 * @param key
	 * @return
	 */
	public String getHeaderInfo(Header[] header, String key) {
		if (key == null || "".equals(key.trim()))
			return null;
		for (Header h : header) {
			if (h.getName().equals(key.trim()))
				return h.getValue();
		}
		return null;
	}

	/**
	 * 解析起点短网址
	 * 
	 * @param url
	 * @return
	 */
	public String parseShortUrl(String url) {
		if (url.contains("u.qidian.com")) {
			HttpGet get = null;
			HttpPost post = null;
			HttpResponse response = null;
			//为短网址，需要进行POST请求，得到Headers中的Location
			post = new HttpPost(url.trim());
			try {
				response = httpclient.execute(post);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			post.abort();
			url = getHeaderInfo(response.getAllHeaders(), "Location");
		}
		return url;
	}

	/**
	 * @param cs 字符串
	 * @return 是不是为空白字符串
	 */
	private boolean isBlank(CharSequence cs) {
		if (null == cs)
			return true;
		int length = cs.length();
		for (int i = 0; i < length; i++) {
			if (!(Character.isWhitespace(cs.charAt(i))))
				return false;
		}
		return true;
	}

}
