package cn.ksmcbrigade.darktitlebar.mixin;

import cn.ksmcbrigade.darktitlebar.DarkTitleBar;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "createDisplay", at = @At("TAIL"))
    private void setTitleBar(CallbackInfo ci) {
        DarkTitleBar.setDarkTitlebar();
    }
}
