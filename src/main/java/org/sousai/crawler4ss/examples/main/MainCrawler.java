package org.sousai.crawler4ss.examples.main;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.sousai.crawler4ss.database.Db;
import org.sousai.crawler4ss.database.Jdbc;
import org.sousai.crawler4ss.examples.happypp.HappyppCrawler;
import org.sousai.crawler4ss.examples.happypp.HappyppMatchesUrl;
import org.sousai.crawler4ss.examples.kaiqiu.KaiqiuCrawler;
import org.sousai.crawler4ss.examples.kaiqiu.KaiqiuMatchesUrl;
import org.sousai.crawler4ss.parser.MatchData;

public class MainCrawler implements ActionListener {
	JFrame frame = new JFrame("抓取比赛数据");// 框架布局
	JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
	Container con1 = new Container();
	Container con2 = new Container();
	JLabel label1 = new JLabel("存储目录");
	JLabel label2 = new JLabel("文件目录");
	JTextField text1 = new JTextField();// TextField 目录的路径
	JTextField text2 = new JTextField();// TextField 目录的路径
	JButton selectButton1 = new JButton("...");// 选择
	JButton selectButton2 = new JButton("...");// 选择
	JFileChooser jfc = new JFileChooser();// 文件选择器
	JButton crawlerButton1 = new JButton("kaiqiu");//
	JButton crawlerButton2 = new JButton("happy");//
	JButton importButton = new JButton("开始");//
	JTextArea jText1 = new JTextArea();
	JTextArea jText2 = new JTextArea();

	String folder1 = null;
	String folder2 = null;

	MainCrawler() {
		jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘

		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
		frame.setSize(280, 225);// 设定窗口大小
		frame.setContentPane(tabPane);// 设置布局
		label1.setBounds(10, 10, 70, 20);
		label2.setBounds(10, 10, 70, 20);
		text1.setBounds(75, 10, 120, 20);
		text2.setBounds(75, 10, 120, 20);
		selectButton1.setBounds(200, 10, 50, 20);
		selectButton2.setBounds(200, 10, 50, 20);
		crawlerButton1.setBounds(50, 60, 80, 20);
		crawlerButton2.setBounds(150, 60, 80, 20);
		importButton.setBounds(105, 60, 60, 20);
		jText1.setBounds(0, 82, 280, 100);
		jText1.append("帮助:" + "\n" + "1.选择存储文件的目录." + "\n" + "2.点击开始" + "\n"
				+ "3.等待程序运行,抓取完成后有提示.");
		jText2.setBounds(0, 82, 280, 100);
		jText2.append("帮助:" + "\n" + "1.选择数据文件目录." + "\n" + "2.点击开始" + "\n"
				+ "3.等待程序运行,导入数据库完成后有提示.");
		selectButton1.addActionListener(this); // 添加事件处理
		selectButton2.addActionListener(this); // 添加事件处理
		crawlerButton1.addActionListener(this); // 添加事件处理
		crawlerButton2.addActionListener(this); // 添加事件处理
		importButton.addActionListener(this);
		con1.add(label1);
		con1.add(text1);
		con1.add(selectButton1);
		con1.add(crawlerButton1);
		con1.add(crawlerButton2);
		con1.add(jText1);

		con2.add(label2);
		con2.add(text2);
		con2.add(selectButton2);
		con2.add(importButton);
		con2.add(jText2);

		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
		tabPane.add("抓取数据", con1);// 添加布局1
		tabPane.add("导入数据库", con2);

	}

	/**
	 * 时间监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(selectButton1)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为选择到的目录
				folder1 = f.getAbsolutePath();
				text1.setText(folder1);
			}
		}
		if (e.getSource().equals(selectButton2)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为选择到的目录
				folder2 = f.getAbsolutePath();
				text2.setText(folder2);
			}
		}
		if (e.getSource().equals(crawlerButton1)) {
			if (folder1 == null) {
				JOptionPane.showMessageDialog(null, "请选择存储目录", "提示", 2);
			} else {
				/*
				 * "/home/lei/data"为存储文件的目录,可改为自己的 抓取下来的信息文件,内容分为
				 * 比赛信息源地址,比赛名称,比赛发起人,比赛类型,比赛地点,比赛开始时间,比赛结束时间,截止日期,比赛人数,比赛简介
				 */
				// kaiqiu网
				ArrayList<String> urls1 = KaiqiuMatchesUrl.getUrls(folder1
						+ "/data");
				KaiqiuCrawler kaiqiuCrawler = new KaiqiuCrawler(urls1, folder1
						+ "/data");
				Thread t1 = new Thread(kaiqiuCrawler);
				t1.start();
				/*
				 * if(t.getState()==java.lang.Thread.State.TERMINATED){
				 * JOptionPane.showMessageDialog(null, "抓取完成", "提示", 2); }
				 */
				// happypp网
				/*ArrayList<String> urls2 = HappyppMatchesUrl.getUrls(folder1
						+ "/data");
				HappyppCrawler happyppCrawler = new HappyppCrawler(urls2,
						folder1 + "/data");
				Thread t2 = new Thread(happyppCrawler);
				t2.start();*/
			}
		}
		if (e.getSource().equals(crawlerButton2)) {
			if (folder1 == null) {
				JOptionPane.showMessageDialog(null, "请选择存储目录", "提示", 2);
			} else {
				// happypp网
				ArrayList<String> urls2 = HappyppMatchesUrl.getUrls(folder1
						+ "/data");
				HappyppCrawler happyppCrawler = new HappyppCrawler(urls2,
						folder1 + "/data");
				Thread t2 = new Thread(happyppCrawler);
				t2.start();
			}
		}
		if (e.getSource().equals(importButton)) {
			if (folder2 == null) {
				JOptionPane.showMessageDialog(null, "请选择文件目录", "提示", 2);
			} else {
				Jdbc jdbc = new Jdbc();
				Db db = new Db("urls");
				Db matches = new Db("matches");
				matches.start(folder2);
				db.start(folder2);
				ArrayList<String> urls = db.selectAll();
				/*
				 * "/home/lei/data"为存储文件的目录,可改为自己的 抓取下来的信息文件,内容分为
				 * 比赛信息源地址,比赛名称,比赛类型,比赛地点,比赛开始时间,截止日期,比赛简介
				 */
				for (String url : urls) {
					System.out.println(url);
					db.delete(url+":url");
					jdbc.add(new MatchData.Builder(url, matches.selectByKey(url
							+ ":name"))
							.matchType(matches.selectByKey(url + ":type"))
							.matchAddress(matches.selectByKey(url + ":address"))
							.matchStartTime(matches.selectByKey(url + ":starttime"))
							.matchDeadline(matches.selectByKey(url + ":deadline"))
							.matchIntroduction(matches.selectByKey(url + ":introduction")).build());
				}
				JOptionPane.showMessageDialog(null, "导入数据库完成", "提示", 2);
				db.close();
				matches.close();
			}
		}
	}

	public static void main(String[] args) {
		new MainCrawler();
	}
}
