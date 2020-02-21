package com.floongle.helpers;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;


public class Validatorer {

  private static int dotCounter = 0;
  

  
  
  public static void main(String[] args) {
    
    Validatorer XMLValidator = new Validatorer();   
    ArrayList<File> fileList = new ArrayList<File>();
    
    //Get the configuration
    HashMap<String, String> configData = XMLValidator.getConfig("c:/sbsData/config.cfg");
    
    
    //Get the list of xml files to judge
    fileList = XMLValidator.getFileList(configData.get("xmlDirectory"));


    // Loop through each xml file and 
    for(File xmlFile : fileList)
    {
      String valid = XMLValidator.validate(xmlFile, configData.get("xsdFile"));
      if (valid == null ) printDot();  
      else 
        {
        System.out.println("\n" + xmlFile.getName() +" Validation Failed: " + valid);

        }
      
    }
    
   System.out.println(" Done.");
  
  }

  private HashMap<String, String> getConfig(String configFileName) {
    
    HashMap<String, String> result = new HashMap<String, String>();
    
    // Config file format is Key|Value\n
    //
    // For example:
    //
    //  xsdFile|c:\test\someSchema.xsd
    //  xmlDirectory|c:\test\xmlData
    //
    System.out.println("starts");
    try {

      File myObj = new File(configFileName);
      Scanner myReader = new Scanner(myObj);
      System.out.println("doingit");
      while (myReader.hasNextLine()) {
        String lineData = myReader.nextLine();
        String[] splitLineArray = lineData.split("\\|");
        System.out.println("len:"+ splitLineArray.length + "\n");
        if (splitLineArray.length == 2) result.put(splitLineArray[0], splitLineArray[1]);
        
      }
      
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }    

    return result;
  }

  private static void printDot() {
    if (dotCounter > 80)
    {
      dotCounter = 1;
      System.out.println();      
    }
    else
    {
      dotCounter++;
    }
    System.out.print(".");
    
  }

  private ArrayList<File> getFileList(String path) {
    ArrayList<File> result = new ArrayList<File>();
    
    
    File folder = new File(path);
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        System.out.println("File " + listOfFiles[i].getName());
        
        if (listOfFiles[i].getName().endsWith(".xml")) result.add(listOfFiles[i]);
        
      } else if (listOfFiles[i].isDirectory()) {
        System.out.println("Directory " + listOfFiles[i].getName());
      }
    }
    
    
    return result;
  }

  private String validate(File xmlFile, String schemaFile) {
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      try {

          Schema schema = schemaFactory.newSchema(new File(schemaFile));

          Validator validator = schema.newValidator();

          validator.validate(new StreamSource( xmlFile));
          return null;
      } catch (SAXException | IOException e) {
        //  e.printStackTrace();
        
        if (e.toString().contains("0000233A")) return null; // This line hides any errors thrown up by DTT files getting into the mix.
        
          return e.toString();
      }
  }


}
