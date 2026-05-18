package toufoumaster.btwaila;

import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.input.InputDevice;
import net.minecraft.client.option.*;
import toufoumaster.btwaila.util.BarStyle;
import toufoumaster.btwaila.util.TooltipFormatting;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Keyboard.KEY_F9;

public class BTWailaOptions {
	public static final KeyBinding keyOpenBTWailaMenu = new KeyBinding("btwaila.key.menu").bind(InputDevice.keyboard, KEY_NUMPAD0).setDefault(InputDevice.keyboard, KEY_NUMPAD0);
	public static final KeyBinding keyToggleBlockTooltips = new KeyBinding("btwaila.key.toggleBlockTooltips").bind(InputDevice.keyboard, KEY_BACKSLASH).setDefault(InputDevice.keyboard, KEY_BACKSLASH);
	public static final KeyBinding keyToggleEntityTooltips = new KeyBinding("btwaila.key.toggleEntityTooltips").bind(InputDevice.keyboard, KEY_BACKSLASH).setDefault(InputDevice.keyboard, KEY_BACKSLASH);
	public static final KeyBinding keyDemoCycle = new KeyBinding("btwaila.key.democycle").bind(InputDevice.keyboard, KEY_F9).setDefault(InputDevice.keyboard, KEY_F9);
	public static final OptionBoolean blockTooltips = new OptionBoolean("blockTooltips", true);
	public static final OptionBoolean blockAdvancedTooltips = new OptionBoolean("blockAdvancedTooltips", true);
	public static final OptionBoolean entityTooltips = new OptionBoolean("entityTooltips", true);
	public static final OptionBoolean entityAdvancedTooltips = new OptionBoolean("entityAdvancedTooltips", true);
	public static final OptionRange heartRows = new OptionRange("smallHealthBar", 0, 6);
	public static final OptionBoolean showBlockId = new OptionBoolean("showBlockId", false);
	public static final OptionBoolean showBlockDescriptions = new OptionBoolean("showBlockDesc", true);
	public static final OptionBoolean showHarvestText = new OptionBoolean("showHarvestText", true);
	public static final OptionEnum<TooltipFormatting> tooltipFormatting = new OptionEnum<>("tooltipFormatting",TooltipFormatting.class,TooltipFormatting.LEFT);
	public static final OptionEnum<BarStyle> barStyle = new OptionEnum<>("barStyle", BarStyle.class, BarStyle.PLAIN);
	public static final OptionFloat scaleTooltips = new OptionFloat("scaleTooltips", 0.5f);
	public static final OptionTooltip backgroundStyle = new OptionTooltip("backgroundStyle", "default")
		.withDisplayStringProvider((mc, i18n, option) -> {
			TooltipElement.Style style = TooltipElement.styleMap.get(option.value);
			if (style != null) {
				return i18n.translateKey(style.translationKey);
			}
			return "Unknown";
		});
	public static final OptionFloat backgroundOpacity = new OptionFloat("backgroundOpacity", 1.0F);

}
