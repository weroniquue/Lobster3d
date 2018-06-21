/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;


import org.joml.Vector4f;
import engine.GameItem;
import engine.IHud;
import engine.TextItem;
import engine.Window;
import engine.graph.Material;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Weronika
 */
public class Hud implements IHud {

    private static final int FONT_COLS = 16;
    private static final int FONT_ROWS = 16;
    private static final String FONT_TEXTURE = "textures/font_texture.png";
    private final GameItem[] gameItems;
    private TextItem statusTextItem;
    private final GameItem compassItem;

    public Hud(String statusText) throws Exception {
        
        ///nie dziala tekst !!!
        
        this.statusTextItem = new TextItem(statusText, FONT_TEXTURE, FONT_COLS, FONT_ROWS);
        this.statusTextItem.getMesh().getMaterial().setAmbientColour(new Vector4f(1, 1, 1, 1));
            
        
        
        // Create compass
        Mesh mesh = OBJLoader.loadMesh("/models/compass.obj");
        Material material = new Material();
        material.setAmbientColour(new Vector4f(1, 1, 0, 1));
        mesh.setMaterial(material);
        compassItem = new GameItem(mesh);
        compassItem.setScale(40.0f);
        // Rotate to transform it to screen coordinates
        compassItem.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
         gameItems = new GameItem[]{compassItem};
    }

    public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }

    public void rotateCompass(float angle) {
        this.compassItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }

    public void updateSize(Window window) {
        this.statusTextItem.setPosition(10f, window.getHeight() - 50f, 0);
        this.compassItem.setPosition(window.getWidth() - 40f, 50f, 0);
    }
}
