package org.sousai.crawler4ss.examples.baidusearch;

import java.awt.Container;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sousai.crawler4ss.fetcher.PageFetcher;

public class BaiduKuaiZhao implements ActionListener {
	private JFrame frame = new JFrame("百度快照-比赛数据");// 框架布局
	private Container container = new Container();

	private JLabel labelSave = new JLabel("目录");
	private JTextField folderFiled = new JTextField();
	private JButton selectButton = new JButton("...");
	private JFileChooser jfc = new JFileChooser();// 文件选择器

	private JButton searchButton = new JButton("搜索");
	private JTextField searchContent = new JTextField();

	private String folder = null;

	public BaiduKuaiZhao() {
		jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘

		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

		frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
		frame.setSize(280, 225);// 设定窗口大小
		frame.setContentPane(container);// 设置布局

		labelSave.setBounds(10, 10, 50, 20);
		folderFiled.setBounds(50, 10, 140, 20);
		selectButton.setBounds(200, 10, 50, 20);
		selectButton.addActionListener(this);

		searchContent.setBounds(0, 70, 280, 30);
		searchButton.setBounds(105, 115, 60, 30);
		searchButton.addActionListener(this);

		container.add(labelSave);
		container.add(folderFiled);
		container.add(selectButton);

		container.add(searchButton);
		container.add(searchContent);

		frame.setVisible(true);// 窗口可见
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(selectButton)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(1);// 设定只能选择到文件夹
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File f = jfc.getSelectedFile();// f为选择到的目录
				folder = f.getAbsolutePath();
				folderFiled.setText(folder);
			}
		}

		if (e.getSource().equals(searchButton)) {
			int num = 0 ;
			String content = searchContent.getText();
			content = content.replace("  ", " ").replace(" ", "%20");

			String baiduUrl = "http://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd="
					+ content
					+ "&rsv_pq=d8b5ec7f0014f8af&rsv_t=b54akmdT%2BjPVK1PaH90C3Z7YL19wlNgWNr%2BFHVBhVAtI9R9RElmNMXwM8Fo&rsv_enter=1&rsv_sug3=8&rsv_sug4=359&rsv_sug2=0&inputT=3671";

			Document doc = null;
			PageFetcher pageFetcher = new PageFetcher();
			String html = pageFetcher.fetchPage(baiduUrl).getContent();
			doc = Jsoup.parse(html);
			Elements urlEles = doc.getElementsByClass("f13").select(
					"a[data-nolog]");
			for (Element urlEle : urlEles) {
				// System.out.println(urlEle.attr("href"));
				downloadPage(urlEle.attr("href"), folder, num++);
			}
		}
	}

/*	private void downloadFile(String url, String storageFolder, int num) {
		BufferedWriter bw = null;
		String content = null;
		PageFetcher pageFetcher = new PageFetcher();
		String html = pageFetcher.fetchPage(url).getContent();
		Document doc = Jsoup.parse(html);

		content = doc.body().text();
		File folder = new File(storageFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		File file = new File(storageFolder + "/" + num + ".txt");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/	
	private  void downloadPage(String str, String storageFolder, int num) {  
        BufferedReader br = null;  
        FileOutputStream fos = null;  
        OutputStreamWriter osw = null;  
        String inputLine;  
        try {  
            URL url = null;  
            url = new URL(str);  
  
            // 通过url.openStream(),来获得输入流  
            br = new BufferedReader(new InputStreamReader(url.openStream(),  
                    "GBK"));  
  
            File folder = new File(storageFolder);
    		if (!folder.exists()) {
    			folder.mkdirs();
    		}

    		File file = new File(storageFolder + "/" + num + ".html");
    		
            fos = new FileOutputStream(file);  
            osw = new OutputStreamWriter(fos, "GBK");  
  
            // 将输入流读入到临时变量中，再写入到文件  
            while ((inputLine = br.readLine()) != null) {  
                osw.write(inputLine);  
                // System.out.println(inputLine);  
            }  
              
            br.close();  
            osw.close();  
            System.err.println("下载完毕!");  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (br != null && osw != null) {  
                    br.close();  
                    osw.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  

	public static void main(String[] args) {
		new BaiduKuaiZhao();
	}

}
