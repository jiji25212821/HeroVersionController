import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlController {
	private String srcPath;
	private String dstPath;
	private String workSheet;
	private String keyContent;
	private String keyContent2;
	private String keyContentLowerBound;
	private String keyContentUpperBound;
	private String[] xmlConstruct = {"Worksheet", "Table", "Row", "Cell", "Data"};
	private boolean isFind;
	private boolean isFind2;
	private Element result;
	private int addIndex;
	private int cellIndex;
	private boolean isFindAddEnsure;
	private String returnKeyContent;
	private ArrayList<String> returnArtPath;
	private boolean isExist;
	
	private ArrayList<String> returnContentByIndex;
	private int[] specialIndex;
	
	private ArrayList<Element> multiResults;
	
	public XmlController() {
		this.srcPath = "";
		this.dstPath = "";
		this.workSheet = "";
		this.keyContent = "";
		this.keyContent2 = "";
		this.keyContentLowerBound = "";
		this.keyContentUpperBound = "";
		isFind = false;
		isFind2 = false;
		result = null;
		addIndex = 0;
		cellIndex = 0;
		isFindAddEnsure = false;
		returnKeyContent = null;
		returnArtPath = new ArrayList<>();
		returnContentByIndex = null;
		specialIndex = null;
		isExist = false;
		multiResults = null;
	}
	
	
	public XmlController(String srcPath, String dstPath, String workSheet, String keyContent, String keyContentLowerBound, String keyContentUpperBound, int[] specialIndex, String keyContent2) {
		this.srcPath = srcPath;
		this.dstPath = dstPath;
		this.workSheet = workSheet;
		this.keyContent = keyContent;
		if(keyContent2 != null) {
			this.keyContent2 = keyContent2;
		} else {
			this.keyContent2 = "";
		}
		this.keyContentLowerBound = keyContentLowerBound;
		this.keyContentUpperBound = keyContentUpperBound;
		isFind = false;
		isFind2 = false;
		result = null;
		addIndex = 0;
		cellIndex = 0;
		isFindAddEnsure = false;
		returnKeyContent = null;
		returnArtPath = new ArrayList<>();
		this.specialIndex = specialIndex;
		if(specialIndex != null) {
			returnContentByIndex = new ArrayList<>();
		} else {
			returnContentByIndex = null;
		}
		isExist = false;
		multiResults = null;
	}
	
	public static boolean isNumber(String str) {
		if(str.equals("")) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	public void copyRow() {
		Element copyContent = getCopyContent();
		if(copyContent == null) {
			System.out.println(srcPath + " " + workSheet + ":copyContent " + keyContent + " is null");
			return;
		}
		
		//System.out.println(copyContent.getStringValue());
		parseArtPathList(copyContent);
		
		if(returnKeyContent != null) {
			keyContent = returnKeyContent;
		}
		
		writeContent();
		
		System.out.println(dstPath + ": " + workSheet + " write done!");
	}
	
	public void copyMultiRowsByCrystal() {
		copyMultiRows(1);
	}
	
	public void copyMultiRowsBySkillGroup() {
		copyMultiRows(2);
	}
	
	public void copyMultiRowsByCrystalSkillGroup() {
		copyMultiRows(3);
	}
	
	public void copyMultiRowsBySkills() {
		copyMultiRows(3);
	}
	
	public void copyMultiRowsByCrystalSkills() {
		copyMultiRows(4);
	}
	
	public void copyMultiRowsByRevelationAward() {
		copyMultiRows(0);
	}
	
	public void CopyMultiRowsByFormulas() {
		copyMultiRows(5);
	}
	
	public void copyMultiRows(int style) {
		File srcFile = new File(srcPath);
		SAXReader srcReader = new SAXReader();
		
		int keyStartRowIndex = 0;
		multiResults = new ArrayList<>();
		try {
			Document doc = srcReader.read(srcFile);
			Element root = doc.getRootElement();
			List<Element> workSheetList = root.elements();
			for(int i = 0; i < workSheetList.size(); i++) {
				Element workSheet = workSheetList.get(i);
				if(workSheet.getName().equals("Worksheet") && workSheet.attribute("Name") != null && workSheet.attribute("Name").getValue().equals(this.workSheet)) {
					//System.out.println(workSheet.attribute("Name").getValue() + " " + this.workSheet);
					Element table = (Element)workSheet.elements().get(0);
					List<Element> rowList = table.elements();
					for(int j = 0; j < rowList.size(); j++) {
						Element row = rowList.get(j);
						if(row.getName().equals("Row")) {
							List<Element> cellList = row.elements();
							for(int k = 0; k < cellList.size(); k++) {
								Element cell = cellList.get(k);
								Element data = cell.element("Data");
								if(data != null && data.getData().toString().equals(keyContent)) {
									//keyStartRowIndex = j;
									isFind = true;
									
								}
								
								if(data != null && data.getData().toString().equals(keyContent2)) {
									isFind2 = true;
								}
								
								if(data != null && style == 5 && isNumber(keyContent) && isNumber(keyContentLowerBound) && isNumber(keyContentUpperBound) && data.getData() != null && isNumber(data.getData().toString())) {//formula, get similarity Content
									int keyLowerBound = Integer.parseInt(keyContentLowerBound);
									int keyUpperBound = Integer.parseInt(keyContentUpperBound);
									keyStartRowIndex = Integer.parseInt(keyContent)/10;
									int keyNumber = Integer.parseInt(data.getData().toString());
									
									if((keyNumber >= keyLowerBound && keyNumber <= keyUpperBound && keyNumber/10 == keyStartRowIndex) || (keyNumber >= keyLowerBound*10 && keyNumber <= keyUpperBound*10 && keyNumber/100 == keyStartRowIndex) || (keyNumber >= keyLowerBound*100 && keyNumber <= keyUpperBound*100 && keyNumber/1000 == keyStartRowIndex)) {
										isFind = true;
										
									}
									
								}
								
							}
							Element cell = null;
							if(row.elements().size() > 0) {
								cell = (Element)row.elements().get(0);
							}
							Element data = null;
							if(cell != null && cell.elements().size() > 0) {
								data = (Element)cell.elements().get(0);
							}
							if(isFind == true) {
								isFind2 = false;
								isFind = false;
								multiResults.add(row.createCopy());
								//System.out.println(row.getStringValue());
								
								returnKeyContent = data.getData().toString();
								if(style == 5 && data.getData() != null && isNumber(data.getData().toString())) {//similarity match for formulas
									int keyNumber = Integer.parseInt(data.getData().toString());
									if(keyNumber >= Integer.parseInt(keyContent)*100) {
										returnKeyContent = String.valueOf(keyNumber/100);
									} else if(keyNumber >= Integer.parseInt(keyContent)*10) {
										returnKeyContent = String.valueOf(keyNumber/10);
									}
								}
								if(specialIndex != null) {
									for(int l = 0; l < specialIndex.length; l++) {
										parseSpeicalContentByIndex(row, l);
									}
								}
								
							} else {
								if(returnKeyContent != null) {
									switch (style) {
									case 1://crystal表
										if(isNumber(returnKeyContent)) {
											keyStartRowIndex = Integer.parseInt(returnKeyContent)/100;
											//System.out.println(keyStartRowIndex + " " + data.getStringValue() + " :" + isFind2);
											if(data != null && isNumber(data.getData().toString()) && isFind2 == true && Integer.parseInt(data.getData().toString())/100 == keyStartRowIndex) {
												multiResults.add(row.createCopy());
												isFind2 = false;
												if(specialIndex != null) {
													for(int l = 0; l < specialIndex.length; l++) {
														parseSpeicalContentByIndex(row, l);
													}
												}
											}
										}
										break;
									case 2://skillgroup表
										if(isNumber(returnKeyContent)) {
											keyStartRowIndex = Integer.parseInt(returnKeyContent)/100;
											//System.out.println(keyStartRowIndex + " " + data.getStringValue() + " :" + isFind2);
											if(data != null && isNumber(data.getData().toString()) && Integer.parseInt(data.getData().toString())/100 == keyStartRowIndex) {
												multiResults.add(row.createCopy());
												if(specialIndex != null) {
													for(int l = 0; l < specialIndex.length; l++) {
														parseSpeicalContentByIndex(row, l);
													}
												}
											}
										}
										break;
									case 3://skills表
										if(isNumber(returnKeyContent) && isNumber(keyContentLowerBound) && isNumber(keyContentUpperBound)) {
											
											int keyLowerBound = Integer.parseInt(keyContentLowerBound);
											int keyUpperBound = Integer.parseInt(keyContentUpperBound);
											
											keyStartRowIndex = Integer.parseInt(returnKeyContent)/10;
											//System.out.println(keyStartRowIndex + " " + data.getStringValue() + " :" + isFind2);
											if(data != null && isNumber(data.getData().toString())) {
												int keyNumber = Integer.parseInt(data.getData().toString());
												if((keyNumber >= keyLowerBound && keyNumber <= keyUpperBound && keyNumber/10 == keyStartRowIndex) || (keyNumber >= keyLowerBound*10 && keyNumber <= keyUpperBound*10 && keyNumber/100 == keyStartRowIndex) || (keyNumber >= keyLowerBound*100 && keyNumber <= keyUpperBound*100 && keyNumber/1000 == keyStartRowIndex)) {
													multiResults.add(row.createCopy());
													if(specialIndex != null) {
														for(int l = 0; l < specialIndex.length; l++) {
															parseSpeicalContentByIndex(row, l);
														}
													}
												}
											}
										}
										break;
									case 4://skillgroup表-crystal/ring相关
										if(isNumber(returnKeyContent) && isNumber(keyContentLowerBound) && isNumber(keyContentUpperBound)) {
											int keyLowerBound = Integer.parseInt(keyContentLowerBound);
											int keyUpperBound = Integer.parseInt(keyContentUpperBound);
											keyStartRowIndex = Integer.parseInt(returnKeyContent)/10;
											
											
											//System.out.println(keyStartRowIndex + " " + data.getStringValue() + " :" + isFind2);
											if(data != null && isNumber(data.getData().toString())) {
												int keyNumber = Integer.parseInt(data.getData().toString());
												
												if((keyNumber >= keyLowerBound && keyNumber <= keyUpperBound && keyNumber/10 == keyStartRowIndex) || (keyNumber >= keyLowerBound*10 && keyNumber <= keyUpperBound*10 && keyNumber/100 == keyStartRowIndex)) {
													//System.out.println("test :" + keyNumber/100 + ":" + keyNumber + ":" + keyStartRowIndex + ":" + keyLowerBound*10 + ":" + keyUpperBound*10);
													multiResults.add(row.createCopy());
													if(specialIndex != null) {
														for(int l = 0; l < specialIndex.length; l++) {
															parseSpeicalContentByIndex(row, l);
														}
													}
												}
											}
										}
										break;
									default:
										break;
									}
								}
							}
						}
					}
				}
			}
			if(returnKeyContent != null) {
				keyContent = returnKeyContent;
			}
			
			//System.out.println(keyContent);
			for(int i = 0; i < multiResults.size(); i++) {
				Element element = multiResults.get(i);
				parseArtPathList(element);
			}
			writeMultiContent();
			
			
			System.out.println(dstPath + ": " + workSheet + " write done!");
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void writeContent() {
		File dstFile = new File(dstPath);
		SAXReader dstReader = new SAXReader();
		OutputFormat outputFormat = new OutputFormat();
		
		try {
			Document doc = dstReader.read(dstFile);
			Element root = doc.getRootElement();
			parseStyle(root);
			parseElement(root, 0);
			
			XMLWriter dstWriter = new XMLWriter(new FileOutputStream(dstFile), outputFormat);
			dstWriter.write(doc);
			dstWriter.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeMultiContent() {
		File dstFile = new File(dstPath);
		SAXReader dstReader = new SAXReader();
		OutputFormat outputFormat = new OutputFormat();
		isFind2 = false;
		try {
			Document doc = dstReader.read(dstFile);
			Element root = doc.getRootElement();
			parseStyle(root);
			parseMultiElements(root, 0);
			
			XMLWriter dstWriter = new XMLWriter(new FileOutputStream(dstFile), outputFormat);
			dstWriter.write(doc);
			dstWriter.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parseMultiElements(Element element, int xmlConstructLayer) {
		if(element.getName().equals("Worksheet")) {
			if(element.attribute(0).getName().equals("Name") && !element.attribute(0).getData().equals(workSheet)) {
				return;
			}
		}
		
		if((element.getName().equals("Row") || element.getName().equals("Column")) && isFindAddEnsure == false) {
			addIndex++;
		}
		
		if(element.getName().equals("Data")) {
			if(isNumber(element.getData().toString())) {
				int keyNumber = Integer.parseInt(element.getData().toString());
				int keyTargetNumber = Integer.parseInt(keyContent);
				int keyNumberLowerBound = Integer.parseInt(keyContentLowerBound);
				int keyNumberUpperBound = Integer.parseInt(keyContentUpperBound);
				
				if(keyNumber == keyTargetNumber) {
					isExist = true;
					return;
				}
				
				if(keyNumber >= keyNumberLowerBound && keyNumber <= keyNumberUpperBound && keyNumber > keyTargetNumber) {
					isFindAddEnsure = true;
				}
			}
			return;
		}
		
		String elementName = xmlConstruct[xmlConstructLayer];
		List<Element> list = element.elements();
		for(int i = 0; i < list.size(); i++) {
			Element nextElement = list.get(i);
			if(nextElement.getName().equals(elementName) || nextElement.getName().equals("Column")) {
				parseMultiElements(nextElement, xmlConstructLayer+1);
			}
		}
		
		
		if(element.getName().equals("Row")) {
			Element cell = null;
			if(element.elements().size() > 0) {
				cell = (Element)element.elements().get(0);
			}
			Element data = null;
			if(cell.elements().size() > 0) {
				data = (Element)cell.elements().get(0);
			}
			if(data != null && data.getData() != null && isNumber(data.getData().toString())) {
				int keyLowerBound = Integer.parseInt(keyContentLowerBound);
				int keyUpperBound = Integer.parseInt(keyContentUpperBound);
				int keyStartRowIndex = Integer.parseInt(keyContent)/10;
				int keyNumber = Integer.parseInt(data.getData().toString());
				
				if(multiResults.size() > 0 && ((keyNumber >= keyLowerBound && keyNumber <= keyUpperBound && keyNumber/10 >= keyStartRowIndex) || (keyNumber >= keyLowerBound*10 && keyNumber <= keyUpperBound*10 && keyNumber/100 >= keyStartRowIndex) || (keyNumber >= keyLowerBound*100 && keyNumber <= keyUpperBound*100 && keyNumber/1000 >= keyStartRowIndex))) {
					Element resultRow = freshStyleElement(multiResults.get(0));
					Element resultCell = (Element)resultRow.elements().get(0);
					Element resultData = (Element)resultCell.elements().get(0);
					if(resultData.getData() != null && isNumber(resultData.getData().toString())) {
						int resultKey = Integer.parseInt(resultData.getData().toString());
						if(keyNumber == resultKey) {
							isExist = true;
						}
					}
					
				}
				if(isFind2 == false  && isFindAddEnsure == true) {
					if((keyNumber >= keyLowerBound && keyNumber <= keyUpperBound && keyNumber/10 >= keyStartRowIndex) || (keyNumber >= keyLowerBound*10 && keyNumber <= keyUpperBound*10 && keyNumber/100 >= keyStartRowIndex) || (keyNumber >= keyLowerBound*100 && keyNumber <= keyUpperBound*100 && keyNumber/1000 >= keyStartRowIndex)) {
						isFind2 = true;
					} else {
						isFindAddEnsure = false;
					}
				}
			}
		}
		
		
		
		if(element.getName().equals("Table")) {
			Attribute attribute = element.attribute("ExpandedRowCount");
			String expandedRowCount = attribute.getData().toString();
			if(isNumber(expandedRowCount)) {
				attribute.setData(Integer.parseInt(expandedRowCount)+multiResults.size());
			}
			//Element addElement = createNewElementByCopied(styleElement, result);
			for(int i = 0; i < multiResults.size(); i++) {
				Element addElement = freshStyleElement(multiResults.get(i));
				if(addElement == null) {
					System.out.println(srcPath + " " + workSheet + ":addElement is null");
					return;
				}
				//System.out.println(result.getStringValue());
				if(isExist == false) {
					element.elements().add(addIndex-1+i, addElement);
				}
				//System.out.println(addIndex);
			}
			return;
		}
	}
	
	public void parseElement(Element element, int xmlConstructLayer) {
		if(element.getName().equals("Worksheet")) {
			if(element.attribute(0).getName().equals("Name") && !element.attribute(0).getData().equals(workSheet)) {
				return;
			}
		}
		
		if((element.getName().equals("Row") || element.getName().equals("Column")) && isFindAddEnsure == false) {
			addIndex++;
		}
		
		if(element.getName().equals("Data")) {
			if(isNumber(element.getData().toString())) {
				int keyNumber = Integer.parseInt(element.getData().toString());
				int keyTargetNumber = Integer.parseInt(keyContent);
				int keyNumberLowerBound = Integer.parseInt(keyContentLowerBound);
				int keyNumberUpperBound = Integer.parseInt(keyContentUpperBound);

				if(keyNumber == keyTargetNumber && cellIndex == 0) {
					isExist = true;
					return;
				}
				
				if(keyNumber >= keyNumberLowerBound && keyNumber <= keyNumberUpperBound && keyNumber > keyTargetNumber) {
					isFindAddEnsure = true;
				}
			}
			return;
		}
		
		String elementName = xmlConstruct[xmlConstructLayer];
		List<Element> list = element.elements();
		for(int i = 0; i < list.size(); i++) {
			Element nextElement = list.get(i);
			if(nextElement.getName().equals("Cell")) {
				cellIndex = i;
			}
			if(nextElement.getName().equals(elementName) || nextElement.getName().equals("Column")) {
				parseElement(nextElement, xmlConstructLayer+1);
			}
		}
		
		if(element.getName().equals("Row")) {
			if(element.attribute("Index") != null) {
				int indexNow = Integer.parseInt(element.attribute("Index").getValue());
				element.attribute("Index").setValue(String.valueOf(indexNow + 1));
			}
			/*
			if(isExist == true) {
				Element cell = null;
				if(element.elements().size() > 0) {
					cell = (Element)element.elements().get(0);
				}
				Element data = null;
				if(cell.elements().size() > 0) {
					data = (Element)cell.elements().get(0);
				}
				
				int keyNumber = Integer.parseInt(data.getData().toString());
				int keyTargetNumber = Integer.parseInt(keyContent);
				
				if(keyNumber != keyTargetNumber) {
					System.err.println(srcPath + ":" + workSheet + "|" + keyNumber + "|" + keyTargetNumber);
					isExist = false;
				}
			}
			*/
			
		}
		
		if(element.getName().equals("Table")) {
			Attribute attribute = element.attribute("ExpandedRowCount");
			String expandedRowCount = attribute.getData().toString();
			if(isNumber(expandedRowCount)) {
				attribute.setData(Integer.parseInt(expandedRowCount)+1);
			}
			//Element addElement = createNewElementByCopied(styleElement, result);
			Element addElement = freshStyleElement(result);
			if(addElement == null) {
				System.out.println(srcPath + " " + workSheet + ":addElement is null");
				return;
			}
			//System.out.println(result.getStringValue());
			if(isExist == false) {
				element.elements().add(addIndex-1, addElement);
			}
			//System.out.println(addIndex);
			return;
		}
	}
	
	
	public Element getCopyContent() {
		File srcFile = new File(srcPath);
		SAXReader srcReader = new SAXReader();
		
		try {
			Document doc = srcReader.read(srcFile);
			Element root = doc.getRootElement();
			getContent(root, 0, keyContent, workSheet);
			
			return result;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void getContent(Element element, int xmlConstructLayer, String target, String workSheetName) {
		if(element.getName().equals("Data")) {
			if(element.getData().equals(target)) {
				isFind = true;
			}
			return;
		}
		
		if(element.getName().equals("Worksheet")) {
			if(element.attribute(0).getName().equals("Name") && !element.attribute(0).getData().equals(workSheetName)) {
				return;
			}
		}
		
		String elementName = xmlConstruct[xmlConstructLayer];
		List<Element> list = element.elements();
		for(int i = 0; i < list.size(); i++) {
			Element nextElement = list.get(i);
			if(nextElement.getName().equals(elementName)) {
				getContent(nextElement, xmlConstructLayer+1, target, workSheetName);
			}
		}
		if(element.getName().equals("Row") && isFind == true) {
			Element cell = (Element)element.elements().get(0);
			Element keyData = (Element)cell.elements().get(0);
			if(keyContent2.equals("0") && !keyData.getData().toString().equals(keyContent)) {
				isFind = false;
				return;
			}
			returnKeyContent = keyData.getData().toString();
			//System.out.println(element.createCopy().getStringValue());
			result = element.createCopy();
			//System.out.println(result.getStringValue());
			//System.out.println("-------------------------------------------------");
			isFind = false;
			
			if(specialIndex != null) {
				for(int j = 0; j < specialIndex.length; j++) {
					parseSpeicalContentByIndex(element, j);
				} 
			}
		}
	}
	
	public void parseSpeicalContentByIndex(Element element, int index) {
		Element contentCell = null;
		for(int i = 0; i < element.elements().size(); i++) {
			contentCell = (Element)element.elements().get(i);
			Attribute attribute = contentCell.attribute("Index");
			if(attribute != null) {
			}
			if(attribute != null && attribute.getValue().equals(String.valueOf(specialIndex[index]+1))) {
				break;
			}
			if(i == specialIndex[index]) {
				break;
			}
		}
		//System.out.println(contentCell.getStringValue());
		if(contentCell != null && contentCell.element("Data") != null) {
			Element contentData = (Element)contentCell.elements().get(0);
			//System.out.println("! " + contentData.getStringValue());
			returnContentByIndex.add(contentData.getData().toString());
		}
	}
	
	public Element createNewElementByCopied(Element styleElment, Element addElement) {
		Element result = styleElment.createCopy();
		if(result.attribute("Index") != null) {
			Attribute index = result.attribute("Index");
			result.remove(index);
		}
		//System.out.println(result.getStringValue());
		List<Element> cellList1 = result.elements();
		List<Element> cellList2 = addElement.elements();
		for(int i = 0; i < Math.max(cellList1.size(), cellList2.size()); i++) {
			if(i < Math.min(cellList1.size(), cellList2.size())) {
				Element cell1 = cellList1.get(i);
				Element cell2 = cellList2.get(i);
				if(cell1.element("Data") != null && cell2.element("Data") != null) {					
					//deal with index
					if(cell1.attributeCount() <= cell2.attributeCount()) {
						for(int j = 0; j < cell2.attributeCount(); j++) {
							if(cell1.attribute(cell2.attribute(j).getName()) == null) {
								cell1.addAttribute("ss:" + cell2.attribute(j).getName(), cell2.attribute(j).getData().toString());
							}
						}
					}
					if(cell1.element("Data").elements().size() > 0) {
						Element dataElement = cell1.element("Data");
						List<Element> removeElementList = dataElement.elements();
						
						for(int j = 0; j < removeElementList.size(); j++) {
							Element removeElement = removeElementList.get(j);
							dataElement.remove(removeElement);
						}
					}
					
					cell1.element("Data").setText(cell2.getStringValue());
					//System.out.println(cell1.element("Data").getData());
				}
				
				if(cell1.element("Data") == null && cell2.element("Data") != null) {
					cell1.add(cell2.element("Data").createCopy());
				}
				if(cell1.element("Data") != null && cell2.element("Data") == null) {
					Element removeElement = cell1.element("Data");
					cell1.remove(removeElement);
				}
			} else if(cellList1.size() > cellList2.size()) {
				Element cell1 = cellList1.get(i);
				Element removeElement = cell1.element("Data");
				cell1.remove(removeElement);
			} else if(cellList1.size() < cellList2.size()) {
				Element tempCell1 = cellList2.get(i).createCopy();
				cellList1.add(tempCell1);
			}
		}
		return result;
	}
	
	public String getReturnKeyContent() {
		return returnKeyContent;
	}
	
	public ArrayList<String> getReturnArtPath() {
		return returnArtPath;
	}
	
	public ArrayList<String> getReturnContentByIndex() {
		return returnContentByIndex;
	}
	
	public void parseArtPathList(Element root) {
		List<Element> cellList = root.elements();
		for(int i = 0; i < cellList.size(); i++) {
			Element cell = cellList.get(i);
			if(cell.getName().equals("Cell") && cell.element("Data") != null) {
				String data = cell.element("Data").getData().toString();
				if(data.contains("res/")) {
					returnArtPath.add(data);
				}
			}
		}
	}
	
	public Element freshStyleElement(Element element) {
		Element result = element.createCopy();
		if(result.attribute("Index") != null) {
			Attribute index = result.attribute("Index");
			result.remove(index);
		}
		if(result.attribute("StyleID") != null) {
			Attribute styleId = result.attribute("StyleID");
			result.remove(styleId);
		}
		List<Element> cellList = result.elements();
		for(int i = 0; i < cellList.size(); i++) {
			Element cell = cellList.get(i);
			Attribute styleId = cell.attribute("StyleID");
			if(styleId != null) {
				styleId.setValue("Default");
			}
		}
		return result;
	}
	
	public void parseStyle(Element root) {
		Element styles = root.element("Styles");
		List<Element> styleList = styles.elements();
		for(int i = 0; i < styleList.size(); i++) {
			Element style = styleList.get(i);
			if(style.attribute("ID") != null && style.attribute("ID").getData().equals("Default")) {
				Element alignment = style.element("Alignment");
				if(alignment.attribute("Horizontal") != null) {
					alignment.attribute("Horizontal").setValue("Left");
				} else {
					alignment.addAttribute("ss:Horizontal", "Left");
				}
				
				Element font = style.element("Font");
				if(font.attribute("FontName") != null) {
					font.attribute("FontName").setValue("Arial Unicode MS");
				} else {
					font.addAttribute("ss:FontName", "Arial Unicode MS");
				}
				
				if(font.attribute("Size") != null) {
					font.attribute("Size").setValue("11");
				} else {
					font.addAttribute("ss:Size", "11");
				}
			}
		}
	}
	
	public ArrayList<String> getMultiReturnKeyContent() {
		ArrayList<String> result = new ArrayList<>();
		for(int i = 0; i < multiResults.size(); i++) {
			List<Element> cellList = multiResults.get(i).elements();
			List<Element> dataList = cellList.get(0).elements();
			result.add(dataList.get(0).getData().toString());
		}
		return result;
	}
}
