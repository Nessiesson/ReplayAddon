package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
	private static ScriptEngine engine = new ScriptEngineManager(null).getEngineByName("nashorn");
	private static Bindings bindings = engine.createBindings();

	protected MixinWorldClient(ISaveHandler ish, WorldInfo wi, WorldProvider wp, Profiler p, boolean c) {
		super(ish, wi, wp, p, c);
	}

	/**
	 * @author nessie
	 * @reason simplest way to achieve what we need.
	 */
	@Overwrite
	public void setWorldTime(long time) {
		if (time < 0L) {
			time = -time;
		}

		final long worldTime = this.getTotalWorldTime();
		bindings.put("t", worldTime);
		bindings.put("x", worldTime);

		try {
			time = ((Number) engine.eval(Configuration.timeFunction, bindings)).longValue();
		} catch (Exception ignored) {
			// noop
		}

		super.setWorldTime(time);
	}
}
