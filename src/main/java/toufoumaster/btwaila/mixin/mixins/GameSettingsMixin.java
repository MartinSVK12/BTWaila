package toufoumaster.btwaila.mixin.mixins;

import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.EnumOption;
import net.minecraft.client.option.FloatOption;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import toufoumaster.btwaila.mixin.interfaces.IOptions;
import toufoumaster.btwaila.util.SmallHealthBarEnum;
import toufoumaster.btwaila.util.TooltipFormatting;

import static org.lwjgl.input.Keyboard.KEY_F9;
import static org.lwjgl.input.Keyboard.KEY_NUMPAD0;

@Mixin(value = GameSettings.class, remap = false)
public class GameSettingsMixin implements IOptions {
    @Unique
    private GameSettings thisAs = (GameSettings)(Object)this;
    @Unique
    public final KeyBinding keyOpenBTWailaMenu = new KeyBinding("btwaila.key.menu").bindKeyboard(KEY_NUMPAD0);
    @Unique
    public final KeyBinding keyDemoCycle = new KeyBinding("btwaila.key.democycle").bindKeyboard(KEY_F9);
    @Unique
    public final BooleanOption blockTooltips = new BooleanOption(thisAs, "blockTooltips", true);
    @Unique
    public final BooleanOption blockAdvancedTooltips = new BooleanOption(thisAs, "blockAdvancedTooltips", true);
    @Unique
    public final BooleanOption entityTooltips = new BooleanOption(thisAs, "entityTooltips", true);
    @Unique
    public final BooleanOption entityAdvancedTooltips = new BooleanOption(thisAs, "entityAdvancedTooltips", true);
    @Unique
    public final EnumOption<SmallHealthBarEnum> smallEntityHealthBar = new EnumOption<>(thisAs, "smallHealthBar", SmallHealthBarEnum.class, SmallHealthBarEnum.TWO);
    @Unique
    public final BooleanOption showBlockId = new BooleanOption(thisAs, "showBlockId", false);
    @Unique
    public final BooleanOption showBlockDescriptions = new BooleanOption(thisAs, "showBlockDesc", true);
    @Unique
    public final BooleanOption showHarvestText = new BooleanOption(thisAs, "showHarvestText", true);
    @Unique
    public final EnumOption<TooltipFormatting> tooltipFormatting = new EnumOption<>(thisAs, "tooltipFormatting",TooltipFormatting.class,TooltipFormatting.LEFT);
    @Unique
    public final FloatOption scaleTooltips = new FloatOption(thisAs, "scaleTooltips", 0.5f);

    public KeyBinding bTWaila$getKeyOpenBTWailaMenu() {
        return keyOpenBTWailaMenu;
    }
    public KeyBinding bTWaila$getKeyDemoCycle() {return keyDemoCycle;}
    public BooleanOption bTWaila$getBlockTooltips() {
        return blockTooltips;
    }
    public BooleanOption bTWaila$getBlockAdvancedTooltips() {
        return blockAdvancedTooltips;
    }
    public BooleanOption bTWaila$getEntityTooltips() {
        return entityTooltips;
    }
    public BooleanOption bTWaila$getEntityAdvancedTooltips() {
        return entityAdvancedTooltips;
    }
    public EnumOption<SmallHealthBarEnum> bTWaila$getSmallEntityHealthBar() {
        return smallEntityHealthBar;
    }
    public BooleanOption bTWaila$getShowBlockId() {return showBlockId;}
    public BooleanOption bTWaila$getShowBlockDesc() {return showBlockDescriptions;}
    public BooleanOption bTWaila$getShowHarvestText() {return showHarvestText;}
    public EnumOption<TooltipFormatting> bTWaila$getTooltipFormatting() {return tooltipFormatting;}
    public FloatOption bTWaila$getScaleTooltips() {return scaleTooltips;}
}
