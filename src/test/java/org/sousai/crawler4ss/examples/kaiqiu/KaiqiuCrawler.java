package org.sousai.crawler4ss.examples.kaiqiu;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.sousai.crawler4ss.database.Db;
import org.sousai.crawler4ss.frontie.WaitingUrlQueues;
import org.sousai.crawler4ss.fetcher.*;
import org.sousai.crawler4ss.parser.MatchData;

public class KaiqiuCrawler implements Runnable {
	// 需要用到的
	private PageFetcher pageFetcher = null;
	private PageFetchResult pageFetchResult = null;
	private KaiqiuDataParser kaiqiuDataParser = null;
	private MatchData matchData = null;
	private Db matches = null ;

	public KaiqiuCrawler(ArrayList<String> urls,String storageFolder) {
		for (String url : urls) {
			WaitingUrlQueues.add(url);
		}
		
		//启动存储比赛数据的数据库
		matches = new Db("matches") ;
		matches.start(storageFolder);
		
		// 初始化抓取器
		pageFetcher = new PageFetcher();
		
		// 初始化开球网数据分析器
		kaiqiuDataParser = new KaiqiuDataParser();
	}

	public void run() {
		while (!WaitingUrlQueues.isEmpty()) {
			// 从等待队列中提取一个Url
			String urlCurrent = WaitingUrlQueues.poll();
			System.out.println("抓取比赛内容-->"+urlCurrent);
			// 页面内容
			pageFetchResult = pageFetcher.fetchPage(urlCurrent);
			String contentCurrent = pageFetchResult.getContent();

			matchData = kaiqiuDataParser.getMatchData(urlCurrent,
					contentCurrent);
			//downloadFile(matchData);
			saveMatches(matchData) ;
		}
		matches.close();
		JOptionPane.showMessageDialog(null, "Kaiqiu网抓取完成", "提示", 2);
	}
	
	private void saveMatches(MatchData matchData){
		
		/*
		 * "/home/lei/data"为存储文件的目录,可改为自己的 抓取下来的信息文件,内容分为
		 * 比赛信息源地址,比赛名称,比赛类型,比赛地点,比赛开始时间,截止日期,比赛简介
		 */
		String url = matchData.getUrl() ;
		matches.set(url+":url", url);
		
		matches.set(url+":name", matchData.getName());
		
		matches.set(url+":type", matchData.getMatchType());
		
		matches.set(url+":address", matchData.getMatchAddress());
		
		matches.set(url+":starttime", matchData.getMatchStartTime());
		
		matches.set(url+":deadline", matchData.getMatchDeadline());
		
		matches.set(url+":introduction", matchData.getMatchIntroduction());
		
		//matches.close();
	}
	
	public void close(){
		matches.close();
	}

	/*private void downloadFile(MatchData matchData) {
		BufferedWriter bw = null;

		File folder = new File(storageFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File file = new File(storageFolder + "/" + matchData.getName()
				+ ".txt");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(matchData.toString());
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
