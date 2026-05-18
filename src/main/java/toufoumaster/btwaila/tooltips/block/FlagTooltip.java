package toufoumaster.btwaila.tooltips.block;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.LightIndexHelper;

import toufoumaster.btwaila.demo.DemoEntry;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.UUIDHelper;

import java.util.Random;

import static toufoumaster.btwaila.BTWaila.translator;

public class FlagTooltip extends TileTooltip<TileEntityFlag> {
    @Override
    public void initTooltip() {
        addClass(TileEntityFlag.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityFlag flag, AdvancedInfoComponent advancedInfoComponent) {
        ItemStack color1 = flag.items[0];
        ItemStack color2 = flag.items[1];
        ItemStack color3 = flag.items[2];
        advancedInfoComponent.drawStringWithShadow(translator.translateKey("btwaila.tooltip.flag.owner").replace("{name}", flag.owner == null ? translator.translateKey("btwaila.tooltip.flag.owner.none") : String.valueOf(UUIDHelper.getNameFromUUID(flag.owner))), 0);
        advancedInfoComponent.addOffY(2);
        renderStringAndStack(advancedInfoComponent,translator.translateKey("btwaila.tooltip.flag.color").replace("{id}", "1") + "    " +  ((color1 != null) ? color1.getItem().getTranslatedName(color1) : translator.translateKey("btwaila.tooltip.flag.empty")), 0, color1);
        renderStringAndStack(advancedInfoComponent,translator.translateKey("btwaila.tooltip.flag.color").replace("{id}", "2") + "    " +  ((color2 != null) ? color2.getItem().getTranslatedName(color2) : translator.translateKey("btwaila.tooltip.flag.empty")), 0, color2);
        renderStringAndStack(advancedInfoComponent,translator.translateKey("btwaila.tooltip.flag.color").replace("{id}", "3") + "    " +  ((color3 != null) ? color3.getItem().getTranslatedName(color3) : translator.translateKey("btwaila.tooltip.flag.empty")), 0, color3);
    }
    @SuppressWarnings("SameParameterValue")
    protected void renderStringAndStack(AdvancedInfoComponent advancedInfoComponent, String s, int offX, ItemStack stack){
        if (stack != null){
            int y = advancedInfoComponent.getOffY() - 1;
            int x = advancedInfoComponent.getPosX() - 16 + advancedInfoComponent.minecraft.font.stringWidth(translator.translateKey("btwaila.tooltip.flag.color").replace("{id}", "1") + "    ");
            y -= 3;
            TessellatorGeneral t = GLRenderer.getTessellator();
            ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
            model.renderGui(t, null, stack, x, y, LightIndexHelper.lightIndex2i(15,15), 1.0F);
            model.renderItemOverlayIntoGUI(t, advancedInfoComponent.getGame().font, advancedInfoComponent.getGame().textureManager, stack, x, y, null,1.0F);
			Lighting.disable();
        }
        advancedInfoComponent.drawStringWithShadow(s, offX);
        advancedInfoComponent.addOffY(4);
    }
    @Override
    public DemoEntry tooltipDemo(Random random){
        TileEntityFlag demoFlag = new TileEntityFlag();
        demoFlag.items = new ItemStack[]{new ItemStack(Items.DYE, 1, random.nextInt(16)), new ItemStack(Items.DYE, 1, random.nextInt(16)), new ItemStack(Items.DYE, 1, random.nextInt(16))};
        //demoFlag.owner = DemoManager.getRandomName(random);
        return new DemoEntry(Blocks.FLAG, 0, demoFlag, new ItemStack[]{Items.FLAG.getDefaultStack()});
    }
}
