package org.sousai.crawler4ss.fetcher;

public class PageFetchResult {
	/* 保存抓取到的内容
     * 抓取的页面内容,url,content
     */

    private String url = null ;
    private String content = null ;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}