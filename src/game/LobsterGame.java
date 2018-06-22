/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import engine.*;
import engine.graph.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import engine.graph.DirectionalLight;
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

    private float lightAngle;
    private static final float CAMERA_POS_STEP = 0.05f;
    private float spotAngle = 0;
    private float spotInc = 1;

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

        //Setup GameItems
        float reflectance = 1f;
        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
        Texture texture = new Texture("/textures/piasek.png");
        Material material = new Material(texture, reflectance);
        mesh.setMaterial(material);

        Mesh palm = OBJLoader.loadMesh("/models/palma.obj");
        Texture palmTexture = new Texture("/textures/linia.png");
        Material palmMaterial = new Material(palmTexture, reflectance);
        palm.setMaterial(palmMaterial);
        
        float blockScale = 0.5f;
        float skyBoxScale = 10.0f;
        float extension = 2.0f;

        float startx = extension * (-skyBoxScale + blockScale);
        float startz = extension * (skyBoxScale - blockScale);
        float starty = -1.0f;
        float inc = blockScale * 2;

        float posx = startx;
        float posz = startz;
        float incy = 0.0f;
        
        for (int i = 0; i < 10; i++) {
                GameItem palmItem = new GameItem(palm);
                palmItem.setScale(blockScale);
                incy = Math.random() > 0.9f ? blockScale * 2 : 0f;
                palmItem.setPosition(posx, starty + incy, posz);
                gameItems.add(palmItem);
            float pom = (float) Math.floor((float) Math.random() * (50 +50 +1) -50);
            posx = pom;
            posz -= inc + pom;
        }
        
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0.0f, 0.0f, 0.0f);

       
        gameItems.add(gameItem);
        scene.setGameItems(gameItems);

        // Setup  SkyBox
        skyBox = new SkyBox("/models/skybox.obj", "/textures/beach.png");
        skyBox.setScale(50.0f);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        // Create HUD
        hud = new Hud("DEMO");

        camera.getPosition().x = 0.65f;
        camera.getPosition().y = 1.15f;
        camera.getPosition().y = 4.34f;

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            cameraInc.z = -2;
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

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {

        System.out.println("Camera:x "+ camera.getPosition().x + "y " + camera.getPosition().y + "z " + camera.getPosition().z);
         System.out.println("Rotation:x "+ camera.getRotation().x + "y " + camera.getRotation().y + "z " + camera.getRotation().z);
        
        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);

            // Update HUD compass
            hud.rotateCompass(camera.getRotation().y);
        }

        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
        

        
        
        SceneLight sceneLight = scene.getSceneLight();

        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = sceneLight.getDirectionalLight();
        lightAngle += 1.1f;
        directionalLight.setIntensity(0);
        /*if (lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360) {
                lightAngle = -90;
            }
            sceneLight.getAmbientLight().set(0.3f, 0.3f, 0.4f);
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
        //renderer.render(window, camera, gameItems, sceneLight, hud);
        renderer.render(window, camera, scene, hud);
        //gameItems, ambientLight, pointLightList, spotLightList, directionalLight);

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

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));
    }

}
