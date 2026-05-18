package toufoumaster.btwaila.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScreenHudEditor;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.phys.HitResult;
import toufoumaster.btwaila.BTWailaOptions;
import toufoumaster.btwaila.demo.DemoManager;
import toufoumaster.btwaila.mixin.mixins.accessors.IPlayerControllerAccessor;
import toufoumaster.btwaila.util.Colors;

import static toufoumaster.btwaila.BTWaila.translator;

public class HarvestInfoComponent extends WailaTextComponent {
    public HarvestInfoComponent(String key, Layout layout) {
        super(key, 8, layout);
    }

    @Override
    public int getTrueAnchorY(ComponentAnchor anchor) {
        return (int)(anchor.yPosition * getBaseYSize());
    }

	@Override
	public void render(HudIngame hud, int xSizeScreen, int ySizeScreen, float partialTick) {

	}

	@Override
	public void renderPreview(Gui gui, Layout layout, int xSizeScreen, int ySizeScreen) {

	}

	@Override
    public int getBaseYSize() {
        if (!(mc.currentScreen instanceof ScreenHudEditor) && !this.isVisible()) {
            return 0;
        }
        return height();
    }
    @Override
    public boolean isVisible() {
        return BTWailaOptions.showHarvestText.value && GameSettings.IMMERSIVE_MODE.drawHotbar() && minecraft.objectMouseOver != null && minecraft.objectMouseOver instanceof HitResult.Tile && minecraft.thePlayer != null && minecraft.thePlayer.gamemode == Gamemodes.SURVIVAL;
    }

    @Override
    public void renderPost(Minecraft minecraft, HudIngame HudIngame, int xScreenSize, int yScreenSize, float f) {
        Player player = minecraft.thePlayer;
        HitResult hitResult = minecraft.objectMouseOver;
        if (player == null || !(hitResult instanceof HitResult.Tile tile)) {
            renderHarvestInfo(Colors.RED, "You shouldn't ever see this message.");
            return;
        }
        Block<?> block = minecraft.currentWorld.getBlockType(tile.tilePos);
        if (player.getGamemode() == Gamemodes.SURVIVAL) {
            int miningLevelColor = Colors.LIGHT_GREEN;
            String harvestString = translator.translateKey("btwaila.component.harvest.info.harvestable");
            if (!player.canHarvestBlock(block)) {
                harvestString = translator.translateKey("btwaila.component.harvest.info.notharvestable");
                miningLevelColor = Colors.LIGHT_RED;
            }
            float damage = ((IPlayerControllerAccessor)minecraft.playerController).getDestroyProgress();
            if (damage != 0) {
                harvestString = translator.translateKey("btwaila.component.harvest.info.harvesting").replace("{progress}", String.valueOf((int)(damage*100)));
            }
            renderHarvestInfo(miningLevelColor, harvestString);
        }
    }

    @Override
    public void renderPreviewPost(Minecraft minecraft, Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        if (BTWailaOptions.showHarvestText.value && DemoManager.getCurrentEntry().block != null){
            renderHarvestInfo(Colors.RED, translator.translateKey("btwaila.component.harvest.info.notharvestable"));
        }
    }
    protected void renderHarvestInfo(int miningLevelColor, String harvestString){
        drawStringJustified(harvestString, 0, getDisplayedXSize(), miningLevelColor);
    }
}
