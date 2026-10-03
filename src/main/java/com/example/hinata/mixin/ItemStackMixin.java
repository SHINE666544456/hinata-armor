package com.example.hinata.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

/**
 * Diamond armor named "Hinata" reports an equippable component whose asset is
 * hinata:hinata, so the renderer loads assets/hinata/equipment/hinata.json.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    private static final ResourceKey<EquipmentAsset> HINATA_KEY =
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("hinata", "hinata"));

    @SuppressWarnings("unchecked")
    @Inject(method = "get", at = @At("RETURN"), cancellable = true)
    private <T> void hinata$swapAsset(DataComponentType<? extends T> type, CallbackInfoReturnable<T> cir) {
        if (type != DataComponents.EQUIPPABLE) return;
        if (!(cir.getReturnValue() instanceof Equippable eq)) return;

        Optional<ResourceKey<EquipmentAsset>> id = eq.assetId();
        if (id.isEmpty() || !id.get().equals(EquipmentAssets.DIAMOND)) return;

        Component name = ((ItemStack) (Object) this).get(DataComponents.CUSTOM_NAME);
        if (name == null || !"Hinata".equals(name.getString())) return;

        cir.setReturnValue((T) new Equippable(
            eq.slot(), eq.equipSound(), Optional.of(HINATA_KEY), eq.cameraOverlay(),
            eq.allowedEntities(), eq.dispensable(), eq.swappable(), eq.damageOnHurt(),
            eq.equipOnInteract(), eq.canBeSheared(), eq.shearingSound()));
    }
}
