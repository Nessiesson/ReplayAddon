package nessiesson.replayaddon;

import net.minecraftforge.common.config.Config;

@SuppressWarnings("unused")
@Config(modid = Reference.MODID)
public class Configuration {
	public static boolean alwaysRenderEntities = true;
	public static boolean alwaysRenderTileEntities = true;
	public static String timeFunction = "t";
	public static boolean renderBeaconBeam = true;
	public static boolean shouldSpamLog = false;
	public static boolean shouldRenderSpectators = true;
	public static boolean noRain = true;
	public static boolean neverRenderTileEntities = false;
	public static boolean neverRenderEntities = false;
	public static boolean smoothPistons = true;
	public static boolean hax = true;
}
