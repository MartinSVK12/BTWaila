package toufoumaster.btwaila.mixin.mixins;

import net.minecraft.client.option.*;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GameSettings.class, remap = false)
public class GameSettingsMixin {
    /*@Inject(method = "getDisplayString(Lnet/minecraft/client/option/Option;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void displayStrings(Option<?> option, CallbackInfoReturnable<String> cir){
        I18n translator = I18n.getInstance();
        if (option == smallEntityHealthBar) {
            cir.setReturnValue(translator.translateKey("options.rowAmount").replace("{x}", String.valueOf(smallEntityHealthBar.value)));
        }
        if (option == backgroundStyle) {
            cir.setReturnValue(translator.translateKey(backgroundStyle.value.getTranslationKey()));
        }
        if (option == this.backgroundOpacity) {
            float value = this.backgroundOpacity.value;
            int percent = (int) (value * 100.0F);
            cir.setReturnValue(percent + "%");
        }
    }*/
}
