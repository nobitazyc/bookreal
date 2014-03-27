package com.example.msviewpart;

import java.io.InputStream;  
import java.util.ArrayList;  
import java.util.List;
  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  

public class DomPerson
{
    public static List<Person> parserXmlByDom(InputStream inputStream) throws Exception
    {
        List<Person> persons = new ArrayList<Person>();
        //    得到一个DocumentBuilderFactory解析工厂类
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //    得到一个DocumentBuilder解析类
        DocumentBuilder builder = factory.newDocumentBuilder();
        //    接收一个xml的字符串来解析xml,Document代表整个xml文档
        Document document = builder.parse(inputStream);
        //    得到xml文档的根元素节点
        Element personsElement = document.getDocumentElement();
        //    得到标签为person的Node对象的集合NodeList
        NodeList nodeList = personsElement.getElementsByTagName("person");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            Person person = new Person();
            //    如果该Node是一个Element
            if(nodeList.item(i).getNodeType() == Document.ELEMENT_NODE)
            {
                Element personElement = (Element)nodeList.item(i);
                //    得到id的属性值
                String id = personElement.getAttribute("id");
                person.setId(Integer.parseInt(id));
                
                //    得到person元素下的子元素
                NodeList childNodesList = personElement.getChildNodes();
                for(int j = 0; j < childNodesList.getLength(); j++)
                {
                    if(childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE)
                    {
                        //    解析到了person下面的name标签
                        if("name".equals(childNodesList.item(j).getNodeName()))
                        {
                            //    得到name标签的文本值
                            String name = childNodesList.item(j).getFirstChild().getNodeValue();
                            person.setName(name);
                        }
                        else if("password".equals(childNodesList.item(j).getNodeName()))
                        {
                            String password = childNodesList.item(j).getFirstChild().getNodeValue();
                            person.setPassword(password);
                        }
                        else if("bookneed".equals(childNodesList.item(j).getNodeName()))
                        {
                            String bookneed = childNodesList.item(j).getFirstChild().getNodeValue();
                            person.setBookneed(bookneed);
                        }
                    }
                }
                
                persons.add(person);
                person = null;
            }
        }
        
        return persons;
    }
}