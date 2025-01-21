package com.bobobo.plugins.cutieDrops.events;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
public class InteractListener implements Listener {
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!(event.getRightClicked() instanceof Ageable mob)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.SHEARS) return;
        EntityType mobType = mob.getType();
        Material dropMaterial = CutieDrops.getInstance()
                .getConfigManager()
                .getDropMaterial(mobType);
        if (dropMaterial == null) {
            return;
        }
        if (!mob.isAdult()) {
            if (CutieDrops.getInstance().getConfigManager().isEnableAlreadyBabyMessage()) {
                player.sendMessage(CutieDrops.getInstance().getMessage("mob_already_baby"));
                player.playSound(player.getLocation(), CutieDrops.getInstance().getConfigManager().getAlreadyBabySound(), 1.0f, 1.0f);
            }
            return;
        }
        mob.setBaby();
        mob.getWorld().spawnParticle(
                CutieDrops.getInstance().getConfigManager().getTransformedParticle(),
                mob.getLocation(),
                10,
                0.5, 0.5, 0.5
        );
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(dropMaterial));
        if (CutieDrops.getInstance().getConfigManager().isEnableTransformedMessage()) {
            player.sendMessage(CutieDrops.getInstance().getMessage("mob_transformed"));
            player.playSound(player.getLocation(), CutieDrops.getInstance().getConfigManager().getTransformedSound(), 1.0f, 1.0f);
        }
    }
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        if (event.getItem().getType() != Material.SHEARS) return;
        Block block = event.getBlock();
        Directional directional = (Directional) block.getBlockData();
        BlockFace face = directional.getFacing();
        Block targetBlock = block.getRelative(face);
        Ageable mob = targetBlock.getWorld().getNearbyEntities(targetBlock.getLocation(), 1, 1, 1).stream()
                .filter(entity -> entity instanceof Ageable)
                .map(entity -> (Ageable) entity)
                .findFirst()
                .orElse(null);
        if (mob == null) return;
        EntityType mobType = mob.getType();
        Material dropMaterial = CutieDrops.getInstance()
                .getConfigManager()
                .getDropMaterial(mobType);

        if (dropMaterial == null || !mob.isAdult()) {
            event.setCancelled(true);
            return;
        }
        mob.setBaby();
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(dropMaterial));
        event.setCancelled(true);
    }
}
