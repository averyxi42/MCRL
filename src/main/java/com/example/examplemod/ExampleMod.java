package com.example.examplemod;

import org.slf4j.Logger;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.logging.LogUtils;
import java.lang.Math;

import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.core.registries.BuiltInRegistries;
// import net.minecraft.core.registries.Registries;
// import net.minecraft.network.chat.Component;
// import net.minecraft.world.food.FoodProperties;
// import net.minecraft.world.item.BlockItem;
// import net.minecraft.world.item.CreativeModeTab;
// import net.minecraft.world.item.CreativeModeTabs;
// import net.minecraft.world.item.Item;
// import net.minecraft.world.level.block.Block;
// import net.minecraft.world.level.block.Blocks;
// import net.minecraft.world.level.block.state.BlockBehaviour;
// import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import net.neoforged.neoforge.client.event.RenderFrameEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.ClientPauseChangeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
// tick.PlayerTickEvent.Pre;
import net.neoforged.neoforge.common.NeoForge;
// import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
// import net.neoforged.neoforge.event.entity.player.
import java.util.HexFormat;
// import net.neoforged.neoforge.registries.DeferredBlock;
// import net.neoforged.neoforge.registries.DeferredHolder;
// import net.neoforged.neoforge.registries.DeferredItem;
// import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import java.io.File;

import net.minecraft.world.entity.player.Input;
import com.mojang.blaze3d.platform.InputConstants;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mcrl_cl";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    // public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    // public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    // public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    // public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    // public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    // public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
    //         .alwaysEdible().nutrition(1).saturationModifier(2f).build()));
    public static BroadcastServer videoServer;
    public static BroadcastServer mdServer; //metadata
    public static LineListener lineListener;

    public static Minecraft mc;
    public static RenderTarget window;

    public static ByteBuffer mdBuffer = ByteBuffer.allocateDirect(8);
    // // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    // public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
    //         .title(Component.translatable("itemGroup.examplemod")) //The language key for the title of your CreativeModeTab
    //         .withTabsBefore(CreativeModeTabs.COMBAT)
    //         .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
    //         .displayItems((parameters, output) -> {
    //             output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
    //         }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExampleMod(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // // Register the Deferred Register to the mod event bus so blocks get registered
        // BLOCKS.register(modEventBus);
        // // Register the Deferred Register to the mod event bus so items get registered
        // ITEMS.register(modEventBus);
        // // Register the Deferred Register to the mod event bus so tabs get registered
        // CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        mc = Minecraft.getInstance();

        // window = mc.getMainRenderTarget();

        try{
           videoServer = new BroadcastServer(8888);
           mdServer = new BroadcastServer(8000);
           lineListener = new LineListener(8083,new ActionModel());
           
           LOGGER.info("video server initialized");
        } catch (IOException e){
           LOGGER.error(MODID, e);
           LOGGER.info("failed to enable video stream server");
        }
        // // Register the item to a creative tab
        // modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        // if (Config.logDirtBlock)
        //     LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // // Add the example block item to the building blocks tab
    // private void addCreative(BuildCreativeModeTabContentsEvent event)
    // {
    //     if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
    //         event.accept(EXAMPLE_BLOCK_ITEM);
    // }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        // @SubscribeEvent
        // public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event){
        //     mc.getWindow().setWidth(640);
        //     mc.getWindow().setHeight(480);
        //     // mc.resizeDisplay(); 
        // }
    }
    

    // @EventBusSubscriber(modid = MODID,value = Dist.CLIENT,bus = EventBusSubscriber.Bus.GAME)

    @EventBusSubscriber(modid = MODID,bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
    public static class GameTickEvents
    {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Pre event)
        {
            // System.out.println("client tick");
            try{
                videoServer.handle_connections();
                mdServer.handle_connections();
                lineListener.handle_connections();
                if(mc.player!=null){
                    lineListener.read();
                    // mc.player.turn((Math.random()-0.5)*5, (Math.random()-0.5)*5);
                    // mc.player.input.keyPresses = new Input(
                    //     true,
                    //     true,
                    //     false,
                    //     false,
                    //     true,
                    //     true,
                    //     false
                    // );
                    // if(mc.mouseHandler.isMouseGrabbed()){
                    //     mc.player.turn(-4, 0);

                    // }else{
                    //     // @ts-ignore
                    //     mc.mouseHandler.onMove(mc.getWindow().getWindow(), 854*Math.random(), 540*Math.random());
                    //     KeyMapping.click(mc.options.keyAttack.getKey());
          
                    // }
                    // if(Math.random()<0.8){
                    //     if(!mc.options.keyAttack.isDown())
                    //         KeyMapping.click(mc.options.keyAttack.getKey());
                    //     KeyMapping.set(mc.options.keyAttack.getKey(), true);
                    // }
                    // else{
                    //     KeyMapping.set(mc.options.keyAttack.getKey(),false);
                    // }
                    
                    // if(Math.random()<0.05)
                    //     KeyMapping.set(mc.options.keyUp.getKey(),true);
                    //mc.options.keyAttack.click(InputConstants.getKey("key.mouse.left"));
                }
            } catch(IOException e){
                LOGGER.error("video server connection handling failed", e);
            }
        }

        @SubscribeEvent
        public static void onPause(ClientPauseChangeEvent.Post event){
            if(event.isPaused())
                System.out.println("trying to cancel pause");
                mc.pause = false;
                // event.setCanceled(true);
        }

        @SubscribeEvent
        public static void onClientTick(RenderFrameEvent.Post event) {
            mc.gameRenderer.lastActiveTime = Util.getMillis();
            // VideoCapture captureHandler = new VideoCapture();
            //BufferedImage frame = VideoCapture.captureFrame();
            // System.out.println("render tick");

            // if(mc.getWindow().getHeight()!= mc.getMainRenderTarget().height){
            //     mc.resizeDisplay();
            // }


            try{
                // byte[] raw_frame = VideoCapture.capture_image("png");
                window = mc.getMainRenderTarget();
                
                
                mdBuffer.clear().putInt(window.width).putInt(window.height).rewind();

                mdServer.broadcast(mdBuffer);

                
                byte[] raw_frame = VideoCapture.captureRaw();
                
                ByteBuffer buff = ByteBuffer.wrap(raw_frame);
                videoServer.broadcast(buff);




            } catch(IOException e){
                LOGGER.error("broadcast failure", e);
            }
                // Process or send the frame as needed
            //  LOGGER.info("capturing frames");
            //  LOGGER.info(frame.toString());

            // try {
            // // retrieve image
            // File outputfile = new File("saved.png");
            // ImageIO.write(frame, "png", outputfile);
            // } catch (IOException e) {
            //     LOGGER.error(MODID, e);
            // }
        }
    }


}
