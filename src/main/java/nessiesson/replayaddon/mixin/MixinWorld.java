package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
	@Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
	private void onGetRainStrength(float delta, CallbackInfoReturnable<Float> cir) {
		if (Configuration.noRain) {
			cir.setReturnValue(0F);
		}
	}
}
