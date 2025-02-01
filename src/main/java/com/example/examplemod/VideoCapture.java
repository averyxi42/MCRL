package com.example.examplemod;


// import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.pipeline.RenderTarget;
// import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class VideoCapture {

    public BufferedImage captureFrame() {
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

        // Create ByteArrayInputStream from byte array
        // ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        // Read image from ByteArrayInputStream

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        image.getRaster().setDataElements(0, 0, width, height, imageBytes);
        return image;






        // Create a BufferedImage
        // int[] pixels = new int[width * height];
        // BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // // Convert from byte buffer to pixels
        // for (int i = 0; i < width * height; i++) {
        //     int r = buffer.get(i * 4) & 0xFF;
        //     int g = buffer.get(i * 4 + 1) & 0xFF;
        //     int b = buffer.get(i * 4 + 2) & 0xFF;
        //     int a = buffer.get(i * 4 + 3) & 0xFF;
        //     pixels[i] = ((a << 24) | (r << 16) | (g << 8) | b);
        // }

        // // Set the BufferedImage pixels and flip vertically
        // image.setRGB(0, 0, width, height, pixels, 0, width);
        //return flipVertically(image);
    }

    private BufferedImage flipVertically(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, img.getType());
        
        Graphics2D g = newImage.createGraphics();
        g.drawImage(img, 0, height, width, -height, null);
        g.dispose();
        
        return newImage;
    }
}

