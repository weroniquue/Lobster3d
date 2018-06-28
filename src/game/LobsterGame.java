/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import engine.items.SkyBox;
import engine.items.GameItem;
import engine.*;
import engine.graph.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import engine.graph.lights.DirectionalLight;
import engine.items.Terrain;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Weronika
 */
public class LobsterGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    private final Camera camera;
    //private GameItem[] gameItems;
    private List<GameItem> gameItems;
    //private Scene scene;
    private SceneLight sceneLight;
    private SkyBox skyBox;
    private Hud hud;
    private Scene scene;
    private Terrain terrain;

    private float lightAngle;
    private static final float CAMERA_POS_STEP = 0.05f;

    private GameItem gameItem;
    private float przemieszczenie = 0.0f;

    public LobsterGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0.0f, 0.0f, 0.0f);
        lightAngle = -90;

        gameItems = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {

        renderer.init(window);

        scene = new Scene();

        float skyBoxScale = 50.0f;
        float terrainScale = 10;
        int terrainSize = 3;
        float minY = -0.1f;
        float maxY = 0.1f;
        int textInc = 40;
        terrain = new Terrain(terrainSize, terrainScale, minY, maxY, "/textures/heightmap2.png", "/textures/ground.png", textInc);
        scene.setGameItems(terrain.getGameItems());

        //Setup GameItems
        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/piasek.png");
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        Mesh palmLeaf = OBJLoader.loadMesh("/models/liscie.obj");
        Texture palmLeafTexture = new Texture("/textures/lisc.png");
        Material palmLeafMaterial = new Material(palmLeafTexture, reflectance);
        palmLeaf.setMaterial(palmLeafMaterial);
        
        Mesh palmTrunk = OBJLoader.loadMesh("/models/pien.obj");
        Texture palmTrunkTexture = new Texture("/textures/pien.png");
        Material palmTrunkMaterial = new Material(palmTrunkTexture, reflectance);
        palmTrunk.setMaterial(palmTrunkMaterial);
        

        float blockScale = 0.25f;


        float startx = -skyBoxScale*0.1f + blockScale;
        float startz = skyBoxScale*0.1f - blockScale;
        float starty = -2.65f;
        float inc = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy = 0.0f;

        for (int i = 0; i < 10; i++) {
            GameItem palmLeafItem = new GameItem(palmLeaf);
            GameItem palmTrunkItem = new GameItem(palmTrunk);
            palmLeafItem.setScale(blockScale);
            palmTrunkItem.setScale(blockScale);
            incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
            palmLeafItem.setPosition(posx, starty + incy, posz);
            palmTrunkItem.setPosition(posx, starty + incy, posz);
            System.out.println(palmLeafItem.getPosition());
            gameItems.add(palmLeafItem);
            gameItems.add(palmTrunkItem);
            float pom = (float) Math.floor((float) Math.random() * (50 + 50 + 1) - 50);
            posx = pom;
            posz -= inc + pom;
        }
        
        //Parasolki:
        Mesh umbrella = OBJLoader.loadMesh("/models/Umbrella2.obj");
        Texture umbrellaTexture = new Texture("/textures/stripes.png");
        Material umbrellaMaterial = new Material(umbrellaTexture, reflectance);
        umbrella.setMaterial(umbrellaMaterial);

        float distanceUmbrella = 3.0f;
        float umbrellaOne = -7.0f;
        for (int i = 0; i < 5; i++) {

            GameItem umbrellaItem = new GameItem(umbrella);
            umbrellaItem.setScale(0.3f);
            umbrellaItem.setPosition(umbrellaOne, -2.65f, 0.0f);
            gameItems.add(umbrellaItem);
            umbrellaOne+=distanceUmbrella;

        }
        
        //Lezaki:
        Mesh sunbed = OBJLoader.loadMesh("/models/sun_chair.obj");
        Texture sunbedTexture = new Texture("/textures/wood.png");
        Material sunbedMaterial = new Material(sunbedTexture, reflectance);
        sunbed.setMaterial(sunbedMaterial);
        
        float distanceSunbed = 3.5f;
        float sunbedOne = -6.75f;
        for (int i = 0; i < 4; i++) {

            GameItem sunBedItem = new GameItem(sunbed);
            sunBedItem.setScale(0.4f);
            sunBedItem.setPosition(sunbedOne, -2.40f, 0.0f);
            gameItems.add(sunBedItem);
            sunbedOne+=distanceSunbed;

        }
        
        //most:
        Mesh bridge = OBJLoader.loadMesh("/models/bridge.obj");
        bridge.setMaterial(sunbedMaterial);
        
        GameItem bridgeItem = new GameItem(bridge);
        bridgeItem.setPosition(10.0f, -5.5f, -45.0f);
        bridgeItem.setRotation(0.0f, 90.0f, 0.0f);
        gameItems.add(bridgeItem);
        
        
        scene.setGameItems(gameItems);

        // Setup  SkyBox
        skyBox = new SkyBox("/models/skybox.obj", "/textures/beach.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        // Create HUD
        hud = new Hud("DEMO");

        camera.getPosition().x = 5.0f;
        camera.getPosition().y = -0.65f;
        camera.getPosition().z = -10.0f;
        camera.getRotation().x = 8.0f;
        camera.getRotation().y = -158.0f;

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            cameraInc.z = -2;
            //gameItem.getPosition().x += 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraInc.z = 2;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            cameraInc.x = -2;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            cameraInc.x = 2;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;

        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        if (window.isKeyPressed(GLFW_KEY_8)) {
            przemieszczenie = 0.1f;
            gameItem.getPosition().x += 0.1f;
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

        System.out.println("Kamera aktualna: x: " + camera.getPosition().x + " y: " + camera.getPosition().y + " z: " + camera.getPosition().z);
        System.out.println("Rotation:x " + camera.getRotation().x + "y " + camera.getRotation().y + "z " + camera.getRotation().z);

        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }

        // Update camera position
        Vector3f prevPos = new Vector3f(camera.getPosition());
        //System.out.println("Poprzednia pozycja: "+prevPos);
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Check if there has been a collision. If true, set the y position to
        // the maximum height
//Nie jest dobrze obliczana odległość od tych wgórzy
        /*float height = terrain.getHeight(camera.getPosition());
       
        
        System.out.println("Wysokość: "+height);
        
        if (camera.getPosition().y <= height) {
            camera.setPosition(prevPos.x, prevPos.y, prevPos.z);
        }*/
        //Zapobieganie wyjscia poza teren
        if(camera.getPosition().x <= -7.5f){
            camera.getPosition().x=-7.5f;
        }
        
        if(camera.getPosition().x >= 7.5f){
            camera.getPosition().x = 7.5f;
        }
        
        if(camera.getPosition().z <= -10.0f){
            camera.getPosition().z = -10.0f;
        }
        
        if(camera.getPosition().z >= 15.0f){
            camera.getPosition().z = 15.0f;
        }
        SceneLight sceneLight = scene.getSceneLight();

        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 0.6f;
        directionalLight.setIntensity(0);

        //noc
        //sceneLight.getAmbientLight().set(0.03f, 0.03f, 0.8f);
        /*if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
            sceneLight.getAmbientLight().set(0.03f, 0.03f, 0.8f);
        } else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
            sceneLight.getAmbientLight().set(factor, factor, factor);
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            sceneLight.getAmbientLight().set(1, 1, 1);
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);*/
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        hud.updateSize(window);
        renderer.render(window, camera, scene, hud);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        if (gameItems != null) {
            for (GameItem gameItem : gameItems) {
                gameItem.getMesh().cleanUp();
            }
        }
        hud.cleanup();
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(1.0f, 1.0f, 1.0f));
        //sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(1, 1, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

}
