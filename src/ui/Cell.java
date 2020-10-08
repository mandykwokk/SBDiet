/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

public class Cell
{
  private String imageFileName;
  
  public Cell()
  {
    imageFileName = null;
  }
  
  public String getImageFileName()
  {
    return imageFileName;
  }
  
  public void setImageFileName(String fileName)
  {
    imageFileName = fileName;
  }
}