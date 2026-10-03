package com.example.hinata.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    @Unique
    private static final ResourceKey<EquipmentAsset> HINATA_KEY =
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("hinata", "hinata"));

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(DataComponentType<? extends T> type) {
        T value = this.getComponents().get(type);
        if (type != DataComponents.EQUIPPABLE || !(value instanceof Equippable eq)) return value;

        Optional<ResourceKey<EquipmentAsset>> id = eq.assetId();
        if (id.isEmpty() || !id.get().equals(EquipmentAssets.DIAMOND)) return value;

        Component name = this.getComponents().get(DataComponents.CUSTOM_NAME);
        if (name == null || !"Hinata".equals(name.getString())) return value;

        return (T) new Equippable(
            eq.slot(), eq.equipSound(), Optional.of(HINATA_KEY), eq.cameraOverlay(),
            eq.allowedEntities(), eq.dispensable(), eq.swappable(), eq.damageOnHurt(),
            eq.equipOnInteract(), eq.canBeSheared(), eq.shearingSound());
    }
}
