package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityPiston.class)
public abstract class MixinTileEntityPiston extends TileEntity {
	@Unique
	private static final float REPLAYADDON_MATH_NEXT_DOWN_OF_ONE = Math.nextDown(1F);
	@Shadow
	private float progress;

	@Inject(method = "getProgress", at = @At("HEAD"), cancellable = true)
	public void getProgress(float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (!Configuration.smoothPistons) return;

		if (this.tileEntityInvalid && Math.abs(this.progress - 1F) < 1E-5F) {
			cir.setReturnValue(REPLAYADDON_MATH_NEXT_DOWN_OF_ONE);
		}

		cir.setReturnValue(Math.min(1F, (2F * this.progress + partialTicks) / 3F));
	}
}
