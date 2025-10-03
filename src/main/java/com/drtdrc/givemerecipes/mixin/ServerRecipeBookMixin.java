package com.drtdrc.givemerecipes.mixin;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerRecipeBook.class)
public class ServerRecipeBookMixin {

    @Shadow @Final protected Set<RegistryKey<Recipe<?>>> unlocked;
    @Shadow @Final protected Set<RegistryKey<Recipe<?>>> highlighted;

    @Inject(
            method = "sendInitRecipesPacket(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
            at = @At("HEAD")
    )
    private void onSendInitRecipesPacket(ServerPlayerEntity player, CallbackInfo ci) {

        ServerRecipeManager rm = player.getEntityWorld().getServer().getRecipeManager();
        for (RecipeEntry<?> e : rm.values()) {
            this.unlocked.add(e.id());
        }

        this.highlighted.clear();
    }

}
