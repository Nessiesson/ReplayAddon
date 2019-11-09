package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.tileentity.TileEntityBeacon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityBeaconRenderer.class)
public abstract class MixinTileEntityBeaconRenderer {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void onRender(TileEntityBeacon te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo ci) {
		if (Configuration.renderBeaconBeam) {
			ci.cancel();
		}
	}
}
