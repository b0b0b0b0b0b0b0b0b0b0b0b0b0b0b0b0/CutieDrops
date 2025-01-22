package com.bobobo.plugins.cutieDrops.events;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import com.bobobo.plugins.cutieDrops.utils.ConfigManager;
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
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!(event.getRightClicked() instanceof Ageable mob)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.SHEARS) return;
        handleInteraction(player, mob);
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

        handleInteraction(null, mob);
        event.setCancelled(true);
    }

    /**
     * Обрабатывает взаимодействие с мобом.
     *
     * @param player Игрок, если взаимодействие инициировано им (null для раздатчика)
     * @param mob    Моб, с которым происходит взаимодействие
     */
    private void handleInteraction(Player player, Ageable mob) {

        String messageKey;

        Material dropMaterial = determineDrop(mob.getType());

        if (dropMaterial == null) return;

        if (!mob.isAdult()) {
            if (player != null && ConfigManager.isEnableAlreadyBabyMessage()) {
                messageKey = "mob_already_baby";
                player.sendMessage(CutieDrops.getInstance().getMessage(messageKey));
                player.playSound(player.getLocation(), ConfigManager.getAlreadyBabySound(), 1.0f, 1.0f);
            }
            return;
        }
        mob.setBaby();
        mob.getWorld().dropItem(mob.getLocation(), new ItemStack(dropMaterial));
        mob.getWorld().spawnParticle(
                ConfigManager.getTransformedParticle(),
                mob.getLocation(),
                10,
                0.5, 0.5, 0.5
        );
        if (player != null && ConfigManager.isEnableTransformedMessage()) {
            messageKey = "mob_transformed";
            player.sendMessage(CutieDrops.getInstance().getMessage(messageKey));
            player.playSound(player.getLocation(), ConfigManager.getTransformedSound(), 1.0f, 1.0f);
        }
    }
    /**
     * Определяет, какой дроп выпадет (обычный или огненный) с учётом шанса.
     *
     * @param mobType Тип моба
     * @return Материал дропа
     */
    private Material determineDrop(EntityType mobType) {
        Material dropMaterial = ConfigManager.getDropMaterial(mobType);
        Material fireDropMaterial = ConfigManager.getFireDropMaterial(mobType);
        if (fireDropMaterial != null && Math.random() * 100 < ConfigManager.getFireDropChance()) {
            return fireDropMaterial;
        }
        return dropMaterial;
    }
}
