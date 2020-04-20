package nessiesson.replayaddon;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MODID)
public class Configuration {
	@Config.Comment("Makes it so entities in loaded chunks are always rendered. This unfortunately requires server-side support to properly work.")
	public static boolean alwaysRenderEntities = true;
	@Config.Comment("Makes it so tile entities in loaded chunks are always rendered.")
	public static boolean alwaysRenderTileEntities = true;
	@Config.Comment("Specifies what custom javascript expression to use [requires `customTime=true`], you can use either `f`, `t`, or `x` as a variable for the world time.")
	public static String timeFunction = "t";
	@Config.Comment("Toggles whether to use custom time or default time.")
	public static boolean customTime = false;
	@Config.Comment("Specifies what custom javascript expression to use [requires `customFOV=true`], you can use either `f`, `t`, or `x` as a variable for the world time.")
	public static String fovFunction = "70";
	@Config.Comment("Toggles whether to use custom FOV or default (locked) FOV.")
	public static boolean customFOV = false;
	@Config.Comment("Toggles rendering of beacon beams.")
	public static boolean renderBeaconBeam = true;
	@Config.Comment("Some specialised thing designed for Robitobi01 and SARC.")
	public static boolean shouldSpamLog = false;
	@Config.Comment("Entirely stops rendering spectator players at all.")
	public static boolean shouldRenderSpectators = true;
	@Config.Comment("Toggles whether to show rain (including darkening of the sky) or not.")
	public static boolean noRain = true;
	@Config.Comment("Entirely stops rendering of tile entities with special renderers (like chests, banners, signs, etc).")
	public static boolean neverRenderTileEntities = false;
	@Config.Comment("Entirely stops rendering of entities.")
	public static boolean neverRenderEntities = false;
	@Config.Comment("Toggles smoother nonblinkier pistons (similar to the G4meSpeed mod).")
	public static boolean smoothPistons = true;
	@Config.Comment("Toggles pre-1.11 'smooth' item movement (matters for item elevators, etc.)'.")
	public static boolean smoothItemMovement = true;
}
