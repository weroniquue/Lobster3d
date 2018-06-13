/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import engine.GameEngine;
import engine.IGameLogic;
/**
 *
 * @author Weronika
 */
public class Main {
    
    public static int width = 1280;
    public static int height = 720;

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new LobsterGame();
            GameEngine gameEng = new GameEngine("LobsterGame", width, height, vSync, gameLogic);
            gameEng.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
