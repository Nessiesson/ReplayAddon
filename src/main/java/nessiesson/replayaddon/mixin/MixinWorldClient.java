package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import nessiesson.replayaddon.ReplayAddon;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.script.Bindings;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
	@Unique
	private static final Bindings bindings = ReplayAddon.engine.createBindings();

	protected MixinWorldClient(ISaveHandler ish, WorldInfo wi, WorldProvider wp, Profiler p, boolean c) {
		super(ish, wi, wp, p, c);
	}

	@Inject(method = "setWorldTime", at = @At(value = "HEAD"), cancellable = true)
	public void setWorldTime(long time, CallbackInfo ci) {

		if (time < 0L) {
			time = -time;
		}

		final long worldTime = this.getTotalWorldTime();
		bindings.put("f", worldTime);
		bindings.put("t", worldTime);
		bindings.put("x", worldTime);

		try {
			time = ((Number) ReplayAddon.engine.eval(Configuration.timeFunction, bindings)).longValue();
		} catch (Exception ignored) {
			// noop
		}

		super.setWorldTime(time);
	}
}
