package com.example.examplemod;


// import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
// import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderTarget;
// import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
// import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;

// import javax.imageio.ImageIO;

public class VideoCapture {

    public static byte[] captureRaw(){
        Minecraft mc = Minecraft.getInstance();
        RenderTarget framebuffer = mc.getMainRenderTarget();

        int width = framebuffer.width;
        int height = framebuffer.height;
        // Bind the framebuffer
        //RenderSystem.bindTexture(framebuffer.frameBufferId);
        framebuffer.bindRead();
        // Allocate buffer for pixel data
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        // Read pixels from framebuffer
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        framebuffer.unbindRead();

        byte[] imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);
        return imageBytes;
    }


    public static BufferedImage captureFrame() {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget framebuffer = mc.getMainRenderTarget();

        int width = framebuffer.width;
        int height = framebuffer.height;
        
        // Bind the framebuffer
        //RenderSystem.bindTexture(framebuffer.frameBufferId);
        framebuffer.bindRead();
        // Allocate buffer for pixel data
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        // Read pixels from framebuffer
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        framebuffer.unbindRead();

        byte[] imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        image.getRaster().setDataElements(0, 0, width, height, imageBytes);
        return flipVertically(image);//image;

    }

    public static byte[] capture_image(String format) throws IOException{
        BufferedImage frame = captureFrame();
        return toByteArray(frame,format);
    }

    public static byte[] toByteArray(BufferedImage image,String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    private static BufferedImage flipVertically(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, img.getType());
        
        Graphics2D g = newImage.createGraphics();
        g.drawImage(img, 0, height, width, -height, null);
        g.dispose();
        
        return newImage;
    }
}

