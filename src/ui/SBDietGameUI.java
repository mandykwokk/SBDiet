package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;

public class SBDietGameUI extends JComponent implements KeyListener, MouseListener
{
    private Cell[][] cells;
    private JFrame frame;
    private int lastKeyPressed;
    private Location lastLocationClicked;
    private BufferedImage backgroundImage;
    private boolean bgSet = false;

  public SBDietGameUI(int numRows, int numCols,String imageName)
  {
      init(numRows, numCols);
      setBackground(imageName);
  }
  public void setBackground(String imgName)
  {
      backgroundImage = loadImage(imgName);
      bgSet=true;
      repaint();
  }
  
  private BufferedImage loadImage(String imageFileName)
  {
    URL url = getClass().getResource(imageFileName);
    if (url == null)
      throw new RuntimeException("cannot find file:  " + imageFileName);
    try
    {
      return ImageIO.read(url);
    }
    catch(IOException e)
    {
      throw new RuntimeException("unable to read from file:  " + imageFileName);
    }
  }

  public int getNumRows()
  {
    return cells.length;
  }

  public int getNumCols()
  {
    return cells[0].length-1;
  }

  
  //initiate the cells with double array
  private void init(int numRows, int numCols)
  {
    lastKeyPressed = -1;
    lastLocationClicked = null;

    cells = new Cell[numRows][numCols];
    for (int row = 0; row < numRows; row++)
    {
      for (int col = 0; col < numCols; col++)
        cells[row][col] = new Cell();
    }

    frame = new JFrame("SB's Diet");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addKeyListener(this);

    int cellSize = Math.max(Math.min(500 / getNumRows(), 500 / getNumCols()), 1);
    setPreferredSize(new Dimension(cellSize * numCols, cellSize * numRows));
    addMouseListener(this);
    frame.getContentPane().add(this);
    frame.pack();
    frame.setSize(1500, 900);
    frame.setVisible(true);
  }

  private void showImage(BufferedImage image)
  {
    repaint();
  }

  private int getCellSize()
  {
    int cellWidth = getWidth() / getNumCols();
    int cellHeight = getHeight() / getNumRows();
    return Math.min(cellWidth, cellHeight);
  }

  public void keyPressed(KeyEvent e)
  {
    lastKeyPressed = e.getKeyCode();
  }

  public void keyReleased(KeyEvent e)
  {
    //ignored
  }

  public void keyTyped(KeyEvent e)
  {
    //ignored
  }

  public void mousePressed(MouseEvent e)
  {
    int cellSize = getCellSize();
    int row = e.getY() / cellSize;
    if (row < 0 || row >= getNumRows())
      return;
    int col = e.getX() / cellSize;
    if (col < 0 || col >= getNumCols())
      return;
    lastLocationClicked = new Location(row, col);
  }

  public void mouseReleased(MouseEvent e)
  {
    //ignore
  }

  public void mouseClicked(MouseEvent e)
  {
    //ignore
  }

  public void mouseEntered(MouseEvent e)
  {
    //ignore
  }

  public void mouseExited(MouseEvent e)
  {
    //ignore
  }

  public void paintComponent(Graphics g)
  {
      if(bgSet) // If there's a background image
          g.drawImage(backgroundImage,0,0,frame.getWidth(),frame.getHeight(),null);
    for (int row = 0; row < getNumRows(); row++)
    {
      for (int col = 0; col < getNumCols()+1; col++)
      {
        Location loc = new Location(row, col);
        Cell cell = cells[loc.getRow()][loc.getCol()];
        g.setColor(new Color(0,0,0,0));
        int cellSize = getCellSize();
        int x = col * cellSize;
        int y = row * cellSize;
        if(!bgSet)
            g.fillRect(x, y, cellSize, cellSize);

        String imageFileName = cell.getImageFileName();
        if (imageFileName != null)
        {
          URL url = getClass().getResource(imageFileName);
          if (url == null)
            System.out.println("File not found:  " + imageFileName);
          else
          {

            Image image = new ImageIcon(url).getImage();
            int width =  image.getWidth(null);
            int height = image.getHeight(null);
            int max;
            if (width > height)
            {
              int drawHeight = cellSize * height / width;
              g.drawImage(image, x, y + (cellSize - drawHeight) / 2, cellSize, drawHeight, null);
            }
            else
            {
              int drawWidth = cellSize * width / height;

              g.drawImage(image, x + (cellSize - drawWidth) / 2, y, drawWidth, cellSize, null);
            }
          }
        }
          g.drawRect(x, y, cellSize, cellSize);
        }
      }
  }

  public void setTitle(String title)
  {
    frame.setTitle(title);
  }

  
  //whether the location is inside the screen and valid
  public boolean isValid(Location loc)
  {
    int row = loc.getRow();
    int col = loc.getCol();
    return 0 <= row && row < getNumRows() && 0 <= col && col < getNumCols()+1;
  }

  public void setImage(Location loc, String imageFileName)
  {
    if (!isValid(loc))
      throw new RuntimeException("cannot set image for invalid location " + loc + " to \"" + imageFileName + "\"");
    cells[loc.getRow()][loc.getCol()].setImageFileName(imageFileName);
    repaint();
  }

  public String getImage(Location loc)
  {
    if (!isValid(loc))
      throw new RuntimeException("cannot get image for invalid location " + loc);
    return cells[loc.getRow()][loc.getCol()].getImageFileName();
  }

  public static void pause(int milliseconds)
  {
    try
    {
      Thread.sleep(milliseconds);
    }
    catch(Exception e)
    {
      //ignore
    }
  }

  //returns -1 if no key pressed since last call.
  //otherwise returns the code for the last key pressed.
  public int checkLastKeyPressed()
  {
    int key = lastKeyPressed;
    lastKeyPressed = -1;
    return key;
  }

  //returns null if no location clicked since last call.
  public Location checkLastLocationClicked()
  {
    Location loc = lastLocationClicked;
    lastLocationClicked = null;
    return loc;
  }
  
  public void showMessageDialog(String title, String imgFile)
  {
    JLabel lbl = new JLabel(new ImageIcon(getClass().getResource(imgFile)));
    JOptionPane.showMessageDialog(this, lbl, title, 
                                 JOptionPane.PLAIN_MESSAGE, null);
  }

  public String showInputDialog(String message)
  {
    return JOptionPane.showInputDialog(this, message);
  }
  
  public void close(){
      frame.setVisible(false);
  }
}