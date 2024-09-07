package net.mehvahdjukaar.dummmmmmy.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.mehvahdjukaar.dummmmmmy.common.EnchantHackHelper;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {


    @WrapOperation(method = {"isImmuneToDamage", "modifyDamageProtection", "runLocationChangedEffects"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ConditionalEffect;matches(Lnet/minecraft/world/level/storage/loot/LootContext;)Z"))
    private boolean dummy$entityAwareMatch(ConditionalEffect<?> instance, LootContext context, Operation<Boolean> operation) {
        return operation.call(instance, context)
                || EnchantHackHelper.matches(context, (Enchantment) (Object) this);
    }

    @ModifyExpressionValue(method = "applyEffects",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ConditionalEffect;matches(Lnet/minecraft/world/level/storage/loot/LootContext;)Z"))
    private static boolean dummy$entityAwareMatch2(boolean original, @Local(argsOnly = true) LootContext context) {
        return original || EnchantHackHelper.matches(context, (Enchantment) (Object) this);
    }
}
