package crit.bounceback.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class AxeBounce {
	@Shadow
	@Nullable
	public abstract LivingEntity getAttacker();

	@Shadow public abstract LivingEntity getLastAttacker();

	@Shadow @Nullable public abstract PlayerEntity getAttackingPlayer();

	@Shadow @Nullable public abstract LivingEntity getPrimeAdversary();

	@Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
	private void init(ServerWorld world, LivingEntity attacker, CallbackInfo ci) {
		if (!world.isClient){
			if (attacker == null) return;
			if (attacker.getMainHandStack().isIn(ItemTags.AXES)) {
				Vec3d direction = attacker.getRotationVec(1.0f);
				if (attacker.isPlayer()){
					if	( -attacker.getVelocity().y <= 1.5) {
						attacker.setVelocity(new Vec3d(-direction.x * .75, -attacker.getVelocity().y, -direction.z * .75));
						attacker.velocityModified = true;
					} else {
						attacker.setVelocity(new Vec3d(-direction.x * .75, 1.5, -direction.z * .75));
						attacker.velocityModified = true;
					}
				} else if (!attacker.isPlayer()) {
					attacker.setVelocity(new Vec3d(-direction.x *.5, .25, -direction.z *.5));
					attacker.velocityModified = true;
				}

			}
		}
	}
}