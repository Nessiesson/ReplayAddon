package nessiesson.replayaddon;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

public class LoadingPlugin implements IFMLLoadingPlugin {
	public LoadingPlugin() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.replayaddon.json");
	}

	// @formatter:off
	@Override public String getAccessTransformerClass() { return null; }
	@Override public String[] getASMTransformerClass() { return null; }
	@Nullable @Override public String getSetupClass() { return null; }
	@Override public String getModContainerClass() { return null; }
	@Override public void injectData(Map<String, Object> data) {}
	// @formatter:on
}
