package org.sousai.crawler4ss.frontie;

import java.util.LinkedList;

//等待访问的url队列
public class WaitingUrlQueues {
  // 保存等待接受抓取的url,因为主要涉及删除添加操作,所以用LinkedList
  private static LinkedList<String> waitingUrlQueues = new LinkedList<String>();

  //提取出一个url
  public static String poll(){
      return waitingUrlQueues.pollFirst() ;
  }

  // 添加一个新的url到等待队列中
  public static void add(String url) {
      waitingUrlQueues.add(url);
  }

  // 删除等待队列中的某个url
  public static boolean remove(String url) {
      return waitingUrlQueues.remove(url);
  }

  // 判断是否存在已经存在该url
  public static boolean contains(String url) {
      return waitingUrlQueues.contains(url);
  }

  // 得到该队列url的数量
  public static int size() {
      return waitingUrlQueues.size();
  }

  public static boolean isEmpty(){
      return waitingUrlQueues.isEmpty() ;
  }

}