package toufoumaster.btwaila.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toufoumaster.btwaila.BTWailaClient;
import toufoumaster.btwaila.BTWailaOptions;

@Mixin(value = HudIngame.class, remap = false)
public class HudIngameMixin extends Gui {

    @Unique
    boolean blockTooltipKeyReleased = true;
    @Unique
    boolean entityTooltipKeyReleased = true;

    @Shadow protected Minecraft mc;
    @Inject( method = "updateTick", at = @At("TAIL"))
    public void updateTick(CallbackInfo ci) {
		if (BTWailaOptions.keyOpenBTWailaMenu.isPressed() && this.mc.currentScreen == null) {
			this.mc.displayScreen(BTWailaClient.getOptionsPage(null));
		}

		if (BTWailaOptions.keyToggleEntityTooltips.isPressed() && this.mc.currentScreen == null) {
			if (entityTooltipKeyReleased) {
				BTWailaOptions.entityTooltips.toggle();
			}
			entityTooltipKeyReleased = false;
		} else if (BTWailaOptions.keyToggleEntityTooltips.isReleaseEvent()) {
			entityTooltipKeyReleased = true;
		}

		if (BTWailaOptions.keyToggleBlockTooltips.isPressed() && this.mc.currentScreen == null) {
			if (blockTooltipKeyReleased) {
				BTWailaOptions.blockTooltips.toggle();
			}
			blockTooltipKeyReleased = false;
		} else if (BTWailaOptions.keyToggleBlockTooltips.isReleaseEvent()) {
			blockTooltipKeyReleased = true;
		}

    }
}
