/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

/**
 *
 * @author Weronika
 */
public interface IGameLogic {
    void init(Window window) throws Exception;

    void input(Window window,  MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput);
    
    void render(Window window);
    
    void cleanup();
}
