package org.sousai.crawler4ss.examples.happypp;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sousai.crawler4ss.database.Db;
import org.sousai.crawler4ss.fetcher.PageFetcher;

public class HappyppMatchesUrl {
	public static ArrayList<String> getUrls(String folder) {
		ArrayList<String> urls = new ArrayList<String>();
		Db urlsData = new Db("urls");
		urlsData.start(folder);

		String baseUrl = "http://www.happypingpang.com";
		String pageUrl = null; // 比赛列表页面
		String matchUrl = null; // 比赛具体地址
		Document doc = null;
		PageFetcher pageFetcher = new PageFetcher();
		String content = null;
		for (int i = 1; i < 67; ++i) {
			pageUrl = baseUrl + "/games/all/page:" + i + "?url=%2Fgames%2Fall";
			content = pageFetcher.fetchPage(pageUrl).getContent();
			doc = Jsoup.parse(content);

			Elements urlsEle = doc.getElementsByClass("mid-p")
					.select("a[href]");
			for (int j = 0; j < urlsEle.size(); ++j) {
				matchUrl = baseUrl + urlsEle.get(j).attr("href");
				urlsData.set("url", matchUrl);
				System.out.println("获取比赛链接-->"+matchUrl);
			}
		}
		urls = urlsData.selectAll() ;
		urlsData.close();
		return urls;
	}
}
