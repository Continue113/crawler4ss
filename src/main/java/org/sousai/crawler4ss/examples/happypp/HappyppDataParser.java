package org.sousai.crawler4ss.examples.happypp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.sousai.crawler4ss.parser.DataParser;
import org.sousai.crawler4ss.parser.MatchData;

public class HappyppDataParser implements DataParser {
    private MatchData happyppData = null;

    private Logger logger = Logger.getLogger("HappyppDataParser");

    public MatchData getMatchData(String url, String content) {
        /*
		 * "/home/lei/data"为存储文件的目录,可改为自己的 抓取下来的信息文件,内容分为
		 * 比赛信息源地址,比赛名称,比赛类型,比赛地点,比赛开始时间,截止日期,比赛简介
		 */
        try {
            Document doc = Jsoup.parse(content);
            Element matchContent = doc.getElementById("main");

            // 比赛名称
		/*String name = doc.getElementsByClass("title_top").get(0)
				.getElementsByClass("bold").text();*/
            String name = doc.getElementsByClass("name-a").text();

            // 比赛类型
            String matchType = doc.getElementsByClass("e-left").get(0).select("em").get(0).text();

            // 比赛地点
		/*String matchAddress = matchContent.getElementsByClass("left")
				.select("li").get(3).text().replace("所在城市：", "");*/

            String matchAddress = doc.getElementsByClass("about").get(0)
                    .select("dd").get(3).text().replace("比赛场地：", "");

            // 比赛开始时间
            String time[] = doc.getElementsByClass("dl-01")
                    .select("dd").get(0).text().split("到");
            String matchStartTime = time[0].trim();

            // 截止时间
            int length = time[1].length();
            String matchDeadline = time[1].substring(length - 11, length);

            //中文格式的日期转为英文格式
            DateFormat f1 = new SimpleDateFormat("yyyy年MM月dd日");
            DateFormat f2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = f1.parse(matchStartTime);
                matchStartTime = f2.format(date);
                date = f1.parse(matchDeadline);
                matchDeadline = f2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // 比赛简介
            String matchIntroduction = doc.getElementsByClass("about").text()
                    .replace("比赛简介：", "");

            matchIntroduction = matchStartTime + " 到 " + matchDeadline + "\n"
                    + "比赛场地: " + matchAddress + "\n"
                    + "比赛类型: " + matchType + "\n"
                    + "比赛简介: " + matchIntroduction;


            happyppData = new MatchData.Builder(url, name).matchType(matchType)
                    .matchAddress(matchAddress).matchStartTime(matchStartTime)
                    .matchDeadline(matchDeadline)
                    .matchIntroduction(matchIntroduction).build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return happyppData;
    }
}
