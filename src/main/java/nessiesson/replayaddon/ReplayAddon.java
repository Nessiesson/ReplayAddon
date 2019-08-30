package nessiesson.replayaddon;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
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

import java.util.Locale;

import static nessiesson.replayaddon.Configuration.shouldSpamLog;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, clientSideOnly = true)
public class ReplayAddon {
	private static Logger logger;
	private static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(Reference.MODID)) {
			ConfigManager.sync(Reference.MODID, Config.Type.INSTANCE);
		}
	}

	@SubscribeEvent
	public void printPlayerPosition(TickEvent.RenderTickEvent event) {
		if(!shouldSpamLog) return;
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer camera = mc.player;
		if (event.phase != TickEvent.Phase.START || camera == null) return;

		Vec2f look = camera.getPitchYaw();
		logger.info(String.format(Locale.US, "%.2f,%.2f,%.2f,%.4f,%.4f,0", camera.posX, camera.posY, camera.posZ, look.x, look.y));
	}
}
