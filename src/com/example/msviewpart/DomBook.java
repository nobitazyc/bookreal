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
        //    得到一个DocumentBuilderFactory解析工厂类
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //    得到一个DocumentBuilder解析类
        DocumentBuilder builder = factory.newDocumentBuilder();
        //    接收一个xml的字符串来解析xml,Document代表整个xml文档
        Document document = builder.parse(inputStream);
        //    得到xml文档的根元素节点
        Element BooksElement = document.getDocumentElement();
        //    得到标签为Book的Node对象的集合NodeList
        NodeList nodeList = BooksElement.getElementsByTagName("book");
        for(int i = 0; i < nodeList.getLength(); i++)
        {
            Book book = new Book();
            //    如果该Node是一个Element
            if(nodeList.item(i).getNodeType() == Document.ELEMENT_NODE)
            {
                Element BookElement = (Element)nodeList.item(i);
                //    得到id的属性值
                String id = BookElement.getAttribute("id");
                book.setId(Integer.parseInt(id));
                
                //    得到Book元素下的子元素
                NodeList childNodesList = BookElement.getChildNodes();
                for(int j = 0; j < childNodesList.getLength(); j++)
                {
                    if(childNodesList.item(j).getNodeType() == Document.ELEMENT_NODE)
                    {
                        //    解析到了Book下面的name标签
                        if("name".equals(childNodesList.item(j).getNodeName()))
                        {
                            //    得到name标签的文本值
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