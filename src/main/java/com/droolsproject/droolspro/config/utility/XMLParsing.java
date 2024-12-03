package com.droolsproject.droolspro.config.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.xml.parsers.DocumentBuilder;
import jakarta.xml.parsers.DocumentBuilderFactory;
import jakarta.xml.xpath.XPath;
import jakarta.xml.xpath.XPathConstants;
import jakarta.xml.xpath.XPathFactory;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.droolsproject.droolspro.model.Devices;
import com.droolsproject.droolspro.model.Points;
import com.droolsproject.droolspro.model.PointsOutputValue;
import com.droolsproject.droolspro.model.SpaceTemp;
import com.droolsproject.droolspro.model.URLPoints;
import java.io.StringReader;

public class XMLParsing {

	public static List<SpaceTemp> mergeTwoList(List<SpaceTemp> list1, List<SpaceTemp> list2) {
		Map<String, SpaceTemp> map2 = list2.stream().collect(Collectors.toMap(SpaceTemp::getName, Function.identity()));

		// Merge the two lists based on the common name
		List<SpaceTemp> mergedList = list1.stream().map(spaceTemp -> {
			SpaceTemp spaceTemp2 = map2.get(spaceTemp.getName());
			if (spaceTemp2 != null) {
				spaceTemp.setAverage(spaceTemp2.getAverage());
				spaceTemp.setStddeviation(spaceTemp2.getStddeviation());
			}
			return spaceTemp;
		}).collect(Collectors.toList());

		// Print the merged list
		return mergedList;
	}

	public static List<PointsOutputValue> parsexml(String xmlResponse, List<PointsOutputValue> pointsOutputValueList,
			URLPoints urlPoints) {
		try {
			// Create a DocumentBuilder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Parse the XML response
			ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlResponse.getBytes());
			Document document = builder.parse(inputStream);
			// Get the root element
			Element rootElement = document.getDocumentElement();
			// Get the list of all child elements under the root element
			NodeList childNodes = rootElement.getChildNodes();
			// Process each child element
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node childNode = childNodes.item(i);
				if (childNode.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) childNode;
					String tagName = element.getTagName();
					String attributeVal = element.getAttribute("val");
					String attributeName = element.getAttribute("name");
					if (attributeName.contains("out")) {
						//double parsedNumber = attributeVal.isEmpty() ? 0.00 : Double.parseDouble(attributeVal);
//						DecimalFormat decimalFormat = new DecimalFormat("#.##");
//						String formattedValue = decimalFormat.format(parsedNumber);
						PointsOutputValue obj = new PointsOutputValue();
						obj.setDeviceStatus(urlPoints.getStatus().toString());
						obj.setOutValue(attributeVal);
						obj.setTagName(urlPoints.getTagName());
						obj.setUrlPoints(urlPoints);
						obj.setUpdatetimestamp(new Date());
						obj.setGroupId(1);
						obj.setUnit("F");
						pointsOutputValueList.add(obj);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pointsOutputValueList;
	}

	public List<SpaceTemp> getAvgSpaceTempData(List<SpaceTemp> avgSpaceList) {
		List<SpaceTemp> spaceTempList = null;// ictSpaceTempRepository.getSpaceTempData();
		Map<String, List<SpaceTemp>> spaceTempMap = spaceTempList.stream()
				.collect(Collectors.groupingBy(SpaceTemp::getName));

		// Calculate average and standard deviation for each name group
		for (Map.Entry<String, List<SpaceTemp>> entry : spaceTempMap.entrySet()) {
			String name = entry.getKey();
			List<Double> values = entry.getValue().stream().map(spaceTemp -> Double.parseDouble(spaceTemp.getValue()))
					.collect(Collectors.toList());

			DescriptiveStatistics stats = new DescriptiveStatistics();
			values.forEach(stats::addValue);

			double average = stats.getMean();
			double stdDeviation = stats.getStandardDeviation();
			DecimalFormat decimalFormat = new DecimalFormat("#.##");
			SpaceTemp sp = new SpaceTemp();
			sp.setAverage(Double.parseDouble(decimalFormat.format(average)));
			sp.setName(name);
			sp.setStddeviation(Double.parseDouble(decimalFormat.format(stdDeviation)));
			avgSpaceList.add(sp);
		}
		return avgSpaceList;
	}

	public static List<String> parseDeviceResponse(String xmlString) {
		List<String> deviceList = new ArrayList<>();
		try {
			// Parse the XML string
			/*
			 * DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 * DocumentBuilder builder = factory.newDocumentBuilder(); Document document =
			 * builder.parse(new InputSource(new StringReader(xmlString)));
			 * 
			 * // Get all elements with attribute "display" containing "BacnetDevice"
			 * NodeList nodeList = document.getElementsByTagName("ref"); for (int i = 0; i <
			 * nodeList.getLength(); i++) { Element element = (Element) nodeList.item(i);
			 * String displayValue = element.getAttribute("display"); if
			 * (displayValue.contains("Niagara Station")) { String pattern = "\\{(.*?)\\}";
			 * Pattern regex = Pattern.compile(pattern); Matcher matcher =
			 * regex.matcher(displayValue); if (matcher.find()||true) { String
			 * extractedValue = matcher.group(1); System.out.println(extractedValue);
			 * deviceList.add(extractedValue); }
			 * 
			 * } }
			 * 
			 */
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parse the XML content
			Document document = builder.parse(new InputSource(new StringReader(xmlString)));

			// Find all elements with the name "ref"
			NodeList refElements = document.getElementsByTagName("ref");

			// Iterate through the elements and extract display values for "Niagara Station"
			for (int i = 0; i < refElements.getLength(); i++) {
				Element refElement = (Element) refElements.item(i);
				String displayValue = refElement.getAttribute("display");
				String isValue = refElement.getAttribute("is");
				String name = refElement.getAttribute("name");
				if (isValue.contains("/obix/def/niagaraDriver:NiagaraStation")) {
					System.out.println("Niagara Station Display: " + displayValue);
					deviceList.add(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceList;
	}

	public static Devices parseDeviceInfoXML(String xmlData) {
		Devices deviceInfo = new Devices();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inputSource = new InputSource(new StringReader(xmlData));
			Document document = builder.parse(inputSource);

			String status = getElementValue(document, "str", "name", "status", "val");
			String enabled = getElementValue(document, "bool", "name", "enabled", "val");
			String health = getElementValue(document, "obj", "name", "health", "display");
			String faultCause = getElementValue(document, "str", "name", "faultCause", "val");
			String networkNumber = getElementValue(document, "int", "name", "networkNumber", "val");
			String macAddress = getElementValue(document, "str", "name", "macAddress", "val");

			deviceInfo.setStatus(status);
			deviceInfo.setEnabled(enabled);
			deviceInfo.setFaultCause(faultCause);
			deviceInfo.setHealth(health);
			deviceInfo.setNetwork(networkNumber);
			deviceInfo.setMacAddress(macAddress);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceInfo;
	}

	private static String getElementValue(Document document, String elementTag, String attributeName,
			String attributeValue, String attributeToGet) {
		NodeList nodeList = document.getElementsByTagName(elementTag);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				String attributeVal = element.getAttribute(attributeName);
				if (attributeValue.equals(attributeVal)) {
					return element.getAttribute(attributeToGet);
				}
			}
		}
		return null;
	}

	public static List<Points> parsePoints(String xmlString, String apiurl) {
		List<Points> pointsList = new ArrayList<>();
		try {
			// Create a DocumentBuilder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parse the XML string into a Document
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

			// Get the root element
			Element root = doc.getDocumentElement();

			// Get the NodeList of all <ref> elements
			NodeList refNodes = root.getElementsByTagName("ref");

			// Loop through the <ref> elements to extract name and display value
			for (int i = 0; i < refNodes.getLength(); i++) {
				Element refElement = (Element) refNodes.item(i);
				String name = refElement.getAttribute("name");
				String displayValue = refElement.getAttribute("display");
				Points points = new Points();
				points.setName(name);
				points.setStatus("INCTIVE");
				points.setDisplay(displayValue);
				points.setApiurl(apiurl + "/" + name);
				pointsList.add(points);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	public static List<URLPoints> parseURLPoints(String xmlString, String apiurl) {
		List<URLPoints> pointsList = new ArrayList<>();
		try {
			// Create a DocumentBuilder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			// Parse the XML string into a Document
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

			// Get the root element
			Element root = doc.getDocumentElement();

			// Get the NodeList of all <ref> elements
			NodeList refNodes = root.getElementsByTagName("ref");

			// Loop through the <ref> elements to extract name and display value
			for (int i = 0; i < refNodes.getLength(); i++) {
				Element refElement = (Element) refNodes.item(i);
				String name = refElement.getAttribute("name");
				String displayValue = refElement.getAttribute("display");
				URLPoints points = new URLPoints();
				points.setName(name);
				points.setStatus("INCTIVE");
				points.setDisplay(displayValue);
				points.setApiurl(apiurl + "/" + name);
				pointsList.add(points);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	public static List<String> parseSlotPath(String xml, String base_url) {
		List<String> slotList = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));

			NodeList slotPathList = document.getElementsByTagName("str");
			for (int i = 0; i < slotPathList.getLength(); i++) {
				Element element = (Element) slotPathList.item(i);
				if ("slotPath".equals(element.getAttribute("name"))) {
					String slotPath = element.getAttribute("val");
					if (slotPath != null) {
						slotPath = slotPath.replace("[", "");
						slotPath = slotPath.replace("]", "");
						slotPath = slotPath.replace("slot:", base_url);
						slotList.addAll(Arrays.asList(slotPath.split(",")));
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return slotList;
	}

	public static String parseTagName(String xml) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xml)));

			// Find and print the value of "Test1" element
			NodeList test1List = document.getElementsByTagName("str");
			for (int i = 0; i < test1List.getLength(); i++) {
				Element element = (Element) test1List.item(i);
				if ("Test1".equals(element.getAttribute("name"))) {
					return element.getAttribute("val");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static List<Points> pasrseObject(String xml, String url, String tagName, String deviceName) {
		List<Points> pointsList = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();

			// Extracting strname a nd display values from tags that have both attributes
			NodeList strNodes = (NodeList) xPath.evaluate("//str | //bool | //real", doc, XPathConstants.NODESET);
			for (int i = 0; i < strNodes.getLength(); i++) {
				Points points = new Points();
				Element element = (Element) strNodes.item(i);
				String name = element.getAttribute("name");
				String display = element.getAttribute("display");
				if (name.toLowerCase().contains("out")) {
					points.setApiurl(url);
					points.setDisplay(display);
					points.setName(name);
					points.setTagName(tagName);
					points.setDeviceName(deviceName);
					pointsList.add(points);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	public static List<URLPoints> pasrseObjectURL(String xml, String url, String tagName, String deviceName) {
		List<URLPoints> urlPointsList = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xml)));

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();

			// Extracting strname a nd display values from tags that have both attributes
			NodeList strNodes = (NodeList) xPath.evaluate("//str | //bool | //real", doc, XPathConstants.NODESET);
			for (int i = 0; i < strNodes.getLength(); i++) {
				URLPoints urlPoints = new URLPoints();
				Element element = (Element) strNodes.item(i);
				String name = element.getAttribute("name");
				String display = element.getAttribute("display");
				if (name.toLowerCase().contains("out")) {
					urlPoints.setApiurl(url);
					urlPoints.setDisplay(display);
					urlPoints.setName(name);
					urlPoints.setTagName(tagName);
					urlPoints.setDeviceName(deviceName);
					urlPointsList.add(urlPoints);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return urlPointsList;
	}

	public static List<String> parseNiagaraDeviceResponse(String xmlString) {
		List<String> deviceList = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			// Parse the XML content
			Document document = builder.parse(new InputSource(new StringReader(xmlString)));
			// Find all elements with the name "ref"
			NodeList refElements = document.getElementsByTagName("ref");
			// Iterate through the elements and extract display values for "Niagara Station"
			for (int i = 0; i < refElements.getLength(); i++) {
				Element refElement = (Element) refElements.item(i);
				String displayValue = refElement.getAttribute("display");
				String isValue = refElement.getAttribute("is");
				String name = refElement.getAttribute("name");
				if (isValue.contains("/obix/def/niagaraDriver:NiagaraStation")) {
					System.out.println("Niagara Station Display: " + displayValue);
					deviceList.add(name);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceList;
	}

	public static List<String> parseBacnetDeviceResponse(String xmlString) {
	    List<String> deviceList = new ArrayList<>();
	    try {
	    	System.out.println(xmlString);
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        InputSource inputSource = new InputSource(new StringReader(xmlString));
	        Document document = builder.parse(inputSource);

	        Element rootElement = document.getDocumentElement();
	        NodeList objElements = rootElement.getElementsByTagName("*");

	        for (int i = 0; i < objElements.getLength(); i++) {
	            Element objElement = (Element) objElements.item(i);
	            String displayAttribute = objElement.getAttribute("display");
	            if (displayAttribute != null && displayAttribute.toLowerCase().contains("bacnetdevice") || displayAttribute.toLowerCase().contains("folder")) {
	                String nameAttribute = objElement.getAttribute("name");
	                deviceList.add(nameAttribute);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return deviceList;
	}

}
