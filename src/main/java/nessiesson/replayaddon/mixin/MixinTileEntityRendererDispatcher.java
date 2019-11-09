package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class MixinTileEntityRendererDispatcher {
	@Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getDistanceSq(DDD)D"))
	private double onGetDistanceSq(TileEntity te, double x, double y, double z) {
		if (Configuration.alwaysRenderTileEntities) {
			return 0D;
		}

		if (Configuration.neverRenderTileEntities) {
			return Double.MAX_VALUE;
		}

		return te.getDistanceSq(x, y, z);
	}
}
