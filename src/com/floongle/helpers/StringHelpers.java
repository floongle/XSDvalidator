package com.floongle.helpers;

public class StringHelpers {

  
  private static int dotCounter = 0;
  
  static void printDot() {
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

}
