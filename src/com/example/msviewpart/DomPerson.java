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
        //    �õ�һ��DocumentBuilderFactory����������
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //    �õ�һ��DocumentBuilder������
        DocumentBuilder builder = factory.newDocumentBuilder();
        //    ����һ��xml���ַ���������xml,Document��������xml�ĵ�
        Document document = builder.parse(inputStream);
        //    �õ�xml�ĵ��ĸ�Ԫ�ؽڵ�
        Element personsElement = document.getDocumentElement();
        //    �õ���ǩΪperson��Node����ļ���NodeList
        NodeList nodeList = personsElement.getElementsByTagName("person");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            Person person = new Person();
            //    �����Node��һ��Element
            if(nodeList.item(i).getNodeType() == Document.ELEMENT_NODE)
            {
                Element personElement = (Element)nodeList.item(i);
                //    �õ�id������ֵ
                String id = personElement.getAttribute("id");
                person.setId(Integer.parseInt(id));
                
                //    �õ�personԪ���µ���Ԫ��
                NodeList childNodesList = personElement.getChildNodes();
                for(int j = 0; j < childNodesList.getLength(); j++)
                {
                    if(childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE)
                    {
                        //    ��������person�����name��ǩ
                        if("name".equals(childNodesList.item(j).getNodeName()))
                        {
                            //    �õ�name��ǩ���ı�ֵ
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