/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import static org.lwjgl.opengl.GL11.*;
import engine.Utils;
import engine.Window;
import engine.graph.ShaderProgram;
import engine.GameItem;
import engine.graph.Camera;
import engine.graph.Mesh;
import engine.graph.Transformation;
import java.util.List;
import org.joml.Matrix4f;

/**
 *
 * @author Weronika
 */
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;
    private ShaderProgram shaderProgram;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        //Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("fragment.fs"));
        shaderProgram.link();

        // Create uniforms for world and projection matrices
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");

        // Create uniform for default colour and the flag that controls it
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");

        window.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, List<GameItem> gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderProgram.setUniform("texture_sampler", 0);
        // Render each gameItem
        for(GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform("colour", mesh.getColour());
            shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
            mesh.render();
        }

        shaderProgram.unbind();

        //shaderProgram.bind();
        //shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        // Draw the mesh
        /* glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);*/
 /*The parameters of that method are:

mode: Specifies the primitives for rendering, triangles in this case. No changes here.
count: Specifies the number of elements to be rendered.
type: Specifies the type of value in the indices data. In this case we are using integers.
indices: Specifies the offset to apply to the indices data to start rendering.*/
        //glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        // Draw the vertices
        //glDrawArrays(GL_TRIANGLES, 0, 3);
        // Restore state
        //glDisableVertexAttribArray(0);
        //glDisableVertexAttribArray(1);
        //glBindVertexArray(0);
        //shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
