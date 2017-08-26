package org.redrock.util;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by jx on 2017/7/21.
 */
public class Test {

    public static Map<String, String> nodeTypes;

    static {
        nodeTypes = new HashMap<>();
        nodeTypes.put("1", "ELEMENT_NODE");
        nodeTypes.put("2", "ATTRIBUTE_NODE");
        nodeTypes.put("3", "TEXT_NODE");
        nodeTypes.put("4", "CDATA_SECTION_NODE");
        nodeTypes.put("5", "ENTITY_REFERENCE_NODE");
        nodeTypes.put("6", "ENTITY_NODE");
        nodeTypes.put("7", "PROCESSING_INSTRUCTION_NODE");
        nodeTypes.put("8", "COMMENT_NODE");
        nodeTypes.put("9", "DOCUMENT_NODE");
    }

//<?xml version="1.0" encoding="UTF-8"?>
//<Articles>
//    <item category='language'>
//        <Title>Java</Title>
//        <Description>跨平台</Description>
//    </item>
//    <item>
//        <Title>C#</Title>
//        <Description>跨语言</Description>
//    </item>
//</Articles>

    public static void testXMLEventReader() {
        try {
            String xml = "<jwData><errorCode>0</errorCode><errorInfo>正常</errorInfo><dataNums>6</dataNums><data><item><考试日期>第19周星期1第3-4节</考试日期><学号>2014211478</学号><课程号>050093  </课程号><课程名>外贸实务英语</课程名><考场>3206</考场><考试资格>正常考试</考试资格><考试时间>10:10-12:10</考试时间><座位>1</座位></item><item><考试日期>第19周星期4第3-4节</考试日期><学号>2014211478</学号><课程号>040319a </课程号><课程名>操作系统</课程名><考场>3206</考场><考试资格>正常考试</考试资格><考试时间>10:10-12:10</考试时间><座位>41</座位></item><item><考试日期>第18周星期6第3-4节</考试日期><学号>2014211478</学号><课程号>040200  </课程号><课程名>计算机网络</课程名><考场>3106</考场><考试资格>正常考试</考试资格><考试时间>10:10-12:10</考试时间><座位>52</座位></item><item><考试日期>第18周星期5第7-8节</考试日期><学号>2014211478</学号><课程号>040111  </课程号><课程名>面向对象程序设计－Java</课程名><考场>3106</考场><考试资格>正常考试</考试资格><考试时间>16:10-18:10</考试时间><座位>20</座位></item><item><考试日期>第18周星期7第7-8节</考试日期><学号>2014211478</学号><课程号>040523  </课程号><课程名>数学建模</课程名><考场>3107</考场><考试资格>正常考试</考试资格><考试时间>16:10-18:10</考试时间><座位>49</座位></item><item><考试日期>第18周星期3第9-10节</考试日期><学号>2014211478</学号><课程号>040157  </课程号><课程名>数据库原理</课程名><考场>3109</考场><考试资格>正常考试</考试资格><考试时间>19:30-21:30</考试时间><座位>12</座位></item></data></jwData>";
            XMLInputFactory factory = XMLInputFactory.newInstance();
            InputStream in = new ByteArrayInputStream(xml.getBytes());
            XMLEventReader reader = factory.createXMLEventReader(in);
            while(reader.hasNext()){
                XMLEvent event = reader.nextEvent();
                if(event.isStartElement()){
                    String name = event.asStartElement().getName().toString();
                    if("item".equals(name)){
                        test(reader);
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static void test(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                String name = event.asStartElement().getName().toString();
                String value = reader.getElementText();
                System.out.println(name + "  " + value);
            }
        }
    }

    public static void main(String[] args) {
        testXMLEventReader();
    }
}