package nessiesson.replayaddon.asm;

import net.minecraftforge.classloading.FMLForgePlugin;

import java.util.NoSuchElementException;

public class MCPNames {
	private static final String[][] names = {
			// @formatter:off
			{"func_72867_j",  "getRainStrength"},
			{"func_72877_b" , "setWorldTime"},
			{"func_147285_a", "handleTimeUpdate"},
			{"func_145835_a", "getDistanceSq"},
			{"func_178635_a", "shouldRender"},
			{"func_180446_a", "renderEntities"},
			{"func_180546_a", "render"},
			{"func_188205_a", "renderBeamSegment"},
			// @formatter:on
	};

	public static boolean mcp() {
		return !FMLForgePlugin.RUNTIME_DEOBF;
	}

	public static String method(String srg) {
		for (String[] elem : names) {
			if (elem[0].equals(srg)) {
				return elem[mcp() ? 1 : 0];
			}
		}
		throw new NoSuchElementException("If you see this message, tell nessie this: " + srg);
	}
}
