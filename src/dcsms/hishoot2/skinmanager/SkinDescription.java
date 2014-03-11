package dcsms.hishoot2.skinmanager;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;

public class SkinDescription {
	String result = null;
	String device = null;
	String author = null;
	int tx, ty, bx, by;
	int densType;
	InputStream filename = null;

	public String getDevice() {
		return device;
	}

	public String getAuthor() {
		return author;

	}
	public int getDensType(){
		return densType;
	}

	public int getTX() {
		return tx;
	}

	public int getTY() {
		return ty;
	}

	public int getBX() {
		return bx;
	}

	public int getBY() {
		return by;
	}

	public void getKeterangan(InputStream is) {

		Document obj_doc = null;
		DocumentBuilderFactory doc_build_fact = null;
		DocumentBuilder doc_builder = null;

		try {
			doc_build_fact = DocumentBuilderFactory.newInstance();
			doc_builder = doc_build_fact.newDocumentBuilder();
			obj_doc = doc_builder.parse(is);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NodeList items = null;
		if (null != obj_doc) {
			Element root = obj_doc.getDocumentElement();
			items = root.getElementsByTagName("DCSMS-Hishoot");
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				NodeList properties = item.getChildNodes();
				for (int j = 0; j < properties.getLength(); j++) {
					Node property = properties.item(j);
					String name = property.getNodeName();
					if (name.equalsIgnoreCase("device")) {
						// Store it where you want
						device = property.getFirstChild().getNodeValue();

					}
					if (name.equalsIgnoreCase("author")) {
						author = property.getFirstChild().getNodeValue();

					}

					if (name.equalsIgnoreCase("topx")) {
						tx = Integer.parseInt(property.getFirstChild()
								.getNodeValue());

					}
					if (name.equalsIgnoreCase("topy")) {
						ty = Integer.parseInt(property.getFirstChild()
								.getNodeValue());

					}
					if (name.equalsIgnoreCase("botx")) {
						bx = Integer.parseInt(property.getFirstChild()
								.getNodeValue());

					}
					if (name.equalsIgnoreCase("boty")) {
						by = Integer.parseInt(property.getFirstChild()
								.getNodeValue());

					}
					if (name.equalsIgnoreCase("deviceDpi")) {
						densType = Integer.parseInt(property.getFirstChild()
								.getNodeValue());

					}

					// result = title + "\n" + author + "\n" + ver + "\n"+
					// Integer.toString(x)+ "\n"+Integer.toString(y)
					// + "\n"+Integer.toString(tinggi)+
					// "\n"+Integer.toString(lebar);
					// Log.w("DESC", result);
				}
			}

		}

	}

	

}
