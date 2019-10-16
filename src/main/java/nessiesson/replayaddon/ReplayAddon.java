package nessiesson.replayaddon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, clientSideOnly = true)
public class ReplayAddon {
	private Logger logger;
	private final KeyBinding test = new KeyBinding("hax", KeyConflictContext.IN_GAME, Keyboard.KEY_R, "poop");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		ClientRegistry.registerKeyBinding(this.test);
	}

	@SubscribeEvent
	public void onKeyPressed(InputEvent.KeyInputEvent event) {
		if (this.test.isPressed()) {
			Configuration.hax = !Configuration.hax;
		}
	}

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent
	public void printPlayerPosition(TickEvent.RenderTickEvent event) {
		if (!Configuration.shouldSpamLog) return;
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer camera = mc.player;
		if (event.phase != TickEvent.Phase.START || camera == null) return;

		Vec2f look = camera.getPitchYaw();
		this.logger.info(String.format(Locale.US, "%.2f,%.2f,%.2f,%.4f,%.4f,0", camera.posX, camera.posY, camera.posZ, look.x, look.y));
	}
}
