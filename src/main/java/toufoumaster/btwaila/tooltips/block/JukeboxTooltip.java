package toufoumaster.btwaila.tooltips.block;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityJukebox;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.LightIndexHelper;

import toufoumaster.btwaila.demo.DemoEntry;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;

import java.util.Random;

import static toufoumaster.btwaila.BTWaila.translator;

public class JukeboxTooltip extends TileTooltip<TileEntityJukebox> {
    @Override
    public void initTooltip() {
        addClass(TileEntityJukebox.class);
    }
    @Override
    public void drawAdvancedTooltip(TileEntityJukebox recordPlayer, AdvancedInfoComponent advancedInfoComponent) {
        String text = translator.translateKey("btwaila.tooltip.jukebox.disc").replace("{id}", String.valueOf(recordPlayer.record));
        int y = advancedInfoComponent.getOffY() + 1;
        advancedInfoComponent.setOffY(y);
        advancedInfoComponent.drawStringWithShadow(text, 0);
        if (Item.itemsList[recordPlayer.record] != null){
            ItemStack stack = new ItemStack(Item.itemsList[recordPlayer.record]);
            int x = advancedInfoComponent.getPosX() + advancedInfoComponent.getGame().font.stringWidth(text) + 2;
            y -= 4;
			TessellatorGeneral t = GLRenderer.getTessellator();
            ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
            model.renderGui(t, null, stack, x, y, LightIndexHelper.lightIndex2i(15,15),1.0F);
            model.renderItemOverlayIntoGUI(t, advancedInfoComponent.getGame().font, advancedInfoComponent.getGame().textureManager, stack, x, y, null, 1.0F);
			Lighting.disable();
        }
    }
    @Override
    public DemoEntry tooltipDemo(Random random){
        TileEntityJukebox demoJukeBox = new TileEntityJukebox();
        demoJukeBox.record = Items.RECORD_13.id + random.nextInt(11);
        Block jukeBox = Blocks.JUKEBOX;
        return new DemoEntry(jukeBox, 0, demoJukeBox, new ItemStack[]{jukeBox.getDefaultStack()});
    }
}
