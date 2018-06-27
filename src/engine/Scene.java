/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import engine.items.SkyBox;
import engine.items.GameItem;
import engine.graph.Mesh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Weronika
 */
public class Scene {

    private List<GameItem> gameItems;
    private Map<Mesh, List<GameItem>> meshMap;
    private SkyBox skyBox;
    private SceneLight sceneLight;

    public Scene() {
        gameItems = new ArrayList<>();
        meshMap = new HashMap();
    }

    public List<GameItem> getGameItems() {
        return gameItems;
    }
    
    public Map<Mesh, List<GameItem>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(List<GameItem> gameItems) {
        int numGameItems = gameItems.size();
        if(gameItems == null){
            numGameItems = 0;
        }
        
        for (int i=0; i<numGameItems; i++) {
            GameItem gameItem = gameItems.get(i);
            Mesh mesh = gameItem.getMesh();
            List<GameItem> list = meshMap.get(mesh);
            if ( list == null ) {
                list = new ArrayList<>();
                meshMap.put(mesh, list);
            }
            list.add(gameItem);
        }
        
        //this.gameItems = gameItems;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }
}
