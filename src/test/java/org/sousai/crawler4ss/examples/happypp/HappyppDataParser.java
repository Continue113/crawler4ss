package org.sousai.crawler4ss.examples.happypp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sousai.crawler4ss.parser.DataParser;
import org.sousai.crawler4ss.parser.MatchData;

public class HappyppDataParser implements DataParser{
	private MatchData happyppData = null;

	public MatchData getMatchData(String url, String content) {
		/*
		 * "/home/lei/data"为存储文件的目录,可改为自己的 抓取下来的信息文件,内容分为
		 * 比赛信息源地址,比赛名称,比赛类型,比赛地点,比赛开始时间,截止日期,比赛简介
		 */

		Document doc = Jsoup.parse(content);
		Element matchContent = doc.getElementById("main");

		// 比赛名称
		String name = doc.getElementsByClass("title_top").get(0)
				.getElementsByClass("bold").text();

		System.out.println(name);
		// 比赛类型
		String matchType = matchContent.getElementsByClass("f-left").text();

		// 比赛地点
		String matchAddress = matchContent.getElementsByClass("left")
				.select("li").get(3).text().replace("所在城市：", "");

		// 比赛开始时间
		String time[] = matchContent.getElementsByClass("left")
				.select("span.date").get(0).text().split(" 到 ");
		String matchStartTime = time[0];

		// 截止时间
		String matchDeadline = time[1];

		// 比赛简介
		String matchIntroduction = matchContent.getElementsByClass("left")
				.select("dd").get(0).select("li").last().text()
				.replace("比赛简介：", "");

		happyppData = new MatchData.Builder(url, name).matchType(matchType)
				.matchAddress(matchAddress).matchStartTime(matchStartTime)
				.matchDeadline(matchDeadline)
				.matchIntroduction(matchIntroduction).build();
		return happyppData;
	}
}
