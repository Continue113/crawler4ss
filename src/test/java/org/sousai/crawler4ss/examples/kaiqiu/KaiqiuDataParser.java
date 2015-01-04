package org.sousai.crawler4ss.examples.kaiqiu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sousai.crawler4ss.parser.DataParser;
import org.sousai.crawler4ss.parser.MatchData;

public class KaiqiuDataParser implements DataParser {
	private MatchData kaiqiuData = null;

	public MatchData getMatchData(String url, String content) {
		Document doc = Jsoup.parse(content);
		Element matchContent = null;

		// 获取比赛名称
		matchContent = doc.getElementById("mainarea");
		String name = matchContent.getElementsByClass("title").get(0)
				.getElementsByTag("a").get(1).text();

		// 获取比赛基本信息的其它内容,在id="content"中
		matchContent = doc.getElementById("content");

		/*
		 * String sponsor = matchContent.getElementsByClass("m_box").get(0)
		 * .getElementsByClass("event_content").get(0)
		 * .getElementsByTag("dd").get(0).getElementsByTag("a").text();
		 */

		String matchType = matchContent.getElementsByClass("m_box").get(0)
				.getElementsByClass("event_content").get(0)
				.getElementsByTag("dd").get(1).text();

		String matchAddress = matchContent.getElementsByClass("m_box").get(0)
				.getElementsByClass("event_content").get(0)
				.getElementsByTag("dd").get(2).text();

		String matchStartTime = matchContent.getElementsByClass("m_box").get(0)
				.getElementsByClass("event_content").get(0)
				.getElementsByTag("dd").get(3).text();

		String matchDeadline = matchContent.getElementsByClass("m_box").get(0)
				.getElementsByClass("event_content").get(0)
				.getElementsByTag("dd").get(4).text();

		/*
		 * String peopleNum = matchContent.getElementsByClass("m_box").get(0)
		 * .getElementsByClass("event_content").get(0)
		 * .getElementsByTag("dd").get(5).text();
		 */

		// 此处是比赛内容介绍,需要用到原html文本,但此处为了测试方便直接获取其文本
		String matchIntroduction = matchContent.getElementsByClass("m_box")
				.get(1).text();

		kaiqiuData = new MatchData.Builder(url, name).matchType(matchType)
				.matchAddress(matchAddress).matchStartTime(matchStartTime)
				.matchDeadline(matchDeadline)
				.matchIntroduction(matchIntroduction).build();

		return kaiqiuData;
	}
}
