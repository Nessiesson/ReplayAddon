package nessiesson.replayaddon;

import net.minecraftforge.common.config.Config;

@SuppressWarnings("unused")
@Config(modid = Reference.MODID)
public class Configuration {
	public static boolean alwaysRenderEntities = true;
	public static boolean alwaysRenderTileEntities = true;
	public static String timeFunction = "t";
	public static boolean renderBeaconBeam = true; // used in ClassTransformer
	public static boolean shouldSpamLog = false;
}
