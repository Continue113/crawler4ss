package org.sousai.crawler4ss.fetcher;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.sousai.crawler4ss.frontie.VisitedUrlQueues;

//传进一个Url,然后对该页面抓取到的内容存入到一个pageFetchResult对象中.
public class PageFetcher {
  /*
   * 通过Url,爬去该Url页面的内容
   * 
   * @return content
   */
  private CloseableHttpClient client = null;

  public PageFetchResult fetchPage(String url) {
      // 将页面的结果保存到pageFetchResult中
      PageFetchResult pageFetchResult = new PageFetchResult();
      // 创建一个httpclient客户端
      client = HttpClients.createDefault();
      HttpGet getHttp = new HttpGet(url);
      HttpResponse response;
      try {
          // 获得信息
          response = client.execute(getHttp);
          HttpEntity entity = response.getEntity();

          //将该url添加到已访问队列中
          VisitedUrlQueues.add(url);

          //将抓取到的页面信息存入到pageFetchResult中
          if (url != null && entity != null) {
              pageFetchResult.setUrl(url);
              pageFetchResult.setContent(EntityUtils.toString(entity));
          }
      } catch (ClientProtocolException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }finally{
          try {
              client.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }

      return pageFetchResult;
  }
}
