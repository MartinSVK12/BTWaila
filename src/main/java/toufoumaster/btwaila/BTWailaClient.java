package toufoumaster.btwaila;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.options.ScreenOptions;
import net.minecraft.client.gui.options.components.*;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Option;
import net.minecraft.core.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toufoumaster.btwaila.entryplugins.waila.BTWailaCustomTooltipPlugin;
import toufoumaster.btwaila.entryplugins.waila.BTWailaPlugin;
import toufoumaster.btwaila.gui.components.WailaTextComponent;
import toufoumaster.btwaila.tooltips.TooltipRegistry;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import turniplabs.halplibe.util.OptionsInitEntrypoint;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BTWailaClient implements ClientModInitializer, ClientStartEntrypoint, OptionsInitEntrypoint {
    public static OptionsPage wailaOptions;
    public static Map<String, String > modIds = new HashMap<>();

    public static final String MOD_ID = "btwaila|client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void onLoad(){
		wailaOptions = new OptionsPage("btwaila.options.title", Items.BASKET.getDefaultStack())
			.withComponent(new OptionsCategory("btwaila.options.category.general")
				.withComponent(new ToggleableOptionComponent<>(BTWailaOptions.tooltipFormatting))
				.withComponent(new ToggleableOptionComponent<>(BTWailaOptions.backgroundStyle))
				.withComponent(new FloatOptionComponent(BTWailaOptions.backgroundOpacity)))
			.withComponent(new OptionsCategory("btwaila.options.category.block")
				.withComponent(new BooleanOptionComponent(BTWailaOptions.blockTooltips))
				.withComponent(new BooleanOptionComponent(BTWailaOptions.blockAdvancedTooltips))
				.withComponent(new BooleanOptionComponent(BTWailaOptions.showBlockId))
				.withComponent(new BooleanOptionComponent(BTWailaOptions.showBlockDescriptions))
				.withComponent(new BooleanOptionComponent(BTWailaOptions.showHarvestText))
				.withComponent(new ToggleableOptionComponent<>(BTWailaOptions.barStyle)))
			.withComponent(new OptionsCategory("btwaila.options.category.entity")
				.withComponent(new BooleanOptionComponent(BTWailaOptions.entityTooltips))
				.withComponent(new BooleanOptionComponent(BTWailaOptions.entityAdvancedTooltips))
				.withComponent(new ToggleableOptionComponent<>(BTWailaOptions.heartRows)))
			.withComponent(new OptionsCategory("btwaila.options.category.keybinds")
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyOpenBTWailaMenu))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyDemoCycle))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyToggleBlockTooltips))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyToggleEntityTooltips)));

        OptionsPages.register(wailaOptions);
        for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
            modIds.put(container.getMetadata().getId(), container.getMetadata().getName());
        }

		OptionsPages.CONTROLS.withComponent(
			new OptionsCategory("btwaila.options.category.keybinds.explicit")
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyOpenBTWailaMenu))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyDemoCycle))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyToggleBlockTooltips))
				.withComponent(new KeyBindingComponent(BTWailaOptions.keyToggleEntityTooltips)));

    }
    public static Screen getOptionsPage(Screen parent){
        return new ScreenOptions(parent, wailaOptions);
    }

    @Override
    public void onInitializeClient() {

    }

    @Override
    public void beforeClientStart() {

    }

    @Override
    public void afterClientStart() {
        onLoad();
        WailaTextComponent.init();
        LOGGER.info("Loading implementations.");
        new BTWailaPlugin().initializePlugin(TooltipRegistry.getInstance(), LOGGER); // Load BTWaila tooltips first
        FabricLoader.getInstance().getEntrypointContainers("btwaila", BTWailaCustomTooltipPlugin.class).forEach(plugin -> plugin.getEntrypoint().initializePlugin(TooltipRegistry.getInstance(), LOGGER));
    }

	@Override
	public void initOptions() {
		for (Field field : BTWailaOptions.class.getDeclaredFields()) {
			try {
				Object o = field.get(null);
				if(o instanceof KeyBinding key){
					GameSettings.register(key);
				} else {
					GameSettings.register((Option<?>) o);
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
