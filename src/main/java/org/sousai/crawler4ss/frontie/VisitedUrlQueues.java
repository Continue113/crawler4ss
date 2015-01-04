package org.sousai.crawler4ss.frontie;

import java.util.HashSet;

//已访问的url队列
public class VisitedUrlQueues {
  //此处存储已经访问过的Url
  private static HashSet<String> visitedUrlQueues = new HashSet<String>() ;

  //添加一个已经访问过的url
  public static void add(String url){
	  visitedUrlQueues.add(url) ;
  }

  //删除一个已经访问过的url
  public static boolean remove(String url){
      return visitedUrlQueues.remove(url) ;
  }

  //已访问队列中是否已经存在该url
  public static boolean contains(String url){
      return visitedUrlQueues.contains(url) ;
  }

  //得到该队列url的数量
  public static int size(){
      return visitedUrlQueues.size() ;
  }

}
