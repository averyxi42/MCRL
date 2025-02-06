package com.example.examplemod;

import java.util.function.Consumer;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class ActionModel implements Consumer<String>{
    private static Minecraft mc = Minecraft.getInstance();

    // public ActionHandler(Minecraft mc){
    //     this.mc = mc;
    // }

    public static void setKey(String key_str,boolean val){
        KeyMapping key = KeyMapping.get(key_str);
        if(key==null) return;

        if(!key.isDown() & val){
            KeyMapping.click(key.getKey());
        }
        KeyMapping.set(key.getKey(), val);
    }
    /**
     * Sets the position of the mouse pointer.
     * @param x x position, scaled between -1 to 1
     * @param y y position, scaled between -1 to 1
     */
    @SuppressWarnings("null")
    public static void setMousePos(double x,double y){
        if(mc.mouseHandler.isMouseGrabbed()){
            mc.player.turn(20* y, 20*x);
        }else{
            // @ts-ignore
            mc.mouseHandler.onMove(mc.getWindow().getWindow(), mc.getWindow().getScreenWidth()*x, mc.getWindow().getScreenHeight()*y);
        }
    }
    @Override
    public void accept(String arg0) {
        String[] param = arg0.split(" ");
        try {
            if(param[0].equals("mouse")){
                setMousePos(Double.parseDouble(param[1]),Double.parseDouble(param[2]));
            }
            else{
                setKey(param[0], Integer.parseInt(param[1])!=0);
            } 
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }

    }
}
