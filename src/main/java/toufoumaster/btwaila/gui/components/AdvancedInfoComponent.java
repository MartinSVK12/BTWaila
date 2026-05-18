package toufoumaster.btwaila.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocalMultiplayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScreenHudEditor;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.Global;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import org.lwjgl.input.Keyboard;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.BTWailaOptions;
import toufoumaster.btwaila.demo.DemoManager;
import toufoumaster.btwaila.network.packet.PacketRequestEntityData;
import toufoumaster.btwaila.network.packet.PacketRequestTileEntityData;
import toufoumaster.btwaila.tooltips.EntityTooltip;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.tooltips.TooltipRegistry;

public class AdvancedInfoComponent extends WailaTextComponent {
    private static boolean keyPressed = false;
    public AdvancedInfoComponent(String key, Layout layout) {
        super(key,68, layout);
    }

    @Override
    public int getTrueAnchorY(ComponentAnchor anchor) {
        return (int)(anchor.yPosition * getBaseYSize());
    }

	@Override
	public void render(HudIngame hud, int xSizeScreen, int ySizeScreen, float partialTick) {
		super.render(hud, xSizeScreen, ySizeScreen, partialTick);
	}

	@Override
	public void renderPreview(Gui gui, Layout layout, int xSizeScreen, int ySizeScreen) {
		super.renderPreview(gui, layout, xSizeScreen, ySizeScreen);
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
        return GameSettings.IMMERSIVE_MODE.drawHotbar();
    }

    @Override
    public void renderPost(Minecraft minecraft, HudIngame hud, int xScreenSize, int yScreenSize, float partialTick) {
        HitResult hitResult = minecraft.objectMouseOver;
        if (hitResult == null) {return;}
        if (hitResult instanceof HitResult.Tile tileResult) {
            TileEntity tileEntity = minecraft.currentWorld.getTileEntity(tileResult.tilePos);
            renderBlockOverlay(tileEntity);
        } else if (hitResult instanceof HitResult.Entity entityResult) {
            renderEntityOverlay(hud, entityResult.entity);
        }
    }
    @Override
    public void renderPreviewPost(Minecraft minecraft, Gui gui, Layout layout, int xScreenSize, int yScreenSize) {
        if (BTWailaOptions.keyDemoCycle.isPressed()){
            if (!keyPressed){
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
                    DemoManager.demoOffset -= 1;
                } else {
                    DemoManager.demoOffset += 1;
                }
                keyPressed = true;
            }
        } else {
            keyPressed = false;
        }
        TileEntity demoTileEntity = DemoManager.getCurrentEntry().tileEntity;
        Entity demoAnimal = DemoManager.getCurrentEntry().entity;
        if (demoTileEntity != null){
            renderBlockOverlay(demoTileEntity);
        } else if (demoAnimal != null) {
            renderEntityOverlay(gui, demoAnimal);
        }
    }
    private void renderBlockOverlay(TileEntity tileEntity){
        setScale(BTWailaOptions.scaleTooltips.value+0.5f);
        if (!BTWailaOptions.blockTooltips.value) return;
		if (BTWailaOptions.blockAdvancedTooltips.value) {
			drawFunctionalBlocksData(tileEntity);
		}
	}
    private void renderEntityOverlay(Gui hud, Entity entity){
        setScale(BTWailaOptions.scaleTooltips.value+0.5f);
        if (!BTWailaOptions.entityTooltips.value) return;
        boolean isLivingEntity = (entity instanceof Mob);
        Mob Mob = isLivingEntity ? (Mob) entity : null;

        if (BTWailaOptions.entityAdvancedTooltips.value) {
            if (minecraft.thePlayer instanceof PlayerLocalMultiplayer playerMP && BTWaila.canUseAdvancedTooltips) {
				playerMP.sendQueue.addToSendQueue(new PacketRequestEntityData(entity.id));
            }

            if (isLivingEntity) drawEntityHealth(hud, Mob);
            for (EntityTooltip<?> tooltip : TooltipRegistry.entityTooltips) {
                if (tooltip.isInstance(entity) && tooltip.isInList(entity.getClass())) {
                    tooltip._drawAdvancedTooltip(entity, this);
                }
            }
        }
    }
    private void drawFunctionalBlocksData(TileEntity tileEntity) {
        if (tileEntity != null && tileEntity.worldObj != null) {
            boolean askTileEntity = !(BTWaila.excludeContinuousTileEntityData.get(tileEntity.getClass()) != null ? BTWaila.excludeContinuousTileEntityData.get(tileEntity.getClass()) : false);
            if (tileEntity.worldObj == null) return;
            if (!Global.isServer && BTWaila.canUseAdvancedTooltips && askTileEntity) {
                PlayerLocalMultiplayer playerMP = (PlayerLocalMultiplayer) minecraft.thePlayer;
                playerMP.sendQueue.addToSendQueue(new PacketRequestTileEntityData(tileEntity.tilePos.x, tileEntity.tilePos.y, tileEntity.tilePos.z));
                if (tileEntity instanceof TileEntityChest){
                    requestOtherHalfOfChest(playerMP.world,tileEntity.tilePos, playerMP);
                }
            }
            for (TileTooltip<?> tooltip : TooltipRegistry.tileTooltips) {
                if (tooltip.isInstance(tileEntity) && tooltip.isInList(tileEntity.getClass())) {
                    tooltip._drawAdvancedTooltip(tileEntity, this);
                }
            }
        }
    }
    private void requestOtherHalfOfChest(World world, TilePos tilePos, PlayerLocalMultiplayer playerMP) {
        int meta = world.getBlockData(tilePos);
        BlockLogicChest.Type type = BlockLogicChest.getTypeFromMeta(meta);
        if (type != BlockLogicChest.Type.SINGLE) {
            int otherMeta;
            Direction direction = BlockLogicChest.getDirectionFromMeta(meta);
            int otherChestX = tilePos.x;
            int otherChestZ = tilePos.z;
            if (direction == Direction.NORTH) {
                if (type == BlockLogicChest.Type.LEFT) {
                    --otherChestX;
                }
                if (type == BlockLogicChest.Type.RIGHT) {
                    ++otherChestX;
                }
            }
            if (direction == Direction.EAST) {
                if (type == BlockLogicChest.Type.LEFT) {
                    --otherChestZ;
                }
                if (type == BlockLogicChest.Type.RIGHT) {
                    ++otherChestZ;
                }
            }
            if (direction == Direction.SOUTH) {
                if (type == BlockLogicChest.Type.LEFT) {
                    ++otherChestX;
                }
                if (type == BlockLogicChest.Type.RIGHT) {
                    --otherChestX;
                }
            }
            if (direction == Direction.WEST) {
                if (type == BlockLogicChest.Type.LEFT) {
                    ++otherChestZ;
                }
                if (type == BlockLogicChest.Type.RIGHT) {
                    --otherChestZ;
                }
            }
            if (BlockLogicChest.isChest(world, new TilePos(otherChestX, tilePos.y, otherChestZ)) && BlockLogicChest.getDirectionFromMeta(otherMeta = world.getBlockData(new TilePos(otherChestX, tilePos.y, otherChestZ))) == direction) {
                BlockLogicChest.Type otherType = BlockLogicChest.getTypeFromMeta(otherMeta);
                if (type == BlockLogicChest.Type.LEFT && otherType == BlockLogicChest.Type.RIGHT || type == BlockLogicChest.Type.RIGHT && otherType == BlockLogicChest.Type.LEFT) {
                    playerMP.sendQueue.addToSendQueue(new PacketRequestTileEntityData(otherChestX, tilePos.y, otherChestZ));
                }
            }
        }
    }
}
