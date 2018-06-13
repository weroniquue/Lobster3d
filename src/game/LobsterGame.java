/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.OBJLoader;
import engine.graph.Texture;
import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;

/**
 *
 * @author Weronika
 */
public class LobsterGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;
    private final Vector3f cameraInc;
    private final Renderer renderer;
    //private GameItem[] gameItems;
    private List<GameItem> gameItems;
    private final Camera camera;
    private static final float CAMERA_POS_STEP = 0.05f;

    public LobsterGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        gameItems = new ArrayList<>();
    }

    @Override
    public void init(Window window) throws Exception {

        renderer.init(window);
        //        // Create the Mesh
        //        float[] positions = new float[]{
        //            // VO
        //            -0.5f, 0.5f, 0.5f,
        //            // V1
        //            -0.5f, -0.5f, 0.5f,
        //            // V2
        //            0.5f, -0.5f, 0.5f,
        //            // V3
        //            0.5f, 0.5f, 0.5f,
        //            // V4
        //            -0.5f, 0.5f, -0.5f,
        //            // V5
        //            0.5f, 0.5f, -0.5f,
        //            // V6
        //            -0.5f, -0.5f, -0.5f,
        //            // V7
        //            0.5f, -0.5f, -0.5f,};
        //
        //        int[] indices = new int[]{
        //            // Front face
        //            0, 1, 3, 3, 1, 2,
        //            // Top Face
        //            4, 0, 3, 5, 4, 3,
        //            // Right face
        //            3, 2, 7, 5, 3, 7,
        //            // Left face
        //            6, 1, 0, 6, 0, 4,
        //            // Bottom face
        //            2, 1, 6, 2, 6, 7,
        //            // Back face
        //            7, 6, 4, 7, 4, 5,};
        //
        //        float[] colours = new float[]{
        //            1.0f, 1.0f, 0.0f,
        //            0.545f, 0.0f, 0.545f,
        //            0.780f, 0.082f, 0.522f,
        //            1.0f, 0.078f, 0.576f,
        //            1.0f, 1.0f, 0.0f,
        //            0.545f, 0.0f, 0.545f,
        //            0.780f, 0.082f, 0.522f,
        //            1.0f, 0.078f, 0.576f,};
        //        Cube cube = new Cube();
        //        Texture texture = new Texture("button.png");
        //        Texture a = new Texture("a.png");
        //
        //        Mesh mesh = new Mesh(cube.getPositions(), cube.getTextCoords(), cube.getIndices(), texture);
        //        GameItem gameItem = new GameItem(mesh);
        //        gameItem.setScale(0.5f);
        //        gameItem.setPosition(0, 0, -2);
        //        gameItem.setActive(true);
        //
        //        Mesh mesh1 = new Mesh(cube.getPositions(), cube.getTextCoords(), cube.getIndices(), a);
        //
        //        GameItem gameItem2 = new GameItem(mesh1);
        //        gameItem2.setScale(0.5f);
        //        gameItem2.setPosition(0.5f, 0.5f, -2);
        //
        //        GameItem gameItem3 = new GameItem(mesh);
        //        gameItem3.setScale(0.5f);
        //        gameItem3.setPosition(0, 0, -2.5f);
        //
        //        GameItem gameItem4 = new GameItem(mesh1);
        //        gameItem4.setScale(0.5f);
        //        gameItem4.setPosition(0.5f, 0, -2.5f);
        //
        //        gameItems.add(gameItem);
        //        gameItems.add(gameItem2);
        //        gameItems.add(gameItem3);
        //        gameItems.add(gameItem4);

        Mesh mesh = OBJLoader.loadMesh("piasek.obj");
        Texture texture = new Texture("piasek.png");
        mesh.setTexture(texture);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPosition(0, -1, -2);
        
        gameItems.add(gameItem);
        
        
        Mesh palma = OBJLoader.loadMesh("palma.obj");
        GameItem gameItemPalma = new GameItem(palma);
        gameItemPalma.setScale(0.095f);
        gameItemPalma.setPosition(0, -1, -2);
        
        gameItems.add(gameItemPalma);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;

        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        //wybor aktywenej kostki po kliknieciu na tab, updatuje sie tylko aktywna kostka
        if (window.isKeyPressed(GLFW_KEY_TAB)) {
            int index = 0;
            for (int i = 0; i < gameItems.size(); i++) {
                if (gameItems.get(i).isActive()) {
                    gameItems.get(i).setActive(false);
                    i += 1;
                    if (i >= gameItems.size()) {
                        i = 0;
                    }
                    gameItems.get(i).setActive(true);
                }

            }
        }

    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderer.render(window, camera, gameItems);

    }

    @Override
    public void cleanup() {
        renderer.cleanup();

        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

}
