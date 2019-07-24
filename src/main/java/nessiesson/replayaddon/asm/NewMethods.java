package nessiesson.replayaddon.asm;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class NewMethods {
	private static ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
	private static Bindings bindings = engine.createBindings();

	public static boolean overrideShouldRender(RenderManager rm, Entity e, ICamera ic, double x, double y, double z) {
		if (Configuration.alwaysRenderEntities) {
			return true;
		}
		return rm.shouldRender(e, ic, x, y, z);
	}

	public static double overrideGetDistanceSq(TileEntity te, double x, double y, double z) {
		if (Configuration.alwaysRenderTileEntities) {
			return 0.0D;
		}
		return te.getDistanceSq(x, y, z);
	}

	public static void overrideSetWorldTime(WorldClient world, long time) {
		if (time < 0L) {
			time = -time;
		}

		final long worldTime = world.getTotalWorldTime();
		bindings.put("t", worldTime);
		bindings.put("x", worldTime);

		try {
			time = ((Number) engine.eval(Configuration.timeFunction, bindings)).longValue();
		} catch (Exception e) {
			ClassTransformer.logger.info(e);
		}
		world.provider.setWorldTime(time);
	}

	public static void noopSetWorldTimeCall(long time) {
		// noop
	}

}
