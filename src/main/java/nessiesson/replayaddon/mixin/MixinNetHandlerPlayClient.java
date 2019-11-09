package nessiesson.replayaddon.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
	@Shadow
	private WorldClient world;

	@Redirect(method = "handleTimeUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;setWorldTime(J)V"))
	private void onSetWorldTime(WorldClient world, long time) {
		// noop
	}
}
