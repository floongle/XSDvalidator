package com.floongle.helpers;

import java.io.File;
import java.util.ArrayList;

public class FileHelpers {

  
  
  public static ArrayList<File> getFileList(String path) {
    ArrayList<File> listOfFilesToReturn = new ArrayList<File>();
    
    File folder = new File(path);
    File[] arrayOfAllFilesFound = folder.listFiles();

    for (int i = 0; i < arrayOfAllFilesFound.length; i++) {
      if (arrayOfAllFilesFound[i].isFile() && arrayOfAllFilesFound[i].getName().endsWith(".xml"))
      {
        System.out.println("XML File found: " + arrayOfAllFilesFound[i].getName());        
        listOfFilesToReturn.add(arrayOfAllFilesFound[i]);             
      } 
    }    
    return listOfFilesToReturn;
  }

  
}
