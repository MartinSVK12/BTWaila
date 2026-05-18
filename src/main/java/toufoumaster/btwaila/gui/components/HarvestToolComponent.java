package toufoumaster.btwaila.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.HudComponentMovable;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.phys.HitResult;

import toufoumaster.btwaila.demo.DemoManager;

public class HarvestToolComponent extends HudComponentMovable {
    public HarvestToolComponent(String key, Layout layout) {
        super(key, 18, 18, layout);
    }

    protected void renderTool(Block<?> block, int xScreenSize, int yScreenSize){
		Minecraft minecraft = Minecraft.getMinecraft();
        int x = getLayout().getComponentX( this, xScreenSize);
        int y = getLayout().getComponentY(this, yScreenSize);
        Item itemHarvestTool = null;
        if (Items.TOOL_PICKAXE_STEEL.canHarvestBlock(minecraft.thePlayer, new ItemStack(block), block)) {
            itemHarvestTool = Items.TOOL_PICKAXE_STEEL;
        } else if (Items.TOOL_SHEARS_STEEL.canHarvestBlock(minecraft.thePlayer,new ItemStack(block), block)) {
            itemHarvestTool = Items.TOOL_SHEARS_STEEL;
        } else if (Items.TOOL_AXE_STEEL.canHarvestBlock(minecraft.thePlayer,new ItemStack(block), block)) {
            itemHarvestTool = Items.TOOL_AXE_STEEL;
        } else if (Items.TOOL_SWORD_STEEL.canHarvestBlock(minecraft.thePlayer,new ItemStack(block),block)) {
            itemHarvestTool = Items.TOOL_SWORD_STEEL;
        } else if (Items.TOOL_SHOVEL_STEEL.canHarvestBlock(minecraft.thePlayer,new ItemStack(block),block)) {
            itemHarvestTool = Items.TOOL_SHOVEL_STEEL;
        } else if (Items.TOOL_HOE_STEEL.canHarvestBlock(minecraft.thePlayer,new ItemStack(block),block)) {
            itemHarvestTool = Items.TOOL_HOE_STEEL;
        }
        if (itemHarvestTool == null) return;

        GLRenderer.enableState(State.DEPTH_TEST);

		TessellatorGeneral t = GLRenderer.getTessellator();
        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(itemHarvestTool);
        model.renderGui(t, null, itemHarvestTool.getDefaultStack(), x + (getDisplayedXSize() - 16)/2, y + (getDisplayedYSize() - 16)/2, LightIndexHelper.lightIndex2i(15,15),1.0F);
        GLRenderer.disableState(State.DEPTH_TEST);
		Lighting.disable();
    }

	@Override
	public boolean isVisible() {
		return GameSettings.IMMERSIVE_MODE.drawHotbar() && Minecraft.getMinecraft().thePlayer.gamemode == Gamemodes.SURVIVAL;
	}

	@Override
	public void render(HudIngame hudIngame, int xScreenSize, int yScreenSize, float v) {
		if (Minecraft.getMinecraft().objectMouseOver == null) return;
		if (!(Minecraft.getMinecraft().objectMouseOver instanceof HitResult.Tile tile)){
			return;
		} else {
			Block<?> block = Minecraft.getMinecraft().currentWorld.getBlockType(tile.tilePos);
			renderTool(block, xScreenSize, yScreenSize);
		}
	}

	@Override
	public void renderPreview(Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
		Block block = DemoManager.getCurrentEntry().block;
		if (block != null){
			renderTool(block, xScreenSize, yScreenSize);
		}
	}
}
