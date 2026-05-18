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
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.util.phys.HitResult;

import toufoumaster.btwaila.BTWailaOptions;
import toufoumaster.btwaila.demo.DemoManager;

import static toufoumaster.btwaila.gui.components.AdvancedInfoComponent.entityIconMap;

public class DropIconComponent extends HudComponentMovable {
    public DropIconComponent(String key, Layout layout) {
        super(key, 18, 18, layout);
    }

    @Override
    public boolean isVisible() {
        return GameSettings.IMMERSIVE_MODE.drawHotbar();
    }

    @Override
    public void render(HudIngame HudIngame, int xScreenSize, int yScreenSize, float partialTick) {
		Minecraft minecraft = Minecraft.getMinecraft();
        HitResult hitResult = minecraft.objectMouseOver;
        if (hitResult == null) return;
        if (hitResult instanceof HitResult.Tile tileResult && BTWailaOptions.blockTooltips.value) {
            Block<?> block = minecraft.currentWorld.getBlockType(tileResult.tilePos);
            ItemStack[] drops;
            if (block != null) {
                drops = block.getBreakResult(minecraft.currentWorld, EnumDropCause.PICK_BLOCK, tileResult.tilePos, minecraft.currentWorld.getBlockData(tileResult.tilePos), null);
                ItemStack icon = block.getDefaultStack();
                if (drops != null && drops.length > 0){
                    icon = drops[0];
                }
                icon.stackSize = 1;
                renderItemDisplayer(icon, xScreenSize, yScreenSize);
            }
        } else if (hitResult instanceof HitResult.Entity entityResult && BTWailaOptions.entityTooltips.value) {
            ItemStack itemToRender = getEntityIcon(entityResult.entity);
            itemToRender.stackSize = 1;
            renderItemDisplayer(itemToRender, xScreenSize, yScreenSize);
        }
    }
    @Override
    public void renderPreview(Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        ItemStack icon = null;
        if (DemoManager.getCurrentEntry().block != null){
            icon = DemoManager.getCurrentEntry().drops[0];
        } else if (DemoManager.getCurrentEntry().entity != null) {
            icon = getEntityIcon(DemoManager.getCurrentEntry().entity);
        }
        if (icon != null){
            renderItemDisplayer(icon, xScreenSize, yScreenSize);
        }
    }
    protected void renderItemDisplayer(ItemStack blockResult, int xScreenSize, int yScreenSize){
        int x = getLayout().getComponentX(this, xScreenSize);
        int y = getLayout().getComponentY(this, yScreenSize);
        Lighting.enableInventoryLight();
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.enableState(State.DEPTH_TEST);

        TessellatorGeneral t = GLRenderer.getTessellator();
        ItemModel model = ItemModelDispatcher.getInstance().getDispatch(blockResult);
        model.renderGui(t, null, blockResult, x + (getDisplayedXSize() - 16)/2, y + (getDisplayedYSize() - 16)/2, LightIndexHelper.lightIndex2i(15,15),
	    1.0F);
        model.renderItemOverlayIntoGUI(t, Minecraft.getMinecraft().font, Minecraft.getMinecraft().textureManager, blockResult, x + (getDisplayedXSize() - 16)/2, y + (getDisplayedYSize() - 16)/2, null,1f);
        GLRenderer.disableState(State.DEPTH_TEST);
        Lighting.disable();
    }
    public ItemStack getEntityIcon(Entity entity){
        ItemStack icon = entityIconMap.get(entity.getClass());
        if (icon == null && entity instanceof Mob){
            Mob living = (Mob)entity;
            if (!living.mobDrops.isEmpty()){
                WeightedRandomLootObject lootObject = living.mobDrops.get(0);
                icon = lootObject.getItemStack();
            }

        }
        if (icon != null){
            return icon;
        } else {
            return Items.EGG_CHICKEN.getDefaultStack();
        }
    }
}
