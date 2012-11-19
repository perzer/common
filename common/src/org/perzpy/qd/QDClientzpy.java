package org.perzpy.qd;

import org.apache.log4j.Logger;

/**
 * private use
 * @author perzpy(perzpy@gmail.com)
 * @date Sep 17, 2011
 *
 */
public class QDClientzpy {
	private static Logger log = Logger.getLogger(QDClientzpy.class);
	private static final String[] BIDS = new String[] { "1936629", "2083259", "1908768", "1735921", "1686883",
			"1460814", "2110915", "2110943" }; //签到书籍数组
	private static final int TICKET_NUM = 3; //投票数量
	private static final int TICKET_NUM_LOW = 1;

	public static void main(String[] args) {
		QDOperate client = new QDOperate();
		try {
			//1
			client.login("username", "password", false);
			client.getGift();
			client.voteBook(BIDS[0], TICKET_NUM);
			client.voteBook("2084557", TICKET_NUM); //女频
			client.voteBook("1979635", TICKET_NUM); //文学
			client.sign();
			client.bookCheckIn(BIDS);
			client.abortConnection();
			Thread.sleep(6000);
			log.info("休息60秒……");
			
			//2
			client.login("username", "password", false);
			client.getGift();
			client.voteBook(BIDS[0], TICKET_NUM);
			client.voteBook("2084557", TICKET_NUM); //女频
			client.voteBook("1979635", TICKET_NUM); //文学
			client.sign();
			client.bookCheckIn(BIDS);
			client.abortConnection();
			Thread.sleep(6000);
			log.info("休息60秒……");
			
			//3
			client.login("username", "password", false);
			client.getGift();
			client.voteBook(BIDS[0], TICKET_NUM_LOW);
			client.voteBook("2084557", TICKET_NUM_LOW); //女频
			client.voteBook("1979635", TICKET_NUM_LOW); //文学
			client.sign();
			client.bookCheckIn(BIDS);
			client.abortConnection();
			Thread.sleep(6000);
			log.info("休息60秒……");
			
			
			//4
			client.login("username", "password", false);
			client.getGift();
			client.voteBook(BIDS[0], TICKET_NUM_LOW);
			client.voteBook("2084557", TICKET_NUM_LOW); //女频
			client.voteBook("1979635", TICKET_NUM_LOW); //文学
			client.sign();
			client.bookCheckIn(BIDS);
			client.abortConnection();
			Thread.sleep(6000);
			log.info("休息60秒……");
			
			//5
			client.login("username", "password", false);
			client.getGift();
			client.voteBook(BIDS[0], TICKET_NUM_LOW);
			client.voteBook("2084557", TICKET_NUM_LOW); //女频
			client.voteBook("1979635", TICKET_NUM_LOW); //文学
			client.sign();
			client.bookCheckIn(BIDS);
			client.abortConnection();
			
			log.info("操作全部结束，请及时查看日志！");
			/*client.batchReplyByFile("[fn=4]", true, "/tmp/qd2.txt");
			client.postThread("2084557", "支持一下", "RT");*/
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			client.abortConnection();
		}
	}
}
