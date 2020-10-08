/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

public class SBDietEventHandler {
    private SBDietUI root;
    public SBDietEventHandler(SBDietUI root){
        this.root = root;
    }
    
    public void respondToStartGame(){
        root.play();
    }

}
