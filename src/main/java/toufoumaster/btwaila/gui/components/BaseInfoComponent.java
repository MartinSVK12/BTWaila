package toufoumaster.btwaila.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScreenHudEditor;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.HudComponent;
import net.minecraft.client.gui.hud.component.HudComponents;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.CompareFunc;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import toufoumaster.btwaila.BTWailaClient;
import toufoumaster.btwaila.BTWailaOptions;
import toufoumaster.btwaila.demo.DemoManager;
import toufoumaster.btwaila.util.Colors;

import static net.minecraft.client.gui.TooltipElement.styleMap;
import static toufoumaster.btwaila.BTWaila.translator;

public class BaseInfoComponent extends WailaTextComponent {
    private boolean drawing = false;
    private final int backgroundHPadding = 4;
    private final int backgroundVPadding = 4;
    private final int topPadding = backgroundVPadding;
	protected final static TooltipElement.Style defaultStyle = new TooltipElement.Style("default", "default", 3, 3, 3, 3);
    public BaseInfoComponent(String key, Layout layout) {
        super(key, 24, layout);
    }

	@Override
	public int getTrueAnchorY(@NotNull ComponentAnchor anchor) {
		if (anchor.yPosition == 0.0f && !(anchor == ComponentAnchor.TOP_CENTER)){
			return (int)(anchor.yPosition * getBaseYSize()) + topPadding;
		}
		return (int)(anchor.yPosition * getBaseYSize());
	}

	@Override
    public int getBaseYSize() {
        if (!(mc.currentScreen instanceof ScreenHudEditor) && !this.isVisible()) {
            return 0;
        }
        return height();
    }

    public void drawBoxyTexture(int minX, int minY, double width, double height, float opacity) {
        GLRenderer.setColor4f(1, 1, 1 , opacity);
        TessellatorGeneral tl = GLRenderer.getTessellator();
        int maxX = (int) (minX+width);
        int maxY = (int) (minY+height);
        int bottomLeftCornerY = maxY - 7;
        int topRightCornerX = maxX - 7;
        int bottomRightCornerX = maxX - 7;
        int bottomRightCornerY = maxY - 7;
        int horWidth = maxX - minX - 14;
        int vertHeight = maxY - minY - 14;
        int horWidth2 = maxX - minX - 6;
        int vertHeight2 = maxY - minY - 6;
        tl.startDrawingQuads();
        tl.drawRectangleWithUV(minX, minY, 7, 7, 0.0F, 0.0F, 0.21875F, 0.21875F);
        tl.drawRectangleWithUV(minX, bottomLeftCornerY, 7, 7, 0.0F, 0.21875F, 0.21875F, 0.21875F);
        tl.drawRectangleWithUV(topRightCornerX, minY, 7, 7, 0.21875F, 0.0F, 0.21875F, 0.21875F);
        tl.drawRectangleWithUV(bottomRightCornerX, bottomRightCornerY, 7, 7, 0.21875F, 0.21875F, 0.21875F, 0.21875F);

        for(int x = minX + 7; x < minX + 7 + horWidth / 11 * 11; x += 11) {
            tl.drawRectangleWithUV(x, minY, 11, 3, 0.4375F, 0.0F, 0.34375F, 0.09375F);
        }

        int finalWidth = horWidth - horWidth / 11 * 11;
        tl.drawRectangleWithUV(topRightCornerX - finalWidth, minY, finalWidth, 3, 0.4375F, 0.0F, ((float)finalWidth / 32.0F), 0.09375F);

        for(int x = minX + 7; x < minX + 7 + horWidth / 11 * 11; x += 11) {
            tl.drawRectangleWithUV(x, maxY - 3, 11, 3, 0.4375F, 0.34375F, 0.34375F, 0.09375F);
        }

        tl.drawRectangleWithUV(bottomRightCornerX - finalWidth, maxY - 3, finalWidth, 3, 0.4375F, 0.34375F, ((float)finalWidth / 32.0F), 0.09375F);

        for(int y = minY + 7; y < minY + 7 + vertHeight / 11 * 11; y += 11) {
            tl.drawRectangleWithUV(minX, y, 3, 11, 0.0F, 0.4375F, 0.09375F, 0.34375F);
        }

        int finalHeight = vertHeight - vertHeight / 11 * 11;
        tl.drawRectangleWithUV(minX, bottomLeftCornerY - finalHeight, 3, finalHeight, 0.0F, 0.4375F, 0.09375F, ((float)finalHeight / 32.0F));

        for(int y = minY + 7; y < minY + 7 + vertHeight / 11 * 11; y += 11) {
            tl.drawRectangleWithUV(maxX - 3, y, 3, 11, 0.34375F, 0.4375F, 0.09375F, 0.34375F);
        }

        tl.drawRectangleWithUV(maxX - 3, bottomRightCornerY - finalHeight, 3, finalHeight, 0.34375F, 0.4375F, 0.09375F, ((float)finalHeight / 32.0F));

        for(int x = minX + 3; x < minX + 3 + horWidth2 / 8 * 8; x += 8) {
            for(int y = minY + 3; y < minY + 3 + vertHeight2 / 8 * 8; y += 8) {
                tl.drawRectangleWithUV(x, y, 8, 8, 0.4375F, 0.4375F, 0.25F, 0.25F);
            }
        }

        int finalHeight2 = vertHeight2 - vertHeight2 / 8 * 8;
        int finalWidth2 = horWidth2 - horWidth2 / 8 * 8;

        for(int x = minX + 3; x < minX + 3 + horWidth2 / 8 * 8; x += 8) {
            tl.drawRectangleWithUV(x, maxY - 3 - finalHeight2, 8, finalHeight2, 0.4375F, 0.4375F, 0.25F, ((float)finalHeight2 / 32.0F));
        }

        for(int y = minY + 3; y < minY + 3 + vertHeight2 / 8 * 8; y += 8) {
            tl.drawRectangleWithUV(maxX - 3 - finalWidth2, y, finalWidth2, 8, 0.4375F, 0.4375F, ((float)finalWidth2 / 32.0F), 0.25F);
        }

        tl.drawRectangleWithUV(maxX - 3 - finalWidth2, maxY - 3 - finalHeight2, finalWidth2, finalHeight2, 0.4375F, 0.4375F, ((float)finalWidth2 / 32.0F), ((float)finalHeight2 / 32.0F));
        tl.draw();
        GLRenderer.setColor4f(1, 1, 1 , 1);
    }

    public void renderBackground(int xScreenSize, int yScreenSize) {
        if (!isVisible() || !drawing) return;
        int minX = getLayout().getComponentX(this, xScreenSize);
        int minY = getLayout().getComponentY(this, yScreenSize);
        int maxX = minX + getBaseXSize();
        int maxY = minY + getBaseYSize();
        for (HudComponent component : HudComponents.INSTANCE.getComponents()) {
            if (component == this) continue;
            try {
                if (!component.isVisible()) continue;
            } catch (NullPointerException e) {
                continue;
            }
            if (!WailaTextComponent.isComponentDeepAnchoredTo(component, this)) continue;

            int compMinX = component.getLayout().getComponentX(component, xScreenSize);
            if (compMinX < minX) minX = compMinX;

            int compMinY = component.getLayout().getComponentY(component, yScreenSize);
            if (compMinY < minY) minY = compMinY;

            int compMaxX = compMinX+component.getBaseXSize();
            if (compMaxX > maxX) maxX = compMaxX;

            int compMaxY = compMinY+component.getBaseYSize();
            if (compMaxY > maxY) maxY = compMaxY;
        }
		float opacity = BTWailaOptions.backgroundOpacity.value;
		drawBackground(minX,minY,maxX,maxY,opacity);
		//drawBackground(minX-backgroundHPadding, minY, maxX+backgroundHPadding*2-minX, maxY+backgroundVPadding-minY);

        /*GLRenderer.pushFrame();
        GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
        GLRenderer.enableState(State.BLEND);
        GLRenderer.setColor4f(1, 1, 1, 1);
        TextureManager textureManager = minecraft.textureManager;
        //String style = modSettings().bTWaila$getBarStyle().value.name();
        Texture tex = minecraft.textureManager.loadTexture((Options.backgroundStyle.value).getFilePath());//textureManager.loadTexture("minecraft:gui/tooltip/battle_ui.png");
        textureManager.bindTexture(tex);
        float opacity = Options.backgroundOpacity.value;
        drawBoxyTexture(, opacity);
        GLRenderer.disableState(State.BLEND);
        GLRenderer.popFrame();*/
        //drawRect(minX-backgroundHPadding, minY-backgroundVPadding, maxX+backgroundHPadding, maxY+backgroundVPadding, 0xff000000);
    }

	private void drawBackground(final int minX, final int minY, final int maxX, final int maxY, final float opacity) {
		final TooltipElement.Style style = styleMap.getOrDefault(BTWailaOptions.backgroundStyle.value, defaultStyle);
		if (style != null) {
			GLRenderer.pushFrame();
			GLRenderer.setDepthFunc(CompareFunc.ALWAYS);
			GLRenderer.enableState(State.BLEND);
			GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
			GLRenderer.setColor4f(1, 1, 1, opacity);
			activeGUI.drawGuiIcon(
				minX - 2,
				minY - 2,
				maxX - minX + 4,
				maxY - minY + 4,
				style.getInnerIcon());
			activeGUI.drawGuiIcon(
				minX - 2 - style.paddingLeft,
				minY - 2 - style.paddingTop,
				maxX - minX + 4 + style.paddingLeft + style.paddingRight,
				maxY - minY + 4 + style.paddingTop + style.paddingBottom,
				style.getBorderIcon());

			GLRenderer.disableState(State.BLEND);
			GLRenderer.popFrame();
		}
	}

    @Override
    public boolean isVisible() {
        return GameSettings.IMMERSIVE_MODE.drawHotbar();
    }

    @Override
    public void render(HudIngame HudIngame, int xScreenSize, int yScreenSize, float partialTick) {
        HitResult hitResult = minecraft.objectMouseOver;
        if (hitResult == null) return;
        if ((hitResult instanceof HitResult.Tile && BTWailaOptions.blockTooltips.value) ||
            (hitResult instanceof HitResult.Entity && BTWailaOptions.entityTooltips.value)) {
            renderBackground(xScreenSize, yScreenSize);
        }
		super.render(HudIngame, xScreenSize, yScreenSize, partialTick);
    }

    @Override
    public void renderPreview(Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        renderBackground(xScreenSize, yScreenSize);
        super.renderPreview(gui, layout, xScreenSize, yScreenSize);
    }

    @Override
    public void renderPost(Minecraft minecraft, HudIngame HudIngame, int xScreenSize, int yScreenSize, float f) {
        HitResult hitResult = minecraft.objectMouseOver;
        drawing = hitResult != null;
        if (hitResult == null) {return;}
        addOffY(topPadding);
        if (hitResult instanceof HitResult.Tile tileResult) {
            Block<?> block = minecraft.currentWorld.getBlockType(tileResult.tilePos);
            int meta = minecraft.currentWorld.getBlockData(tileResult.tilePos);
            ItemStack[] drops = null;
            if (block != null) {
                drops = block.getBreakResult(minecraft.currentWorld, EnumDropCause.PICK_BLOCK, tileResult.tilePos, minecraft.currentWorld.getBlockData(tileResult.tilePos), null);
            }
            baseBlockInfo(block, meta, drops);
        } else if (hitResult instanceof HitResult.Entity entityResult) {
            baseEntityInfo(entityResult.entity);
        }
    }

    @Override
    public void renderPreviewPost(Minecraft minecraft, Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        addOffY(topPadding);
        drawing = true;
        Block<?> block = DemoManager.getCurrentEntry().block;
        int meta = DemoManager.getCurrentEntry().meta;
        ItemStack[] drops = DemoManager.getCurrentEntry().drops;
        Entity entity = DemoManager.getCurrentEntry().entity;
        if (block != null){
            baseBlockInfo(block, meta, drops);
        } else if (entity != null) {
            baseEntityInfo(entity);
        }
    }
    protected void baseBlockInfo(Block<?> block, int blockMetadata, ItemStack[] blockDrops){
        if (!BTWailaOptions.blockTooltips.value) return;
        if (minecraft.font == null) return;

        ItemStack renderItem = new ItemStack(block, 1, blockMetadata);
        if (blockDrops != null && blockDrops.length > 0) renderItem = blockDrops[0];

        String languageKey = renderItem.getItemKey();

        String blockName = translator.translateKey(languageKey+".name");
        String blockDesc = translator.translateKey(languageKey+".desc");
        String blockSource = "Minecraft";
        for (String modId: BTWailaClient.modIds.keySet()){
            if (languageKey.contains(modId)){
                blockSource = BTWailaClient.modIds.get(modId);
            }
        }
        String idString = block.id() + ":" + blockMetadata;
        if (BTWailaOptions.showBlockId.value){
            blockName += " " + idString;
        }

        drawStringJustified(blockName,0,getDisplayedXSize(), Colors.WHITE);
        drawStringJustified(blockSource,0,getDisplayedXSize(), Colors.BLUE);
        if (BTWailaOptions.showBlockDescriptions.value){
            drawStringJustified(blockDesc,0,getDisplayedXSize(), Colors.LIGHT_GRAY);
        }
    }
    protected void baseEntityInfo(Entity entity){
        if (!BTWailaOptions.entityTooltips.value) return;
        boolean isLivingEntity = (entity instanceof Mob);
        Mob Mob = isLivingEntity ? (Mob) entity : null;

        int color = Colors.WHITE;
        if (isLivingEntity) {
            color = Colors.GREEN;
            if (entity instanceof MobMonster) {
                color = Colors.RED;
            }
            else if (entity instanceof Player) {
                color = Mob.chatColor;
            }
        }
        drawStringJustified(AdvancedInfoComponent.getEntityName(entity), 0, getDisplayedXSize(), color);
    }

}
