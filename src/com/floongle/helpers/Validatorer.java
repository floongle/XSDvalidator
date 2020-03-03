package com.floongle.helpers;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Validatorer {

   
  
  public static void main(String[] args) { 
    Validatorer xmlValidator = new Validatorer();   
  
    //Get the configuration
    HashMap<String, String> configData = xmlValidator.getConfig("c:/sbsData/config.cfg");    
    
    //Get the list of xml files to judge
    ArrayList<File> listOfFiles = FileHelpers.getFileList(configData.get("xmlDirectory"));
    
  
    // Loop through each xml file and validate it
    for(File xmlFile : listOfFiles)
    {
      String errorStringFromValidator = xmlValidator.validate(xmlFile, configData.get("xsdFile"));
      if (errorStringFromValidator == null ) 
        {
        StringHelpers.printDot();  
        }
      else 
        {
        System.out.println("\n" + xmlFile.getName() +" Validation Failed: " + errorStringFromValidator);
        }
      
    } //end For
    
   System.out.println(" Done.");  
  }








  
  

  private String validate(File xmlFile, String schemaFile) {
    // validates the file and returns any error (or null if the file validates OK)
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      
      try {
          Schema schema = schemaFactory.newSchema(new File(schemaFile));

          Validator validator = schema.newValidator();
          
          
          // This is to stop the validator bothering about reading inline DTDs in the XML files. 
          LSResourceResolver lsr = new LSResourceResolver() {            
            @Override
            public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
              LSInput input = new DOMInputImpl();     
              input.setCharacterStream(new StringReader("")); //This is the magic line. It sets the returned DTD to be empty.
              input.setSystemId(systemId);
              input.setPublicId(publicId);
              return input;
            }
          };   
          
          validator.setResourceResolver(lsr);

          //Perform the validation
          validator.validate(new StreamSource(xmlFile));

          return null;
      } catch (SAXException | IOException e) {
        //  e.printStackTrace();
        
        if (e.toString().contains("0000233A")) return null; // This line hides any errors thrown up by DTT files getting into the mix.
        
          return e.toString();
      }
  }


  
  
  
  
  
  
  //Configuration File loading
  
  private HashMap<String, String> getConfig(String configFileName) {
    
    HashMap<String, String> result = new HashMap<String, String>();
    
    // Config file format is Key|Value|\n
    //
    // For example:
    //
    //  xsdFile|c:\test\someSchema.xsd|
    //  xmlDirectory|c:\test\xmlData|
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

  
}
