package com.example.msviewpart;

import java.io.InputStream;  
import java.util.ArrayList;  
import java.util.List;
  
import javax.xml.parsers.DocumentBuilder;  
import javax.xml.parsers.DocumentBuilderFactory;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  

public class DomBook
{
    public static List<Book> parserXmlByDom(InputStream inputStream) throws Exception
    {
        List<Book> books = new ArrayList<Book>();
        //    �õ�һ��DocumentBuilderFactory����������
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //    �õ�һ��DocumentBuilder������
        DocumentBuilder builder = factory.newDocumentBuilder();
        //    ����һ��xml���ַ���������xml,Document��������xml�ĵ�
        Document document = builder.parse(inputStream);
        //    �õ�xml�ĵ��ĸ�Ԫ�ؽڵ�
        Element BooksElement = document.getDocumentElement();
        //    �õ���ǩΪBook��Node����ļ���NodeList
        NodeList nodeList = BooksElement.getElementsByTagName("book");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            Book book = new Book();
            //    �����Node��һ��Element
            if(nodeList.item(i).getNodeType() == Document.ELEMENT_NODE)
            {
                Element BookElement = (Element)nodeList.item(i);
                //    �õ�id������ֵ
                String id = BookElement.getAttribute("id");
                book.setId(Integer.parseInt(id));
                
                //    �õ�BookԪ���µ���Ԫ��
                NodeList childNodesList = BookElement.getChildNodes();
                for(int j = 0; j < childNodesList.getLength(); j++)
                {
                    if(childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE)
                    {
                        //    ��������Book�����name��ǩ
                        if("name".equals(childNodesList.item(j).getNodeName()))
                        {
                            //    �õ�name��ǩ���ı�ֵ
                            String name = childNodesList.item(j).getFirstChild().getNodeValue();
                            book.setName(name);
                        }
                        else if("author".equals(childNodesList.item(j).getNodeName()))
                        {
                            String author = childNodesList.item(j).getFirstChild().getNodeValue();
                            book.setAuthor(author);
                        }
                        else if("price".equals(childNodesList.item(j).getNodeName()))
                        {
                            String price = childNodesList.item(j).getFirstChild().getNodeValue();
                            book.setPrice(Double.parseDouble(price));
                        }
                        else if("isbn".equals(childNodesList.item(j).getNodeName()))
                        {
                            String isbn = childNodesList.item(j).getFirstChild().getNodeValue();
                            book.setIsbn(isbn);
                        }
                    }
                }
                
                books.add(book);
                book = null;
            }
        }
        
        return books;
    }
}