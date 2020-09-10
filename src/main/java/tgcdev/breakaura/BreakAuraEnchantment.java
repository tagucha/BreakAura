package tgcdev.breakaura;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;
import java.util.UUID;

public class BreakAuraEnchantment extends Enchantment {
    public BreakAuraEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.BREAKABLE, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
        this.setRegistryName(new ResourceLocation("breakaura","break_aura"));
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ToolItem;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return canApplyAtEnchantingTable(stack);
    }

    public static class CallBreakEvent {
        @SubscribeEvent

        public void onBreakBlock(BlockEvent.BreakEvent event) {
            PlayerEntity player = event.getPlayer();
            ItemStack item = player.inventory.getCurrentItem();
            if (item.getItem() instanceof ToolItem) {
                ToolItem tool = (ToolItem) item.getItem();
                int lvl = EnchantmentHelper.getEnchantmentLevel(BreakAura.BREAK_AURA, item);
                if (lvl >= 1) {
                    BlockState state = event.getState();
//                    player.sendMessage(new StringTextComponent(state.getBlock().getHarvestTool(state).getName()),UUID.randomUUID());
                    if (tool.getToolTypes(item).contains(state.getBlock().getHarvestTool(state))){
                        int ulvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING,item);
                        int damage = item.getDamage();
                        if (!UnbreakingEnchantment.negateDamage(item,ulvl,new Random())) damage++;
                        for (int y = -lvl;y <= lvl;y++) {
                            if (0 <= event.getPos().getY() + y && event.getPos().getY() + y < 256) for (int x = -lvl;x <= lvl;x++) {
                                for (int z = -lvl;z <= lvl;z++) {
                                    if (player.world.getBlockState(event.getPos().add(x,y,z)).getBlock() == state.getBlock()) {
                                        player.world.destroyBlock(event.getPos().add(x, y, z), true);
                                        if (!UnbreakingEnchantment.negateDamage(item,ulvl,new Random())) damage++;
                                    }
                                }
                            }
                        }
                        item.setDamage(damage);
                    }
                }
            }
        }
    }
}
