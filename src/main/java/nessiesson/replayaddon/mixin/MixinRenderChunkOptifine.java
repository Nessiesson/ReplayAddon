package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.renderer.chunk.RenderChunk;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(RenderChunk.class)
public abstract class MixinRenderChunkOptifine {
	@Dynamic
	@Inject(method = "isPlayerUpdate", at = @At("HEAD"), cancellable = true, remap = false)
	private void stopDumbBlinking(CallbackInfoReturnable<Boolean> ci) {
		if (Configuration.smoothPistons) {
			ci.setReturnValue(true);
		}
	}
}
