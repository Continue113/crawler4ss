package org.sousai.crawler4ss.examples.kaiqiu;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.sousai.crawler4ss.database.Db;
import org.sousai.crawler4ss.fetcher.PageFetcher;

public class KaiqiuMatchesUrl {

	public static ArrayList<String> getUrls(String folder) {
		ArrayList<String> urls = new ArrayList<String>();
		Db urlsData = new Db("urls");
		urlsData.start(folder);

		String base[] = {
				"http://www.kaiqiu.cc/home/space-0-do-event-view-all-type-going-page-1.html",
				"http://www.kaiqiu.cc/home/space-0-do-event-view-all-type-going-page-2.html",
				"http://www.kaiqiu.cc/home/space-0-do-event-view-all-type-going-page-3.html",
				"http://www.kaiqiu.cc/home/space-0-do-event-view-all-type-going-page-4.html",
				"http://www.kaiqiu.cc/home/space-0-do-event-view-all-type-going-page-5.html" };
		String html = null;
		PageFetcher pageFetcher = new PageFetcher();
		Document doc = null;
		String url = null;

		for (int i = 0; i < 5; i++) {
			html = pageFetcher.fetchPage(base[i]).getContent();
			doc = Jsoup.parse(html);

			// 一个页面赛事列表
			Elements eventList = doc.getElementById("content")
					.getElementsByClass("event_list").get(0)
					.getElementsByClass("event_content");
			for (int j = 0; j < eventList.size(); ++j) {
				url = "http://www.kaiqiu.cc/home/"
						+ eventList.get(j).getElementsByClass("event_title")
								.get(0).getElementsByTag("a").attr("href");

				urlsData.set("url", url);
				System.out.println("获取比赛链接-->"+url);
			}

		}
		urls = urlsData.selectAll();
		urlsData.close();
		return urls;
	}
}
