package com.cqupt.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zsw
 * @create 2023-06-27 21:30
 */
@Component
public class SensitiveFilter {
    private  static  final Logger logger= LoggerFactory.getLogger(SensitiveFilter.class);

    //定义前缀树的结构
    private class TrieNode{
        //是否叶子结点（是否存在敏感词）
        public boolean isSenditiveKey=false;
        //子节点(key是下级结点的字符，value是下级结点
        private Map<Character,TrieNode> subNodes=new HashMap<>();
        //添加子节点
        public  void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        //获取子节点
        public  TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }
    //替换符
    public static final String REPLACEMENT="***";
    //根节点
    TrieNode rootNode = new TrieNode();

    //初始化前缀树
    @PostConstruct
    public  void init(){
        try ( InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader= new BufferedReader(new InputStreamReader(is))
        ){
                 String keyword;
                 while ((keyword=reader.readLine())!=null){
                     //将敏感词添加到前缀树
                     this.addKeyword(keyword);
                 }

        } catch (IOException e) {
            logger.error("加载敏感字文件失败",e.getMessage());
        }

    }
    /*
    * 过滤敏感词
    * 输入待过滤的敏感词
    * 返回过滤后的敏感词
    * */
    public String  filter(String s){
        if (StringUtils.isBlank(s)){
            return  "";
        }
        //指针1
        TrieNode tempNode=rootNode;
        //指针2
        int begin=0;
        //指针3
        int pos=0;
        //结果
        StringBuilder sb = new StringBuilder();
        while (pos < s.length()) {
            char c = s.charAt(pos);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                pos++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(s.charAt(begin));
                // 进入下一个位置
                pos = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isSenditiveKey) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++pos;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                pos++;
            }
        }
            //将可能不是敏感词的一部分加入字符
            sb.append(s.substring(begin));
            return sb.toString();
    }
    //判断是否为符号
    private  boolean isSymbol(Character c){
        //0x2E80~0x9FFF 是东亚文字符号
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }
     //将一个敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tempNode =rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c=keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode==null){
                //初始化子节点
                 subNode=new TrieNode();
                 tempNode.subNodes.put(c,subNode);

            }
            //指向子节点，进入下一轮循环
            tempNode = subNode;

            //设置结束标识

            if (i==keyword.length()-1){
                subNode.isSenditiveKey=true;
            }

        }

    }
}
