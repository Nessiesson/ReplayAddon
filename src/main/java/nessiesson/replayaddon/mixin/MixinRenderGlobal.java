package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
public abstract class MixinRenderGlobal {
	@Redirect(method = "renderEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;shouldRender(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;DDD)Z"))
	private boolean onShouldRender(RenderManager rm, Entity e, ICamera ic, double x, double y, double z) {
		if (Configuration.neverRenderEntities) {
			return false;
		}

		EntityPlayer player = null;
		if (e instanceof EntityPlayer) {
			player = (EntityPlayer) e;
		}

		if (!Configuration.shouldRenderSpectators && player != null) {
			return !player.isSpectator();
		}

		if (Configuration.alwaysRenderEntities && player == null) {
			return true;
		}

		return rm.shouldRender(e, ic, x, y, z);
	}
}
