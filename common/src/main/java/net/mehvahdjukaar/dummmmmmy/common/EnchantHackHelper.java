package net.mehvahdjukaar.dummmmmmy.common;

import net.mehvahdjukaar.dummmmmmy.mixins.LivingEntityMixin;
import net.mehvahdjukaar.moonlight.api.misc.DataObjectReference;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;

public class EnchantHackHelper {

    private static final List<DataObjectReference<Enchantment>> ENCHANTS = List.of(
            new DataObjectReference<>(Enchantments.SMITE),
            new DataObjectReference<>(Enchantments.BANE_OF_ARTHROPODS),
            new DataObjectReference<>(Enchantments.SHARPNESS),
            new DataObjectReference<>(Enchantments.IMPALING));


    public static boolean matches(LootContext context, Enchantment enchantment) {
        var entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof TargetDummyEntity e){
            for (DataObjectReference<Enchantment> ench : ENCHANTS) {
                if (ench.get().equals(enchantment)) {
                    return true; //TODO:
                }
            }
        }
        return false;
    }

    public static boolean freezeHurtsExtra(Entity entity) {
        if(entity instanceof TargetDummyEntity e){

        }
        return false;
    }
}
