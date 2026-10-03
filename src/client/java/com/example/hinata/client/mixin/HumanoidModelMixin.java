package com.example.hinata.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;

/** Makes the sleeves of the Hinata jumper thinner. */
@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {

    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;

    // 1.0 = normal armor thickness. Lower = thinner. Try 0.7 to 0.9.
    private static final float SLIM = 0.7f;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V",
            at = @At("RETURN"))
    private void hinata$slimSleeves(HumanoidRenderState state, CallbackInfo ci) {
        if ((Object) this instanceof PlayerModel) return; // never touch the player's own arms

        Component name = state.chestEquipment.get(DataComponents.CUSTOM_NAME);
        float s = (name != null && "Hinata".equals(name.getString())) ? SLIM : 1.0f;

        rightArm.xScale = s; rightArm.zScale = s;
        leftArm.xScale = s;  leftArm.zScale = s;
    }
}
