package nessiesson.replayaddon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Logger;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Locale;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES, clientSideOnly = true)
public class ReplayAddon {
	public static final ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
	private static final Bindings bindings = ReplayAddon.engine.createBindings();
	private Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
		Configuration.fovFunction = String.valueOf(Minecraft.getMinecraft().gameSettings.fovSetting);
	}

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent
	public void printPlayerPosition(TickEvent.RenderTickEvent event) {
		if (!Configuration.shouldSpamLog || event.phase != TickEvent.Phase.START) return;
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer camera = mc.player;
		if (camera == null) return;
		final Vec2f look = camera.getPitchYaw();
		this.logger.info(String.format(Locale.US, "%.2f,%.2f,%.2f,%.4f,%.4f,0", camera.posX, camera.posY, camera.posZ, look.x, look.y));
	}

	@SubscribeEvent
	public void dynamicFOV(TickEvent.RenderTickEvent event) {
		if (!Configuration.customFOV || event.phase != TickEvent.Phase.START) return;
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer camera = mc.player;
		if (camera == null) return;
		final World world = camera.world;
		final long worldTime = world.getTotalWorldTime();
		bindings.put("f", worldTime);
		bindings.put("t", worldTime);
		bindings.put("x", worldTime);
		try {
			final float fov = ((Number) ReplayAddon.engine.eval(Configuration.fovFunction, bindings)).floatValue();
			mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov);
		} catch (Exception ignored) {
			// noop
		}
	}
}
