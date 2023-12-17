package toufoumaster.btwaila.tooltips.block;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import toufoumaster.btwaila.*;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

public class FlagTooltip implements IBTWailaCustomBlockTooltip {

    @Override
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup("minecraft", TileEntityFlag.class, this);
        tooltipGroup.addTooltip(TileEntityFlag.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        I18n stringTranslate = I18n.getInstance();
        TileEntityFlag flag = (TileEntityFlag) tileEntity;
        ItemStack color1 = flag.items[0];
        ItemStack color2 = flag.items[1];
        ItemStack color3 = flag.items[2];
        renderStringAndStack(guiBlockOverlay,"Color 1: "+ ((color1 != null) ? stringTranslate.translateKey(color1.getItemTranslateKey()) : "No Item"), 0, color1);
        renderStringAndStack(guiBlockOverlay,"Color 2: "+ ((color2 != null) ? stringTranslate.translateKey(color2.getItemTranslateKey()) : "No Item"), 0, color2);
        renderStringAndStack(guiBlockOverlay,"Color 3: "+ ((color3 != null) ? stringTranslate.translateKey(color3.getItemTranslateKey()) : "No Item"), 0, color3);
    }
    private void renderStringAndStack(GuiBlockOverlay guiBlockOverlay, String s, int offX, ItemStack stack){
        if (stack != null){
            int y = guiBlockOverlay.getOffY() - 1;
            int x = guiBlockOverlay.getPosX() + guiBlockOverlay.getGame().fontRenderer.getStringWidth(s) + 33;
            y -= 3;
            guiBlockOverlay.itemRender.renderItemIntoGUI(guiBlockOverlay.getGame().fontRenderer, guiBlockOverlay.getGame().renderEngine, stack, x, y, 1.0F);
            guiBlockOverlay.itemRender.renderItemOverlayIntoGUI(guiBlockOverlay.getGame().fontRenderer, guiBlockOverlay.getGame().renderEngine, stack, x, y, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        guiBlockOverlay.drawStringWithShadow(s, offX);
        guiBlockOverlay.addOffY(4);
    }
}
