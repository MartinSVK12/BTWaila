package toufoumaster.btwaila.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.hud.Layout;
import net.minecraft.client.gui.hud.MovableHudComponent;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import toufoumaster.btwaila.demo.DemoManager;
import toufoumaster.btwaila.mixin.mixins.accessors.EntityLivingAccessor;

import static toufoumaster.btwaila.gui.components.AdvancedInfoComponent.entityIconMap;
import static toufoumaster.btwaila.gui.components.AdvancedInfoComponent.itemRender;

public class DropIconComponent extends MovableHudComponent {
    public DropIconComponent(String key, Layout layout) {
        super(key, 18, 18, layout);
    }

    @Override
    public boolean isVisible(Minecraft minecraft) {
        return minecraft.gameSettings.immersiveMode.drawHotbar();
    }

    @Override
    public void render(Minecraft minecraft, GuiIngame guiIngame, int xScreenSize, int yScreenSize, float partialTick) {
        HitResult hitResult = minecraft.objectMouseOver;
        if (hitResult == null) {return;}
        if (hitResult.hitType == HitResult.HitType.TILE) {
            Block block = Block.getBlock(minecraft.theWorld.getBlockId(hitResult.x, hitResult.y, hitResult.z));
            ItemStack[] drops = block.getBreakResult(minecraft.theWorld, EnumDropCause.PICK_BLOCK, hitResult.x, hitResult.y, hitResult.z, minecraft.theWorld.getBlockMetadata(hitResult.x, hitResult.y, hitResult.z), null);
            ItemStack icon = block.getDefaultStack();
            if (drops != null && drops.length > 0){
                icon = drops[0];
            }
            renderItemDisplayer(minecraft,icon, xScreenSize, yScreenSize);
        } else if (hitResult.hitType == HitResult.HitType.ENTITY) {
            ItemStack itemToRender = getEntityIcon(hitResult.entity);
            renderItemDisplayer(minecraft, itemToRender, xScreenSize, yScreenSize);
        }
    }
    @Override
    public void renderPreview(Minecraft minecraft, Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        ItemStack icon = null;
        if (DemoManager.getCurrentEntry().block != null){
            icon = DemoManager.getCurrentEntry().drops[0];
        } else if (DemoManager.getCurrentEntry().entity != null) {
            icon = getEntityIcon(DemoManager.getCurrentEntry().entity);
        }
        if (icon != null){
            renderItemDisplayer(minecraft,icon, xScreenSize, yScreenSize);
        }
    }
    protected void renderItemDisplayer(Minecraft minecraft, ItemStack blockResult, int xScreenSize, int yScreenSize){
        int x = getLayout().getComponentX(minecraft, this, xScreenSize);
        int y = getLayout().getComponentY(minecraft, this, yScreenSize);
        Lighting.enableInventoryLight();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        itemRender.renderItemIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, blockResult, x + (getXSize(minecraft) - 16)/2, y + (getYSize(minecraft) - 16)/2, 1f, 1.0F);
        itemRender.renderItemOverlayIntoGUI(minecraft.fontRenderer, minecraft.renderEngine, blockResult, x + (getXSize(minecraft) - 16)/2, y + (getYSize(minecraft) - 16)/2, 1f);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
        Lighting.disable();
    }
    public ItemStack getEntityIcon(Entity entity){
        ItemStack icon = entityIconMap.get(entity.getClass());
        if (icon == null && entity instanceof EntityLiving){
            Item dropItem = Item.itemsList[((EntityLivingAccessor)entity).callGetDropItemId()];
            if (dropItem != null){
                icon = dropItem.getDefaultStack();
            }
        }
        if (icon != null){
            return icon;
        } else {
            return Item.eggChicken.getDefaultStack();
        }
    }
}
