package nessiesson.replayaddon.mixin;

import nessiesson.replayaddon.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityItem.class)
public abstract class MixinEntityItem extends Entity {
	public MixinEntityItem(World worldIn) {
		super(worldIn);
	}

	@Redirect(method = "onUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;isRemote:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
	private boolean clientPushOutOfBlocks(World world) {
		return !Configuration.smoothItemMovement && !Minecraft.getMinecraft().isSingleplayer();
	}
}
