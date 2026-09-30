package dev.warriorrr.altTabCursorCenterFix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "releaseMouse", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MouseHandler;mouseGrabbed:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER), cancellable = true)
    private void preventCenterMouseWhileUnfocused(CallbackInfo info) {
        if (!this.minecraft.isWindowActive()) {
            info.cancel();
        }
    }
}
