package com.realpage.productname.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PageObjectController 
{
	private static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder dBuilder;
	private static Document doc;
	
	public static Map<String, String> buildDictonaryForPageObject(String strFileName)
	{
		
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(Constants.strPathForPageObjectConfigs + strFileName);
			doc.getDocumentElement().normalize();
			map.putAll(fillDictionary("Element"));
			map.putAll(fillDictionary("Constant"));
		}
		catch(Exception ex)
		{
			Reporter.log("Class PageObjectController | Method buildDictonaryForPageObject | Exception desc : " + ex.getMessage(), true);			
		}
		return map;
	}
	
	private static Map<String, String> fillDictionary(String strTagName)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			Node nNode;
			Element eElement;
			NodeList nodeList = doc.getElementsByTagName(strTagName);
			map.clear();
			for (int i = 0; i < nodeList.getLength(); i++) 
			{
				nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					eElement = (Element) nNode;
					if(eElement.hasAttribute("name") && eElement.hasAttribute("locator"))
					{
						map.put(eElement.getAttribute("name").toString(), eElement.getAttribute("locator").toString() + "=" + eElement.getTextContent());
					}
					else if (eElement.hasAttribute("name")) 
					{
						map.put(eElement.getAttribute("name").toString(), eElement.getTextContent());
					}
				}
			}
		}
		catch(Exception ex)
		{
			Reporter.log("Class PageObjectController | Method fillDictionary | Exception desc : " + ex.getMessage(), true);
		}
		return map;
	}

}
