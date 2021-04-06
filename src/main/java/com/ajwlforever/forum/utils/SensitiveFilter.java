package com.ajwlforever.forum.utils;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {


    public static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private TireNode root = new TireNode();
    @PostConstruct
    public void init(){
        // 加载敏感词
        String path = "sensitive_word.txt";
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));

                ){
            String keyword = "";
            while ( (keyword=bf.readLine())!=null)
            {
                addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词资源文件出错"+e.getMessage());
            e.printStackTrace();
        }

    }
    // 敏感词加入前缀树
    public void addKeyword(String keyWord) {
        TireNode p = root;  //辅助结点
        for(int i=0 ;i<keyWord.length();i++){

            char word = keyWord.charAt(i);
            TireNode node = p.getChildren(word);
            if(node==null){
                //字符未存在于目前结点的孩子结点
                p.addChildren(word,new TireNode());
            }
            p = p.getChildren(word);  //移到新结点
        }
        //加入完成，
        p.setKeyWordEnd(true);
    }
    //从text中过滤敏感词
    public String filter(String text){
        StringBuilder after =new StringBuilder("");
        int i = 0, n = text.length(), j = 0;
        for(; i < n; ++i ){

            char word = text.charAt(i);
            boolean isSensitiveWord = false ;
            TireNode node = root.getChildren(word);
            //是否为敏感词的开头
            if(node != null){
                isSensitiveWord = false;
                j = i+1;
                for(; j < n;++j){

                    node = node.getChildren(text.charAt(j));
                    if(node == null){   //不是敏感词
                        break;
                    }
                    if(node.isKeyWordEnd()){
                        //是敏感词
                        isSensitiveWord = true;
                        break;
                    }
                }

            }
            // after后追加单词
            if(isSensitiveWord){
                i = j;
                after.append("**");

            }else{
                after.append(text.charAt(i));
            }
        }

        return after.toString();
    }
    private boolean isSymbo(char c) {
        // 0X2EE~0X9FFF 之间是东亚文字，不算符号。
        return !CharUtils.isAsciiAlphanumeric(c) &&( c< 0X2EE || c>0X9FFF);
    }
    private class  TireNode{
        boolean isKeyWordEnd ;
        private Map<Character,TireNode> children = new HashMap<>();

        public void addChildren(Character t,TireNode c){
            children.put(t,c);
        }
        public TireNode getChildren(Character t){
            return children.get(t);
        }
        public boolean isKeyWordEnd(){
            return isKeyWordEnd;
        }
        public void setKeyWordEnd(boolean isKeyWordEnd){
            this.isKeyWordEnd = isKeyWordEnd;
        }
    }


}
