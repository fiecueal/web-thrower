package io.github.fiecueal.web_thrower;

import java.util.function.Predicate;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;

public class WebThrowerItem extends ProjectileWeaponItem {
	public WebThrowerItem(Item.Properties p) {
		super(p);
	}

	@Override
	public Predicate<ItemStack> getAllSupportedProjectiles() {
		return ARROW_ONLY;
	}

	@Override
	public int getDefaultProjectileRange() {
		return 8;
	}

	/** Called on right click (default item use) */
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		List<ItemStack> list = draw(itemstack, player.getProjectile(itemstack), player);
		if (player.hasInfiniteMaterials() || (!player.getProjectile(itemstack).isEmpty() && !list.isEmpty())) {
			if (level instanceof ServerLevel serverlevel) {
				this.shoot(
						serverlevel,
						player,
						hand,
						itemstack,
						list,
						2F,
						4F,
						true,
						null);

				level.playSound(
						null,
						player.getX(),
						player.getY(),
						player.getZ(),
						SoundEvents.EGG_THROW,
						SoundSource.PLAYERS,
						1F,
						level.getRandom().nextFloat() / 2F);

				player.getCooldowns().addCooldown(this, 5);
				return InteractionResultHolder.consume(itemstack);
			}
		}
		return InteractionResultHolder.fail(itemstack);
	}

	@Override
	protected void shootProjectile(LivingEntity shooter, Projectile projectile,
			int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
		projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() +
				angle, 0.0F, velocity, inaccuracy);
	}
}
